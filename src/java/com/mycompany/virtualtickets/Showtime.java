/*
 * Created by Nicholas Phillpott on 2016.04.20  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

/**
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

    public String getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(String theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        
        return this.parseTime();
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    private String parseTime() {
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
