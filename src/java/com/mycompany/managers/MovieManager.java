/*
 * Created by Benjamin Sweeney on 2016.04.13  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.virtualtickets.ApiManager;
import com.mycompany.virtualtickets.Movie;
import com.mycompany.virtualtickets.SendEmail;
import com.mycompany.virtualtickets.Showtime;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Ben
 */
@Named(value = "movieManager")
@SessionScoped
public class MovieManager implements Serializable {

    private List<Movie> nowPlaying;
    private String searchTitle;
    private Movie currMovie;
    private Showtime selectedShowtime;
    private ApiManager apiManager;
    private String type;
    private String zipCode;
    private String checkoutAs = "Guest";
    private String numTickets = "1";
    private String email;
    private boolean locationChanged = false;
    
    private static final String SEARCH_BY_TITLE = "Search By Title";
    
    /**
     * Creates a new instance of ApiManager
     */
    public MovieManager() {
        zipCode = "24061";
        apiManager = new ApiManager();

//        nowPlaying = new ArrayList<>();
        
//        for (int i = 0; i < 128; i++) {
//            nowPlaying.add(new Movie("Batman vs. Superman" + i));
//        }
    }
    
    public String getTickets() {
        String s = numTickets + " ticket";
        if (!numTickets.equals("1")) {
            s += "s";
        }
        return s;
    }

    public void onTabChange(TabChangeEvent event) {
        String title = event.getTab().getTitle();
        System.err.println("Title: " + title);
        for (Movie m : nowPlaying) {
            if (m.getTitle().equals(title)) {
                currMovie = m;
                System.err.println("Image uri: " + m.getPreferredImageUri());
                break;
            }
        }
    }
    
    public String getConfirmationMessage() {
        return "Thank you for your purchase of " + numTickets + " of the "
                + selectedShowtime.getTime() + " showtime of " + currMovie.getTitle();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTickets() {
        return numTickets;
    }

    public String getTotalPrice() {
        return String.format("Total: $%.2f", 11.0 * Integer.parseInt(getNumTickets()));
    }

    public void setNumTickets(String numTickets) {
        this.numTickets = numTickets;
    }

    public String getCheckoutAs() {
        return checkoutAs;
    }

    public void setCheckoutAs(String checkoutAs) {
        this.checkoutAs = checkoutAs;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zip) {
        zipCode = zip;
    }

    public Movie getCurrMovie() {
        return currMovie;
    }

    public void setCurrMovie(Movie currMovie) {
        this.currMovie = currMovie;
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

        for (Movie m : list) {
            titles.add(m.getTitle());

        }

        return titles;
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
        posters.add("http://i.imgur.com/hQsw8Oe.jpg");
        posters.add("http://i.imgur.com/ZUli3ZH.jpg");
        posters.add("http://i.imgur.com/M2AYIht.jpg");
        posters.add("http://i.imgur.com/ZcMT33m.jpg");
        posters.add("http://i.imgur.com/sODzs5B.png");

        /*
        for (Movie m : list) {
            posters.add(apiManager.getPosterURL(m.getTitle()));
        }
         */
        //System.out.println("*****************************************" + posters);
        System.err.println("Number of posters: " + posters.size());
        return posters;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String searchByTitle() {
        type = SEARCH_BY_TITLE;

        return "Movies";
    }

    public String searchByTitle(String title) {
        type = SEARCH_BY_TITLE;
        System.err.println("search by title: " + title);

        return "Movies";
    }

    public void view() {
        if (currMovie != null) {
            currMovie.retrievePreferredImageUri();
            System.err.println("Updated!");
        }
        System.err.println("poop");
    }

    public List<Movie> getNowPlaying() {
       if (locationChanged)
       {
           nowPlaying = apiManager.moviesPlayingInLocalTheatres(zipCode);
       }
       else if (nowPlaying == null) {
           nowPlaying = apiManager.moviesPlayingInLocalTheatres(zipCode);
       }
       
       return nowPlaying;
    }

    public List<Movie> getSearchByTitle() {
        System.out.println("Getting movies by title");
        return apiManager.searchByTitleOmdb(this.searchTitle);
    }

    public List<Movie> getMovies() {
        switch (type) {
            case "Now Playing":
                return getNowPlaying();
            case SEARCH_BY_TITLE:
                return getSearchByTitle();
        }

        return null;
    }

    public String getMovieTableHeader() {

        return type;
    }

    public String purchaseMovie(Movie movie, Showtime showtime) {
        setSelectedShowtime(showtime);
        setCurrMovie(movie);
        return "Purchase";
    }

    public String showNowPlaying() {
        type = "Now Playing";
        
        return "Movies";
    }

    public String comingSoon() {
        return "";
    }

    public String boxOffice() {

        return "";
    }

    public String showFavorites() {
        return "";
    }

    public String showRecommended() {
        return "";
    }

    public String confirmPurchase() {
        
        return "Confirmation";
    }
}
