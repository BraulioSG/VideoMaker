package json;

public abstract class JsonObject{
    protected JsonDataType dataType;

    public JsonObject(){
        setDataType(JsonDataType.Object);
    }

    protected void setDataType(JsonDataType type){
        this.dataType = type;
    }

    public JsonDataType getDataType(){
        return  this.dataType;
    }

    public abstract String stringify();
    public abstract JsonObject get(String key);
    public abstract JsonObject get(int index);

    @Override
    public String toString(){
        return this.stringify();
    }
}
