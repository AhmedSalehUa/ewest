package com.example.ewestmembers.SideActivites.Messeges.Contact;

public class MessageContact {
    int id;
    String name;
    String photoUrl;
    String latestMessage;
    String time;
    String date;

    public MessageContact() {
    }

    public MessageContact(int id, String name, String photoUrl, String latestMessage, String time, String date) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.latestMessage = latestMessage;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
