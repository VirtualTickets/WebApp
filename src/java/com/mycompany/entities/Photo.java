/*
 * Created by Nicholas Greer on 2016.04.10  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
//represents an entry in the Photo table in the database
//this corresponds to a single user's photo
package com.mycompany.entities;

import com.mycompany.managers.Constants;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Imploding40
 */
@Entity
@Table(name = "Photo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Photo.findAll", query = "SELECT p FROM Photo p"),
    @NamedQuery(name = "Photo.findById", query = "SELECT p FROM Photo p WHERE p.id = :id"),
    @NamedQuery(name = "Photo.findPhotosByUserId", query = "SELECT p FROM Photo p WHERE p.userId.id = :userId"),
    @NamedQuery(name = "Photo.findByExtension", query = "SELECT p FROM Photo p WHERE p.extension = :extension")})
public class Photo implements Serializable {

    //private fields
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "extension")
    private String extension;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    //constructors
    public Photo() {
    }

    public Photo(Integer id) {
        this.id = id;
    }

    public Photo(Integer id, String extension) {
        this.id = id;
        this.extension = extension;
    }
    
    public Photo(String extension, User id) {
        this.extension = extension;
        userId = id;
    }

    //getters and setters for the fields
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    //create a hash code for use in the database
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    //check if two Photo objects are equal
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Photo)) {
            return false;
        }
        Photo other = (Photo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    //get a string representation of the Photo object
    @Override
    public String toString() {
        return "com.mycompany.entities.Photo[ id=" + id + " ]";
    }
    
    //get the file path where the actual image is stored
    public String getFilePath() {
        return Constants.ROOT_DIRECTORY + getFilename();
    }

    //get the name of the file of the actual image
    public String getFilename() {
        return getId() + "." + getExtension();
    }
    
    //get the name of the file of the thumbnail corresponding to this image
    public String getThumbnailName() {
        return getId() + "_thumbnail." + getExtension();
    }
    
    //get the full filepath of the thumbnail corresponding to this photo
    public String getThumbnailFilePath() {
        return Constants.ROOT_DIRECTORY + getThumbnailName();
    }
}
