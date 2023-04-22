package json;

import json.exceptions.IncorrectJsonFormat;

public class JsonBoolean extends JsonObject{
    private boolean value;

    public JsonBoolean(String bool) throws IncorrectJsonFormat{
        if(bool.equals("true")) setValue(true);
        if(bool.equals("false")) setValue(false);
        else throw new IncorrectJsonFormat("booleans can only be true or false ");
    }

    public JsonBoolean(boolean bool){
        setValue(bool);
    }

    public void setValue(boolean bool){
        this.value = bool;
    }

    public boolean getValue(){
        return this.value;
    }

    @Override
    public String stringify() {
        return value ? "true" : "false";
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
