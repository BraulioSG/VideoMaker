package interpreters;

import java.io.File;

public class FFmpeg extends Interpreter{

    /**
     * Returns the executable command depending on the operating system
     * @return
     */
    private static String getExecutable(){
        OperatingSystem os = getOperatingSystem();
        StringBuilder pathBuilder = new StringBuilder();
        if(os == OperatingSystem.Windows){
            pathBuilder.append(WINDOWS_TOOLS_PATH);
            pathBuilder.append("/ffmpeg/bin/ffmpeg.exe");
            File executable = new File(pathBuilder.toString());
            return String.format("\"%s\"", executable.getAbsolutePath());
        }else{
            pathBuilder.append(UNIX_TOOLS_PATH);
            pathBuilder.append("/ffmpeg/ffmpeg");
            File executable = new File(pathBuilder.toString());
            return String.format("%s", executable.getAbsolutePath());
        }


    }

    /**
     * Converts an image into a video using ffmpeg
     * @param imagePath image to convert
     * @param destination destination file
     */
    public static void convertImageToVideo(String imagePath, String destination){
        //System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(),"-v", "error", "-loop", "1","-i", filePath.replace("\\", "/"), "-c:v", "libx264", "-t", "5", "-pix_fmt", "yuv420p", "-sn", destination, "-y");
        //System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(), "-h");

        String[] commandResponse = Terminal.execute(getExecutable(), "-v", "error", "-loop", "1", "-i", imagePath, "-t", "5", "-filter:v", "crop=1080:1920", destination, "-y");
        for(String line: commandResponse){
            System.out.println(line);
        }
        //System.out.println("done");

    }

    /**
     * Converts video to mp4
     * @param inputVideo file to convert
     * @param destination destination
     */
    public static void convertToMp4(String inputVideo, String destination){
        String[] commandResponse = Terminal.execute(getExecutable(),"-v","error", "-i", inputVideo, "-filter:v", "crop=1080:1920", destination, "-y");
        //System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(), "-h");
        for(String line: commandResponse){
            System.out.println(line);
        }
        //System.out.println("done");
    }

    /**
     * Given a directory this will concat all the videos in one single video
     * @param list text file with the files listed
     */
    public static void concatVideos(String list, String destination){

        String[] commandResponse = Terminal.execute(getExecutable(),"-v","error", "-f", "concat", "-i", list, "-c", "copy", destination, "-y");
        //System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(), "-h");
        for(String line: commandResponse){
            System.out.println(line);
        }
        //System.out.println("done");
    }

    public static void cropVideo(File file, int width, int height, String destination){
        String filePath  = String.format("%s", file.getAbsolutePath());
        String[] commandResponse = Terminal.execute(getExecutable(),"-v","error", "-i", filePath.replace("\\", "/"), "-filter:v",String.format("crop=%d:%d", width, height), destination, "-y");
        //System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(), "-h");
        for(String line: commandResponse){
            System.out.println(line);
        }
        //System.out.println("done");
    }

    public static void burnSubtitles(String videoInput, String subtitles, String destination){
        String[] commandResponse = Terminal.execute(getExecutable(),"-v","error", "-i", videoInput, "-vf", String.format("subtitles=%s", subtitles),destination, "-y");
        //System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(), "-h");
        for(String line: commandResponse){
            System.out.println(line);
        }
        //System.out.println("done");
    }


}
