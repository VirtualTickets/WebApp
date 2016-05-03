/*
 * Created by Benjamin Sweeney on 2016.04.13  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.managers;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mycompany.entities.Bought;
import com.mycompany.entities.Favorited;
import com.mycompany.entities.FavoritedPK;
import com.mycompany.entities.User;
import com.mycompany.facades.BoughtFacade;
import com.mycompany.facades.FavoritedFacade;
import com.mycompany.facades.UserFacade;
import com.mycompany.virtualtickets.ApiManager;
import com.mycompany.virtualtickets.Movie;
import com.mycompany.virtualtickets.Showtime;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.vcard.VCard;
import net.glxn.qrgen.javase.QRCode;

/**
 *
 * Handles when a page is selected and when a movie is selected to send to the
 * write manager to handle the movies.
 *
 * @author Ben & Alex
 */
@Named(value = "movieManager")
@SessionScoped
public class MovieManager implements Serializable {

    private List<Movie> nowPlaying;
    private List<Movie> mostPopular;
    private ArrayList<String> mostPopularTitles;
    private String searchTitle;
    private String posterClickedTitle;
    private Movie selectedMovie;
    private Showtime selectedShowtime;
    private final ApiManager apiManager;
    private String zipCode;
    private String numTickets = "1";
    private boolean locationChanged = false;
    private User user;
    private final List<Movie> criticallyAcclaimed;
    private final String[] criticallyAcclaimedTitles;
    private User userTemp;

    @EJB
    private BoughtFacade boughtFacade;

    @EJB
    private UserFacade userFacade;

    @EJB
    private FavoritedFacade favoritedFacade;

    /**
     * Creates a new instance of ApiManager
     */
    public MovieManager() {
        zipCode = "24061";
        apiManager = new ApiManager();
        criticallyAcclaimed = new ArrayList<>();
        String[] caTitles = {"The Jungle Book", "Barbershop: The Next Cut",
            "Zootopia", "Eye In The Sky", "The Witch", "10 Cloverfield Lane"};
        criticallyAcclaimedTitles = caTitles;

        for (int i = 0; i <= 5; i++) {
            criticallyAcclaimed.add(apiManager.searchForMovieOmdb(caTitles[i]));
//            System.out.println("Movie " + i);
//            System.out.println("~~~~~~~~~~~~rtRATING: " + criticallyAcclaimed.get(i).getRTRating());
//            System.out.println("~~~~~~~~~~~~rtCriticsConsensus: " + criticallyAcclaimed.get(i).getRTCriticsConsensus());
//            System.out.println("~~~~~~~~~~~~imdbRating: " + criticallyAcclaimed.get(i).getImdbRating());
        }

//        nowPlaying = new ArrayList<>();
//        for (int i = 0; i < 128; i++) {
//            nowPlaying.add(new Movie("Batman vs. Superman" + i));
//        }
    }

    /**
     * THis method gets the image url of a movie
     *
     * @param m The movie that we want the image of.
     * @return The string of the movie URL.
     */
    public String imageOf(Movie m) {
        int i;
        if ((i = nowPlaying.indexOf(m)) >= 0) {
            return nowPlaying.get(i).getPreferredImageUri();
        } else {
            return null;
        }
    }

    /**
     * Checks to see if a movie is favorited by the logged in user.
     *
     * @param currMovie The movie that is checked if favorited
     * @return True if in user favorites. False otherwise.
     */
    public boolean isFavorited(Movie currMovie) {

        if (getUser() != null && currMovie != null) {
            return userFacade.findByUsername(getUser().getUsername()).favorited(currMovie);
        }
        return false;
    }

    /**
     * Sets a movie to a users favorites list.
     *
     * @param favorited The value boolean that will be flipped.
     * @param currMovie The currMovie that will be either added or removed from
     * favorites.
     */
    public void setFavorited(boolean favorited, Movie currMovie) {
        //System.err.println("Setting favorite");
        if (getUser() != null && currMovie != null) {
            Favorited f = favoritedFacade.find(new FavoritedPK(user.getId(), currMovie.getTmsId()));

            if (favorited && f == null) {
                Favorited fav = new Favorited(user, currMovie.getTmsId());
                favoritedFacade.create(fav);
                getUser().getFavoritedList().add(fav);
            } else if (!favorited && f != null) {
                favoritedFacade.remove(f);
                getUser().getFavoritedList().remove(f);
            }
        }
    }

