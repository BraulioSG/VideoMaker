package system;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    /**
     * Looks for the file and returns true if the file or directory exists and false
     * if it does not exist
     * @param path The pathname String
     */
    public static boolean exists(String path){
        File file = new File(path);
        return file.exists();
    }

    /**
     * Creates a Directory in the path specified, if a current directory
     * already exists it will delete it and create a new one, return true if the file was successfully created
     * @param path path with the name of the new directory
     */
    public static  boolean createDirectory(String path){
        return createDirectory(path, true);
    }

    /**
     * Creates a Directory in the path specified, if a current directory
     * already exists and overwrite is true it will delete the folder and create a new one return true if the file was successfully created,
     * if the overwrite parameter is false and the directory already exists it will return true
     * @param path path with the name of the new directory
     * @param overwrite does it will overwrite if the directory already exists
     */
    public static boolean createDirectory(String path, boolean overwrite)  {
        if(!overwrite && exists(path)) return  true;

        try{
            Path target = Path.of(path);
            if (!deleteDirectory(path)) return false;
            Files.createDirectory(target);
            return true;

        }catch (IOException ioe){
            System.out.println(ioe.getMessage());
            return false;
        }
    }

    /**
     * Deletes the provided directory, returns true if was successfully deleted or
     * the directory does not exist, if the path is not a directory or if it was
     * a problem deleting the directory it will return false
     * @param path Directory to be deleted
     */
    public static boolean deleteDirectory(String path){
        File file = new File(path);
        if (!file.exists()) return true;
        if (!file.isDirectory()) return false;

        try{
            Files.delete(Path.of(path));
            return true;
        }catch(IOException ioe){
            System.out.println(ioe.getMessage());
            return  false;
        }
    }
}