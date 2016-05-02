/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
//Provides a view through which to access the logged in user's profile
package com.mycompany.managers;

import com.mycompany.entities.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "profileViewManager")
@SessionScoped
/**
 *
 * @author Greer
 */
public class ProfileViewManager implements Serializable {

    // Instance Variable (Property)
    private User user;

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private com.mycompany.facades.UserFacade userFacade;

    public ProfileViewManager() {

    }

    //navigate to the profile page
    public String viewProfile() {
        return "Profile";
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    //get the currently logged in user
    public User getLoggedInUser() {
        if (!AccountManager.isLoggedIn()) {
            return new User(-1, "", "", "", "", "");
        }
        return userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

}
