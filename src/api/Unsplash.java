package api;

import interpreters.Curl;
import interpreters.RequestType;
import json.JsonObject;
import json.JsonParser;

public class Unsplash extends ApiConnection{
    public Unsplash(){
        super("h2UFwQCqrQXNUV2p7pCgv2AycVSAwaNaeRJnyrgn-2U", "https://api.unsplash.com/search/photos/");
    }
    @Override
    public JsonObject sendRequest(String... params) {
        if(true)return Mock();

        StringBuilder url = new StringBuilder();
        url.append(getAPI_URL());
        url.append(String.format("?client_id=%s", getAPI_KEY()));
        url.append(String.format("&query=%s", params[0]));
        url.append("&orientation=portrait&per_page=1");

        String[] responseArr = Curl.sendRequest(RequestType.GET, url.toString());
        StringBuilder responseSB = new StringBuilder();
        for(String line : responseArr){
            responseSB.append(line);
        }

        //System.out.println(responseSB);

        return JsonParser.parse(responseSB.toString());
    }

    public JsonObject Mock(){
        return JsonParser.parse("{\"total\":43,\"total_pages\":43,\"results\":[{\"id\":\"ZdOsQiwp0Ss\",\"created_at\":\"2021-07-25T20:26:05Z\",\"updated_at\":\"2023-04-22T12:58:36Z\",\"promoted_at\":\"2021-07-26T07:27:02Z\",\"width\":3933,\"height\":4916,\"color\":\"#260c0c\",\"blur_hash\":\"L45g|?~CaHITNgI[M{Ri54Int9xc\",\"description\":null,\"alt_description\":\"boy in black long sleeve shirt sitting on chair in front of black flat screen tv\",\"urls\":{\"raw\":\"https://images.unsplash.com/photo-1627244714766-94dab62ed964?ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\\u0026ixlib=rb-4.0.3\",\"full\":\"https://images.unsplash.com/photo-1627244714766-94dab62ed964?crop=entropy\\u0026cs=srgb\\u0026fm=jpg\\u0026ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\\u0026ixlib=rb-4.0.3\\u0026q=85\",\"regular\":\"https://images.unsplash.com/photo-1627244714766-94dab62ed964?crop=entropy\\u0026cs=tinysrgb\\u0026fit=max\\u0026fm=jpg\\u0026ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\\u0026ixlib=rb-4.0.3\\u0026q=80\\u0026w=1080\",\"small\":\"https://images.unsplash.com/photo-1627244714766-94dab62ed964?crop=entropy\\u0026cs=tinysrgb\\u0026fit=max\\u0026fm=jpg\\u0026ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\\u0026ixlib=rb-4.0.3\\u0026q=80\\u0026w=400\",\"thumb\":\"https://images.unsplash.com/photo-1627244714766-94dab62ed964?crop=entropy\\u0026cs=tinysrgb\\u0026fit=max\\u0026fm=jpg\\u0026ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\\u0026ixlib=rb-4.0.3\\u0026q=80\\u0026w=200\",\"small_s3\":\"https://s3.us-west-2.amazonaws.com/images.unsplash.com/small/photo-1627244714766-94dab62ed964\"},\"links\":{\"self\":\"https://api.unsplash.com/photos/ZdOsQiwp0Ss\",\"html\":\"https://unsplash.com/photos/ZdOsQiwp0Ss\",\"download\":\"https://unsplash.com/photos/ZdOsQiwp0Ss/download?ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\",\"download_location\":\"https://api.unsplash.com/photos/ZdOsQiwp0Ss/download?ixid=Mnw0MzkzODF8MHwxfHNlYXJjaHwxfHx2aWRlb21ha2VyfGVufDB8MXx8fDE2ODIxNzM1OTk\"},\"likes\":111,\"liked_by_user\":false,\"current_user_collections\":[],\"sponsorship\":null,\"topic_submissions\":{\"business-work\":{\"status\":\"approved\",\"approved_on\":\"2021-07-28T08:17:42Z\"},\"work\":{\"status\":\"approved\",\"approved_on\":\"2021-07-28T08:33:38Z\"}},\"user\":{\"id\":\"99F2rjzG4Js\",\"updated_at\":\"2023-04-21T09:07:33Z\",\"username\":\"nublson\",\"name\":\"Nubelson Fernandes\",\"first_name\":\"Nubelson\",\"last_name\":\"Fernandes\",\"twitter_username\":\"nublson\",\"portfolio_url\":\"https://nublson.com\",\"bio\":\"Developer \uD83E\uDDD1\uD83C\uDFFD\u200D\uD83D\uDCBB | Creator \uD83D\uDCF7 | Tech Lover \uD83D\uDCF1\\r\\n  If you appreciate my work, consider supporting me making a donation and following me on Instagram ☕⬇️\",\"location\":\"Lisboa, Portugal\",\"links\":{\"self\":\"https://api.unsplash.com/users/nublson\",\"html\":\"https://unsplash.com/@nublson\",\"photos\":\"https://api.unsplash.com/users/nublson/photos\",\"likes\":\"https://api.unsplash.com/users/nublson/likes\",\"portfolio\":\"https://api.unsplash.com/users/nublson/portfolio\",\"following\":\"https://api.unsplash.com/users/nublson/following\",\"followers\":\"https://api.unsplash.com/users/nublson/followers\"},\"profile_image\":{\"small\":\"https://images.unsplash.com/profile-1648677385138-43d198854d6d?ixlib=rb-4.0.3\\u0026crop=faces\\u0026fit=crop\\u0026w=32\\u0026h=32\",\"medium\":\"https://images.unsplash.com/profile-1648677385138-43d198854d6d?ixlib=rb-4.0.3\\u0026crop=faces\\u0026fit=crop\\u0026w=64\\u0026h=64\",\"large\":\"https://images.unsplash.com/profile-1648677385138-43d198854d6d?ixlib=rb-4.0.3\\u0026crop=faces\\u0026fit=crop\\u0026w=128\\u0026h=128\"},\"instagram_username\":\"nublson\",\"total_collections\":1,\"total_likes\":3,\"total_photos\":332,\"accepted_tos\":true,\"for_hire\":false,\"social\":{\"instagram_username\":\"nublson\",\"portfolio_url\":\"https://nublson.com\",\"twitter_username\":\"nublson\",\"paypal_email\":null}},\"tags\":[{\"type\":\"search\",\"title\":\"filmmaker\"},{\"type\":\"search\",\"title\":\"youtuber\"},{\"type\":\"search\",\"title\":\"youtube studio\"}]}]}");
    }
}
