/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
//Part of a Favorited entry in the database has user id and movie id of the entry
package com.mycompany.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ben
 */
@Embeddable
public class FavoritedPK implements Serializable {

    //private fields
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "movie_id")
    private String movieId;

    //constructors
    public FavoritedPK() {
    }

    public FavoritedPK(int userId, String movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    //getters and setters for the fields
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    //create a hash code for use in the database table
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (movieId != null ? movieId.hashCode() : 0);
        return hash;
    }

    //check if two FavoritedPK objects are equal
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FavoritedPK)) {
            return false;
        }
        FavoritedPK other = (FavoritedPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if ((this.movieId == null && other.movieId != null) || (this.movieId != null && !this.movieId.equals(other.movieId))) {
            return false;
        }
        return true;
    }

    //get a string representation of the FavoritedPK object
    @Override
    public String toString() {
        return "com.mycompany.entities.FavoritedPK[ userId=" + userId + ", movieId=" + movieId + " ]";
    }
    
}
