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
        try{
            Process process = processBuilder.command(command).start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;


            while((line = bufferedReader.readLine()) != null){
                response.add(line);
            }
            return  response.toArray(new String[0]);

        }catch (IOException e){
            System.out.println(e);
            return new String[]{};
        }

    }
}
