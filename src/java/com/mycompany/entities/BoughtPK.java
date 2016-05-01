/*
 * Created by Benjamin Sweeney on 2016.04.29  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
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

    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "purchase_date")
    private long purchaseDate;

    public BoughtPK() {
    }

    public BoughtPK(int userId, long purchaseDate) {
        this.userId = userId;
        this.purchaseDate = purchaseDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getPurchaseTime() {
        return Bought.formatDate(new Date(purchaseDate));
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) purchaseDate;
        return hash;
    }

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

    @Override
    public String toString() {
        return "com.mycompany.entities.BoughtPK[ userId=" + userId + ", purchaseDate=" + purchaseDate + " ]";
    }

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
