/*
 * Created by Benjamin Sweeney on 2016.04.29  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NamedQuery(name = "Bought.findByTitle", query = "SELECT b FROM Bought b WHERE b.title = :title"),
    @NamedQuery(name = "Bought.findByTheatre", query = "SELECT b FROM Bought b WHERE b.theatre = :theatre"),
    @NamedQuery(name = "Bought.findByPurchaseDate", query = "SELECT b FROM Bought b WHERE b.boughtPK.purchaseDate = :purchaseDate"),
    @NamedQuery(name = "Bought.findByViewDate", query = "SELECT b FROM Bought b WHERE b.viewDate = :viewDate"),
    @NamedQuery(name = "Bought.findByNumTickets", query = "SELECT b FROM Bought b WHERE b.numTickets = :numTickets"),
    @NamedQuery(name = "Bought.findByCost", query = "SELECT b FROM Bought b WHERE b.cost = :cost")})
public class Bought implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoughtPK boughtPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "theatre")
    private String theatre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "view_date")
    private long viewDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_tickets")
    private int numTickets;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private Float cost;

    public Bought() {
    }

    public Bought(BoughtPK boughtPK) {
        this.boughtPK = boughtPK;
    }

    public Bought(BoughtPK boughtPK, String title, String theatre, long viewDate, int numTickets) {
        this.boughtPK = boughtPK;
        this.title = title;
        this.theatre = theatre;
        this.viewDate = viewDate;
        this.numTickets = numTickets;
    }
    
    public Bought(int userId, long purchaseDate, String title, String theatre, long viewDate, int numTickets, float cost) {
        this.boughtPK = new BoughtPK(userId, purchaseDate);
        this.title = title;
        this.theatre = theatre;
        this.viewDate = viewDate;
        this.numTickets = numTickets;
        this.cost = cost;
    }

    public Bought(int userId, long purchaseDate) {
        this.boughtPK = new BoughtPK(userId, purchaseDate);
    }

    public BoughtPK getBoughtPK() {
        return boughtPK;
    }

    public void setBoughtPK(BoughtPK boughtPK) {
        this.boughtPK = boughtPK;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTheatre() {
        return theatre;
    }

    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }
    
    public Date getViewTime() {
        return new Date(viewDate);
    }


    public long getViewDate() {
        return viewDate;
    }

    public void setViewDate(long viewDate) {
        this.viewDate = viewDate;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public Float getCost() {
        return cost;
    }
    
    public String getFormattedCost() {
        return String.format("$%.2f", getCost());
    }

    public void setCost(Float cost) {
        this.cost = cost;
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
    
}
