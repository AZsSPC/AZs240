package com.azspc.azchat240;

import android.content.res.Resources;

public class Post {
    String author, title, text, date;
    int color;

    public int getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    Post(Resources r, String author, String title, String text, String date, int type) {
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = date;
        this.color = r.getColor(initColor(type));
    }

    int initColor(int c) {
        switch (c) {
            default:
                return R.color.t_normal;
            case -1:
                return R.color.t_system;
            case 1:
                return R.color.t_spy;
            case 2:
                return R.color.t_alarm;
        }
    }
}
