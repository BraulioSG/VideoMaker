package api;

import interpreters.Curl;
import interpreters.RequestType;
import json.JsonObject;
import json.JsonParser;

import java.util.Arrays;

public class OpenWeather extends ApiConnection{
    public OpenWeather(){
        super("3af74a8d19245824e760e8840ac0f971", "https://api.openweathermap.org/data/2.5/weather");
    }

    /**
     * Send a request to the api of open weather
     * @param params latitude, longitude
     * @return
     */
    public JsonObject sendRequest(String... params) {
        if(false) return  Mock();
        StringBuilder url = new StringBuilder();
        url.append(getAPI_URL());
        url.append(String.format("?lat=%s&lon=%s&appid=",params[0], params[1]));
        url.append(getAPI_KEY());

        String[] responseArr = Curl.sendRequest(RequestType.GET, url.toString());
        StringBuilder responseSB = new StringBuilder();
        for(String line : responseArr){
            responseSB.append(line);
        }

        //System.out.println(responseSB);

        return JsonParser.parse(responseSB.toString());
    }

    private JsonObject Mock(){
        return JsonParser.parse("{\"coord\":{\"lon\":-103.3992,\"lat\":20.6688},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":289.43,\"feels_like\":288.42,\"temp_min\":287.64,\"temp_max\":289.43,\"pressure\":1018,\"humidity\":50},\"visibility\":10000,\"wind\":{\"speed\":0.45,\"deg\":241,\"gust\":0.89},\"clouds\":{\"all\":0},\"dt\":1682165075,\"sys\":{\"type\":2,\"id\":268566,\"country\":\"MX\",\"sunrise\":1682166576,\"sunset\":1682212457},\"timezone\":-21600,\"id\":3979770,\"name\":\"Zapopan\",\"cod\":200}");
    }
}
