package interpreters;

import java.io.File;

public class FFmpeg extends Interpreter{

    private static Thread t;
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
        }else{
            pathBuilder.append(UNIX_TOOLS_PATH);
            pathBuilder.append("/ffmpeg");
        }

        File executable = new File(pathBuilder.toString());
        return String.format("\"%s\"", executable.getAbsolutePath());
    }

    public static void convertImageToVideo(File image){
        String filePath  = String.format("\"%s\"", image.getAbsolutePath());
        String dest = String.format("\"%s\"", image.getParentFile().getAbsolutePath().replace("\\", "/") + "/output.mp4");
        String[] commandResponse = Terminal.execute(getExecutable(),"-v","error", "-framerate", "1", "-i", filePath.replace("\\", "/"), "-c:v", "libx264", "-r", "30", dest, "-y");
        System.out.println(getExecutable());
        //String[] commandResponse = Terminal.execute(getExecutable(), "-h");
        for(String line: commandResponse){
            System.out.println(line);
        }
        System.out.println("done");

    }


}
