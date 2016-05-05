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
     *Constructor based on theatre Id and name of theatre
     * 
     * @param theatreId
     * @param name
     */
    public TheatreWithShowtimes(String theatreId, String name) {
        super(theatreId, name);
        showtimes = new ArrayList<>();
    }

    /**
     *get showtimes
     * 
     * @return
     */
    public List<Showtime> getShowtimes() {
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
     *add a showtime to this theatrewithshowtimes's showtimes
     * 
     * @param s
     */
    public void addShowtime(Showtime s) {
        showtimes.add(s);
    }

}
