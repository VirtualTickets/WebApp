/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
//centralized Class for controlling account operations CRUD
package com.mycompany.managers;

import com.mycompany.entities.Bought;
import com.mycompany.entities.Favorited;
import com.mycompany.entities.FavoritedPK;
import com.mycompany.entities.Photo;
import com.mycompany.entities.User;
import com.mycompany.facades.FavoritedFacade;
import com.mycompany.facades.PhotoFacade;
import com.mycompany.facades.BoughtFacade;
import com.mycompany.facades.UserFacade;
import com.mycompany.virtualtickets.Movie;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

@Named(value = "accountManager")
@SessionScoped
/**
 *
 * @author Greer
 */
public class AccountManager implements Serializable {

    // Instance Variables (Properties)
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String statusMessage;
    private BigInteger ccNumber;
    private short ccExMonth;
    private short ccExYear;

    //private int zipcode;
    private final String[] listOfStates = Constants.STATES;
    private Map<String, Object> security_questions;

    private User selected;

    /**
     * These instance variables 'are annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * for each one
     */
    @EJB
    private UserFacade userFacade;
    @EJB
    private PhotoFacade photoFacade;
    @EJB
    private BoughtFacade boughtFacade;
    
    @EJB
    private FavoritedFacade favoritedFacade;

    //adds a movie to the favorite to the current user.
    public void addFavorite(Movie movie) {
        if (selected != null && movie != null) {
            System.err.println("Movie id: " + movie.getTmsId());
            if(null != favoritedFacade.find(new FavoritedPK(selected.getId(), movie.getTmsId()))) {
                favoritedFacade.create(new Favorited(selected, movie.getTmsId()));
            }
            
        }
    }
    //toggles whether or not a movie is in a user's favorite list
    public void toggleFavorite(Movie movie) {
        if (selected != null && movie != null) {
            Favorited f = favoritedFacade.find(new FavoritedPK(selected.getId(), movie.getTmsId()));
            if (f == null) {
                favoritedFacade.create(new Favorited(selected, movie.getTmsId()));
            }
            else {
                favoritedFacade.remove(f);
            }
            
        }
    }
    
    //returns a list of the US states currently unused
    public String[] getListOfStates() {
        return listOfStates;
    }

    //get the creditcard number of the selected user
    public BigInteger getCcNumber() {
        if (selected != null && selected.getCcNumber() != null) {
            setCcNumber(selected.getCcNumber());
        }
        return ccNumber;
    }

    //set the credit card number of the selected user
    public void setCcNumber(BigInteger num) {
        ccNumber = num;
    }

    //get the creditcard expiration month of the selected user
    public int getCcExMonth() {
        if (selected != null && selected.getCcExMonth() != null) {
            setCcExMonth(selected.getCcExMonth());
        }
        return ccExMonth;
    }

    //set the creditcard expiration month of the selected user
    public void setCcExMonth(int month) {
        ccExMonth = (short) month;
    }

    //get the creditcard expiration year of the selected user
    public int getCcExYear() {
        if (selected != null && selected.getCcExYear() != null) {
            setCcExYear(selected.getCcExYear());
        }
        return ccExYear;
    }

    //set the creditcard expiration year of the selected user
    public void setCcExYear(int year) {
        ccExYear = (short) year;
    }

    /**
     * Creates a new instance of AccountManager
     */
    public AccountManager() {
    }

    /**
     * @return the first name
     */
    public String getFirstName() {
        if (selected != null && selected.getFirstName() != null) {
            setFirstName(selected.getFirstName());
        }
        return firstName;
    }

    /**
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the last name
     */
    public String getLastName() {
        if (selected != null && selected.getLastName() != null) {
            setLastName(selected.getLastName());
        }
        return lastName;
    }

    /**
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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
     * @return the email
     */
    public String getEmail() {
        if (selected != null && selected.getEmail() != null) {
            setEmail(selected.getEmail());
        }
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
    ZIPCODE
    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }
     */
    
    //security questions currently unused. We are considering adding multiple recovery options to our app
    public Map<String, Object> getSecurity_questions() {
        if (security_questions == null) {
            security_questions = new LinkedHashMap<>();
            for (int i = 0; i < Constants.QUESTIONS.length; i++) {
                security_questions.put(Constants.QUESTIONS[i], i);
            }
        }
        return security_questions;
    }

