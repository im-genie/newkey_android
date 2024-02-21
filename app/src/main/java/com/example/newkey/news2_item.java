package com.example.newkey;

public class news2_item {
    private String title;
    private String publisher;
    private String time;
    private String img;

    public news2_item(String title, String publisher, String time, String img) {
        this.title = title;
        this.publisher = publisher;
        this.time = time;
        this.img=img;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTime() {
        return time;
    }

    public String getImg() {return img;}

    public void setImg(String img2) {this.img=img2;}

}
