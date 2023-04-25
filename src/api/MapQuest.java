package api;

import interpreters.Curl;
import interpreters.OperatingSystem;
import interpreters.RequestType;
import json.JsonDictionary;
import json.JsonObject;
import json.JsonParser;
import system.FileManager;

import java.io.File;

import static interpreters.Interpreter.getOperatingSystem;

public class MapQuest extends ApiConnection{

    public MapQuest(){
        super("2PLA6xboMKL7QLY2ZxiCB3qccvqwduRu", "https://www.mapquestapi.com/staticmap/v5/map");
    }
    @Override
    public JsonObject sendRequest(String... params) {
        String lat1 = params[0];
        String lon1 = params[1];
        String lat2 = params[2];
        String lon2 = params[3];
        StringBuilder url = new StringBuilder();
        url.append(String.format("%s?key=%s", getAPI_URL(), getAPI_KEY()));
        url.append("&locations=");
        url.append(String.format("%s,%s||%s,%s|", lat1, lon1,lat2,lon2));
        url.append("marker-red-sm&size=1080,1920&defaultMarker=marker-sm-22407F-3B5998&size=1080,1920@2x");

        OperatingSystem os = getOperatingSystem();
        StringBuilder pathBuilder = new StringBuilder();
        if(os == OperatingSystem.Windows){
            Curl.sendRequest(RequestType.GET, String.format("\"%s\"",url), "--output", "./temp/map/map.png");
        }else{
            Curl.sendRequest(RequestType.GET, String.format("%s",url), "--output", "./temp/map/map.png");
        }


        //System.out.println(responseSB);
        JsonDictionary dic = new JsonDictionary();
        dic.add("status", "complete");
        return dic;
    }
}
