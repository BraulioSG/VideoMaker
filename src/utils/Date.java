package utils;

public class Date {
    private int year, month, day, hour, minute, second;
    private String utc;

    public Date(){
        this(0,0,0,0,0,0,"0");
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
