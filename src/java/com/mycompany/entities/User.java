/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
//an entry in the user table of the database. This corresponds to a single
//user profile in our app
package com.mycompany.entities;

import com.mycompany.virtualtickets.Movie;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ben
 */
@Entity
@Table(name = "User")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByCcNumber", query = "SELECT u FROM User u WHERE u.ccNumber = :ccNumber"),
    @NamedQuery(name = "User.findByCcExMonth", query = "SELECT u FROM User u WHERE u.ccExMonth = :ccExMonth"),
    @NamedQuery(name = "User.findByCcExYear", query = "SELECT u FROM User u WHERE u.ccExYear = :ccExYear")})
public class User implements Serializable {

    //private fields
    @OneToMany(mappedBy = "userId")
    private Collection<Photo> photoCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "last_name")
    private String lastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "password")
    private String password;
    @Column(name = "cc_number")
    private BigInteger ccNumber;
    @Column(name = "cc_ex_month")
    private Short ccExMonth;
    @Column(name = "cc_ex_year")
    private Short ccExYear;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Favorited> favoritedList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Location> locationList;

    //constructors
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String firstName, String lastName, String email, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    //getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigInteger getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(BigInteger ccNumber) {
        this.ccNumber = ccNumber;
    }

    public Short getCcExMonth() {
        return ccExMonth;
    }

    public void setCcExMonth(Short ccExMonth) {
        this.ccExMonth = ccExMonth;
    }

    public Short getCcExYear() {
        return ccExYear;
    }

    public void setCcExYear(Short ccExYear) {
        this.ccExYear = ccExYear;
    }

    //get the full list of entries in the favorited table corresponding to this
    //user
    @XmlTransient
    public List<Favorited> getFavoritedList() {
        return favoritedList;
    }

    public void setFavoritedList(List<Favorited> favoritedList) {
        this.favoritedList = favoritedList;
    }

    @XmlTransient
    public List<Location> getLocationList() {
        return locationList;
    }
    
    //get the full list of entries in the location table corresponding to this
    //user
    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    //create a hash code for use in the database
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    //check if two User objects are equal
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    //get a string representation of the User object
    @Override
    public String toString() {
        return "com.mycompany.entities.User[ id=" + id + " ]";
    }

    //get a full list of photo table entries corresponding to this user
    @XmlTransient
    public Collection<Photo> getPhotoCollection() {
        return photoCollection;
    }

    public void setPhotoCollection(Collection<Photo> photoCollection) {
        this.photoCollection = photoCollection;
    }
    
    public boolean favorited(Movie m) {
        for (Favorited f : favoritedList) {
            if (f.getFavoritedPK().getMovieId().equals(m.getTmsId())) return true;
        }
        
        return false;
    }
    public String ccNumberDisplay()
    {
        BigInteger temp = this.ccNumber;
        temp = temp.mod(new BigInteger("10000"));
        return "************"+temp.toString();
    }
    
}
