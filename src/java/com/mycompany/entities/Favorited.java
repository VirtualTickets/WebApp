/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
//represents a single favorited movie
//a favorited movie is a user-movie relationship, so one movie can have an entry
//for multiple users tied to it
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
@Table(name = "Favorited")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Favorited.findAll", query = "SELECT f FROM Favorited f"),
    @NamedQuery(name = "Favorited.findByUserId", query = "SELECT f FROM Favorited f WHERE f.favoritedPK.userId = :userId"),
    @NamedQuery(name = "Favorited.findByMovieId", query = "SELECT f FROM Favorited f WHERE f.favoritedPK.movieId = :movieId")})
public class Favorited implements Serializable {

    //private fields in a Favorited table entry
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FavoritedPK favoritedPK;//contains movie id and user id for a specific favorited entry
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    //constructors
    public Favorited() {
    }

    public Favorited(FavoritedPK favoritedPK) {
        this.favoritedPK = favoritedPK;
    }

    public Favorited(User user, String movieId) {
        this.favoritedPK = new FavoritedPK(user.getId(), movieId);
        this.user = user;
    }

    //getters and setters for the fields
    public FavoritedPK getFavoritedPK() {
        return favoritedPK;
    }

    public void setFavoritedPK(FavoritedPK favoritedPK) {
        this.favoritedPK = favoritedPK;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //create hash code for use in the table
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (favoritedPK != null ? favoritedPK.hashCode() : 0);
        return hash;
    }

    //check if two Favorited objects are equal
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Favorited)) {
            return false;
        }
        Favorited other = (Favorited) object;
        if ((this.favoritedPK == null && other.favoritedPK != null) || (this.favoritedPK != null && !this.favoritedPK.equals(other.favoritedPK))) {
            return false;
        }
        return true;
    }

    //get a string representation of the object
    @Override
    public String toString() {
        return "com.mycompany.entities.Favorited[ favoritedPK=" + favoritedPK + " ]";
    }
    
}
