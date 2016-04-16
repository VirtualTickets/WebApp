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

/**
 *
 * @author painter
 */
public class ApiManager {

      private static final String[] OCAPI = {"yqcmwu38uh7mf9rpgsdbqnyj", "fw8t9na72xqh8ggq8abcgya8", "xj3ferv39aeteuxmbyv56j9d", "s8f22txztahfh3uttvhw4tj7","bkenv8zj8vxssgnewvveqsv5"};
      private int keyIdx = 0;
//    public static void main(String args[]) {
//        ArrayList<Movie> movs = searchOnConnectZip("23453");
//        for (Movie i : movs) {
//            System.out.println(i.getTitle() + " "
//                    + i.getReleased() + " "
//                    + i.getRated() + " "
//                    + i.getRuntime() + " "
//                    + i.getShowtimes().toString() + "\n\n");
//        }
//
//    }
    /**
     *
     * @param title
     * @return
     */
    public Movie searchForMovieOmdb(String title) {
        Movie mov = null;

        String url = "http://www.omdbapi.com/?t="
                + title + "&y=&plot=full&r=json";

        try {
            JSONObject json = readJsonFromUrl(url);
            System.out.println(json);
            mov = new Movie();
            mov.setTitle(json.getString("title"));
            mov.setYear(Integer.parseInt(json.getString("year")));
            mov.setRated(json.getString("rated"));
            mov.setReleased(json.getString("released"));
            mov.setRuntime(json.getString("runtime"));
            mov.setMetascore(json.getString("metascore"));
            mov.setImdbRating(json.getString("imbdscore"));
            mov.setDescription(json.getString("Plot"));
        } catch (Exception e) {
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
            JSONArray json = readJsonArrayFromUrl(url);
            JSONObject j;
            Movie mov; 
            for (int i = 0; i < json.length(); i++) {
                j = json.getJSONObject(i);
                mov = new Movie();
                mov.setTitle(j.getString("Title"));
                mov.setYear(j.getInt("Year"));
                mov.setPoster(j.getString("Poster"));
                movs.add(mov);
            }
        } catch (Exception e) {
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

        try {
            JSONObject json = readJsonFromUrl(url);
            poster = json.getString("Poster");

        } catch (Exception e) {
            return null;
        }
        return poster;
    }

    public ArrayList<Movie> searchOnConnectZip(String zip) {
        System.out.println("new zip: " + zip);
        ArrayList<Movie> movs = new ArrayList<Movie>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String d = dateFormat.format(date);

       keyIdx = 4;
        
        String url = "http://data.tmsapi.com/v1.1/movies/showings?startDate="
                + d
                + "&zip="
                + zip
                + "&api_key=" + OCAPI[keyIdx];

        System.out.println(url);
        
        try {
            JSONArray json = readJsonArrayFromUrl(url);
            JSONObject j;
            Movie mov;
            for (int i = 0; i < json.length(); i++) {
                j = json.getJSONObject(i);
                mov = new Movie();
                mov.setTitle(j.getString("title"));
                mov.setYear(j.getInt("releaseYear"));
                //mov.setRated(j.getJSONArray("ratings").getJSONObject(0).getString("code"));
                mov.setReleased(j.getString("releaseDate"));
                mov.setRuntime("120min");
//                mov.setRuntime(j.getString("runTime"));
                mov.setShowtimes(j.getJSONArray("showtimes"));
                mov.setDescription(j.getString("longDescription"));
                //System.out.println("Poster: " + j.getString("preferredImage").toString());
                //System.out.println(j);
                movs.add(mov);
            }

        } catch (Exception e) {
            
            //If nothing is returned, try with a different key. 
            /*if (e instanceof IOException && e.toString().regionMatches(57, "403", 0, 3)) {
                if (keyIdx < OCAPI.length - 1) {
                    keyIdx++;
                    return searchOnConnectZip(zip);
                }
            }*/
            
            return null;
        }

        return movs;
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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

    public JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
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

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
