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
 *The facade or java to database table interface for the table User
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

    /**
     * constructor
     */
    public UserFacade() {
        super(User.class);
    }
    
    /**
     *finds existing entities in the User table by searching by username
     * 
     * @param username
     * @return
     */
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

    /**
     * find existing entities in the User table by searching their Ids
     * 
     * @param id
     * @return
     */
    public User getUser(int id) {
        return em.find(User.class, id);
    }
     
    /**
     * Delete an existing user entity from the database identified by their Ids
     * 
     * @param id
     */
    public void deleteUser(int id){
        
        User user = em.find(User.class, id);
        em.remove(user);
    }
    
}
