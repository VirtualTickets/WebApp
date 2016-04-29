/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.facades;

import com.mycompany.entities.Bought;
import com.mycompany.entities.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
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

    public BoughtFacade() {
        super(Bought.class);
    }
    
    public List<Bought> findByUser(User user) {
        return em.createNamedQuery("Bought.findByUserId")
                .setParameter("userId", user.getId())
                .getResultList();
    }
}
