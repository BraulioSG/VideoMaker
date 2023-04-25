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
        FileManager.createDirectory("./temp/cover");
        FileManager.createDirectory("./temp/map");

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
            FFmpeg.convertImageToVideo("./temp/cover/image.jpg", "./temp/cover/video.mp4");
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

    }

    //GETTERS AND SETTERS
    private boolean isInterfaceType() {
        return interfaceType;
    }

    private void setInterfaceType(boolean interfaceType) {
        this.interfaceType = interfaceType;
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
