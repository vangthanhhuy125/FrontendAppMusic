package com.example.manhinhappmusic;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Feedback{
    private String id;
    private String user_id;
    private String content;
    private String status;
    private String admin_reply;
    private int rating;

    private String date;

    public Feedback() {
        this.date = getCurrentDateTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getContent() { return content; }
    public void setContent(String content ) {this.content = content;}
    public String getUser_id() {return user_id;}
    public void setUser_id(String user_id) {this.user_id = user_id;}
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return formatter.format(new Date());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
