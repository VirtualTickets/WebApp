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
 * A movie object that contains all the attributes we get from the API call.
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
    private String rtRating;
    private String rtCriticsConsensus;


    public Movie() {
        //Empty Constructor
    }

    public Movie(Favorited f) {
        this.tmsId = f.getFavoritedPK().getMovieId();
    }

    public Movie(String tmsId) {
        this.tmsId = tmsId;
    }

    public Movie(String tmsId, String zipcode) {
        this.tmsId = tmsId;
        this.zipcode = zipcode;
    }

    public static List<Movie> convertFavorited(List<Favorited> favorited, String zipcode) {
        List<Movie> list = new ArrayList<>();

        for (Favorited f : favorited) {
            list.add(new Movie(f.getFavoritedPK().getMovieId(), zipcode));
        }

        return list;
    }
    /**
     * Gets url for the trailer from the trailer api.
     * @return the string url of the trailer
     */
    public String getTrailer() {
        String ret = new ApiManager().getTrailer(this.getTitle());
        return ret;
    }

    /**
     * Tms ID getter and setter
     * @return tmsID
     */
    public String getTmsId() {
        return tmsId;
    }

    public void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    /**
     * Title getter and setter. If title is null, set up the title.
     * @return title
     */
    public String getTitle() {
        if (title == null) {
            set();
        }

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Release year getter and setter
     * @return releaseYear
     */
    public int getReleaseYear() {
        if (releaseYear == 0) {
            set();
        }
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * ReleaseDate getter and setter Converts the release day into a readable date
     * @return release Date
     */
    public String getReleaseDate() {

        if (releaseDate == null) {
            set();
        }
        if (releaseDate == null) {
            return null;
        }

        int month;
        try {
            month = Integer.parseInt(releaseDate.substring(5, 7));
        } catch (Exception e) {
            return null;
        }
        String year = releaseDate.substring(0, 4);
        String day = releaseDate.substring(8);
        return Constants.MONTHS[month - 1] + " " + day + ", " + year;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Long description getter and setter
     * @return longDescription
     */
    public String getLongDescription() {
        if (longDescription == null) {
            set();
        }
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * Rating getter and setter
     * @return rating
     */
    public String getRating() {
        if (rating == null) {
            set();
        }
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    /**
     * Run time getter and setter
     * @return runtime
     */
    public String getRuntime() {
        if (runtime == null) {
            set();
        }
        if (runtime == null) {
            return null;
        }
        int hours = Integer.parseInt(runtime.substring(2, 4));
        int minutes = Integer.parseInt(runtime.substring(5, 7));

        return String.format("%d:%02d", hours, minutes);
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     * Gets the URL image of the poster for the movie
     * @return preferredImageUri
     */
    public String getPreferredImageUri() {
        if (preferredImageUri == null) {
            retrievePreferredImageUri();
        }
        return preferredImageUri;
    }
    
    /**
     * Retrieves the preferredImageUri from the api manager.
     */
    public void retrievePreferredImageUri() {
        if (preferredImageUri == null) {
            setPreferredImageUri(new ApiManager().getPosterURL(getTitle()));
        }
    }

    /**
     * preferredImageURI setter
     * @param preferredImageUri 
     */
    public void setPreferredImageUri(String preferredImageUri) {
        this.preferredImageUri = preferredImageUri;
    }

    /**
     * showtimes getter and setter
     * @return showtimes
     */
    public List<Showtime> getShowtimes() {
        if (showtimes == null) {
            set();
        }
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
    /**
     * Metascore getter and setter
     * @return metascore
     */
    public String getMetascore() {
        if (metascore == null) {
            set();
        }
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    /**
     * Imbd rating getter and setter
     * @return imbdRating
     */    
    public String getImdbRating() {
        if (imdbRating == null) {
            set();
        }
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
    
    /**
     * rtRating getter and setter
     * @return rtRating
     */
    public String getRTRating() {
        return rtRating;
    }

    public void setRTRating(String rtRating) {
        this.rtRating = rtRating;
    }

    /**
     * RT critics consensus getter and setter
     * @return rtCriticsConsensus
     */
    public String getRTCriticsConsensus() {
        return rtCriticsConsensus;
    }

    public void setRTCriticsConsensus(String rtCriticsConsensus) {
        this.rtCriticsConsensus = rtCriticsConsensus;
    }

    /**
     * Gets all the theatres corresponding to the current selected zipcode.
     * @return list List of all the movies in the zip code
     */
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
        if (title != null && m.title != null) {
            return title.compareTo(m.title);
        }
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

    /**
     * Accesses the api manager with the list of movies
     */
    private void set() {
        this.set(new ApiManager().movieShowtimes(tmsId, zipcode));
    }

    /**
     * Sets the showtimes of the movie from the one got from the Api manager.
     * @param movieShowtimes A list of showtimes.
     */
    private void set(ArrayList<Movie> movieShowtimes) {
        if (movieShowtimes != null && movieShowtimes.size() > 0) {
            this.set(movieShowtimes.get(0));
        }
    }

    /**
     * Set to the movie object to the information got from the API
     * @param movie 
     */
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
