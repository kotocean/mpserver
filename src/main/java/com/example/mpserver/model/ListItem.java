package com.example.mpserver.model;

/**
 * Created by zhanghd16 on 2018/6/4.
 */
public class ListItem {
    private String to;
    private String image;
    private String label;
    private String sublabel;
    private int sublabelLines;

    public ListItem(String to, String image, String label, String sublabel, int sublabelLines) {
        this.to = to;
        this.image = image;
        this.label = label;
        this.sublabel = sublabel;
        this.sublabelLines = sublabelLines;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSublabel() {
        return sublabel;
    }

    public void setSublabel(String sublabel) {
        this.sublabel = sublabel;
    }

    public int getSublabelLines() {
        return sublabelLines;
    }

    public void setSublabelLines(int sublabelLines) {
        this.sublabelLines = sublabelLines;
    }
}
