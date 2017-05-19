package com.example.carva.whatsnext;

//Class to take Task object
public class Task {
    private String name="";
    private String note="";
    private String day="";
    private String month="";
    private String year="";
    private String hour="";
    private String minute="";

    public Task() {

    }

    public Task(String name, String note, String day, String month, String year, String minute,String hour) {
        this.name = name;
        this.note = note;
        this.day = day;
        this.month = month;
        this.year = year;
        this.minute = minute;
        this.hour= hour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}