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
@Table(name = "Favorited")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Favorited.findAll", query = "SELECT f FROM Favorited f"),
    @NamedQuery(name = "Favorited.findByUserId", query = "SELECT f FROM Favorited f WHERE f.favoritedPK.userId = :userId"),
    @NamedQuery(name = "Favorited.findByMovieId", query = "SELECT f FROM Favorited f WHERE f.favoritedPK.movieId = :movieId")})
public class Favorited implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FavoritedPK favoritedPK;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Favorited() {
    }

    public Favorited(FavoritedPK favoritedPK) {
        this.favoritedPK = favoritedPK;
    }

    public Favorited(int userId, String movieId) {
        this.favoritedPK = new FavoritedPK(userId, movieId);
    }

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (favoritedPK != null ? favoritedPK.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "com.mycompany.entities.Favorited[ favoritedPK=" + favoritedPK + " ]";
    }
    
}
