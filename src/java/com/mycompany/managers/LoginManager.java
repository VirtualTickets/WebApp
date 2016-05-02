/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
//manages the login/logout user operations
package com.mycompany.managers;

import com.mycompany.entities.User;
import com.mycompany.facades.UserFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "loginManager")
@SessionScoped
/**
 *
 * @author Greer
 */

public class LoginManager implements Serializable {

    private String username;
    private String password;
    private String errorMessage;
    private boolean success;

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    /**
     * Creates a new instance of LoginManager
     */
    public LoginManager() {
    }

    /**
     * @return the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the user to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    //navigate to the user creation screen
    public String createUser() {
        return "CreateAccount";
    }

    //navigate to the password forgot screen
    public String resetPassword() {
        return "/customer/ForgotPassword?faces-redirect=true";
    }

    //we moved isLoggedIn() to accountManager, and made it static
    public boolean isLoggedIn() {
        return false;
    }

    //getter and setter for success
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //log a user into the session
    public void loginUser() {
        //try to find the requested user to login as
        User user = userFacade.findByUsername(getUsername());
        if (user == null) {
            //user doesn't exist
            errorMessage = "Invalid username or password!";
            success = false;
        } else {
            if (user.getUsername().equals(getUsername()) && user.getPassword().equals(getPassword())) {
                //user exists and password is good, log in
                errorMessage = "";
                initializeSessionMap(user);
                success = true;
            }
            else {
                //password wrong
                errorMessage = "Invalid username or password!";
                success = false;
            }
            
        }
        
        //handle updates on successful login
        if (success) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext(); 
            try {
                //reload the page to fill in fields with user info
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            } catch (IOException ex) {
                Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            //hide the log in dialog box
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg2').hide();");
        }
    }

    /* unused method slightly different logInUser()
    public String checkLogIn() {

        User user = userFacade.findByUsername(getUsername());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/customer/Profile.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (user == null) {
            errorMessage = "Invalid username or password!";
            return "return false";
        } else {
            if (user.getUsername().equals(getUsername()) && user.getPassword().equals(getPassword())) {
                errorMessage = "";
                initializeSessionMap(user);
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("");
                } catch (IOException ex) {
                    Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                return "/index";
            }
            errorMessage = "Invalid username or password!";
            return "return false";
        }
    }*/

    //starta session with a user
    public void initializeSessionMap(User user) {
        //set the various context fields to the correct things
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("first_name", user.getFirstName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("last_name", user.getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }
    
    

}
