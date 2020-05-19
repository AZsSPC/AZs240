package com.azspc.azchat240;

import android.content.res.Resources;

public class Post {
    String  title, text;
    int color;

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    Post(Resources r,  String title, String text, String type) {
        this.title = title;
        this.text = text;
        this.color = r.getColor(initColor(type));
    }

    int initColor(String c) {
        switch (c) {
            case "-1":
                return R.color.t_system;
            default:
                return R.color.t_normal;
            case "1":
                return R.color.t_spy;
            case "2":
                return R.color.t_alarm;
        }
    }
}
