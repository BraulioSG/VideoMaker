import system.FileManager;

import java.io.IOException;
import java.nio.file.Path;
import java.rmi.server.ExportException;
import java.util.Scanner;

public class VideoMaker {
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

    private void setUpNewProject() throws IOException{
        if(!FileManager.createDirectory("./temp", true)){
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
        String option;
        while(true){
            System.out.println("Menu:");
            System.out.println("1) create new video");
            System.out.println("2) exit");
            System.out.print("[I] ");
            option = scanner.nextLine().trim();
            if(option.equals("2")) break;
            if(!option.equals("1")) continue;

            System.out.print("Source Path\n[I] ");
            String sourcePath = scanner.nextLine().trim();
            System.out.print("Source Path\n[I] ");
            String outputPath = scanner.nextLine().trim();

            try{
                createNewVideo(sourcePath, outputPath);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean createNewVideo(String sourcePath, String outputPath) throws Exception {
        if(!FileManager.exists(sourcePath)) return false;
        return true;
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
