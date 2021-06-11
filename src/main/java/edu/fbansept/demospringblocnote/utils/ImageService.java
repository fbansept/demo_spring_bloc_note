package edu.fbansept.demospringblocnote.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    public final String storageDirectoryPath = "C:\\Users\\fbansept\\Documents\\cours\\java\\spring\\projets\\demo de cours\\bloc note\\back\\image_storage";
    public String uploadToLocalFileSystem(File file, byte[] fileBytes) throws IOException {
        /* we will extract the file name (with extension) from the given file to store it in our local machine for now
        and later in virtual machine when we'll deploy the project
         */
        String fileName = StringUtils.cleanPath(file.getPath());

        InputStream targetStream = new ByteArrayInputStream(fileBytes);

        Path storageDirectory = Paths.get(storageDirectoryPath);

        if(!Files.exists(storageDirectory)){ // if the folder does not exist
            try {
                Files.createDirectories(storageDirectory); // we create the directory in the given storage directory path
            }catch (Exception e){
                e.printStackTrace();// print the exception
            }
        }

        Path destination = Paths.get(storageDirectory.toString() + "\\" + fileName);

        Files.copy(targetStream, destination, StandardCopyOption.REPLACE_EXISTING);// we are Copying all bytes from an input stream to a file

        return fileName;
    }

    public byte[] getImageWithMediaType(String imageName) throws IOException {
        Path destination =   Paths.get(storageDirectoryPath+"\\"+imageName);// retrieve the image by its name

        return IOUtils.toByteArray(destination.toUri());
    }

}