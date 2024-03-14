package com.example.newkey;

public class AlrimItem {

    private String alrim_newstitle;
    private String alrim_time;
    private int type; // 0: alrim_list, 1: alrim_list_todayhot

    public AlrimItem(String alrim_newstitle, String alrim_time, int type) {
        this.alrim_newstitle = alrim_newstitle;
        this.alrim_time = alrim_time;
        this.type = type;
    }

    public String getAlrim_newstitle() {
        return alrim_newstitle;
    }

    public String getAlrim_time() {
        return alrim_time;
    }

    public int getType() {
        return type;
    }
}