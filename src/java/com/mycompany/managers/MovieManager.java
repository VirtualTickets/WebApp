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
    private String currTitle;
    private Movie currMovie;
    
    /**
     * Creates a new instance of ApiManager
     */
    public MovieManager() {
        
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
    
    

    public String getCurrTitle() {
        return currTitle;
    }

    public void setCurrTitle(String currTitle) {
        this.currTitle = currTitle;
    }
    
    
    
    public void view() {
        if (getCurrTitle() == null) setCurrTitle("Batman vs Superman");
        
        else setCurrTitle(null);
    }
    
    public List<Movie> getNowPlaying() {
        if (nowPlaying == null) {
            nowPlaying = ApiManager.searchOnConnectZip("24061");
        }
        return nowPlaying;
    }
    
    public String getMovieTableHeader() {
        
        return "Movies";
    }
    
    public String purchase() {
        return "Purchase";
    }
    
    public String nowPlaying() {
        return "";
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
