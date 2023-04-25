package api;

import interpreters.Curl;
import interpreters.RequestType;
import json.JsonObject;
import json.JsonParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NinjaApi extends ApiConnection{
    public NinjaApi(){
        super("cO0ealnQdl9JL2RYwoix0UFlKin0KnEP8EFexmUk", "https://api.api-ninjas.com/v1/dadjokes?limit=1");
    }
    @Override
    public JsonObject sendRequest(String... params){
        return null;
    }
    public String getJoke(String... params) {
        String[] response = Curl.sendRequest(RequestType.GET, getAPI_URL(), "--header", String.format("\"X-Api-Key: %s\"", getAPI_KEY()));
        StringBuilder responseSB = new StringBuilder();
        for(String line : response){
            responseSB.append(line);
        }

        System.out.println(responseSB);
        String res = responseSB.toString();

        if (!res.startsWith("[{\"joke\": ")) return "Missing API Key";
        return String.copyValueOf(res.toCharArray(), 11, res.length() - 14);
    }
}
