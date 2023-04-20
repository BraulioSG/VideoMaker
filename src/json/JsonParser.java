package json;

import json.exceptions.IncorrectJsonFormat;

public class JsonParser {
    /**
     * Given a string, it will throw return a JSON object with all its properties,
     * if there is wrong formatted then it will return a IncorrectJsonFormat exception
     * @param text json in form of text
     * @return Json
     * @throws IncorrectJsonFormat if the format is wrong
     */
    public static JsonObject parse(String text) throws IncorrectJsonFormat {
        String singleLine = text.replace("\n","").trim();
        return parseDictionary(singleLine);
    }

    /**
     * Parse a string into a JsonDictionary
     * @param txt json object in form of text
     * @return Json dictionary
     * @throws IncorrectJsonFormat if the format is wrong
     */
    private static JsonDictionary parseDictionary(String txt) throws IncorrectJsonFormat{
        if(!txt.startsWith("{")||  !txt.endsWith("}")) {
            if(!txt.endsWith("}"))
                throw new IncorrectJsonFormat("The %s string cannot be converted missing closing brace '} ");
            else
                throw new IncorrectJsonFormat("The %s string cannot be converted missing opening brace '{' ");
        }
        JsonDictionary dict = new JsonDictionary();

        //Scanning
        boolean scanningKey = true;
        boolean openQuotes = false;
        char[] charList = txt.toCharArray();
        for(int idx = 1; idx < charList.length - 1; idx++){
            char character = charList[idx];
            if(character == ' ') continue;
            if(!scanningKey) return null;
        }

        return dict;
    }

    /**
     * Parse a string into a JsonArray
     * @param txt json array in form of text
     * @return Json array
     * @throws IncorrectJsonFormat if the format is wrong
     */
    private static JsonArray parseArray(String txt) throws IncorrectJsonFormat{
        if(!txt.startsWith("[") || !txt.endsWith("]")) {
            if(!txt.endsWith("]"))
                throw new IncorrectJsonFormat("The %s string cannot be converted missing closing bracket ']' ");
            else
                throw new IncorrectJsonFormat("The %s string cannot be converted missing opening bracket ']' ");
        }
        JsonArray arr = new JsonArray();

        return arr;
    }

    /**
     * Parse a string into a JsonArray
     * @param txt json object in form of text
     * @return Json string
     * @throws IncorrectJsonFormat if the format is wrong
     */
    private static JsonString  parseString(String txt) throws IncorrectJsonFormat{
        txt = txt.trim();
        if(!txt.startsWith("\"") || !txt.endsWith("\"")) {
            if(!txt.endsWith("\""))
                throw new IncorrectJsonFormat("The %s string cannot be converted missing closing bracket '\"' ");
            else
                throw new IncorrectJsonFormat("The %s string cannot be converted missing opening bracket '\"' ");
        }
        StringBuilder str = new StringBuilder();
        return new JsonString(str.toString());
    }

    /**
     * Parse a string into a JsonNumber
     * @param txt json number in form of text
     * @return Json number
     * @throws IncorrectJsonFormat if the format is wrong
     */
    private static JsonNumber parseNumber(String txt) throws IncorrectJsonFormat{
        JsonNumber num = new JsonNumber(txt);
        return num;
    }
}
