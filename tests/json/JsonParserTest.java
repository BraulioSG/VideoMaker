package json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {
    private JsonObject json;
    @Nested
    @DisplayName("Simple JSON")
    class simpleJsonTest{
        String key = "simpleKey";
        @ParameterizedTest(name = "string -> {0}")
        @DisplayName("simple string")
        @ValueSource(strings = {"sample test", "hello world", "just a simple text", "12345", "just a quote\\\""})
        void simpleString(String expected){
            System.out.println(String.format("{\"%s\":\"%s\"}", key, expected));
            json = JsonParser.parse(String.format("{\"%s\":\"%s\"}", key, expected));

            String result = ((JsonString) json.get(key)).getValue();
            System.out.println(result);
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "number -> {0}")
        @DisplayName("Simple integer")
        @ValueSource(floats = {12.5f, 235.1f, 234, -123.3f, 0})
        void simpleNumber(float expected){
            System.out.println(String.format("{\"%s\":\"%s\"}", key, expected));
            json = JsonParser.parse(String.format("{\"%s\":%f}", key, expected));

            float result = ((JsonNumber) json.get(key)).getValue();
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "bool -> {0}")
        @DisplayName("Simple boolean")
        @ValueSource(booleans = {true, false})
        void simpleBoolean(boolean expected){
            json = JsonParser.parse(String.format("{\"%s\":%s}", key, expected ? "true" : "false"));
            if(json.get(key) == null)
                fail("The value is null");

            boolean result = ((JsonBoolean) json.get(key)).getValue();
            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "arr -> {0}")
        @DisplayName("simple Array")
        @ValueSource(strings = {"[1,2,3,4,5,6,7,8,9,0]", "[\"Hello world\",\"how you been\"]"})
        void simpleArray(String expected){
            json = JsonParser.parse(String.format("{\"%s\":%s}", key, expected));
            if(json.get(key) == null)
                fail("The value is null");

            JsonArray result = ((JsonArray) json.get(key));
            assertEquals(expected, result.toString());
        }
    }

    @Nested
    @DisplayName("Nested JSONs")
    class nestedJsonTest{
        String key = "simpleKey";

        @Test
        @DisplayName("json inside a json")
        void nestedJsonInJson(){
            json = JsonParser.parse(String.format("{\"%s\":{\"hello\":\"world\"}}", key));
            if(json.get(key) == null)
                fail("The value is null");


            String result = ((JsonString) json.get(key).get("hello")).getValue();
            assertEquals("world", result);
        }

        @Test
        @DisplayName("json inside an array")
        void nestedJsonInArray(){
            json = JsonParser.parse(String.format("{\"%s\": [0,1,2,\"hello\",{\"hello\":\"world\"}]}", key));
            if(json.get(key) == null)
                fail("The value is null");

            String result = ((JsonString) json.get(key).get(4).get("hello")).getValue();
            assertEquals("world", result);
        }

        @Test
        @DisplayName("array inside an array")
        void nestedArrayInArray(){
            json = JsonParser.parse(String.format("{\"%s\": [0,1,2,\"hello\",[\"hello\",\"world\"]]}", key));
            if(json.get(key) == null)
                fail("The value is null");

            String result = ((JsonString) json.get(key).get(4).get(1)).getValue();
            assertEquals("world", result);
        }

    }
}