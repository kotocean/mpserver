package com.example.mpserver.model;

/**
 * Created by zhanghd16 on 2018/6/6.
 */
public class Article {
    private int id;
    private String title;
    private String author;
    private String datetime;
    private String content;

    public Article(int id, String title, String author, String datetime, String content) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.datetime = datetime;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
