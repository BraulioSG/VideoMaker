import api.*;
import interpreters.Exif;
import interpreters.FFmpeg;
import json.JsonObject;
import json.JsonString;
import system.FileManager;
import system.FileType;
import system.MediaFile;
import utils.Location;
import utils.ProgressBar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Stream;

public class VideoMaker {
    private Vector<MediaFile> contentFiles;
    // false = CLI; true = GUI
    private boolean interfaceType;

    //paths required for projects
    private Path sourcePath;
    private Path outputPath;

    private final int VIDEO_WIDTH = 1080;
    private final int VIDEO_HEIGHT = 1920;

    /**
     * Constructor
     * @param interfaceType false -> CLI, true -> GUI
     */
    public VideoMaker(boolean interfaceType){
        setInterfaceType(interfaceType);
    }

    public VideoMaker(){
        this(false);
    }

    /**
     * Starts the video maker program
     */
    public void init(){
        if(isInterfaceType()) startGUI();
        else startCLI();
    }

    private void setUpNewProject(String source, String output) throws IOException{
        if(!FileManager.exists(source)) throw new IOException("source path does not exists");
        if(!FileManager.createDirectory(output, false)) throw new IOException("could not work with the output directory");
        setOutputPath(Path.of(output));
        setSourcePath(Path.of(source));
        if(!FileManager.createDirectory("./temp")){
            System.out.println("Ups! went wrong creating the temp folder");
            throw new IOException("Error trying creating the temp folder");
        }
    }

    /**
     * Shows the Graphic User Interface
     */
    private void startGUI(){

    }

