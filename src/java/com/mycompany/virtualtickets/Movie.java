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

    /**
     *Change a favorited list into a list of movies
     * 
     * @param favorited
     * @param zipcode
     * @return
     */
    public static List<Movie> convertFavorited(List<Favorited> favorited, String zipcode) {
        List<Movie> list = new ArrayList<>();

        for (Favorited f : favorited) {
            list.add(new Movie(f.getFavoritedPK().getMovieId(), zipcode));
        }

        return list;
    }
    private String rtRating;
    private String rtCriticsConsensus;

    /**
     *Constructor
     * 
     */
    public Movie() {

    }

    /**
     * Get the trailer for this movie
     * 
     * @return
     */
    public String getTrailer() {
        String ret = new ApiManager().getTrailer(this.getTitle());
        return ret;
    }

    /**
     *Constructor based on a favorited entry
     * 
     * @param f
     */
    public Movie(Favorited f) {
        this.tmsId = f.getFavoritedPK().getMovieId();
    }

    /**
     *Constructor based on theatre movie showtime ID
     * 
     * @param tmsId
     */
    public Movie(String tmsId) {
        this.tmsId = tmsId;
    }

    /**
     *Constructor based on theatre movie showtime ID and zipcode
     * 
     * @param tmsId
     * @param zipcode
     */
    public Movie(String tmsId, String zipcode) {
        this.tmsId = tmsId;
        this.zipcode = zipcode;
    }

    /**
     *Get the tmsId
     * 
     * @return
     */
    public String getTmsId() {
        return tmsId;
    }

    /**
     *set the tmsId
     * 
     * @param tmsId
     */
    public void setTmsId(String tmsId) {
        this.tmsId = tmsId;
    }

    /**
     *get the title
     * 
     * @return
     */
    public String getTitle() {
        if (title == null) {
            set();
        }

        return title;
    }

    /**
     *set the title
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *get the release year
     * 
     * @return
     */
    public int getReleaseYear() {
        if (releaseYear == 0) {
            set();
        }
        return releaseYear;
    }

    /**
     *set the release year
     * 
     * @param releaseYear
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     *get the release date
     * 
     * @return
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

    /**
     *set the release date
     * 
     * @param releaseDate
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     *get the long description
     * 
     * @return
     */
    public String getLongDescription() {
        if (longDescription == null) {
            set();
        }
        return longDescription;
    }

    /**
     *set the long description
     * 
     * @param longDescription
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     *get the rating
     * 
     * @return
     */
    public String getRating() {
        if (rating == null) {
            set();
        }
        return rating;
    }

    /**
     *set the rating
     * 
     * @param rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * get the runtime
     * @return
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

    /**
     *set the runtime
     * 
     * @param runtime
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     *get the preferredImageUri
     * 
     * @return
     */
    public String getPreferredImageUri() {
        if (preferredImageUri == null) {
            retrievePreferredImageUri();
        }
        return preferredImageUri;
    }

    /**
     *Give this the preferred image Uri from the API
     * 
     */
    public void retrievePreferredImageUri() {
        if (preferredImageUri == null) {
            setPreferredImageUri(new ApiManager().getPosterURL(getTitle()));
        }
    }

    /**
     *set the preferred image Uri
     * 
     * @param preferredImageUri
     */
    public void setPreferredImageUri(String preferredImageUri) {
        this.preferredImageUri = preferredImageUri;
    }

    /**
     *get showtimes
     * 
     * @return
     */
    public List<Showtime> getShowtimes() {
        if (showtimes == null) {
            set();
        }
        return showtimes;
    }

    /**
     *set showtimes
     * 
     * @param showtimes
     */
    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    /**
     *get the metascore
     * 
     * @return
     */
    public String getMetascore() {
        if (metascore == null) {
            set();
        }
        return metascore;
    }

    /**
     *set the metascore
     * 
     * @param metascore
     */
    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    /**
     *get the imdb rating
     * 
     * @return
     */
    public String getImdbRating() {
        if (imdbRating == null) {
            set();
        }
        return imdbRating;
    }

    /**
     *set the imdb rating
     * 
     * @param imdbRating
     */
    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    /**
     *get Rotten tomatoes rating
     * 
     * @return
     */
    public String getRTRating() {
        return rtRating;
    }

    /**
     *set Rotten tomatoes rating
     * 
     * @param rtRating
     */
    public void setRTRating(String rtRating) {
        this.rtRating = rtRating;
    }

    /**
     *get rotten tomatoes critic consensus
     * 
     * @return
     */
    public String getRTCriticsConsensus() {
        return rtCriticsConsensus;
    }

    /**
     *set rotten tomatoes critic consensus
     * 
     * @param rtCriticsConsensus
     */
    public void setRTCriticsConsensus(String rtCriticsConsensus) {
        this.rtCriticsConsensus = rtCriticsConsensus;
    }

    /**
     *get the theatres and showtimes for this movie
     * 
     * @return
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
     Set this movie with a new showtime set
     */
    private void set() {
        this.set(new ApiManager().movieShowtimes(tmsId, zipcode));
    }

    //helper for set()
    private void set(ArrayList<Movie> movieShowtimes) {
        if (movieShowtimes != null && movieShowtimes.size() > 0) {
            this.set(movieShowtimes.get(0));
        }
    }

    //set this to a different movie
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
