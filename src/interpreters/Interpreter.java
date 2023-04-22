package interpreters;

import java.io.File;

public abstract class Interpreter {
    protected static final String WINDOWS_TOOLS_PATH = "./tools/windows";
    protected static final String UNIX_TOOLS_PATH = "./tools/unix";

    /**
     * returns the Enum of the operating System
     * @return
     */
    protected static OperatingSystem getOperatingSystem(){
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows")) return OperatingSystem.Windows;
        return OperatingSystem.Unix;
    }

}
