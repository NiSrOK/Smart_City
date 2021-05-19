package com.example.smartcity.pojo;

import com.example.smartcity.R;

import java.util.Date;

public class Message {
    private String text;
    private String userEmail;
    private String imageUrl;
    private String latitude;
    private String longitude;
    private String service;
    private String status;
    private long messageTime;

    public  Message(){}

    public Message(String text, String userEmail, String imageUrl, String latitude, String longitude, String service) {
        this.text = text;
        this.userEmail = userEmail;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.service = service;
        this.status = "В обработке.";
        this.messageTime = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getService() {
        return service;
    }

    public String getStatus() {
        return status;
    }

    public long getMessageTime() {
        return messageTime;
    }
}
