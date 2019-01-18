package com.example.mpserver.model;

/**
 * Created by zhanghd16 on 2018/5/31.
 */
public class Page {
    private String name;
    private String configList;
    private String pages;
    private int currentPage;

    public Page(String name, String configList, String pages, int currentPage) {
        this.name = name;
        this.configList = configList;
        this.pages = pages;
        this.currentPage = currentPage;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigList() {
        return configList;
    }

    public void setConfigList(String configList) {
        this.configList = configList;
    }
}
