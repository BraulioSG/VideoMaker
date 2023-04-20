package json;


import java.util.Vector;

public class JsonArray extends JsonObject implements Parseable<Vector<JsonObject>> {
    private Vector<JsonObject> value;

    public JsonArray() {
        setDataType(JsonDataType.Array);
        setValue(new Vector<>());
    }

    public void add(JsonObject... objects){
        for(JsonObject obj : objects)
            getValue().add(obj);
    }

    public void add(String... texts){
        for(String text: texts)
            getValue().add(new JsonString(text));

    }

    public void add(int... numbers){
        for(int num: numbers)
            getValue().add(new JsonNumber(num));
    }

    public void add(float... numbers){
        for(float num: numbers)
            getValue().add(new JsonNumber(num));
    }

    public void add(double... numbers){
        for(double num: numbers)
            getValue().add(new JsonNumber((float) num));
    }

    @Override
    public void setValue(Vector<JsonObject> value){
        this.value = value;
    }
    @Override
    public Vector<JsonObject> getValue(){
        return value;
    }

    @Override
    public String stringify(){
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        stringBuilder.append("[");
        for(JsonObject obj: getValue()){
            stringBuilder.append(obj.stringify());
            if(counter != getValue().size() - 1)  stringBuilder.append(",");
            counter++;

        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public JsonObject get(String key) {
        return null;
    }

    @Override
    public JsonObject get(int index) {
        return getValue().get(index);
    }
}
