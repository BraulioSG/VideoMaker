package json.exceptions;

public class IncorrectJsonFormat extends RuntimeException{
    public IncorrectJsonFormat() {
        this("Incorrect JSON Format");
    }
    public IncorrectJsonFormat(String msg){
        super(msg);
    }
}
