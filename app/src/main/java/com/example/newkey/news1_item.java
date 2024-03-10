package com.example.newkey;

import android.os.Parcel;
import android.os.Parcelable;

public class news1_item {
    private String id;
    private String title;
    private String content;
    private String publisher;
    private String date;
    private String img;
    private String summary;
    private String key;
    private String reporter;
    private String mediaImg;


    public news1_item(String id, String title, String content, String publisher, String date, String img, String summary, String key, String reporter, String mediaImg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publisher = publisher;
        this.date = date;
        this.img = img;
        this.summary = summary;
        this.key = key;
        this.reporter = reporter;
        this.mediaImg = mediaImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getMediaImg() {
        return mediaImg;
    }

    public void setMediaImg(String mediaImg) {
        this.mediaImg = mediaImg;
    }
}
