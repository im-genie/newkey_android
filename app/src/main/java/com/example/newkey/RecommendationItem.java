package com.example.newkey;

public class RecommendationItem {
    private String title;
    private String press;
    private String time;
    private String imageUrl;

    public RecommendationItem(String title, String press, String time, String imageUrl) {
        this.title = title;
        this.press = press;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPress() {
        return press;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
