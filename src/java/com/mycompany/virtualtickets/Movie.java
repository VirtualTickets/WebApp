/*
 * Created by Nicholas Phillpott on 2016.04.12  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import com.mycompany.entities.Favorited;
import com.mycompany.entities.FavoritedPK;
import com.mycompany.managers.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private String zipcode;
    
    public static List<Movie> convertFavorited(List<Favorited> favorited, String zipcode) {
        List<Movie> list = new ArrayList<>();
        
        for (Favorited f : favorited) {
            list.add(new Movie(f.getFavoritedPK().getMovieId(), zipcode));
        }
        
        return list;
    }
    private String rtRating;
    private String rtCriticsConsensus;
    
    public Movie() {
        
    }
    
    public Movie(Favorited f) {
        this.tmsId = f.getFavoritedPK().getMovieId();
    }
    
    public Movie(String tmsId) {
        this.tmsId = tmsId;
    }
    
    public Movie (String tmsId, String zipcode) {
        this.tmsId = tmsId;
        this.zipcode = zipcode;
    }

    public String getTmsId() {
        return tmsId;
    }

    public void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    public String getTitle() {
        if (title == null) {
            set();
        }
        
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        if (releaseYear == 0) {
            set();
        }
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getReleaseDate() {
        if (releaseDate == null) {
            set();
        }
        int month = Integer.parseInt(releaseDate.substring(5, 7));
        String year = releaseDate.substring(0, 4);
        String day = releaseDate.substring(8);
        return Constants.MONTHS[month - 1] + " " + day + ", " + year;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLongDescription() {
        if (longDescription == null) {
            set();
        }
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getRating() {
        if (rating == null) {
            set();
        }
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        if (runtime == null) {
            set();
        }
        int hours = Integer.parseInt(runtime.substring(2, 4));
        int minutes = Integer.parseInt(runtime.substring(5, 7));
        
        return hours + ":" + minutes;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getPreferredImageUri() {
        System.err.println("Uri pre update: " + preferredImageUri);
        System.err.println("Title: |" + getTitle() + "|");
        if (preferredImageUri == null) {
            retrievePreferredImageUri();
        }
        return preferredImageUri;
    }
    
    public void retrievePreferredImageUri() {
        if (preferredImageUri == null) {
            setPreferredImageUri(new ApiManager().getPosterURL(getTitle()));
        }
    }

    public void setPreferredImageUri(String preferredImageUri) {
        this.preferredImageUri = preferredImageUri;
    }

    public List<Showtime> getShowtimes() {
        if (showtimes == null) {
            set();
        }
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    public String getMetascore() {
        if (metascore == null) {
            set();
        }
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getImdbRating() {
        if (imdbRating == null) {
            set();
        }
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
    
    public String getRTRating() {
        return rtRating;
    }

    public void setRTRating(String rtRating) {
        this.rtRating = rtRating;
    }
    
    public String getRTCriticsConsensus() {
        return rtCriticsConsensus;
    }

    public void setRTCriticsConsensus(String rtCriticsConsensus) {
        this.rtCriticsConsensus = rtCriticsConsensus;
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
        if (title != null && m.title != null) return title.compareTo(m.title);
        return tmsId.compareTo(m.tmsId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (!Objects.equals(this.tmsId, other.tmsId)) {
            return false;
        }
        return true;
    }
    
    
    
    private void set() {
        this.set(new ApiManager().movieShowtimes(tmsId, zipcode));
    }

    private void set(ArrayList<Movie> movieShowtimes) {
        if (movieShowtimes != null && movieShowtimes.size() > 0) {
            this.set(movieShowtimes.get(0));
        }
    }
    
    
    private void set(Movie movie) {
        this.imdbRating = movie.imdbRating;
        this.longDescription = movie.longDescription;
        this.metascore = movie.metascore;
        this.rating = movie.rating;
        this.releaseDate = movie.releaseDate;
        this.releaseYear = movie.releaseYear;
        this.runtime = movie.runtime;
        this.showtimes = movie.showtimes;
        this.title = movie.title;
    }
    
    
}
