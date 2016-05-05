
/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
//handles the recovery of account passwords through email 
package com.mycompany.managers;

import com.mycompany.entities.User;
import com.mycompany.facades.UserFacade;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
//import org.apache.commons.mail.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Named(value = "passwordResetManager")
@SessionScoped
/**
 *
 * @author Greer
 */
public class PasswordResetManager implements Serializable{
    
    // Instance Variables (Properties)
    private String username;
    private String message = "";
    private String answer;
    private String password;
    
    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean UserFacade.
     */
    @EJB
    private UserFacade userFacade;

    
    //getters and setters for username and message
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
        
    //currently unused, but we are considering adding in options for account recovery
    public String usernameSubmit() {
        User user = userFacade.findByUsername(username);
        if (user == null) {
            message = "Entered username does not exist!";
            return "EnterUsername?faces-redirect=true";
        }
        else {
            message = "";
            return "SecurityQuestion?faces-redirect=true";
        }
    }
    

    //try to recover an account with username and email 
    public String emailSubmit() {
                //get user from database by the given name
                User user = userFacade.findByUsername(username);
                //check if the submitted email matches the submitted user's email
                if (user == null || !user.getEmail().equals(answer))
                {
                    message ="That email isn't linked to that user";
                    return "";
                }
                
                final String username1 = "vitualtickets.noreply@gmail.com";
		final String password1 = "csd@VT(S16)";

                //set up properties of the email method: smtp from gmail
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

                /*currently unused, but might allow for more specific control of a session*/
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
                        @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username1, password1);
			}
		  });

		try {
                    
                    //set up a new email session
//                    Session session = Session.getDefaultInstance(props, null);
                    session.setDebug(true);

                    //create the basic email info: to, from, and subject
			Message message1 = new MimeMessage(session);
			message1.setFrom(new InternetAddress("virtualtickets.noreply@gmail.com"));
			message1.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(user.getEmail()));
			message1.setSubject("VirtualTickets: Password Recover");
                        
                        //add the body to the email
			message1.setText("Your password is: "+user.getPassword()+"\n\nThank You for using virtual tickets!");
                        
                        //send the email
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com", "virtualtickets.noreply@gmail.com", "csd@VT(S16)");
                        transport.sendMessage(message1, message1.getAllRecipients());
                        transport.close();


			System.out.println("Done");

		} catch (MessagingException e) {
                    //inform the user if this failed
			Logger.getLogger(PasswordResetManager.class.getName()).log(Level.SEVERE, null, e);
                        message = "Sending email failed";
                        return "ForgotPassword?faces-redirect=true";
		}
                message = "Email Sent";
                return "";
        /*User user = userFacade.findByUsername(username);
        if (user.getEmail().equals(answer)) {
            try {
                Email email = new SimpleEmail();
                email.setHostName("smtp.gmail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator("virtualtickets", "csd@VT(S16)"));
                email.setSSLOnConnect(true);
                email.setFrom("virtualtickets@gmail.com");
                email.setSubject("TestMail");
                email.setMsg("This is a test mail ... :-)");
                email.addTo("Imploding40@gmail.com");
                email.send();
                message = "An email has been sent";
                return "ForgotPassword?faces-redirect=true";
            } catch (EmailException ex) {
                Logger.getLogger(PasswordResetManager.class.getName()).log(Level.SEVERE, null, ex);
                message = "Sending email failed";
                return "ForgotPassword?faces-redirect=true";
            }
        }
        else {
            message = "That email isn't linked to that user";
            return "ForgotPassword?faces-redirect=true";
        }*/
    }
    
    //setter and getter for the email
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    //check if the user has submitted the correct password information
    public void validateInformation(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();

        UIComponent components = event.getComponent();
        // get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String pwd = uiInputPassword.getLocalValue() == null ? ""
                : uiInputPassword.getLocalValue().toString();

        // get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? ""
                : uiInputConfirmPassword.getLocalValue().toString();

        if (pwd.isEmpty() || confirmPassword.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!pwd.equals(confirmPassword)) {
            message = "Passwords must match!";
        } else {
            message = "";
        }   
    }   

    //getter and setter for the password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    //change the password of a user
    public String resetPassword() {
        //if the fields on the xhtml page are correct
        if (message.equals("")) {
            message = "";
            //find the user and try to change the password
            User user = userFacade.findByUsername(username);
            try {
                user.setPassword(password);
                userFacade.edit(user);
                username = answer = password = "";                
            } catch (EJBException e) {
                message = "Something went wrong editing your profile, please try again!";
                return "ResetPassword?faces-redirect=true";            
            }
            return "index?faces-redirect=true";            
        }
        else {
            return "ResetPassword?faces-redirect=true";            
        }
    }
    
            
}