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

/**
 *
 * @author painter
 */
public class ApiManager {

    private static final String OCAPI = "yqcmwu38uh7mf9rpgsdbqnyj";

    /**
     *
     * @param title
     * @return
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
     *
     * @param title
     * @return
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
     *
     * @param title
     * @return
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
     *
     * @param zip
     * @return
     */
    public ArrayList<Theatre> findTheatres(String zip) {
        ArrayList<Theatre> the = new ArrayList<Theatre>();

        String url = "http://data.tmsapi.com/v1.1/theatres?zip="
                + zip
                + "&api_key="
                + OCAPI;

        try {
            JSONArray jarray = readJsonArrayFromUrl(url);
            JSONObject theatre;

            for (int i = 0; i < jarray.length(); i++) {
                theatre = jarray.getJSONObject(i);
                the.add(jsonToTheatre(theatre));
            }
        } catch (Exception e) {
            return null;
        }
        return the;
    }

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
     *
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
     *
     * @param theatreId
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
     *
     * @param zip
     * @return
     */
    public ArrayList<Movie> moviesPlayingInLocalTheatres(String zip) {
        //System.out.println("Searching for movies with zip: " + zip);
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
     *
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
     *
     * @param jarray
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
     *
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
     *
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
     *
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
     *
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
     *
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
