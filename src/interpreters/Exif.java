package interpreters;

import java.io.File;
import java.util.HashMap;

public class Exif extends Interpreter{

    /**
     * Extracts the information depending on the fields, if there are no fields provided then
     * it will return all the information of the file. if the requested field does not exist
     * then it will be null the value in the hashmap. Fields are case-sensitive. If the file does not
     * exist it will return null
     * @param fields what information you want to extract
     * @return a hashmap, where the fields are going to be the keys
     */
    public static HashMap<String, String> getFileInformation(String filePath, String... fields){
        HashMap<String, String> metadata = new HashMap<>();
        String[] commandResponse = Terminal.execute(getExecutable(), filePath);
        for(String line : commandResponse){
            String[] tokens = line.split(": ");
            String value, key;
            if(tokens.length < 2) return  metadata;
            key = tokens[0].trim();
            value = tokens[1].trim();
            if(fields.length == 0){
                metadata.put(key, value);
                continue;
            }

            for(String field: fields){
                if(!key.equals(field)) continue;
                metadata.put(key, value);
                break;
            }
        }
        return metadata;
    }

    /**
     * Returns the executable command depending on the operating system
     * @return
     */
    private static String getExecutable(){
        OperatingSystem os = getOperatingSystem();
        StringBuilder pathBuilder = new StringBuilder();
        if(os == OperatingSystem.Windows){
            pathBuilder.append(WINDOWS_TOOLS_PATH);
            pathBuilder.append("/exiftool/exiftool.exe");
            File executable = new File(pathBuilder.toString());
            return String.format("\"%s\"", executable.getAbsolutePath());
        }else{
            pathBuilder.append(UNIX_TOOLS_PATH);
            pathBuilder.append("/exiftool/exiftool");
            File executable = new File(pathBuilder.toString());
            return String.format("%s", executable.getAbsolutePath());
        }
    }
}
