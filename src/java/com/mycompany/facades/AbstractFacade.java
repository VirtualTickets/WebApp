/*
 * Created by Benjamin Sweeney on 2016.04.07  * 
 * Copyright © 2016 Benjamin Sweeney. All rights reserved. * 
 */
package com.mycompany.facades;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Template for other facade classes. Facades provide a java interface
 * to the database tables
 * 
 * @author Ben
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * get the entity manager that corresponds to the type of entitiy
     * in the table
     * 
     * @return
     */
    protected abstract EntityManager getEntityManager();

    /**
     * create a new entity in the database table
     * 
     * @param entity
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * edit an existing entity in the database table
     * 
     * @param entity
     */
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    /**
     *Remove an existing entity from the database table
     * 
     * @param entity
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     *  find an existing entity in the database table
     * 
     * @param id
     * @return
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     *Find all existing entities in a database table that satisfy criteria
     * 
     * @return
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Find the existing entities in a database table that fall in a range
     * 
     * @param range
     * @return
     */
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Count the number of existing entities in a database table that meet criteria
     * 
     * @return
     */
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
