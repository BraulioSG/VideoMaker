package system;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

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
            boolean deleted = deleteDirectory(path);
            if(!deleted) return false;
            Files.createDirectory(target);
            return true;

        }catch (IOException ioe){
            //System.out.println(ioe.getMessage());
            return false;
        }
    }

    /**
     * Extracts all the files inside a directory in a recursive way, this means it will extract
     * in subdirectories too, returns a list of files or null if there is no one;
     * @param directory directory to extract
     * @param extensions list of extensions to filter (should add the dot: .png, .jpg. etc)
     */
    public static File[] extractFilesByExtensions(String directory, String... extensions){
        File dir = new File(directory);
        if(!dir.exists()) return null;
        if(!dir.isDirectory()) return null;

        Vector<File> fileVector = new Vector<>();
        File[] fileList = dir.listFiles();
        if(fileList == null) return null;

        for(File file : fileList){
            if (file.isDirectory()){
                File[] subContent = extractFilesByExtensions(directory, extensions);
                if(subContent == null) continue;
                fileVector.addAll(List.of(subContent));
                continue;
            }

            if(!file.isFile()) continue;

            for(String ext : extensions){
                if(!file.getName().endsWith(ext)) continue;
                fileVector.add(file);
            }
        }

        if(fileVector.isEmpty()) return null;

        return fileVector.toArray(new File[0]);
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
            File[] content = file.listFiles();
            Path dirPath = Path.of(path);
            if(content==null) {
                Files.delete(dirPath);
                return true;
            }
            for(File f : content){
                if(f.isDirectory()) deleteDirectory(f.getPath());
                else Files.delete(Path.of(f.getPath()));
            }
            Files.delete(dirPath);
            return true;
        }catch(IOException ioe){
            System.out.println(ioe);
            System.out.println(ioe.getMessage());
            return  false;
        }
    }

    /**
     * Clones a file into another directory, return true if the copy was successful or null
     * if there was an error in the copy, return null if the clone name already exists
     * @param file file to clone
     * @param directory destination of the clone
     * @param cloneName name of the cloned file
     */
    public static String cloneTo(String file, String directory, String cloneName) {
        if(!(exists(file) && exists(directory))) return null;
        if(!(new File(directory)).isDirectory()) return  null;

        try{
            File target = new File(String.format("%s/%s", directory, cloneName));
            Files.copy(Path.of(file), Path.of(target.getPath()));
            return String.format("%s/%s", directory, cloneName);

        } catch (IOException ioe){
            System.out.println(ioe);
            System.out.println(ioe.getMessage());
            return null;
        }
    }
}