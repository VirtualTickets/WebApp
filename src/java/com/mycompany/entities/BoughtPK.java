/*
 * Created by Benjamin Sweeney on 2016.04.29  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
//Part of a bought entry, this contains the id of th user that purchased, 
//and the date on which they did
package com.mycompany.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ben
 */
@Embeddable
public class BoughtPK implements Serializable, Comparable<BoughtPK> {

    //private fields in the BoughtPK object
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "purchase_date")
    private long purchaseDate;

    //constructors
    public BoughtPK() {
    }

    public BoughtPK(int userId, long purchaseDate) {
        this.userId = userId;
        this.purchaseDate = purchaseDate;
    }

    //getters and setters for the fields
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    //output a formatted time for the purchase
    public String getPurchaseTime() {
        return Bought.formatDate(new Date(purchaseDate));
    }

    //create a hash code for database usage
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) purchaseDate;
        return hash;
    }

    //check if two BoughtPK object are equal
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoughtPK)) {
            return false;
        }
        BoughtPK other = (BoughtPK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.purchaseDate != other.purchaseDate) {
            return false;
        }
        return true;
    }

    //get a string representation of th BoughtPK entry
    @Override
    public String toString() {
        return "com.mycompany.entities.BoughtPK[ userId=" + userId + ", purchaseDate=" + purchaseDate + " ]";
    }

    //Compare two BoughtPK objects
    //comparisons are based on purchase times
    @Override
    public int compareTo(BoughtPK o) {
        long diff = o.purchaseDate - this.purchaseDate;
        
        if (diff < 0) {
            return -1;
        } 
        else if (diff > 0) {
            return 1;
        }
        return 0;
    }
    
}
