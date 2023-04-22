package utils;

public class Location {
    private double longitude;
    private double latitude;

    public Location(){
        this(0, 0);
    }

    /**
     * Convert the coordinates into a double
     * @param minutesFormat hours deg minutes' seconds\" Direction
     */
    public static double coordinatesToDouble(String minutesFormat){

        String[] tokens = minutesFormat.split(" ");
        if(tokens.length != 5) throw  new RuntimeException(String.format("Cannot parse %s into double", minutesFormat));
        if(!tokens[2].endsWith("'")) throw  new RuntimeException(String.format("Cannot parse %s into double", minutesFormat));
        if(!tokens[3].endsWith("\"")) throw  new RuntimeException(String.format("Cannot parse %s into double", minutesFormat));
        if(tokens[4].length() != 1) throw  new RuntimeException(String.format("Cannot parse %s into double", minutesFormat));
        if("NEWS".indexOf(tokens[4].charAt(0)) == -1) throw  new RuntimeException(String.format("Cannot parse %s into double", minutesFormat));

        double hours = Double.parseDouble(tokens[0]);
        double minutes = Double.parseDouble(String.copyValueOf(tokens[2].toCharArray(), 0, tokens[2].toCharArray().length - 1)) / 60;
        double seconds = Double.parseDouble(String.copyValueOf(tokens[3].toCharArray(), 0, tokens[3].toCharArray().length - 1)) / 3600;
        return (hours + minutes + seconds) * ("SW".indexOf(tokens[4].charAt(0)) != -1 ? -1 : 1);
    }

    public Location(double longitude, double latitude){
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
