/*
 * Created by Benjamin Sweeney on 2016.04.21  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ben
 */
public class TheatreWithShowtimes extends Theatre {
    
    private List<Showtime> showtimes;
    
    public TheatreWithShowtimes(String theatreId, String name) {
        super(theatreId, name);
        showtimes = new ArrayList<>();
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }
    
    public void addShowtime(Showtime s) {
        showtimes.add(s);
    }
    
    
}