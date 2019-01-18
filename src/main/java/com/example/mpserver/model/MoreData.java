package com.example.mpserver.model;

import java.util.List;

/**
 * Created by zhanghd16 on 2018/6/1.
 */
public class MoreData {
    private String moreUrl;
    private List<?> items;

    public MoreData(String moreUrl, List<?> items) {
        this.moreUrl = moreUrl;
        this.items = items;
    }

    public String getMoreUrl() {
        return moreUrl;
    }

    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }
}
