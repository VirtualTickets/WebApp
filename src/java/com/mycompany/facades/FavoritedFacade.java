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
 *
 * @author Ben
 */
@Stateless
public class FavoritedFacade extends AbstractFacade<Favorited> {

    @PersistenceContext(unitName = "com.mycompany_VirtualTickets_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FavoritedFacade() {
        super(Favorited.class);
    }
    
}
