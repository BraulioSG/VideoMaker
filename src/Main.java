import api.ApiConnection;
import api.NinjaApi;
import api.Unsplash;
import interpreters.Exif;
import interpreters.FFmpeg;
import interpreters.Terminal;
import json.JsonNumber;
import json.JsonObject;
import json.JsonParser;

import java.io.File;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        //TestJsonParser();
        TestVideoMaker();
        //testUnsplash();
        //TestNinjaApi();
        //testExif();
        //testFFmpeg();
        //TestJsonParser2();
    }

    public static void testFFmpeg(){
        /*
        for(int i = 5; i < 10; i++){
            Terminal.execute("ffmpeg", "-loop", "1", "-i", "./samples/imagen" + i + ".jpg", "-c:v", "libx264", "-t", "8", "-pix_fmt", "yuv420p", "-sn", "./temp/vid-" + i + ".mp4", "-y");
        }
        */
        FFmpeg.convertImageToVideo("./samples/imagen5.jpg", "./samples/test.mov");
        //FFmpeg.convertToMp4(new File("./samples/test.mov"), "./samples/test2.mp4");
        //FFmpeg.cropVideo(new File("./samples/test2.mp4"), 720, 1080, "./samples/test3.mp4");

    }

    public static void testExif(){
        HashMap<String, String> metadata = Exif.getFileInformation("./samples/imagen5.jpg");
        System.out.println(metadata);
    }

    public static void TestNinjaApi(){
        NinjaApi ninja = new NinjaApi();
        String res = ninja.getJoke();
        System.out.println(res);
    }

    public  static void testUnsplash(){
        Unsplash unsplash = new Unsplash();
        unsplash.createCover("m");
        //JsonObject unsplashResponse = unsplash.do("vide");
        //System.out.println(unsplash.sendRequest("a"));
        //System.out.println(unsplashResponse);
        //if(unsplashResponse.get("errors") != null) return;
        //if(((JsonNumber) unsplashResponse.get("total")).getInt() == 0) unsplashResponse = unsplash.sendRequest("videomaker");
        //System.out.println(unsplashResponse);
        //System.out.println(unsplashResponse.get("results").get(0).get("urls").get("regular"));

    }
    public static void TestVideoMaker(){
        VideoMaker vm = new VideoMaker();
        vm.init();

    }

    public static void TestJsonParser2(){
        JsonObject json = JsonParser.parse("{\"simpleKey\":[1,2,3,4,5,6,7,8,9,0]}");
        System.out.println(json);
    }

    public static void TestJsonParser(){
        JsonObject json  = JsonParser.parse("{\"coord\":{\"lon\":-103.3992,\"lat\":20.6688},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\",\"main\":{\"temp\":289.43,\"feels_like\":288.42,\"temp_min\":287.64,\"temp_max\":289.43,\"pressure\":1018,\"humidity\":50},\"visibility\":10000,\"wind\":{\"speed\":0.45,\"deg\":241,\"gust\":0.89},\"clouds\":{\"all\":0},\"dt\":1682165075,\"sys\":{\"type\":2,\"id\":268566,\"country\":\"MX\",\"sunrise\":1682166576,\"sunset\":1682212457},\"timezone\":-21600,\"id\":3979770,\"name\":\"Zapopan\",\"cod\":200}");

    }
}