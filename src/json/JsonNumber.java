package json;


import json.exceptions.IncorrectJsonFormat;

public class JsonNumber extends JsonObject implements Parseable<Float>{
    private float value;

    public JsonNumber(int val){
        this((float) val);
    }

    public JsonNumber(float val){
        setDataType(JsonDataType.Number);
        setValue(val);
    }

    public JsonNumber(String val) throws IncorrectJsonFormat {
        setDataType(JsonDataType.Number);
        try{
            setValue(Float.parseFloat(val));
        }catch (NumberFormatException intException){
            String errorMessage = String.format("Error: %s cannot be parsed into a JSON Number", val);
            throw new IncorrectJsonFormat(errorMessage);
        }
    }

    public int getInt(){
        return  (int) getFloat();
    }

    public float getFloat(){
        return getValue();
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public String stringify(){
        String str;
        if(getValue() - (float)getInt() != 0){
            str = getValue().toString();
        }else{
            str = Integer.toString(getInt());
        }
        return str;
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
