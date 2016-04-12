/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ben
 */
@Entity
@Table(name = "Location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
    @NamedQuery(name = "Location.findByUserId", query = "SELECT l FROM Location l WHERE l.locationPK.userId = :userId"),
    @NamedQuery(name = "Location.findByZipcode", query = "SELECT l FROM Location l WHERE l.locationPK.zipcode = :zipcode")})
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LocationPK locationPK;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Location() {
    }

    public Location(LocationPK locationPK) {
        this.locationPK = locationPK;
    }

    public Location(int userId, int zipcode) {
        this.locationPK = new LocationPK(userId, zipcode);
    }

    public LocationPK getLocationPK() {
        return locationPK;
    }

    public void setLocationPK(LocationPK locationPK) {
        this.locationPK = locationPK;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (locationPK != null ? locationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.locationPK == null && other.locationPK != null) || (this.locationPK != null && !this.locationPK.equals(other.locationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.Location[ locationPK=" + locationPK + " ]";
    }
    
}
