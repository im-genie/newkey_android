package com.example.newkey;

public class RecommendationItemBigsize {
    private String title;
    private String press;
    private String time;
    private String imageUrl;

    private String circleImageUrl;

    public RecommendationItemBigsize(String title, String press, String time, String imageUrl, String circleImageUrl) {
        this.title = title;
        this.press = press;
        this.time = time;
        this.imageUrl = imageUrl;
        this.circleImageUrl = circleImageUrl;
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

    public String getCircleImageUrl() { return circleImageUrl; }

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

    public void setCircleImageUrl(String circleImageUrl) { this.circleImageUrl = circleImageUrl; }
}
