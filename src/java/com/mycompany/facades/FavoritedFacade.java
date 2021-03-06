/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.facades;

import com.mycompany.entities.Favorited;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * The facade or java to database table interface for the table Favorited
 * 
 * @author Ben
 */
@Stateless
public class FavoritedFacade extends AbstractFacade<Favorited> {

    @PersistenceContext(unitName = "VirtualTicketsTestPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * constructor
     */
    public FavoritedFacade() {
        super(Favorited.class);
    }
    
}
