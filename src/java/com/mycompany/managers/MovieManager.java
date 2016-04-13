/*
 * Created by Benjamin Sweeney on 2016.04.13  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.managers;

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

    private List<Movie> movies;
    
    /**
     * Creates a new instance of ApiManager
     */
    public MovieManager() {
        
        movies = new ArrayList<>();
        
        Movie m = new Movie();
        m.setTitle("Batman vs. Superman");
        
        for (int i = 0; i < 128; i++) {
            movies.add(m);
        }
    }
    
    public List<Movie> getMovies() {
        
        return movies;
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
