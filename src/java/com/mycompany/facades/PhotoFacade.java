/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
package com.mycompany.facades;

import com.mycompany.entities.Photo;
import java.util.List; // Added to the generated code
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Greer
 */
// PhotoFacade is an EJB style POJO (Plain Old Java Object) session bean, 
// which is annotated to be @Stateless.
@Stateless

public class PhotoFacade extends AbstractFacade<Photo> {

    @PersistenceContext(unitName = "Users-GreerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PhotoFacade() {
        super(Photo.class);
    }
    
    // The following findPhotosByUserID method is added to the generated code.
    
    public List<Photo> findPhotosByUserID(Integer userID) {
        return (List<Photo>) em.createNamedQuery("Photo.findPhotosByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }

}
