package json;

public interface Parseable<T> {

    public T getValue();
    public void setValue(T value);

}
