package json;


import java.util.HashMap;

public class JsonDictionary extends JsonObject implements Parseable<HashMap<String,JsonObject>>{
    private HashMap<String, JsonObject> content = new HashMap<>();

    public JsonDictionary(){
        setDataType(JsonDataType.Object);
    }


    public void set(HashMap<String, JsonObject> value) {
        this.content = value;
    }

    public void add(String key, JsonObject value) {
        getValue().put(key, value);
    }

    public void add(String key, String value){
        getValue().put(key, new JsonString(value));
    }

    public void add(String key, int value) {
        getValue().put(key, new JsonNumber(value));
    }

    public void add(String key, float value) {
        getValue().put(key, new JsonNumber(value));
    }

    public void add(String key, double value) {
        getValue().put(key, new JsonNumber((float) value));
    }

    @Override
    public JsonObject get(String key) {
        return getValue().get(key);
    }

    @Override
    public JsonObject get(int index) {
        return null;
    }

    @Override
    public HashMap<String, JsonObject> getValue() {
        return this.content;
    }

    @Override
    public void setValue(HashMap<String, JsonObject> value) {
        this.content = value;
    }

    @Override
    public String stringify(){
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        stringBuilder.append("{");
        for(String key: content.keySet()){
            stringBuilder.append(String.format("\"%s\":%s", key, content.get(key).stringify()));
            if(counter != content.keySet().size() -1 ) stringBuilder.append(",");
            counter++;
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
