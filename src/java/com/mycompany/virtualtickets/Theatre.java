/*
 * Created by Nicholas Phillpott on 2016.04.20  * 
 * Copyright © 2016 Nicholas Phillpott. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

import java.util.Objects;

/**
 *  The Theatre object that contains info about a theatre.
 * 
 * @author painter
 */
public class Theatre {
    
    private String theatreId;
    private String name;
    private String telephone; 
    private String address;
    
    /**
     * Constructor
     */
    public Theatre() {
        // Nothing
    }
    
    /**
     *Constructor based on theatre Id and name of theatre
     * 
     * @param theatreId
     * @param name
     */
    public Theatre(String theatreId, String name) {
        this.theatreId = theatreId;
        this.name = name;
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
     *get Name
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *set Name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *get phone number
     * 
     * @return
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     *set phone number
     * 
     * @param telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    /**
     *get address
     * 
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *set address
     * 
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Theatre other = (Theatre) obj;
        if (!Objects.equals(this.theatreId, other.theatreId)) {
            return false;
        }
        return true;
    }
    
    
}
