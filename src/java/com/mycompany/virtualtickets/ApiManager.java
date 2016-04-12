/*
 * Created by Nicholas Phillpott on 2016.03.29  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author painter
 */
public class ApiManager {
    /**
     *
     * @param title
     * @return
     */
    public Movie searchOmdb(String title) {
        Movie mov = null;

        String url = "http://www.omdbapi.com/?t="
                + title + "&y=&plot=full&r=json";

        try {
            JSONObject json = readJsonFromUrl(url);
            mov = new Movie (
            json.getString("title"),
            json.getInt("year"), 
            json.getString("rated"),
            json.getString("released"),
            json.getString("runtime"),
            json.getString("metascore"),
            json.getString("imbdscore"));
        } catch (Exception e) {
            return mov;
        }
        return mov;
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
