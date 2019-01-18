package com.example.mpserver.model;

import java.util.List;

/**
 * Created by zhanghd16 on 2018/6/1.
 */
public class ComponentData {
    private String header;
    private String moreUrl;
    private List<?> items;

    public ComponentData(String header, String moreUrl, List<?> items) {
        this.header = header;
        this.moreUrl = moreUrl;
        this.items = items;
    }

    public String getMoreUrl() {
        return moreUrl;
    }

    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }
}
