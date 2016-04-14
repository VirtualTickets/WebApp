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
    private List<Movie> searchByTitle;
    private String searchTitle;
    private Movie currMovie;
    private ApiManager apiManager;
    private String type;
    
    
    /**
     * Creates a new instance of ApiManager
     */
    public MovieManager() {
        type = "Now Playing";
        apiManager = new ApiManager();
        
//        nowPlaying = new ArrayList<>();
//        
//        for (int i = 0; i < 128; i++) {
//            nowPlaying.add(new Movie("Batman vs. Superman" + i));
//        }
    }

    public Movie getCurrMovie() {
        return currMovie;
    }

    public void setCurrMovie(Movie currMovie) {
        this.currMovie = currMovie;
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
    
    public void view() {
        if (getSearchTitle() == null) setSearchTitle("Batman vs Superman");
        
        else setSearchTitle(null);
    }
    
    public List<Movie> getNowPlaying() {
        if (nowPlaying == null) {
            nowPlaying = apiManager.searchOnConnectZip("24061");
        }
        return nowPlaying;
    }

    public List<Movie> getSearchByTitle() {
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
