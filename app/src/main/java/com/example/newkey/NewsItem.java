package com.example.newkey;

public class NewsItem {
    private String title;
    private String publisher;
    private String time;
    private String imageUrl;

    public NewsItem(String title, String publisher, String time, String imageUrl) {
        this.title = title;
        this.publisher = publisher;
        this.time = time;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }
}