    /**
     * @return the statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * @param statusMessage the statusMessage to set
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    //get the selected user
    public User getSelected() {
        if (selected == null) {
            selected = userFacade.find(FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("user_id"));
        }
        return selected;
    }

    //set the selected user
    public void setSelected(User selected) {
        this.selected = selected;
    }

    //create a new user in the database
    public String createAccount() {

        // Check to see if a user already exists with the username given.
        if(isLoggedIn())
        {
            statusMessage="You are already logged in as an active user.";
            return "";
        }
        //look if the user already exists
        System.out.println(username);
        User aUser = userFacade.findByUsername(username);

        if (aUser != null) {
            username = "";
            statusMessage = "Username already exists! Please select a different one!";
            return "";
        }

        //if the user filled in all the fields correctly, try to create the account
        if (statusMessage.isEmpty()) {
            try {
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setCcNumber(ccNumber);
                user.setCcExMonth(ccExMonth);
                user.setCcExYear(ccExYear);
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(password);
                //ZIPCODE?
                userFacade.create(user);
            } catch (EJBException e) {
                //if it failed, inform the user
                username = "";
                statusMessage = "Something went wrong while creating your account!";
                return "";
            }
            initializeSessionMap();
            return "Profile";
        }
        return "";
    }

    //change the information in a user entry in the database
    public String updateAccount() {
        //if all fields on the page are satisfied
        if (statusMessage.isEmpty()) {
            //find the user
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
            User editUser = userFacade.getUser(user_id);
            //try to update the user
            try {
                editUser.setFirstName(this.selected.getFirstName());
                editUser.setLastName(this.selected.getLastName());
                editUser.setCcNumber(this.selected.getCcNumber());
                editUser.setCcExMonth(this.selected.getCcExMonth());
                editUser.setCcExYear(this.selected.getCcExYear());
                //editUser.setZipcode(this.selected.getZipcode()); ZIPCODE    
                editUser.setEmail(this.selected.getEmail());
                editUser.setPassword(this.selected.getPassword());
                userFacade.edit(editUser);
            } catch (EJBException e) {
                //if the update fails, inform the user
                username = "";
                statusMessage = "Something went wrong while editing your profile!";
                return "";
            }
            return "Profile";
        }
        return "";
    }

    //delete an existing entry in the user table
    public String deleteAccount() {
        //if the user entered the password correctly
        if (statusMessage.isEmpty()) {
            //try to delete the user's account
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
            try {
                userFacade.deleteUser(user_id);

            } catch (EJBException e) {
                //inform the user if something went wrong
                username = "";
                statusMessage = "Something went wrong while deleting your account!";
                return "";
            }
            //logput the user after deleting their account
            this.logout();
            return "/index.xhtml?faces-redirect=true";
        }
        return "";
    }

    //check to see if a user entered the correct information
    public void validateInformation(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();
        // Get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String pwd = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // Get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();

        if (pwd.isEmpty() || confirmPassword.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!pwd.equals(confirmPassword)) {
            statusMessage = "Passwords must match!";
        } else {
            statusMessage = "";
        }
    }

    //set a user as the currently active user
    public void initializeSessionMap() {
        User user = userFacade.findByUsername(getUsername());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

    //check if a user's password matches the password they entered
    private boolean correctPasswordEntered(UIComponent components) {
        UIInput uiInputVerifyPassword = (UIInput) components.findComponent("verifyPassword");
        String verifyPassword = uiInputVerifyPassword.getLocalValue() == null ? ""
                : uiInputVerifyPassword.getLocalValue().toString();
        //check if the two values match
        if (verifyPassword.isEmpty()) {
            statusMessage = "";
            return false;
        } else if (verifyPassword.equals(password)) {
            return true;
        } else {
            statusMessage = "Invalid password entered!";
            return false;
        }
    }

    //log a user out of the app
    public String logout() {
        //clear the sessionmap
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        //clear the fields
        username = firstName = lastName = password = email = statusMessage = "";
        ccNumber = new BigInteger("0");
        ccExMonth = ccExYear = 0;

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    //return the photo to user for the current user
    public String userPhoto() {
        //get the current user
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");
        User user = userFacade.findByUsername(user_name);
        //get the user's photos
        List<Photo> photoList = photoFacade.findPhotosByUserID(user.getId());
        //return either the most recent photo or the default
        if (photoList.isEmpty()) {
            System.out.println("empty images");
            return "defaultUserPhoto.png";
        }
        return photoList.get(0).getThumbnailName();
    }
    
    //get the list of items bought by the current user
    public List<Bought> getBought()
    {
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");
        User user = userFacade.findByUsername(user_name);
        List<Bought> boughtList = boughtFacade.findByUser(user.getId());
        //sort the records most recent to least recent
        Collections.sort(boughtList);
        return boughtList;
    }
    
    //static method used freqently to find if any user is logged in
    public static boolean isLoggedIn() {

        return null != FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
    }

    //get the address of either the current user's picture or the logged out picture
    public String getUserImage() {
        if (isLoggedIn()) {
            return "/FileStorageLocation/"+userPhoto();
        }
        return "/resources/images/LoggedOut.png";
    }
    
    
}
