package utils;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Date {
    private int year, month, day, hour, minute, second;
    private String utc;

    public Date(){
        setYear(0);
        setMonth(0);
        setDay(0);
        setHour(0);
        setMinute(0);
        setSecond(0);
        setUTC("0");
    }

    public Date(int year, int month, int day, int hour, int minute, int second, String utc){
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
        setSecond(second);
        setUTC(utc);
    }

    public Date(String exifMetadata){
        if(exifMetadata.length() < 19 || exifMetadata.length() > 25){
            if (exifMetadata.length() < 19) throw  new RuntimeException(String.format("%s cannot be parsed into date", exifMetadata));
            exifMetadata = String.copyValueOf(exifMetadata.toCharArray(), 0, 19);
        }
        String utc = "-0:00";
        if(exifMetadata.length() > 19){
            utc = String.copyValueOf(exifMetadata.toCharArray(),19, 6);
            exifMetadata = String.copyValueOf(exifMetadata.toCharArray(), 0, 19);
        }

        setUTC(utc);

        String[] tokens = exifMetadata.split(" ");
        if(tokens.length != 2) throw new RuntimeException(String.format("%s cannot be parsed into date", exifMetadata));
        String dateString = tokens[0];
        String timeString = tokens[1];

        if(!Pattern.matches("[0-9]{4}:[0-9]{2}:[0-9]{2}", dateString)) throw new RuntimeException(String.format("%s cannot be parsed into date", exifMetadata));
        if(!Pattern.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}", timeString)) throw new RuntimeException(String.format("%s cannot be parsed into date", exifMetadata));

        int[] dateValues = Arrays.stream(dateString.split(":")).mapToInt(Integer::parseInt).toArray();
        int[] timeValues = Arrays.stream(timeString.split(":")).mapToInt(Integer::parseInt).toArray();

        setYear(dateValues[0]);
        setMonth(dateValues[1]);
        setDay(dateValues[2]);
        setHour(timeValues[0]);
        setMinute(timeValues[1]);
        setSecond(timeValues[2]);

    }

    public boolean isOlderThan(Date date2){
        if(getYear() != date2.getYear()) return getYear() < date2.getYear();
        if(getMonth() != date2.getMonth()) return  getMonth() < date2.getMonth();
        if(getDay() != date2.getDay()) return  getDay() < date2.getDay();
        if(getHour() != date2.getHour()) return getHour() < date2.getHour();
        if(getMinute() != date2.getMinute()) return  getMinute() < date2.getMinute();
        return getSecond() < date2.getSecond();
    }

    //GETTERS AND SETTERS
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if( month < 1 || month > 12){
            this.month = 1;
            return;
        }
        this.month = month;
    }

    public int getDay() {
        int month = getMonth();

        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getUTC() {
        return utc;
    }

    public void setUTC(String utc) {
        this.utc = utc;
    }
}
