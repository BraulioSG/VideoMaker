package interpreters;

public enum RequestType {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE");

    private String txt;

    private RequestType(String txt){
        this.txt = txt;
    }

    public String getType(){
        return txt;
    }
}
