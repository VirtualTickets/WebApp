/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ben
 */
@Entity
@Table(name = "Bought")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bought.findAll", query = "SELECT b FROM Bought b"),
    @NamedQuery(name = "Bought.findByUserId", query = "SELECT b FROM Bought b WHERE b.boughtPK.userId = :userId"),
    @NamedQuery(name = "Bought.findByMovieId", query = "SELECT b FROM Bought b WHERE b.boughtPK.movieId = :movieId"),
    @NamedQuery(name = "Bought.findByNumTickets", query = "SELECT b FROM Bought b WHERE b.numTickets = :numTickets"),
    @NamedQuery(name = "Bought.findByCost", query = "SELECT b FROM Bought b WHERE b.cost = :cost")})
public class Bought implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoughtPK boughtPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_tickets")
    private int numTickets;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private Float cost;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user;

    public Bought() {
    }

    public Bought(BoughtPK boughtPK) {
        this.boughtPK = boughtPK;
    }

    public Bought(BoughtPK boughtPK, int numTickets) {
        this.boughtPK = boughtPK;
        this.numTickets = numTickets;
    }

    public Bought(int userId, String movieId) {
        this.boughtPK = new BoughtPK(userId, movieId);
    }
    
    public Bought(User user, String movieId, int numTickets, float cost) {
        this.boughtPK = new BoughtPK(user.getId(), movieId);
        this.user = user;
        this.numTickets = numTickets;
        this.cost = cost;
    }

    public BoughtPK getBoughtPK() {
        return boughtPK;
    }

    public void setBoughtPK(BoughtPK boughtPK) {
        this.boughtPK = boughtPK;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }
    
    public String getFormattedCost() {
        return String.format("$%.2f", cost);
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
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
        hash += (boughtPK != null ? boughtPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bought)) {
            return false;
        }
        Bought other = (Bought) object;
        if ((this.boughtPK == null && other.boughtPK != null) || (this.boughtPK != null && !this.boughtPK.equals(other.boughtPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.Bought[ boughtPK=" + boughtPK + " ]";
    }
    
    public String getMovieTitle() {
        return this.boughtPK.getMovieId().split("\\|")[0];
    }
    
    
    public String getMovieTheatre() {
        return this.boughtPK.getMovieId().split("\\|")[1];
    }
}
