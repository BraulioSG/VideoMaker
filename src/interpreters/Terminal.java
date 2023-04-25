package interpreters;

import java.io.*;
import java.util.Vector;

public class Terminal {

    /**
     * Executes a command on the windows
     * @param command
     * @return
     */
    public static String[] execute(String... command){
        ProcessBuilder processBuilder = new ProcessBuilder();
        Vector<String> response = new Vector<>();
        /*
        for(String cmd: command){
            System.out.print(cmd + " ");
        }
        System.out.println();
        */
        try{
            Process process = processBuilder.command(command).start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            InputStreamReader errorStreamReader = new InputStreamReader(process.getErrorStream());
            BufferedReader errorBufferReader = new BufferedReader(errorStreamReader);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = errorBufferReader.readLine()) != null) {
                System.out.println(line);
            }



            while((line = bufferedReader.readLine()) != null){
                response.add(line);
            }
            process.waitFor();
            //System.out.println(process.exitValue());
            return  response.toArray(new String[0]);

        }catch (IOException e){
            System.out.println(e);
            return new String[]{};
        }catch (InterruptedException ie){
            System.out.println(ie);
            return new String[]{};

        }

    }
}
