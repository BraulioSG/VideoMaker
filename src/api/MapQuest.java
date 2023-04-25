package api;

import interpreters.Curl;
import interpreters.RequestType;
import json.JsonDictionary;
import json.JsonObject;
import json.JsonParser;
import system.FileManager;

public class MapQuest extends ApiConnection{

    public MapQuest(){
        super("2PLA6xboMKL7QLY2ZxiCB3qccvqwduRu", "https://www.mapquestapi.com/staticmap/v5/map");
    }
    @Override
    public JsonObject sendRequest(String... params) {
        if(false) return  Mock();
        String lat1 = params[0];
        String lon1 = params[1];
        String lat2 = params[2];
        String lon2 = params[3];
        StringBuilder url = new StringBuilder();
        url.append(String.format("%s?key=%s", getAPI_URL(), getAPI_KEY()));
        url.append("&locations=");
        url.append(String.format("%s,%s||%s,%s|", lat1, lon1,lat2,lon2));
        url.append("marker-red-sm&size=1080,1920&defaultMarker=marker-sm-22407F-3B5998&size=1080,1920@2x");

        FileManager.createDirectory("./temp/map");
        Curl.sendRequest(RequestType.GET, String.format("\"%s\"",url), "--output", "./temp/map/map.png");

        //System.out.println(responseSB);
        JsonDictionary dic = new JsonDictionary();
        dic.add("status", "complete");
        return dic;
    }

    public JsonObject Mock(){
        return JsonParser.parse("{\"info\":{\"statuscode\":0,\"copyright\":{\"text\":\"© 2022 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"imageAltText\":\"© 2022 MapQuest, Inc.\"},\"messages\":[]},\"options\":{\"maxResults\":1,\"ignoreLatLngInput\":false},\"results\":[{\"providedLocation\":{\"latLng\":{\"lat\":20.668769444444447,\"lng\":-103.39922777777778}},\"locations\":[{\"street\":\"3475 Calle Santo Santiago\",\"adminArea6\":\"Chapalita\",\"adminArea6Type\":\"Neighborhood\",\"adminArea5\":\"Guadalajara\",\"adminArea5Type\":\"City\",\"adminArea4\":\"\",\"adminArea4Type\":\"County\",\"adminArea3\":\"Jal\",\"adminArea3Type\":\"State\",\"adminArea1\":\"MX\",\"adminArea1Type\":\"Country\",\"postalCode\":\"44500\",\"geocodeQualityCode\":\"L1AAA\",\"geocodeQuality\":\"ADDRESS\",\"dragPoint\":false,\"sideOfStreet\":\"L\",\"linkId\":\"0\",\"unknownInput\":\"\",\"type\":\"s\",\"latLng\":{\"lat\":20.66883,\"lng\":-103.39917},\"displayLatLng\":{\"lat\":20.6688,\"lng\":-103.39919},\"mapUrl\":\"\"}]}]}");
    }
}
