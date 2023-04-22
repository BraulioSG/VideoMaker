package api;

import json.JsonObject;

public abstract class ApiConnection {
    private String API_KEY;
    private String API_URL;

    public ApiConnection(String key, String url){
        setAPI_KEY(key);
        setAPI_URL(url);
    }

    public abstract JsonObject sendRequest(String... params);
    //GETTERS & SETTERS
    private void setAPI_KEY(String key){
        this.API_KEY = key;
    }

    protected String getAPI_KEY(){
        return this.API_KEY;
    }

    private void setAPI_URL(String url){
        this.API_URL = url;
    }

    protected String getAPI_URL(){
        return this.API_URL;
    }

}