    /**
     * Returns a list a user's favorited movies.
     *
     * @return The list of movies.
     */
    public List<Movie> getFavoriteMovies() {
        if (getUser() != null) {
            List<Favorited> l = getUser().getFavoritedList();
            List<Movie> list = new ArrayList<>();

            if (nowPlaying != null) {
                for (Favorited f : l) {
                    Movie m = new Movie(f);
                    int i;
                    if ((i = nowPlaying.indexOf(m)) >= 0) {
                        list.add(nowPlaying.get(i));
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * Gets the current logged in user.
     *
     * @return The current logged in user.
     */
    public User getUser() {
        if (user == null) {
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");
            user = userFacade.findByUsername(user_name);
        }

        return user;
    }

    /**
     * Gets the ticket string with the number of tickets.
     *
     * @return The string of how many tickets is purchased.
     */
    public String getTickets() {
        String s = numTickets + " ticket";
        if (!numTickets.equals("1")) {
            s += "s";
        }
        return s;
    }

    /**
     * Returns the message after a ticket is purchased.
     *
     * @return The message after a ticket is bought.
     */
    public String getConfirmationMessage() {
        return "Thank you for your purchase of " + numTickets + " of the "
                + selectedShowtime.getTime() + " showtime of " + selectedMovie.getTitle();
    }

    /**
     * @return Returns the number of tickets bought.
     */
    public String getNumTickets() {
        return numTickets;
    }

    /**
     * @return Returns the total price in the correct string format.
     */
    public String getTotalPrice() {
        return String.format("Total: $%.2f", getPrice());
    }

    /**
     *
     * @return Returns the price in float.
     */
    public float getPrice() {
        return (float) (11.0 * Integer.parseInt(getNumTickets()));
    }

    /**
     *
     * @param numTickets Sets the numTickets variable to the numTickets
     * parameter.
     */
    public void setNumTickets(String numTickets) {
        this.numTickets = numTickets;
    }

    /**
     *
     * @return The zipcode that will be searched for.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zip Sets the zipcode to the new zip.
     */
    public void setZipCode(String zip) {
        zipCode = zip;
    }

    /**
     *
     * @return The selected movie being bought.
     */
    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    /**
     *
     * @param selectedMovie Sets the selected movie to the new one being
     * purchased.
     */
    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    /**
     *
     * @return The selected showtime being purchased.
     */
    public Showtime getSelectedShowtime() {
        return selectedShowtime;
    }

    /**
     *
     * @param selectedShowtime changes the selectedshowtime.
     */
    public void setSelectedShowtime(Showtime selectedShowtime) {
        this.selectedShowtime = selectedShowtime;
    }

    /**
     * Changes the location
     */
    public void changeLocation() {
        locationChanged = true;
        ArrayList<String> tempNowPlaying = getNowPlayingTitles();
        ArrayList<String> tempMostPopular = getMostPopularTitles();
        locationChanged = false;
    }

    /**
     *
     * @return Gets an arraylist of the nowplaying titles.
     */
    public ArrayList<String> getNowPlayingTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Choose a Movie");
        List<Movie> list = getNowPlaying();

        if (list != null) {
            for (Movie m : list) {
                titles.add(m.getTitle());

            }
        }

        return titles;
    }

    /**
     *
     * @return For the critically acclaimed section in the front page.
     */
    public String[] getBackImages() {
        String[] backImages = {
            "#{resource['images:backImage1.jpg']}",
            "#{resource['images:backImage2.jpg']}",
            "#{resource['images:backImage3.jpg']}",
            "#{resource['images:backImage4.jpg']}",
            "#{resource['images:backImage5.jpg']}",
            "#{resource['images:backImage6.jpg']}",};

        return backImages;
    }

    /**
     *
     * @return Gets the critically acclaimed titles.
     */
    public String[] getCriticallyAcclaimedTitles() {
        return criticallyAcclaimedTitles;
    }

    /**
     *
     * @return A string arraylist of critically acclaimmed ratings.
     */
    public ArrayList<String> getCriticallyAcclaimedRTRating() {
        ArrayList<String> ratings = new ArrayList<>();

        for (Movie m : criticallyAcclaimed) {
            ratings.add(m.getRTRating());
        }

        return ratings;
    }

    /**
     *
     * @return A string arraylist of critically acclaimmed ratings.
     */
    public ArrayList<String> getCriticallyAcclaimedImdbRating() {
        ArrayList<String> ratings = new ArrayList<>();

        for (Movie m : criticallyAcclaimed) {
            ratings.add(m.getImdbRating());
        }

        return ratings;
    }

    /**
     *
     * @return A string arraylist of critically acclaimmed ratings.
     */
    public ArrayList<String> getCriticallyAcclaimedRTCriticsConsensus() {
        ArrayList<String> criticsC = new ArrayList<>();

        for (Movie m : criticallyAcclaimed) {
            criticsC.add(m.getRTCriticsConsensus());
        }

        return criticsC;
    }

    /**
     * gets the titles of the 5 most popular movies.
     *
     * @return the list of the 5 most popular titles
     */
    public ArrayList<String> getMostPopularTitles() {
        if (mostPopularTitles == null || locationChanged) {
            ArrayList<String> titles = new ArrayList<>();
            List<Movie> popularList = getMostPopular();
            List<Movie> nowPlayingList = getNowPlaying();
            int nowPlayingSize = nowPlayingList.size();

            int i = 0; //index for popularList
            //stops scanning through list of popular titles after 5 titles are added.
            while (titles.size() < 5 && i < popularList.size()) {
                System.out.println("Checking to see if popTitle: " + popularList.get(i).getTitle() + " is in now Playing.");
                boolean titleFoundInNowPlaying = false;
                int k = 0;  //index for nowPlayingList
                //stops when a title is found
                while (!titleFoundInNowPlaying && k < nowPlayingSize) {
                    //checks if the popular title is actually playing nearby before adding.
                    if (nowPlayingList.get(k).getTitle().equals(popularList.get(i).getTitle())) {
                        titles.add(popularList.get(i).getTitle());
                        titleFoundInNowPlaying = true;
                    }
                    k++;
                }
                i++;
            }
            System.out.println("*************************Popular Titles After Sorting: " + titles);
            //If less than 5 titles are found, titles will be chosen from now playing in the order given.
            if (titles.size() < 5) {
                int r = 0;  //index for nowPlayingList
                if (titles.size() == 0) {
                    while (titles.size() < 5 && r < nowPlayingSize) {
                        titles.add(nowPlayingList.get(r).getTitle());
                        r++;
                    }
                } else {
                    while (titles.size() < 5 && r < nowPlayingSize) {
                        boolean titleAlreadyInList = false;
                        for (String title : titles) {
                            //checks if the title is already in the list
                            if (nowPlayingList.get(r).getTitle().equals(title)) {
                                titleAlreadyInList = true;
                            }
                        }
                        if (!titleAlreadyInList) {
                            titles.add(nowPlayingList.get(r).getTitle());
                        }
                        r++;
                    }
                }
            }

            mostPopularTitles = titles;
        }
        return mostPopularTitles;
    }

    /**
     *
     * @return returns the most popular movie images.
     */
    public ArrayList<String> getMostPopularPosters() {
        ArrayList<String> popularTitles = getMostPopularTitles();
        ArrayList<String> posters = new ArrayList<>();
        List<Movie> nowPlayingList = getNowPlaying();

        for (String title : popularTitles) {
            for (Movie m : nowPlayingList) {
                if (m.getTitle().equals(title)) {
                    posters.add(m.getPreferredImageUri());
                }
            }
        }

        return posters;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getPosterClickedTitle() {
        return posterClickedTitle;
    }

    public void setPosterClickedTitle(String title) {
        //System.out.println("poster clicked title set to: " + title);
        this.posterClickedTitle = title;
    }

    public String searchByTitle() {

        return "/SearchResults";
    }

    public String searchByPoster() {

        return "/SearchResults";
    }

    /**
     *
     * @param title of the movie that is selected
     * @return the image url of the selected movie
     */
    public String searchByPosterClickedTitle(String title) {
        setPosterClickedTitle(title);
        return searchByPoster();
    }

    /**
     * Gets the selected movie image URL.
     */
    public void view() {
        if (selectedMovie != null) {
            selectedMovie.retrievePreferredImageUri();
        }
    }

    /**
     *
     * @return A list of movies that is currently showing in movies.
     */
    public List<Movie> getNowPlaying() {
        if (locationChanged) {
            nowPlaying = apiManager.moviesPlayingInLocalTheatres(zipCode);
        } else if (nowPlaying == null) {
            nowPlaying = apiManager.moviesPlayingInLocalTheatres(zipCode);
        }

        return nowPlaying;
    }

    /**
     *
     * @return Returns a list of the most popular movies
     */
    public List<Movie> getMostPopular() {
        if (mostPopular == null) {
            mostPopular = apiManager.findPopularMovies();
        }

        return mostPopular;
    }

    /**
     *
     * @return Returns the list of movies found by the search bar.
     */
    public List<Movie> getSearchResults() {
        List<Movie> list = new ArrayList<Movie>();
        String[] searchTokens = searchTitle.toLowerCase().split(" ");
        for (Movie m : nowPlaying) {
            boolean allTokensFound = true;
            for (String s : searchTokens) {
                if (!(m.getTitle().toLowerCase().contains(s))) {
                    allTokensFound = false;
                }
            }
            if (allTokensFound) {
                list.add(m);
            }
        }
        return list;
    }

    /**
     *
     * @return A list of movies based on the clicked title.
     */
    public List<Movie> getSearchResultsFromPoster() {
        List<Movie> list = new ArrayList<Movie>();
        String[] searchTokens = posterClickedTitle.toLowerCase().split(" ");
        for (Movie m : nowPlaying) {
            boolean allTokensFound = true;
            for (String s : searchTokens) {
                if (!(m.getTitle().toLowerCase().contains(s))) {
                    allTokensFound = false;
                }
            }
            if (allTokensFound) {
                list.add(m);
            }
        }
        return list;
    }

    /**
     * Goes to the purchase page with the current selected parameters.
     *
     * @param movie The movie selected to purchase
     * @param showtime the showtime at the theatre to be purchased.
     * @return The path of the purchase page.
     */
    public String purchaseMovie(Movie movie, Showtime showtime) {
        setSelectedShowtime(showtime);
        setSelectedMovie(movie);
        return "/Purchase";
    }

    /**
     * Resets the now showing page
     *
     * @return The page of the nowplaying.
     */
    public String showNowPlaying() {
        reset();
        return "/NowPlaying";
    }

    /**
     * Shows the movies favorited by the movie
     *
     * @return The path of the favorites
     */
    public String showFavorited() {
        reset();
        return "/Favorites";
    }

    /**
     * Confirms the purchase made by the user.
     *
     * @return The confirmation page.
     */
    public String confirmPurchase() {

        if (AccountManager.isLoggedIn()) {
            User u = getUser();
            Calendar c = Calendar.getInstance();
            Date currDate = new Date();
            Date purchaseDate = new Date(currDate.getYear(),
                    currDate.getMonth(),
                    currDate.getDate(),
                    selectedShowtime.getHour(),
                    selectedShowtime.getMinute());
            boughtFacade.create(new Bought(u.getId(),
                    currDate.getTime(),
                    selectedMovie.getTitle(),
                    selectedShowtime.getTheatreName(),
                    purchaseDate.getTime(),
                    Integer.parseInt(numTickets), getPrice()));
        }
        sendConfirm();
        return "/Confirmation";
    }

    /**
     * Gets the user purchase history.
     *
     * @return The list of previously bough tickets
     */
    public List<Bought> getPurchaseHistory() {
        if (getUser() != null) {
            return boughtFacade.findByUser(user.getId());
        }

        return new ArrayList<>();
    }

    /**
     * Resets all the information of the currently playing movies.
     */
    public void reset() {
        this.numTickets = "1";
        this.searchTitle = null;
        this.selectedMovie = null;
        this.selectedShowtime = null;
    }

    /**
     * send an email containing a QR code and information about the users'
     * ticket order
     */
    private void sendConfirm() {

//        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
//        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
//        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
//        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
//        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
//        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
        //set the user to the temporary user
        //to tupport not-logged-in purchases
        User user = userTemp;
        //get the QR code image
        String imageLoc = getQR();
        final String username1 = "vitualtickets.noreply@gmail.com";
        final String password1 = "csd@VT(S16)";

        //set the properties of the email sending
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.debug", "true");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.timeout", 5000);

        Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
                        @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username1, password1);
			}
		  });
        
        try {
            //create a new email session
//            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            //populate the base elements: recipients, subject, and sender for a message
            Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress("virtualtickets.noreply@gmail.com"));
            message1.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message1.setSubject("VirtualTickets: Your ticket order");

            //start creating the body of the message
            MimeMultipart multipart = new MimeMultipart();

            //make the text portion of the message add it to our overall message
            BodyPart messageBodyPart = new MimeBodyPart();
            String text = "Here is your ticket order of\n" + getTickets() + " to see:\n" + selectedMovie.getTitle() + "\n" + selectedShowtime.getTime() + "\n" + selectedShowtime.getTheatreName();
            messageBodyPart.setText(text);
            multipart.addBodyPart(messageBodyPart);

            //add th image to our message
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(imageLoc);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);

