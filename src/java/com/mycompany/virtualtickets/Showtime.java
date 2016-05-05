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
    
    public Showtime() {
        
    }
    
    /**
     * Theatre ID getter and setter
     * @return theatreId
     */
    public String getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(String theatreId) {
        this.theatreId = theatreId;
    }

    /**
     * Theatre Name getter and setter
     * @return theatreName
     */
    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    /**
     * Date getter and setter
     * @return date
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * The function to get the string of the parsed time in standard time.
     * @return 
     */
    public String getTime() {
        
        return parseTime();
    }
    
    /**
     * getHour returns the hour of the showtime
     * @return 
     */
    public int getHour() {
        if (time == null || time.length() == 0) {
            return 0;
        }
        return Integer.parseInt(time.substring(0, 2));
    }
    
    /**
     * getMinute returns the minutes of the showtime
     * @return 
     */
    public int getMinute() {
        if (time == null || time.length() == 0) {
            return 0;
        }
        return Integer.parseInt(time.substring(3));
    
    }

    /**
     * setTime sets the time to a new string time.
     * @param time 
     */
    public void setTime(String time) {
        this.time = time;
    }
    
    /**
     * Parses the time into a time that is in am/pm format
     * @return The parsed time with am/pm format
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
