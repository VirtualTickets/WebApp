/*
 * Created by Benjamin Sweeney on 2016.04.21  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import java.util.ArrayList;
import java.util.List;

/**
 * A theatre object with a list of the showtimes. This is to separate the
 * different movies into different tabs.
 *
 * @author Ben
 */
public class TheatreWithShowtimes extends Theatre {

    private List<Showtime> showtimes;

    /**
     * Calls the super constructor with the parameters passed.
     * @param theatreId
     * @param name 
     */
    public TheatreWithShowtimes(String theatreId, String name) {
        super(theatreId, name);
        showtimes = new ArrayList<>();
    }

    /**
     * Showtimes getter and setter
     * @return showtimes
     */
    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    /**
     * Adds a showtimes to the showtimes list
     * @param s The showtime being added.
     */
    public void addShowtime(Showtime s) {
        showtimes.add(s);
    }

}
