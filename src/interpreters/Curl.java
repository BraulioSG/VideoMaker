package interpreters;

import java.util.Collections;
import java.util.Vector;

public class Curl extends Interpreter{

    /**
     * Creates a new HTTP request using the Curl tool.
     * @param type GET, POST, PUT, DELETE
     * @param URL URL to make the request
     * @param optional optional parameters
     */
    public static String[] sendRequest(RequestType type, String URL, String...optional){
        Vector<String> command = new Vector<>();
        Collections.addAll(command, "curl"," --request", type.getType(), URL);
        Collections.addAll(command, optional);
        Collections.addAll(command, "|", "jq");
        String[] response = Terminal.execute(command.toArray(new String[0]));

        return response;
    }
}
