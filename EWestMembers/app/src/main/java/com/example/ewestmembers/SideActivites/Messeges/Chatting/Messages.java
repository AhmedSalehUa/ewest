package com.example.ewestmembers.SideActivites.Messeges.Chatting;

import android.view.View;

public class Messages {
    int id;
    String sender;
    String date;
    String time;
    String statue;
    String type;

    View.OnClickListener downloadFileClickListener;
    String fileUrl;
    String fileSize;


    View.OnClickListener playVoiceClickListener;
    boolean isPlaying=false;
    String voiceUrl;


    View.OnClickListener viewImageClickListener;
    String photoUrl;


    View.OnLongClickListener copyMessageClickListener;
    String message;

    public Messages() {
    }

    public Messages(int id, String sender, String date, String time, String type, String message, String photoUrl, String voiceUrl, String fileUrl, String fileSize, String statue) {
        this.id = id;
        this.sender = sender;
        this.date = date;
        this.time = time;
        this.photoUrl = photoUrl;
        this.voiceUrl = voiceUrl;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.message = message;
        this.statue = statue;this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getType() {
        return type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public View.OnClickListener getDownloadFileClickListener() {
        return downloadFileClickListener;
    }

    public void setDownloadFileClickListener(View.OnClickListener downloadFileClickListener) {
        this.downloadFileClickListener = downloadFileClickListener;
    }

    public View.OnClickListener getPlayVoiceClickListener() {
        return playVoiceClickListener;
    }

    public void setPlayVoiceClickListener(View.OnClickListener playVoiceClickListener) {
        this.playVoiceClickListener = playVoiceClickListener;
    }

    public View.OnClickListener getViewImageClickListener() {
        return viewImageClickListener;
    }

    public void setViewImageClickListener(View.OnClickListener viewImageClickListener) {
        this.viewImageClickListener = viewImageClickListener;
    }

    public View.OnLongClickListener getCopyMessageClickListener() {
        return copyMessageClickListener;
    }

    public void setCopyMessageClickListener(View.OnLongClickListener copyMessageClickListener) {
        this.copyMessageClickListener = copyMessageClickListener;
    }
}
