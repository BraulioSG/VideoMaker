import json.JsonObject;
import json.JsonParser;

public class Main {
    public static void main(String[] args) {
        //TestJsonParser();
        TestVideoMaker();
    }

    public static void TestVideoMaker(){
        VideoMaker vm = new VideoMaker();
        vm.init();

    }

    public static void TestJsonParser(){
        JsonObject json  = JsonParser.parse("{\"coord\":{\"lon\":-103.3992,\"lat\":20.6688},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":289.43,\"feels_like\":288.42,\"temp_min\":287.64,\"temp_max\":289.43,\"pressure\":1018,\"humidity\":50},\"visibility\":10000,\"wind\":{\"speed\":0.45,\"deg\":241,\"gust\":0.89},\"clouds\":{\"all\":0},\"dt\":1682165075,\"sys\":{\"type\":2,\"id\":268566,\"country\":\"MX\",\"sunrise\":1682166576,\"sunset\":1682212457},\"timezone\":-21600,\"id\":3979770,\"name\":\"Zapopan\",\"cod\":200}");

    }
}