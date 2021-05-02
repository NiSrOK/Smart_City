package com.example.handmadeproject;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Date;

public class Message {
    public String userName;
    public String textMessage;
    private long messageTime;
    public String latitude;
    public  String longitude;
    public  String serv;
    public String url;



    public Message(){}

    public Message (String userName,String textMessage, String latitude, String longitude, String serv, String url){
        this.userName = userName;
        this.textMessage = textMessage;
        this.messageTime = new Date().getTime();
        this.latitude = latitude;
        this.longitude = longitude;
        this.serv = serv;
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServ() {
        return serv;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setServ(String serv) {
        this.serv = serv;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
