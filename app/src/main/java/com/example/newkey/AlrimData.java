package com.example.newkey;

public class AlrimData {

    private int alrim_icon;
    private String alrim_todaynews;
    private String alrim_content;

    public AlrimData(int alrim_icon, String alrim_todaynews, String alrim_content) {
        this.alrim_icon = alrim_icon;
        this.alrim_todaynews = alrim_todaynews;
        this.alrim_content = alrim_content;
    }

    public int getAlrim_icon() {
        return alrim_icon;
    }

    public void setAlrim_icon(int alrim_icon) {
        this.alrim_icon = alrim_icon;
    }

    public String getAlrim_todaynews() {
        return alrim_todaynews;
    }

    public void setAlrim_todaynews(String alrim_todaynews) {
        this.alrim_todaynews = alrim_todaynews;
    }

    public String getAlrim_content() {
        return alrim_content;
    }

    public void setAlrim_content(String alrim_content) {
        this.alrim_content = alrim_content;
    }
}