            message1.setContent(multipart);

            //send the email
            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", "virtualtickets.noreply@gmail.com", "csd@VT(S16)");
            transport.sendMessage(message1, message1.getAllRecipients());
            transport.close();

            System.out.println("Done");

        } catch (MessagingException e) {
            Logger.getLogger(PasswordResetManager.class.getName()).log(Level.SEVERE, null, e);

            return;
        }

    }

    /**
     * create and store a QR code image for a customer's order
     *
     * @return Returns the link to the qr code
     */
    public String getQR() {
        String qrCodeText = getTickets() + " to see:\n" + selectedMovie.getTitle() + "\n" + selectedShowtime.getTime() + "\n" + selectedShowtime.getTheatreName();
        File outf = new File(Constants.ROOT_DIRECTORY + "movieTicketQRCode.png");
        if (!outf.exists()) {
            try {
                outf.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(MovieManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outf);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PasswordResetManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        // get QR file from text using defaults
        File file = QRCode.from(qrCodeText).file();

        // get QR stream from text using defaults
        ByteArrayOutputStream stream = QRCode.from(qrCodeText).stream();

        // override the image type to be JPG
        QRCode.from(qrCodeText).to(ImageType.JPG).file();
        QRCode.from(qrCodeText).to(ImageType.JPG).stream();

        // override image size to be 250x250
        QRCode.from(qrCodeText).withSize(250, 250).file();
        QRCode.from(qrCodeText).withSize(250, 250).stream();

        // override size and image type
        QRCode.from(qrCodeText).to(ImageType.GIF).withSize(250, 250).file();
        QRCode.from(qrCodeText).to(ImageType.GIF).withSize(250, 250).stream();

        // override default colors (black on white)
        // notice that the color format is "0x(alpha: 1 byte)(RGB: 3 bytes)"
        // so in the example below it's red for foreground and yellowish for background, both 100% alpha (FF).
        //QRCode.from(qrCodeText).withColor(0xFFFF0000, 0xFFFFFFAA).file();
        // supply own outputstream
        QRCode.from(qrCodeText).to(ImageType.PNG).writeTo(outputStream);

        // supply own file name
        QRCode.from(qrCodeText).file("QRCode");

        // supply charset hint to ZXING
        QRCode.from(qrCodeText).withCharset("UTF-8");

        // supply error correction level hint to ZXING
        QRCode.from(qrCodeText).withErrorCorrection(ErrorCorrectionLevel.L);

        // supply any hint to ZXING
        QRCode.from(qrCodeText).withHint(EncodeHintType.CHARACTER_SET, "UTF-8");

        // encode contact data as vcard using defaults
        VCard johnDoe = new VCard("John Doe")
                .setEmail("john.doe@example.org")
                .setAddress("John Doe Street 1, 5678 Doestown")
                .setTitle("Mister")
                .setCompany("John Doe Inc.")
                .setPhoneNumber("1234")
                .setWebsite("www.example.org");
        QRCode.from(johnDoe).file();

        // if using special characters don't forget to supply the encoding
        VCard johnSpecial = new VCard("Jöhn Dɵe")
                .setAddress("ëåäöƞ Sträät 1, 1234 Döestüwn");
        QRCode.from(johnSpecial).withCharset("UTF-8").file();

        return Constants.ROOT_DIRECTORY + "movieTicketQRCode.png";
    }

    /**
     *
     * @return The state of the user when they go to the purchase page. Not
     * necessarily need to be logged in.
     */
    public User getLoggedInTemp() {
        if (AccountManager.isLoggedIn()) {
            userTemp = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
            return userTemp;
        }
        userTemp = new User();
        return userTemp;
    }

}