    /**
     * Starts the Command Line Interface
     */
    private void startCLI(){
        System.out.println("=== Video Maker CLI ===");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Source Path\n[I] ");
        String sourcePath = scanner.nextLine().trim();
        System.out.print("output Path\n[I] ");
        String outputPath = scanner.nextLine().trim();

        try{
            createNewVideo(sourcePath, outputPath);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public boolean createSrt(String path, String txt){
        try {
            FileWriter writer = new FileWriter(path);
            StringBuilder content = new StringBuilder();
            content.append("1\n");
            content.append("00:00:00,000 --> 01:00:00,000\n");
            content.append(txt);

            writer.write(content.toString());
            writer.close();
        }catch (IOException ioe){
            return false;
        }
        return  true;
    }

    private void createNewVideo(String sourcePath, String outputPath) throws Exception {
        setUpNewProject(sourcePath, outputPath);
        contentFiles = new Vector<>();

        //if(true) return;
        System.out.println("Extracting metadata from files ...");
        File[] fileList = FileManager.extractFilesByExtensions(sourcePath, ".jpg", ".png", ".heic", ".mp4", ".mkv", ".mov");
        if(fileList == null) throw new Exception("Source has not valid media files");

        //transforms the files into media files
        System.out.println("Mapping files into MediaFiles ...");
        MediaFile[] mediaFiles = Stream.of(fileList).map((File file) -> new MediaFile(file.getPath())).toArray(MediaFile[]::new);

        //sorting the files
        System.out.println("Sorting Files...");
        for(int i = 0; i < mediaFiles.length - 1; i++){
            for(int j = 0; j < mediaFiles.length - 1 - i; j++){
                MediaFile temp = mediaFiles[j];
                if(mediaFiles[j+1].getDate().isOlderThan(mediaFiles[j].getDate())){
                    mediaFiles[j] = mediaFiles[j+1];
                    mediaFiles[j+1] = temp;
                }
            }
        }

        FileManager.createDirectory("./temp/sorted");
        FileManager.createDirectory("./temp/subtitles");
        FileManager.createDirectory("./temp/subbed");

        OpenWeather openWeather = new OpenWeather();
        Unsplash unsplash = new Unsplash();
        NinjaApi ninjaApi = new NinjaApi();
        MapQuest mapQuest = new MapQuest();





        int counter = 1;
        try {
            File list = new File("./temp/subbed/videoList.txt");

            if(!list.createNewFile()) throw new RuntimeException("Error trying to create the video list");
            StringBuilder content = new StringBuilder();

            //Cover
            unsplash.downloadImage("a");
            FFmpeg.convertImageToVideo("./temp/cover/image.jpg", "./temp./cover/video.mp4");
            createSrt("./temp/cover/subs.srt", ninjaApi.getJoke());
            FFmpeg.burnSubtitles("./temp/cover/video.mp4", "./temp/cover/subs.srt", "./temp/subbed/video-0-sub.mp4");
            content.append("file video-0-sub.mp4\n");

            Location firstLocation = null;
            Location lastLocation = null;

            for(MediaFile file: mediaFiles){
                String outputVideoPath = "./temp/sorted/video-" + counter + "-sorted.mp4";
                String inputVideoPath = file.getPath().replace("\\", "/");
                System.out.println(String.format("%s : %s", inputVideoPath, outputVideoPath));

                if(file.getLocation().getLatitude() != 0 && file.getLocation().getLongitude() != 0){
                    if(firstLocation == null) firstLocation = file.getLocation();
                    lastLocation = file.getLocation();
                }




                String latitude = Double.toString(file.getLocation().getLatitude());
                String longitude = Double.toString(file.getLocation().getLongitude());
                JsonObject weatherRes = openWeather.sendRequest(latitude, longitude);

                JsonString weather = (JsonString) weatherRes.get("description");
                createSrt(String.format("./temp/subtitles/subs-%d-org.srt", counter), weather.getValue());


                if(file.getType() == FileType.Image){
                    FFmpeg.convertImageToVideo(inputVideoPath, outputVideoPath);
                    FFmpeg.burnSubtitles(outputVideoPath, String.format("./temp/subtitles/subs-%d-org.srt", counter), String.format("./temp/subbed/video-%d-sub.mp4", counter));
                    content.append(String.format("file video-%d-sub.mp4\n", counter));
                }
                else if(file.getType() ==FileType.Video){
                    FFmpeg.convertToMp4(inputVideoPath, outputVideoPath);
                    FFmpeg.burnSubtitles(outputVideoPath, String.format("./temp/subtitles/subs-%d-org.srt", counter), String.format("./temp/subbed/video-%d-sub.mp4", counter));
                    content.append(String.format("file video-%d-sub.mp4\n", counter));
                }

                counter++;
            }

            if(lastLocation == null) lastLocation = new Location();
            if(firstLocation == null) firstLocation = new Location();

            String firstLat = Double.toString(firstLocation.getLatitude());
            String firstLon = Double.toString(firstLocation.getLongitude());
            String lastLat = Double.toString(lastLocation.getLatitude());
            String lastLon = Double.toString(lastLocation.getLongitude());

            mapQuest.sendRequest(firstLat, firstLon, lastLat, lastLon);
            FFmpeg.convertImageToVideo("./temp/map/map.png", String.format("./temp/subbed/video-%d-sub.mp4", counter));
            content.append(String.format("file video-%d-sub.mp4\n", counter));

            FileWriter writer = new FileWriter("./temp/subbed/videoList.txt");
            writer.write(content.toString());
            writer.close();

            FFmpeg.concatVideos("./temp/subbed/videoList.txt", getOutputPath().toString().replace("\\", "/") + "/output.mp4");

        }catch (IOException e){
            throw new RuntimeException("Error trying to create the video list");
        }


        /*
        System.out.println("cloning and converting files");
        //cloning the files
        int count = 0;
        ProgressBar.displayProgressBar(count, mediaFiles.length);
        for(MediaFile file : mediaFiles){

            if(file.getType() == FileType.Video) {
                String[] tokens = file.getName().split("\\.");
                String ext = tokens[tokens.length - 1];


                String cloned = FileManager.cloneTo(file.getPath(), "./temp", String.format("vid-%d.%s", count + 1, ext));
                if (cloned == null)
                    throw new Exception(String.format("Error trying to clone %s file into the temp folder", file.getName()));
                MediaFile temp = new MediaFile(cloned);
                temp.copyMetadata(file);
                contentFiles.add(temp);
                continue;
            }
            FFmpeg.convertImageToVideo(file.getPath(), String.format("./temp/vid-%d.mp4", count + 1));
            MediaFile temp = new MediaFile(String.format("./temp/vid-%d.mp4", count + 1));
            temp.copyMetadata(file);
            contentFiles.add(temp);
            ProgressBar.displayProgressBar(count, mediaFiles.length);
            count++;
        }
        ProgressBar.displayProgressBar(count, mediaFiles.length);

        prepareVideos();
        createVideoList();
        //FileManager.deleteDirectory("./temp");
         */

    }

    private void prepareVideos(){
        System.out.println("Fetching APIs...");
        FileManager.createDirectory("./temp/finalVideos");
        FileManager.createDirectory("./temp/croppedVideos");
        int count = 0;
        ProgressBar.displayProgressBar(count, contentFiles.size());
        for(MediaFile f: contentFiles){
            System.out.println(f.getName());
            MediaFile temp = new MediaFile(f.getPath());
            temp.copyMetadata(f);
            String lat = String.valueOf(temp.getLocation().getLatitude());
            String lon = String.valueOf(temp.getLocation().getLongitude());

            ApiConnection weather = new OpenWeather();
            JsonObject weatherResponse =  weather.sendRequest(lat, lon);

            ApiConnection map = new MapQuest();
            JsonObject mapResponse = map.sendRequest(lat, lon);

            //System.out.println("Weather -> " + weatherResponse);
            //System.out.println("Map -> " + mapResponse);

            String weatherStr = ((JsonString) weatherResponse.get("weather").get(0).get("description")).getValue();
            String city = ((JsonString) mapResponse.get("results").get(0).get("locations").get(0).get("adminArea5")).getValue();

            System.out.println(String.format("%s -> %s", city, weatherStr));

            try {
                File list = new File("./temp/croppedVideos/sub-" + (count + 1) + ".srt");

                if(!list.createNewFile()) throw new RuntimeException("Error trying to create the video list");
                FileWriter writer = new FileWriter("./temp/croppedVideos/sub-" + (count + 1) + ".srt");
                StringBuilder content = new StringBuilder();
                content.append("1\n");
                content.append("00:00:00,000 --> 01:00:00,000\n");
                content.append(String.format("- %s (%s)", city, weatherStr));

                writer.write(content.toString());
                writer.close();

                FFmpeg.cropVideo(f, VIDEO_WIDTH, VIDEO_HEIGHT, "./temp/croppedVideos/" + f.getName());
                ProgressBar.displayProgressBar(count, contentFiles.size());
                File cropped = new File("./temp/croppedVideos/" + f.getName());
                FFmpeg.burnSubtitles(cropped.getPath(), "./temp/croppedVideos/sub-" + (count + 1) + ".srt", "./temp/finalVideos/" + f.getName());
                count++;

            }catch (IOException e){
                throw new RuntimeException("Error trying to create the video list");
            }
        }
        ProgressBar.displayProgressBar(count, contentFiles.size());
    }
    private void createVideoList(){
        System.out.println("Creating Video");
        try {
            File list = new File("./temp/videoList.txt");

            if(!list.createNewFile()) throw new RuntimeException("Error trying to create the video list");
            StringBuilder content = new StringBuilder();
            File[] fileList = (new File("./temp/finalVideos")).listFiles();
            if(fileList == null) return;
            for(File f : fileList){
                content.append(String.format("file %s\n", f.getName()));
            }

            FileWriter writer = new FileWriter("./temp/finalVideos/videoList.txt");
            writer.write(content.toString());
            writer.close();

            FFmpeg.concatVideos("./temp/finalVideos/videoList.txt", getOutputPath().toString().replace("\\", "/") + "/output.mp4");

        }catch (IOException e){
            throw new RuntimeException("Error trying to create the video list");
        }

    }

    //GETTERS AND SETTERS
    private boolean isInterfaceType() {
        return interfaceType;
    }

    private void setInterfaceType(boolean interfaceType) {
        this.interfaceType = interfaceType;
    }

    private Path getSourcePath() {
        return sourcePath;
    }

    private void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }

    private Path getOutputPath() {
        return outputPath;
    }

    private void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }
}
