package json;

import json.exceptions.IncorrectJsonFormat;

import java.util.Stack;

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

        //System.out.println(txt);
        //Scanning
        boolean scanningKey = true;
        boolean openQuotes = false;
        boolean isNumber = false;
        int startingQuoteIdx = 0;
        int startingNumber = 0;

        Stack<Integer> openBraces = new Stack<>();
        String currentKey = null;
        JsonObject currentValue = null;

        char lastChar = ' ';
        //System.out.println(txt);
        for(int idx = 1; idx < txt.length() - 1; idx++){

            char character = txt.charAt(idx);
            //skip the white spaces
            if(character == ' ' && idx < txt.length() - 2) continue;

            if(scanningKey) System.out.print(character);
            lastChar = character;

            //skip the scape character and the followed one
            if(character == '\\') {
                idx++;
                continue;
            }

            //if (scanningKey && (character != '\"' || character != ':') && !openQuotes) throw new IncorrectJsonFormat("Object keys can only be strings");
            if(character == '\"' && openBraces.empty()){
                if(startingQuoteIdx == 0){
                    if(!scanningKey &&  currentValue != null) throw new IncorrectJsonFormat("invalid element syntax ");
                    openQuotes = true;
                    startingQuoteIdx = idx;
                }else{
                    openQuotes = false;
                    String inQuote =  String.copyValueOf(txt.toCharArray(), startingQuoteIdx, idx + 1  - startingQuoteIdx);
                    if(scanningKey) {
                        currentKey = parseString(inQuote).getValue();
                    }
                    else currentValue = parseString(inQuote);
                    startingQuoteIdx = 0;
                }
                continue;
            }

            if (character == ':' ) {
                //System.out.println("colon");
                if(openQuotes || !openBraces.empty()) continue;
                //System.out.println("not skiped");
                if (scanningKey) {
                    scanningKey = false;
                    continue;
                }
                throw new IncorrectJsonFormat("character : found unexpectedly");
            }

            if(scanningKey && !openQuotes){
                throw new IncorrectJsonFormat("Invalid key format");
            }

            if(isNumber && "0123456789.-".indexOf(character) == -1 ) {
                currentValue = parseNumber(String.copyValueOf(txt.toCharArray(), startingNumber, idx - startingNumber));
                isNumber = false;
                startingNumber = 0;
            }

            if(character == ',' && !scanningKey ){

                if(openQuotes || !openBraces.empty()) continue;
                scanningKey = true;
                if(currentKey == null || currentValue == null) throw new IncorrectJsonFormat("error at scanning key");
                dict.add(currentKey, currentValue);
                currentKey = null;
                currentValue = null;
                continue;
            }

            //if(!scanningKey && currentValue != null) throw new IncorrectJsonFormat("wrong value format");



            if(openQuotes) continue;
            if(character == '{' || character == '['){
                if(openQuotes) continue;
                openBraces.add(idx);
            }
            if(character == '}' || character == ']'){
                if(openQuotes) continue;
                System.out.print(character);
                if(openBraces.empty()) throw new IncorrectJsonFormat(String.format("missing symbol for %c ", character));
                int braceIdx = openBraces.pop();
                if((character == ']' && txt.charAt(braceIdx) == '{') || (character == '}' && txt.charAt(braceIdx) == '[')){
                    System.out.println(String.format("error at -> %s%s%s", txt.charAt(idx-1), character, txt.charAt(idx+1)));
                    throw  new IncorrectJsonFormat("opening and close symbols are not compatible");
                }

                if(openBraces.empty()){
                    String braceContent = String.copyValueOf(txt.toCharArray(), braceIdx,idx + 1 - braceIdx);
                    if(character == ']') currentValue = parseArray(braceContent);
                    else currentValue = parseDictionary(braceContent);
                    continue;
                }

            }

            if(!openBraces.empty()) continue;
            if("0123456789.-".indexOf(character) != -1) {
                isNumber = true;
                if(startingNumber == 0) startingNumber = idx;
                continue;
            }

            try{
                if(String.copyValueOf(txt.toCharArray(), idx, 4).equals("true")){
                    idx += 3;
                    currentValue = new JsonBoolean(true);
                    continue;
                }
                if(String.copyValueOf(txt.toCharArray(), idx, 5).equals("false")){
                    idx += 4;
                    currentValue = new JsonBoolean(false);
                    continue;
                }
                throw new IncorrectJsonFormat("not valid value at boolean");
            }catch(IndexOutOfBoundsException e){
                throw new IncorrectJsonFormat("not valid value");
            }


        }

        if(lastChar == ',') throw new IncorrectJsonFormat("trailing comma found in object");

        if(isNumber && startingNumber != 0) {
            currentValue = parseNumber(String.copyValueOf(txt.toCharArray(), startingNumber, txt.length() - 1 - startingNumber));
        }


        if(currentValue != null && currentKey != null){
            dict.add(currentKey, currentValue);
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
                throw new IncorrectJsonFormat(String.format("The %s string cannot be converted missing closing bracket ']' ", txt));
            else
                throw new IncorrectJsonFormat(String.format("The %s string cannot be converted missing opening bracket '[' ", txt));
        }
        //System.out.println(txt);
        JsonArray arr = new JsonArray();
        JsonObject currentElement = null;

        boolean openQuotes = false;
        int startingQuote = 0;
        boolean isNumber = false;
        int startingNumber = 0;
        char lastChar = ' ';
        Stack<Integer> openBraces = new Stack<>();

        for(int idx = 1; idx < txt.length() - 1; idx++){
            char character = txt.charAt(idx);
            if(character == ' '){
                continue;
            }

            if(isNumber && "0123456789.-".indexOf(character) == -1 ) {
                currentElement = parseNumber(String.copyValueOf(txt.toCharArray(), startingNumber, idx - startingNumber));
                startingNumber = 0;
            }



            lastChar = character;

            if(character == ','){
                if((openQuotes || !openBraces.empty())) continue;
                if(currentElement == null) throw new IncorrectJsonFormat("cannot add null into an JsonArray");
                arr.add(currentElement);
                currentElement = null;
                isNumber = false;
                continue;
            }

            if(character == '\\') {
                idx++;
                continue;
            }

            if(character == '\"' && openBraces.empty()){
                if(startingQuote == 0){
                    if(currentElement != null) throw new IncorrectJsonFormat("invalid element syntax ");
                    openQuotes = true;
                    startingQuote = idx;
                }
                else{
                    currentElement = parseString(String.copyValueOf(txt.toCharArray(), startingQuote, idx + 1  - startingQuote));
                    openQuotes = false;
                    startingQuote = 0;
                    continue;
                }
            }




            if(character == '{' || character == '['){
                if(openQuotes) continue;
                openBraces.add(idx);
            }
            if(character == '}' || character == ']'){
                if(openQuotes) continue;
                if(openBraces.empty()) throw new IncorrectJsonFormat(String.format("missing symbol for %c ", character));
                int braceIdx = openBraces.pop();
                if((character == ']' && txt.charAt(braceIdx) == '{') || (character == '}' && txt.charAt(braceIdx) == '[')) throw  new IncorrectJsonFormat("opening and close symbols are not compatible");

                if(openBraces.empty()){
                    String braceContent = String.copyValueOf(txt.toCharArray(), braceIdx,idx + 1 - braceIdx);
                    if(character == ']') currentElement = parseArray(braceContent);
                    else currentElement = parseDictionary(braceContent);
                    continue;
                }


            }
            if(openQuotes) continue;
            if(!openBraces.empty()) continue;

            if("0123456789.-".indexOf(character) != -1) {
                isNumber = true;
                if(startingNumber == 0) startingNumber = idx;
                continue;
            }

            try{
                if(String.copyValueOf(txt.toCharArray(), idx, 4).equals("true")){
                    idx += 3;
                    currentElement = new JsonBoolean(true);
                }
                else if(String.copyValueOf(txt.toCharArray(), idx, 5).equals("false")){
                    idx += 4;
                    currentElement = new JsonBoolean(false);
                }
                else throw new IncorrectJsonFormat("not valid value at boolean parsing");
            }catch(IndexOutOfBoundsException e){
                throw new IncorrectJsonFormat("not valid value");
            }

        }
        if(lastChar == ',') throw new IncorrectJsonFormat("trailing comma found in array");

        if(isNumber && startingNumber != 0) {
            currentElement = parseNumber(String.copyValueOf(txt.toCharArray(), startingNumber, txt.length() - 1 - startingNumber));
        }

        if(currentElement != null){
            arr.add(currentElement);
        }

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
                throw new IncorrectJsonFormat(String.format("The %s string cannot be converted missing closing quote '\"' ", txt));
            else
                throw new IncorrectJsonFormat(String.format("The %s string cannot be converted missing opening quote '\"' ", txt));
        }

        return new JsonString(String.copyValueOf(txt.toCharArray(), 1, txt.length() - 2));
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
