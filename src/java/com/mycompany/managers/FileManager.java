/*
 * Created by Nicholas Greer on 2016.02.27  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
//Manages upload of user's photos
package com.mycompany.managers;

import com.mycompany.entities.Photo;
import com.mycompany.entities.User;
import com.mycompany.facades.PhotoFacade;
import com.mycompany.facades.UserFacade;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.imgscalr.Scalr;
import org.primefaces.model.UploadedFile;

@Named(value = "fileManager")
@ManagedBean
@SessionScoped
/**
 *
 * @author Greer
 */
public class FileManager {

    // Instance Variables (Properties)
    private UploadedFile file;
    private String message = "";
    
    /**
     * The instance variables are annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject in
     * this instance variable a reference to the @Stateless session bean.
     */
    @EJB
    private UserFacade userFacade;

    
    @EJB
    private PhotoFacade photoFacade;

    // Returns the uploaded file
    public UploadedFile getFile() {
        return file;
    }

    // Obtains the uploaded file
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    // Returns the message
    public String getMessage() {
        return message;
    }

    // Obtains the message
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * "Profile?faces-redirect=true" asks the web browser to display the
     * Profile.xhtml page and update the URL corresponding to that page.
     * @return Profile.xhtml or nothing
     */

    public String upload() {
        if (file.getSize() != 0) {
            copyFile(file);
            message = "";
            return "Profile?faces-redirect=true";
        } else {
            message = "You need to upload a file first!";
            return "";
        }
    }
    
    //cancel a file upload
    public String cancel() {
        message = "";
        return "Profile?faces-redirect=true";
    }

    //copy a file from the temporary file to the FileStorageLocation
    public FacesMessage copyFile(UploadedFile file) {
        try {
            //delete the user's current photo so we can replace it with a new one
            deletePhoto();
            
            //read in the photo from the temporary file
            InputStream in = file.getInputstream();
            
            File tempFile = inputStreamToFile(in, Constants.TEMP_FILE);
            in.close();

            FacesMessage resultMsg;
            //get the current user
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            User user = userFacade.findByUsername(user_name);

            // Insert photo record into database
            String extension = file.getContentType();
            extension = extension.startsWith("image/") ? extension.subSequence(6, extension.length()).toString() : "png";
            List<Photo> photoList = photoFacade.findPhotosByUserID(user.getId());
            if (!photoList.isEmpty()) {
                photoFacade.remove(photoList.get(0));
            }
            //save the new photo in the FileStorageLocaton
            photoFacade.create(new Photo(extension, user));
            Photo photo = photoFacade.findPhotosByUserID(user.getId()).get(0);
            in = file.getInputstream();
            File uploadedFile = inputStreamToFile(in, photo.getFilename());
            saveThumbnail(uploadedFile, photo);
            resultMsg = new FacesMessage("Success!", "File Successfully Uploaded!");
            return resultMsg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FacesMessage("Upload failure!",
            "There was a problem reading the image file. Please try again with a new photo file.");
    }

    //save an input stream of bytes to a file in FileStorageLocation
    private File inputStreamToFile(InputStream inputStream, String childName)
            throws IOException {
        // Read in the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on file.
        File targetFile = new File(Constants.ROOT_DIRECTORY, childName);
        
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        // Save reference to the current image.
        return targetFile;
    }

    //save the thumbnail(a smaller picture) in the FileStorageLocation
    private void saveThumbnail(File inputFile, Photo inputPhoto) {
        try {
            //resize the image to a thumbnail
            BufferedImage original = ImageIO.read(inputFile);
            BufferedImage thumbnail = Scalr.resize(original, Constants.THUMBNAIL_SZ);
            //save the thumbnail
            ImageIO.write(thumbnail, inputPhoto.getExtension(),
                new File(Constants.ROOT_DIRECTORY, inputPhoto.getThumbnailName()));
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //delete the photo from the current user
    public void deletePhoto() {
        //get the current user
        FacesMessage resultMsg;
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");

        User user = userFacade.findByUsername(user_name);

        //get the list of photos for the current user
        List<Photo> photoList = photoFacade.findPhotosByUserID(user.getId());
        if (photoList.isEmpty()) {
            resultMsg = new FacesMessage("Error", "You do not have a photo to delete.");
        } else {
            Photo photo = photoList.get(0);
            try {
                //delete the file and the thumbnail
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
                Files.deleteIfExists(Paths.get(photo.getThumbnailFilePath()));
                
                Files.deleteIfExists(Paths.get(Constants.ROOT_DIRECTORY+"tmp_file"));
                 
                photoFacade.remove(photo);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            resultMsg = new FacesMessage("Success", "Photo successfully deleted!");
        }
        FacesContext.getCurrentInstance().addMessage(null, resultMsg);
    }
}