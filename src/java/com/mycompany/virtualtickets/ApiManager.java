/*
 * Created by Nicholas Phillpott on 2016.03.29  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import org.json.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that allows the calling of API methods from various web services. 
 * 
 * 
 * @author painter
 */
public class ApiManager {

    private static final String OCAPI = "yqcmwu38uh7mf9rpgsdbqnyj";
    
    /**
     * Given movie title return the trailersapi movie trailer URL
     * 
     * @param title movie title
     * @return trailer URL
     */
    public String getTrailer(String title) {
        
        System.err.println("Getting trailer");
        
        if (title == null) {
            return "";
        }
        
        title = title.replaceAll(" ", "%20");
        
        String url = "http://trailersapi.com/trailers.json?movie=" 
                + title + "&limit=1&width=320";
        
        System.err.println("URL: " + url);
        
        try {
            JSONArray array = this.readJsonArrayFromUrl(url);
            
            if (array.length() == 0) {
                return "";
            }
            
            else if (array.getJSONObject(0).has("code")) {
                return (String) array.getJSONObject(0).get("code");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ApiManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    

    /**
     * Searches OMDb API for movie specific information
     * 
     * @param title movie title
     * @return movie information
     */
    public Movie searchForMovieOmdb(String title) {
        Movie mov = null;
        title = title.replaceAll(" ", "+");
        String url = "http://www.omdbapi.com/?t="
                + title + "&y=&plot=full&r=json&tomatoes=true";

        try {
            JSONObject json = readJsonFromUrl(url);
            //System.out.println("json: " + json);
            if (!(json.toString().toLowerCase().contains("2016")) || !(json.toString().toLowerCase().contains("2015"))) {
                url = "http://www.omdbapi.com/?t="
                        + title + "&y=2016&plot=full&r=json&tomatoes=true";
                json = readJsonFromUrl(url);

                if (json.toString().toLowerCase().contains("not found!")) {
                    url = url.replace("2016", "2015");
                    json = readJsonFromUrl(url);
                }

                //System.out.println("url: " + url);
            }
            //System.out.println("RETURNED: " + json);
            mov = new Movie();
            /*
            mov.setReleaseYear(Integer.parseInt(json.getString("year")));
            mov.setRating(json.getString("rated"));
            mov.setReleaseDate(json.getString("released"));
            mov.setRuntime(json.getString("runtime"));
            mov.setMetascore(json.getString("metascore"));
            mov.setLongDescription(json.getString("Plot"));
             */
            //mov.setTitle(json.getString("Title"));
            if (title.toLowerCase().equals("eye+in+the+sky")) {
                mov.setRTRating("94%");
                mov.setRTCriticsConsensus("As taut as it is timely, Eye in the Sky offers a powerfully acted -- and unusually cerebral -- spin on the modern wartime political thriller.");
            } else {
                mov.setRTRating(json.getString("tomatoMeter") + "%");
                mov.setRTCriticsConsensus(json.getString("tomatoConsensus"));
            }
            if (title.toLowerCase().contains("barbershop")) {
                mov.setImdbRating("6.2");
            } else {
                mov.setImdbRating(json.getString("imdbRating"));
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION CAUGHT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return null;
        }
        return mov;
    }

    /**
     * Searches OMDb for all movies matching a search term
     * 
     * @param title search term
     * @return list of movies matching term
     */
    public ArrayList<Movie> searchByTitleOmdb(String title) {
        ArrayList<Movie> movs = new ArrayList<Movie>();

        String url = "http://www.omdbapi.com/?s="
                + title + "&type=movie";

        try {
            JSONObject obj = readJsonFromUrl(url);
            JSONArray json = obj.getJSONArray("Search");
            JSONObject j;
            Movie mov;
            for (int i = 0; i < json.length(); i++) {
                j = json.getJSONObject(i);
                mov = new Movie();
                if (j.has("Title")) {
                    mov.setTitle(j.getString("Title"));
                }
                if (j.has("Year")) {
                    mov.setReleaseYear(j.getInt("Year"));
                }
                if (j.has("Poster")) {
                    mov.setPreferredImageUri(j.getString("Poster"));
                }
                movs.add(mov);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return movs;
    }

    /**
     * Search for the poster URL for a movie
     * @param title movie title 
     * @return poster URL
     */
    public String getPosterURL(String title) {
        String poster = null;

        String url = "http://www.omdbapi.com/?t="
                + title + "&y=&plot=full&r=json";

        //System.err.println("URL: " + url);

        try {
            JSONObject json = readJsonFromUrl(url);
            poster = json.getString("Poster");

        } catch (Exception e) {
            System.out.println("EXCEPTION GETTING POSTER URL!!!!!!!!!");
            e.printStackTrace();
            return null;
        }
        //System.out.println("title to find poster for: " + title + "  poster url: " + poster);
        return poster;
    }

    /**
     * @deprecated @param zip
     * @return
     */
    public ArrayList<Movie> searchOnConnectZip(String zip) {
        return moviesPlayingInLocalTheatres(zip);
    }

    //--------------------------------------------------------------------------
    // Important Stuff below
    
    
    /**
     * Search the Apiary 'themoviedb' API for a list of the countries most 
     * popular movies
     * @param zip 
     * @return
     */
    public ArrayList<Movie> findPopularMovies() {
        ArrayList<Movie> movs = new ArrayList<Movie>();

        for (int k = 1; k <= 3; k++) {

            String url = "http://api.themoviedb.org/3/discover/movie?api_key="
                    + "129068269e8d1c7dd718729d89908946"
                    + "&primary_release_date.gte="
                    + "2016-01-01"
                    + "&page=" + k
                    + "&sort_by=popularity.desc&certification_country=US";

            try {
                JSONObject json = readJsonFromUrl(url);
                JSONArray jarray = json.getJSONArray("results");
                JSONObject j; 
                Movie mov; 

                for (int i = 0; i < jarray.length(); i++) {
                    j = jarray.getJSONObject(i);
                    mov = new Movie(); 
                    mov.setTitle(j.getString("title"));
                    mov.setLongDescription(j.getString("overview"));
                    mov.setReleaseDate(j.getString("release_date"));
                    //mov.setPreferredImageUri(j.getString("poster_path"));

                    movs.add(mov);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return movs;
    }
    
    
    
    
    /**
     * OnConnect API:
     * Search for theatres near a zip 
     * @param zip
     * @return
     */
    public ArrayList<Theatre> findTheatres(String zip) {
        ArrayList<Theatre> the = new ArrayList<Theatre>();

        // Build API URL with key
        String url = "http://data.tmsapi.com/v1.1/theatres?zip="
                + zip
                + "&api_key="
                + OCAPI;

        // Read JSONObject from URL
        try {
            JSONArray jarray = readJsonArrayFromUrl(url);
            JSONObject theatre;

            // Parse the Theatres from the JSONObject
            for (int i = 0; i < jarray.length(); i++) {
                theatre = jarray.getJSONObject(i);
                the.add(jsonToTheatre(theatre));
            }
        } catch (Exception e) {
            return null;
        }
        return the;
    }

    /**
     * OnConnect API:
     * Returns specific data about a theatre
     * @param theatreId
     * @return 
     */
    public Theatre theatreDetails(String theatreId) {
        Theatre the = new Theatre();

        String url = "http://data.tmsapi.com/v1.1/theatres/"
                + theatreId
                + "?api_key="
                + OCAPI;

        try {
            JSONObject json = readJsonFromUrl(url);
            the = jsonToTheatre(json);
        } catch (Exception e) {
            return null;
        }
        return the;
    }

    /**
     * OnConnect API:
     * Returns a list of all show times for a theatre
     * @param zip
     * @return
     */
    public ArrayList<Movie> theatreShowtimes(String theatreId) {
        ArrayList<Movie> movs = new ArrayList<Movie>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String d = dateFormat.format(date);

        String url = "http://data.tmsapi.com/v1.1/theatres/"
                + theatreId
                + "/showings?startDate="
                + d
                + "&api_key="
                + OCAPI;
        try {
            JSONArray json = readJsonArrayFromUrl(url);
            JSONObject movie;

            for (int i = 0; i < json.length(); i++) {
                movie = json.getJSONObject(i);
                movs.add(jsonToMovie(movie));
            }
        } catch (Exception e) {
            return null;
        }
        return movs;
    }

    /**
     * OnConnect API:
     * Returns all the local show times for a given theatre
     * @param theatreId
     * @param zip
     * @return
     */
    public ArrayList<Movie> movieShowtimes(String tmsId, String zip) {
        ArrayList<Movie> movs = new ArrayList<Movie>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String d = dateFormat.format(date);

        String url = "http://data.tmsapi.com/v1.1/movies/"
                + tmsId
                + "/showings?startDate="
                + d
                + "&zip="
                + zip
                + "&api_key="
                + OCAPI;
        try {
            JSONArray json = readJsonArrayFromUrl(url);
            JSONObject movie;

            for (int i = 0; i < json.length(); i++) {
                movie = json.getJSONObject(i);
                movs.add(jsonToMovie(movie));
            }
        } catch (Exception e) {
            return null;
        }
        return movs;
    }

    /**
     * OnConnect API:
     * Returns list of all movies playing in a specific area
     * @param zip
     * @return
     */
    public ArrayList<Movie> moviesPlayingInLocalTheatres(String zip) {
        ArrayList<Movie> movs = new ArrayList<Movie>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String d = dateFormat.format(date);

        String url = "http://data.tmsapi.com/v1.1/movies/showings?startDate="
                + d
                + "&zip="
                + zip
                + "&api_key=" + OCAPI;

        try {
            JSONArray json = readJsonArrayFromUrl(url);
            JSONObject movie;

            for (int i = 0; i < json.length(); i++) {
                movie = json.getJSONObject(i);
                movs.add(jsonToMovie(movie));
            }
        } catch (Exception e) {
            return null;
        }
        Collections.sort(movs);
        return movs;
    }

    /**
     * Given a JSON object representing an OnConnect movie transfer it to a movie
     * object
     * @param json
     * @return
     */
    private Movie jsonToMovie(JSONObject json) {
        Movie mov = new Movie();

        //System.err.println(json);

        try {
            mov.setTmsId(json.getString("tmsId"));
        } catch (Exception e) {
            mov.setTmsId("tmsId");
        }

        try {
            mov.setTitle(json.getString("title"));
        } catch (Exception e) {
            mov.setTitle("title");
        }

        try {
            mov.setReleaseYear(json.getInt("releaseYear"));
        } catch (Exception e) {
            mov.setReleaseYear(9999);
        }

        try {
            mov.setReleaseDate(json.getString("releaseDate"));
        } catch (Exception e) {
            mov.setReleaseDate("releaseDate");
        }

        try {
            mov.setLongDescription(json.getString("longDescription"));
        } catch (Exception e) {
            mov.setLongDescription("longDescription");
        }

        try {
            mov.setRating(json.getJSONArray("ratings").getJSONObject(0).getString("code"));
        } catch (Exception e) {
            mov.setRating("rating");
        }

        try {
            mov.setRuntime(json.getString("runTime"));
        } catch (Exception e) {
            mov.setRuntime(null);
        }

        try {
            mov.setPreferredImageUri(
                    "http://developer.tmsimg.com/"
                    + json.getJSONObject("preferredImage").getString("uri")
                    + "?api_key="
                    + OCAPI);
        } catch (Exception e) {
            mov.setPreferredImageUri("Image not found");
        }

        try {
            JSONArray showTimes = json.getJSONArray("showtimes");
            mov.setShowtimes(arrayOfShowtimes(showTimes));
        } catch (Exception e) {
            mov.setShowtimes(null);
        }

        return mov;
    }

    /**
     * Given a JSONArray returns a list of showtimes
     * @param jarray JSONArray as list of showtimes
     * @return
     */
    private List<Showtime> arrayOfShowtimes(JSONArray jarray) {
        ArrayList<Showtime> timeList = new ArrayList<Showtime>();
        Showtime time;
        for (int i = 0; i < jarray.length(); i++) {
            time = jsonToShowtime(jarray.getJSONObject(i));
            timeList.add(time);
        }
        return timeList;
    }

    /**
     * Converts an OnConnect show time JSONObject to a show time
     * @param json
     * @return
     */
    private Showtime jsonToShowtime(JSONObject json) {
        Showtime time = new Showtime();

        try {
            time.setTheatreId(json.getJSONObject("theatre").getString("id"));
        } catch (Exception e) {
            time.setTheatreId(null);
        }

        try {
            time.setTheatreName(json.getJSONObject("theatre").getString("name"));
        } catch (Exception e) {
            time.setTheatreId(null);
        }

        try {
            time.setDate(json.getString("dateTime").substring(0, 10));
            time.setTime(json.getString("dateTime").substring(11));
        } catch (Exception e) {
            time.setDate("date");
            time.setTime("time");
        }

        return time;
    }

    /**
     * Takes an OnConnect theatre JSONObject to a Theatre 
     * @param json
     * @return
     */
    private Theatre jsonToTheatre(JSONObject json) {
        Theatre the = new Theatre();

        try {
            the.setTheatreId(json.getString("theatreId"));
        } catch (Exception e) {
            the.setTheatreId("threateId");
        }

        try {
            the.setName(json.getString("name"));
        } catch (Exception e) {
            the.setName("name");
        }

        try {
            the.setTelephone(json.getJSONObject("location").getString("telephone"));
        } catch (Exception e) {
            the.setTelephone(null);
        }

        try {
            JSONObject address = json.getJSONObject("location").getJSONObject("address");
            the.setAddress(
                    address.getString("street") + " "
                    + address.getString("state") + " "
                    + address.getString("city") + " "
                    + address.getString("postalCode"));
        } catch (Exception e) {
            the.setAddress("address");
        }

        return the;
    }

    /**
     * Reads a JSONObject from a called URL
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * Reads a JSONArray from a called URL
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * Helper method for reading URL pages. Builds a string from buffered input. 
     * @param rd
     * @return
     * @throws IOException
     */
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
