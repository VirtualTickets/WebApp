/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
//Part of a Location table entry has user id and zipcode
package com.mycompany.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ben
 */
@Embeddable
public class LocationPK implements Serializable {

    //private fields
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zipcode")
    private int zipcode;

    //constructors
    public LocationPK() {
    }

    public LocationPK(int userId, int zipcode) {
        this.userId = userId;
        this.zipcode = zipcode;
    }

    //getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    //create a hash code for use in the database
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) zipcode;
        return hash;
    }

    //check if two LocationPK objects are equal
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LocationPK)) {
            return false;
        }
        LocationPK other = (LocationPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.zipcode != other.zipcode) {
            return false;
        }
        return true;
    }

    //get a string representation of the LocationPK object
    @Override
    public String toString() {
        return "com.mycompany.entities.LocationPK[ userId=" + userId + ", zipcode=" + zipcode + " ]";
    }
    
}
