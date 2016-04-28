/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entities.Favorited;
import com.mycompany.entities.Photo;
import com.mycompany.entities.User;
import com.mycompany.facades.FavoritedFacade;
import com.mycompany.facades.PhotoFacade;
import com.mycompany.facades.UserFacade;
import com.mycompany.virtualtickets.Movie;
import java.io.Serializable;
import java.math.BigInteger;
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
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    /**
     * The instance variable 'photoFacade' is annotated with the @EJB
     * annotation. This means that the GlassFish application server, at runtime,
     * will inject in this instance variable a reference to the @Stateless
     * session bean PhotoFacade.
     */
    @EJB
    private PhotoFacade photoFacade;
    
    
    @EJB
    private FavoritedFacade favoritedFacade;

    public void addFavorite(Movie movie) {
        if (selected != null && movie != null) {
            favoritedFacade.create(new Favorited(selected.getId(), movie.getTmsId()));
        }
    }
    
    public String[] getListOfStates() {
        return listOfStates;
    }

    public BigInteger getCcNumber() {
        if (selected != null && selected.getCcNumber() != null) {
            setCcNumber(selected.getCcNumber());
        }
        return ccNumber;
    }

    public void setCcNumber(BigInteger num) {
        ccNumber = num;
    }

    public int getCcExMonth() {
        if (selected != null && selected.getCcExMonth() != null) {
            setCcExMonth(selected.getCcExMonth());
        }
        return ccExMonth;
    }

    public void setCcExMonth(int month) {
        ccExMonth = (short) month;
    }

    public int getCcExYear() {
        if (selected != null && selected.getCcExYear() != null) {
            setCcExYear(selected.getCcExYear());
        }
        return ccExYear;
    }

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

    public User getSelected() {
        if (selected == null) {
            selected = userFacade.find(FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("user_id"));
        }
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    public String createAccount() {

        // Check to see if a user already exists with the username given.
        System.out.println(username);
        User aUser = userFacade.findByUsername(username);

        if (aUser != null) {
            username = "";
            statusMessage = "Username already exists! Please select a different one!";
            return "";
        }

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
                username = "";
                statusMessage = "Something went wrong while creating your account!";
                return "";
            }
            initializeSessionMap();
            return "Profile";
        }
        return "";
    }

    public String updateAccount() {
        if (statusMessage.isEmpty()) {
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
            User editUser = userFacade.getUser(user_id);
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
                username = "";
                statusMessage = "Something went wrong while editing your profile!";
                return "";
            }
            return "Profile";
        }
        return "";
    }

    public String deleteAccount() {
        if (statusMessage.isEmpty()) {
            int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
            try {
                userFacade.deleteUser(user_id);

            } catch (EJBException e) {
                username = "";
                statusMessage = "Something went wrong while deleting your account!";
                return "";
            }
            this.logout();
            return "/index.xhtml?faces-redirect=true";
        }
        return "";
    }

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

    public void initializeSessionMap() {
        User user = userFacade.findByUsername(getUsername());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("username", username);
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

    private boolean correctPasswordEntered(UIComponent components) {
        UIInput uiInputVerifyPassword = (UIInput) components.findComponent("verifyPassword");
        String verifyPassword = uiInputVerifyPassword.getLocalValue() == null ? ""
                : uiInputVerifyPassword.getLocalValue().toString();
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

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        username = firstName = lastName = password = email = statusMessage = "";
        ccNumber = new BigInteger("0");
        ccExMonth = ccExYear = 0;

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public String userPhoto() {
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");
        User user = userFacade.findByUsername(user_name);
        List<Photo> photoList = photoFacade.findPhotosByUserID(user.getId());
        if (photoList.isEmpty()) {
            System.out.println("empty images");
            return "defaultUserPhoto.png";
        }
        return photoList.get(0).getThumbnailName();
    }

    public static boolean isLoggedIn() {

        return null != FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
    }

    public String getUserImage() {
        if (isLoggedIn()) {
            return "/FileStorageLocation/"+userPhoto();
        }
        return "/resources/images/LoggedOut.png";
    }
}
