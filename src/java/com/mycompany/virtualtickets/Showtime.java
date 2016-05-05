/*
 * Created by Nicholas Phillpott on 2016.04.20  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

/**
 * The Showtime object that conatins the showtimes, theatre locations, and dates.
 * 
 * @author painter
 */
public class Showtime {
    
    private String theatreId;
    private String theatreName;
    private String date; 
    private String time; 
    
    /**
     *Constructor
     */
    public Showtime() {
        
    }

    /**
     *get theatre Id
     * 
     * @return
     */
    public String getTheatreId() {
        return theatreId;
    }

    /**
     *set theatre Id
     * 
     * @param theatreId
     */
    public void setTheatreId(String theatreId) {
        this.theatreId = theatreId;
    }

    /**
     *get theatre name
     * 
     * @return
     */
    public String getTheatreName() {
        return theatreName;
    }

    /**
     *set theatre name
     * 
     * @param theatreName
     */
    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    /**
     *get date
     * 
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     *set date
     * 
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *get time
     * 
     * @return
     */
    public String getTime() {
        
        return parseTime();
    }
    
    /**
     * get the hour from the time
     * @return
     */
    public int getHour() {
        if (time == null || time.length() == 0) {
            return 0;
        }
        return Integer.parseInt(time.substring(0, 2));
    }
    
    /**
     *get the minute from the time
     * 
     * @return
     */
    public int getMinute() {
        if (time == null || time.length() == 0) {
            return 0;
        }
        return Integer.parseInt(time.substring(3));
    
    }

    /**
     *set time
     * 
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }
    
    /**
     Parse the time into a 12-hour AM/PM format
     */
    private String parseTime() {
        if (time == null || time.length() == 0) {
            return "";
        }
        int hour = Integer.parseInt(time.substring(0, 2));
        String minute = time.substring(3);
        String type;
        
        if (hour == 0) {
            hour = 12;
            type = "AM";
        } 
        else if (hour == 12) {
            type = "PM";
        }
        else if (hour > 12) {
            hour -= 12;
            type = "PM";
        }
        else {
            type = "AM";
        }
        
        return hour + ":" + minute + " " + type;
    }
}
