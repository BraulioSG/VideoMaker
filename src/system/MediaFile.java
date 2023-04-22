package system;

import interpreters.Exif;
import json.JsonDataType;
import utils.Date;
import utils.Location;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.util.HashMap;

public class MediaFile extends File {
    private FileType type = null;
    private Date date = new Date();
    private Location location = new Location();
    public MediaFile(String path){
        super(path);

        if(!exists()) throw  new RuntimeException("File does not exists");

        for(String ext : new String[]{".png", ".jpg", ".heic"}) {
            if (!getName().endsWith(ext)) continue;
            setType(FileType.Image);
        }
        if(getType() == null){
            for(String ext : new String[]{".mov", ".mp4", ".mkv"}) {
                if (!getName().endsWith(ext)) continue;
                setType(FileType.Video);
            }
        }
        if(getType() == null) throw  new RuntimeException("File is cannot be a media File");

        setDateAndLocation();
    }

    private void setDateAndLocation(){
        HashMap<String, String> metadata = Exif.getFileInformation(getPath(), "File Creation Date/Time", "Create Date", "GPS Longitude", "GPS Latitude");
        //HashMap<String, String> metadata = Exif.getFileInformation(getPath());
        String dateString = metadata.get("Create Date");
        if(dateString == null) dateString = metadata.get("File Creation Date/Time");
        if(dateString == null) setDate(new Date());
        else setDate(new Date(dateString));

        String latitudeString = metadata.get("GPS Latitude");
        String longitudeString = metadata.get("GPS Longitude");

        if(latitudeString == null || longitudeString == null) setLocation(new Location());
        else {
            try{
                setLocation(new Location(Location.coordinatesToDouble(longitudeString), Location.coordinatesToDouble(latitudeString)));
            }catch (Exception e){
                setLocation(new Location());
            }
        }

    }

    /**
     * Transfers the metadata (date and location)  from another file to the current file
     * @param file file to extract the metadata
     */
    public void copyMetadata(MediaFile file){
        setDate(file.getDate());
        setLocation(file.getLocation());
    }

    //Getters and Setters
    public FileType getType() {
        return type;
    }

    private void setType(FileType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    private void setLocation(Location location) {
        this.location = location;
    }
}
