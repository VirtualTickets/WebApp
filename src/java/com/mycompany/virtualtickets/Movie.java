/*
 * Created by Nicholas Phillpott on 2016.04.12  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author painter
 */
public class Movie implements Comparable<Movie> {
    
    private String tmsId; 
    private String title; 
    private int releaseYear; 
    private String releaseDate;
    private String longDescription; 
    private String rating; 
    private String runtime; 
    private String preferredImageUri; 
    private List<Showtime> showtimes;  
    private String metascore; 
    private String imdbRating; 
    
    public Movie() {
        
    }
    
    public Movie(String title) {
        this.title = title;
    }

    public String getTmsId() {
        return tmsId;
    }

    public void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        int hours = Integer.parseInt(runtime.substring(2, 4));
        int minutes = Integer.parseInt(runtime.substring(5, 7));
        
        return hours + ":" + minutes;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getPreferredImageUri() {
        if (preferredImageUri == null) {
            retrievePreferredImageUri();
        }
        return preferredImageUri;
    }
    
    public void retrievePreferredImageUri() {
        if (preferredImageUri == null) {
            setPreferredImageUri(new ApiManager().getPosterURL(title));
        }
    }

    public void setPreferredImageUri(String preferredImageUri) {
        this.preferredImageUri = preferredImageUri;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
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
    
    public List<TheatreWithShowtimes> getTheatres() {
        ArrayList<TheatreWithShowtimes> list = new ArrayList<>();
        
        for (Showtime s : showtimes) {
            TheatreWithShowtimes t = new TheatreWithShowtimes(s.getTheatreId(), s.getTheatreName());
            if (!list.contains(t)) {
                list.add(t);
            }
            
            int index = list.lastIndexOf(t);
            list.get(index).addShowtime(s);
        }
        
        return list;
    }

    @Override
    public int compareTo(Movie m) {
        return title.compareTo(m.title);
    }
    
    
}
