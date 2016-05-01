/*
 * Created by Benjamin Sweeney on 2016.04.13  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.managers;

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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ben
 */
@Named(value = "movieManager")
@SessionScoped
public class MovieManager implements Serializable {

    private List<Movie> nowPlaying;
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
        
        for (int i=0; i <= 5; i++) {
            criticallyAcclaimed.add(apiManager.searchForMovieOmdb(caTitles[i]));  
            System.out.println("Movie " + i);
            System.out.println("~~~~~~~~~~~~rtRATING: " + criticallyAcclaimed.get(i).getRTRating());
            System.out.println("~~~~~~~~~~~~rtCriticsConsensus: " + criticallyAcclaimed.get(i).getRTCriticsConsensus());
            System.out.println("~~~~~~~~~~~~imdbRating: " + criticallyAcclaimed.get(i).getImdbRating());
        }
        

//        nowPlaying = new ArrayList<>();
//        for (int i = 0; i < 128; i++) {
//            nowPlaying.add(new Movie("Batman vs. Superman" + i));
//        }
    }

    public String imageOf(Movie m) {
        int i;
        if ((i = nowPlaying.indexOf(m)) >= 0) {
            return nowPlaying.get(i).getPreferredImageUri();
        } else {
            return null;
        }
    }

    public boolean isFavorited(Movie currMovie) {

        if (getUser() != null && currMovie != null) {
            return userFacade.findByUsername(getUser().getUsername()).favorited(currMovie);
        }
        return false;
    }

    public void setFavorited(boolean favorited, Movie currMovie) {
        System.err.println("Setting favorite");
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

//    public boolean isFavorited() {
//        if (getUser() != null && selectedMovie != null) {
//            return favoritedFacade.find(new FavoritedPK(user.getId(), selectedMovie.getTmsId())) != null;
//        }
//        
//        return false;
//    }
//    
//    public void setFavorited(boolean favorited) {
//        System.err.println("Setting favorite");
//        if (getUser() != null && selectedMovie != null) {
//            Favorited f = favoritedFacade.find(new FavoritedPK(user.getId(), selectedMovie.getTmsId()));
//            
//            if (favorited && f == null) {
//                favoritedFacade.create(new Favorited(user, selectedMovie.getTmsId()));
//            }
//            else if (!favorited && f != null) {
//                favoritedFacade.remove(f);
//            }
//        }
//    }
    public User getUser() {
        if (user == null) {
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");
            user = userFacade.findByUsername(user_name);
        }

        return user;
    }

    public String getTickets() {
        String s = numTickets + " ticket";
        if (!numTickets.equals("1")) {
            s += "s";
        }
        return s;
    }

    public String getConfirmationMessage() {
        return "Thank you for your purchase of " + numTickets + " of the "
                + selectedShowtime.getTime() + " showtime of " + selectedMovie.getTitle();
    }


    public String getNumTickets() {
        return numTickets;
    }

    public String getTotalPrice() {
        return String.format("Total: $%.2f", getPrice());
    }

    public float getPrice() {
        return (float) (11.0 * Integer.parseInt(getNumTickets()));
    }

    public void setNumTickets(String numTickets) {
        this.numTickets = numTickets;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zip) {
        zipCode = zip;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public Showtime getSelectedShowtime() {
        return selectedShowtime;
    }

    public void setSelectedShowtime(Showtime selectedShowtime) {
        this.selectedShowtime = selectedShowtime;
    }

    public void changeLocation() {
        locationChanged = true;
        ArrayList<String> temp = getNowPlayingTitles();
        locationChanged = false;
        System.out.println("location changed");
    }

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
    
    public String[] getBackImages() {
        String[] backImages = {
            "http://i.imgur.com/QFTEWaa.jpg",
            "http://i.imgur.com/TewghSg.jpg",
            "http://i.imgur.com/t9qwqNm.jpg",
            "http://i.imgur.com/IXeQQtS.jpg",
            "http://i.imgur.com/chV9r5M.jpg",
            "http://i.imgur.com/YlAGj5R.jpg" };
        
        return backImages;
    }

    public String[] getCriticallyAcclaimedTitles() {
        return criticallyAcclaimedTitles;
    }
    
    public ArrayList<String> getCriticallyAcclaimedRTRating() {
        ArrayList<String> ratings = new ArrayList<>();
        
        for (Movie m : criticallyAcclaimed) {
            ratings.add(m.getRTRating());
        } 
        
        return ratings;
    }
    
    public ArrayList<String> getCriticallyAcclaimedImdbRating() {
        ArrayList<String> ratings = new ArrayList<>();
        
        for (Movie m : criticallyAcclaimed) {
            ratings.add(m.getImdbRating());
        } 
        
        return ratings;
    }
    
    public ArrayList<String> getCriticallyAcclaimedRTCriticsConsensus() {
        ArrayList<String> criticsC = new ArrayList<>();
        
        for (Movie m : criticallyAcclaimed) {
            criticsC.add(m.getRTCriticsConsensus());
        } 
        
        return criticsC;
    }
    
    /**
     * gets the titles of the top 5 movies. For now this is simply the first 5
     * returned, but it should be based upon highest grossing or something.
     *
     * @return the list of top 5 titles
     */
    public ArrayList<String> getTopNowPlayingTitles() {
        ArrayList<String> titles = new ArrayList<>();
        List<Movie> list = getNowPlaying();

        for (int i = 0; i <= 5; i++) {
            titles.add(list.get(i).getTitle());
        }

        return titles;
    }

    public ArrayList<String> getNowPlayingPosters() {
        ArrayList<String> posters = new ArrayList<>();

        List<Movie> list = getNowPlaying();
//        posters.add("http://i.imgur.com/hQsw8Oe.jpg");
//        posters.add("http://i.imgur.com/ZUli3ZH.jpg");
//        posters.add("http://i.imgur.com/M2AYIht.jpg");
//        posters.add("http://i.imgur.com/ZcMT33m.jpg");
//        posters.add("http://i.imgur.com/sODzs5B.png");

        for (Movie m : list) {
            posters.add(m.getPreferredImageUri());
        }

        //System.out.println("*****************************************" + posters);
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
        System.out.println("poster clicked title set to: " + title);
        this.posterClickedTitle = title;
    }

    public String searchByTitle() {

        return "/SearchResults";
    }
    
    public String searchByPoster() {
        
        return "/SearchResults";
    }

    public String searchByPosterClickedTitle(String title) {
        setPosterClickedTitle(title);
        return searchByPoster();
    }

    public void view() {
        if (selectedMovie != null) {
            selectedMovie.retrievePreferredImageUri();
            System.err.println("Updated!");
        }
        System.err.println("poop");
    }

    public List<Movie> getNowPlaying() {
        if (locationChanged) {
            nowPlaying = apiManager.moviesPlayingInLocalTheatres(zipCode);
        } else if (nowPlaying == null) {
            nowPlaying = apiManager.moviesPlayingInLocalTheatres(zipCode);
        }

        return nowPlaying;
    }

    private List<Movie> getFavoritedMovies() {
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");
        User u = userFacade.findByUsername(user_name);

        List<Favorited> f = u.getFavoritedList();

        System.err.println("Number of favorited for " + user_name + " : " + f.size());

        return Movie.convertFavorited(f, zipCode);
    }

    public List<Movie> getSearchResults() {
        System.out.println("Getting movies by title: |" + this.searchTitle + "|");
        //List<Movie> list = apiManager.searchByTitleOmdb(this.searchTitle);
        List <Movie> list = new ArrayList<Movie>();
        String[] searchTokens = searchTitle.toLowerCase().split(" ");
        System.err.println("Tokens: " + searchTokens.length);
        for (String s : searchTokens) {
            System.err.println(s);
        }
        for (Movie m : nowPlaying) {
            boolean allTokensFound = true;
            System.err.println(m.getTitle());
            for (String s : searchTokens) {
                if (!(m.getTitle().toLowerCase().contains(s))) {
                    allTokensFound = false;
                }
            }
            if (allTokensFound) {
                list.add(m);
            }
        }
        System.out.println("Movies size: " + list.size());
        return list;
    }
    
        public List<Movie> getSearchResultsFromPoster() {
        System.out.println("Getting movies by title: |" + this.posterClickedTitle + "|");
        //List<Movie> list = apiManager.searchByTitleOmdb(this.searchTitle);
        List <Movie> list = new ArrayList<Movie>();
        String[] searchTokens = posterClickedTitle.toLowerCase().split(" ");
        System.err.println("Tokens: " + searchTokens.length);
        for (String s : searchTokens) {
            System.err.println(s);
        }
        for (Movie m : nowPlaying) {
            boolean allTokensFound = true;
            System.err.println(m.getTitle());
            for (String s : searchTokens) {
                if (!(m.getTitle().toLowerCase().contains(s))) {
                    allTokensFound = false;
                }
            }
            if (allTokensFound) {
                list.add(m);
            }
        }
        System.out.println("Movies size: " + list.size());
        return list;
    }

    public String purchaseMovie(Movie movie, Showtime showtime) {
        setSelectedShowtime(showtime);
        setSelectedMovie(movie);
        return "/Purchase";
    }

    public String showNowPlaying() {
        reset();
        return "/NowPlaying";
    }

    public String showFavorited() {
        reset();
        return "/Favorites";
    }

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
        return "/Confirmation";
    }

    public List<Bought> getPurchaseHistory() {
        if (getUser() != null) {
            return boughtFacade.findByUser(user.getId());
        }

        return new ArrayList<>();
    }
    
    
    public void reset() {
        this.numTickets = "1";
        this.searchTitle = null;
        this.selectedMovie = null;
        this.selectedShowtime = null;
    }
}
