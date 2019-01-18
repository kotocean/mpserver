package com.example.mpserver.model;

/**
 * Created by zhanghd16 on 2018/5/30.
 */
public class ListData {
    private String src;
    private String display;
    private String headline;

    public ListData(String src, String display, String headline) {
        this.src = src;
        this.display = display;
        this.headline = headline;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }
}
