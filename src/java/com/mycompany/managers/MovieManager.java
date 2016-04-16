/*
 * Created by Benjamin Sweeney on 2016.04.13  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.virtualtickets.ApiManager;
import com.mycompany.virtualtickets.Movie;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private ApiManager apiManager;
    private String type;
    private String zipCode;
    private String checkoutAs = "Guest";
    
    /**
     * Creates a new instance of ApiManager
     */
    public MovieManager() {
        type = "Now Playing";
        zipCode = "24061";
        apiManager = new ApiManager();
        
        nowPlaying = new ArrayList<>();
        
        for (int i = 0; i < 128; i++) {
            nowPlaying.add(new Movie("Batman vs. Superman" + i));
        }
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
    
    public void changeLocation() {
        //ArrayList<String> temp = getNowPlayingTitles();
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
     * gets the titles of the top 5 movies. For now this is simply the first
     * 5 returned, but it should be based upon highest grossing or something.
     * @return the list of top 5 titles
     */
    public ArrayList<String> getTopNowPlayingTitles() {
        ArrayList<String> titles = new ArrayList<>();
        List<Movie> list = getNowPlaying();
        
        for (int i=0; i <= 5; i++) {
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

        
        for (Movie m : list) {
            posters.add(m.getPoster());
        }
        
        System.out.println(posters);
        return posters;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }
    
    public String searchByTitle() {
        type = "Search By Title";
        
        return "Movies";
    }
    
    public String searchByTitle(String title) {
        type = "Search By Title";
        System.out.println("search by title: " + title);

        return "Movies";
    }
    
    public void view() {
        if (getSearchTitle() == null) setSearchTitle("Batman vs Superman");
        
        else setSearchTitle(null);
    }
    
    public List<Movie> getNowPlaying() {
//        if (nowPlaying == null) {
//            nowPlaying = apiManager.searchOnConnectZip(zipCode);
//        }
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
            case "Search By Title":
                return getSearchByTitle();
        }
        
        return null;
    }
    
    public String getMovieTableHeader() {
        
        return type;
    }
    
    public String purchase() {
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
