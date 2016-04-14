/*
 * Created by Nicholas Phillpott on 2016.04.12  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import org.json.*;


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
    private String description; 
    private String imdbRating;
    private JSONArray showtimes;
    private String poster; 
    
    
    public Movie() {
    }
    
    public Movie(String title) {
        this.title = title;
        this.released = "Mar 31 2016";
        this.rated = "R";
        this.imdbRating = "9.9";
        this.metascore = "9.8";
        this.runtime = "1:59";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public JSONArray getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(JSONArray showtimes) {
        this.showtimes = showtimes;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
