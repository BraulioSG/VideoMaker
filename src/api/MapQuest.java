package api;

import interpreters.Curl;
import interpreters.RequestType;
import json.JsonObject;
import json.JsonParser;

public class MapQuest extends ApiConnection{

    public MapQuest(){
        super("2PLA6xboMKL7QLY2ZxiCB3qccvqwduRu", "https://www.mapquestapi.com/geocoding/v1/reverse");
    }
    @Override
    public JsonObject sendRequest(String... params) {
        if(false) return  Mock();
        String lat = params[0];
        String lon = params[1];
        StringBuilder url = new StringBuilder();
        url.append(String.format("%s?key=%s", getAPI_URL(), getAPI_KEY()));
        url.append("&location=");
        url.append(lat);
        url.append("%2C");
        url.append(lon);
        url.append("&outFormat=json");
        url.append("&thumbMaps=true");

        String[] responseArr = Curl.sendRequest(RequestType.GET, url.toString());
        StringBuilder responseSB = new StringBuilder();
        for(String line : responseArr){
            responseSB.append(line);
        }

        //System.out.println(responseSB);

        return JsonParser.parse(responseSB.toString());
    }

    public JsonObject Mock(){
        return JsonParser.parse("{\"info\":{\"statuscode\":0,\"copyright\":{\"text\":\"© 2022 MapQuest, Inc.\",\"imageUrl\":\"http://api.mqcdn.com/res/mqlogo.gif\",\"imageAltText\":\"© 2022 MapQuest, Inc.\"},\"messages\":[]},\"options\":{\"maxResults\":1,\"ignoreLatLngInput\":false},\"results\":[{\"providedLocation\":{\"latLng\":{\"lat\":20.668769444444447,\"lng\":-103.39922777777778}},\"locations\":[{\"street\":\"3475 Calle Santo Santiago\",\"adminArea6\":\"Chapalita\",\"adminArea6Type\":\"Neighborhood\",\"adminArea5\":\"Guadalajara\",\"adminArea5Type\":\"City\",\"adminArea4\":\"\",\"adminArea4Type\":\"County\",\"adminArea3\":\"Jal\",\"adminArea3Type\":\"State\",\"adminArea1\":\"MX\",\"adminArea1Type\":\"Country\",\"postalCode\":\"44500\",\"geocodeQualityCode\":\"L1AAA\",\"geocodeQuality\":\"ADDRESS\",\"dragPoint\":false,\"sideOfStreet\":\"L\",\"linkId\":\"0\",\"unknownInput\":\"\",\"type\":\"s\",\"latLng\":{\"lat\":20.66883,\"lng\":-103.39917},\"displayLatLng\":{\"lat\":20.6688,\"lng\":-103.39919},\"mapUrl\":\"\"}]}]}");
    }
}
