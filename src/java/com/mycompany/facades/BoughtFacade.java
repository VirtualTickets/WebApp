/*
 * Created by Benjamin Sweeney on 2016.04.29  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.facades;

import com.mycompany.entities.Bought;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *The facade or java to database table interface for the table Bought
 * 
 * @author Ben
 */
@Stateless
public class BoughtFacade extends AbstractFacade<Bought> {

    @PersistenceContext(unitName = "VirtualTicketsTestPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * constructor
     */
    public BoughtFacade() {
        super(Bought.class);
    }
    
    /**
     *Find existing bought entities in the database table by searching their userId
     * 
     * @param userId
     * @return
     */
    public List<Bought> findByUser(int userId) {
        return em.createNamedQuery("Bought.findByUserId")
                .setParameter("userId", userId)
                .getResultList();
    }
    
}
