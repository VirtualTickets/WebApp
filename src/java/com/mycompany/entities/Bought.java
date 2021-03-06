/*
 * Created by Benjamin Sweeney on 2016.04.29  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
//Represents a purchase record of a ticket order
//The primary purpose of this class is to support a purchase history feature
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
public class Bought implements Serializable, Comparable<Bought> {

    //various fields in an entry to the Bought table
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BoughtPK boughtPK; //Bought PK contains the purchase date and user id of a bought record
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

    //constructors for a Bought object
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

    //getters and setters for the fields of a Bought object
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
    
    //returns a formatted version of viewDate
    public String getViewTime() {
        return formatDate(new Date(viewDate));
    }

    //formats a given timestamp into the form MM/DD/YYYY HH:MM (AM|PM)
    public static String formatDate(Date d) {
        //build the date portion of the output
        StringBuilder s = new StringBuilder().append(d.getMonth() + "/" + d.getDate() + "/" + (d.getYear() + 1900) + " ");
        //build the time portion of the output
        int hour = d.getHours();
        int minute = d.getMinutes();
        String type;
        
        //decide which half of the day the time occurs in
        if (hour == 0) {
            hour = 12;
            type = "AM";
        } 
        else if (hour == 12) {
            type = "PM";
        }
        else if (hour > 12) {
            hour -= 12;
            type = "PM";
        }
        else {
            type = "AM";
        }
        
        //create and return the full output
        s.append(hour + ":" + String.format("%02d", minute) + " " + type);
        
        return s.toString();
    }

    //more getters and setters for the various fields
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
    
    public void setCost(Float cost) {
        this.cost = cost;
    }
    
    //return a cost string formatted in $D.CC
    public String getFormattedCost() {
        return String.format("$%.2f", getCost());
    }

    
    //hash code creation for storing the Bought object in a hash table
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (boughtPK != null ? boughtPK.hashCode() : 0);
        return hash;
    }

    //check if two Bought objects are equal
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

    //unused, but outputs a string representation of Bought
    @Override
    public String toString() {
        return "com.mycompany.entities.Bought[ boughtPK=" + boughtPK + " ]";
    }

    //compare two Bought objects
    @Override
    public int compareTo(Bought o) {
        return this.boughtPK.compareTo(o.boughtPK);
    }
    
}
