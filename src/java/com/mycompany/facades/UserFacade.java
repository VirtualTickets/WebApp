/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.facades;

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
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "VirtualTicketsTestPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User findByUsername(String username) {
        
        List<User> list = em.createNamedQuery("User.findByUsername")
                .setParameter("username", username)
                .getResultList();
        if (list.isEmpty()) {
            return null;
        }
        else {
            User u = list.get(0);
            em.refresh(u);
            return u;  
        }
    }
     public User getUser(int id) {
        return em.find(User.class, id);
    }
     
     public void deleteUser(int id){
        
        User user = em.find(User.class, id);
        em.remove(user);
    }
    
}
