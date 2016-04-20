/*
 * Created by Nicholas Phillpott on 2016.04.20  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author painter
 */
public class ApiManagerTest {
    
    public ApiManagerTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findTheatres method, of class ApiManager.
     */
    @Test
    public void testFindTheatres() {
        System.out.println("findTheatres");
        String zip = "23453";
        ApiManager instance = new ApiManager();
        // ArrayList<Theatre> result = instance.findTheatres(zip);
    }

    /**
     * Test of theatreDetails method, of class ApiManager.
     */
    @Test
    public void testTheatreDetails() {
        System.out.println("theatreDetails");
        String theatreId = "5315";
        ApiManager instance = new ApiManager();
        //Theatre result = instance.theatreDetails(theatreId);
    }

    /**
     * Test of theatreShowtimes method, of class ApiManager.
     */
    @Test
    public void testTheatreShowtimes() {
        System.out.println("theatreShowtimes");
        String theatreId = "5315";
        ApiManager instance = new ApiManager();
        //ArrayList<Movie> result = instance.theatreShowtimes(theatreId);
    }

    /**
     * Test of moviesPlayingInLocalTheatres method, of class ApiManager.
     */
    @Test
    public void testMoviesPlayingInLocalTheatres() {
        System.out.println("moviesPlayingInLocalTheatres");
        String zip = "23453";
        ApiManager instance = new ApiManager();
        //ArrayList<Movie> result = instance.moviesPlayingInLocalTheatres(zip);
    }
    
}
