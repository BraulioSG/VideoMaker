package json;

public class JsonString extends JsonObject implements Parseable<String>{
    private String value;

    public JsonString(String text){
        setDataType(JsonDataType.String);
        setValue(text);
    }

    @Override
    public void setValue(String val){
        this.value = val;
    }
    @Override
    public String getValue(){
        return value;
    }

    @Override
    public String stringify(){
        return String.format("\"%s\"", value);
    }

    @Override
    public JsonObject get(String key) {
        return null;
    }

    @Override
    public JsonObject get(int index) {
        return null;
    }
}
