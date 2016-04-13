/*
 * Created by Nicholas Phillpott on 2016.04.12  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

/**
 *
 * @author painter
 */
public class Movie {
    private String title; 
    private int year; 
    private String rated; 
    private String released; 
    private String runtime; 
    private String metascore; 
    private String imdbRating; 
    
    public Movie() {
        
    }
    
    public Movie(String title) {
        this.title = title;
    }

    public Movie(String title, int year, String rated, String released, String runtime, String metascore, String imdbRating) {
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
    
    
    
}
