package com.example.mpserver.service;

import com.example.mpserver.model.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghd16 on 2018/5/30.
 */
public class JsonHelper {

    public static List<Page> getArray(String jsonStr){
        JsonParser jsonParser = new JsonParser();
        JsonArray array=(JsonArray)jsonParser.parse(jsonStr);
        String[] pageNames = new String[array.size()];
        for(int i=0;i<array.size();i++) {
            JsonObject item = array.get(i).getAsJsonObject();
            String name = item.get("name").toString();
            //去掉name首末的"
            if (name.indexOf("\"") == 0) {
                name = name.substring(1, name.length());
            }
            if (name.lastIndexOf("\"") == name.length() - 1) {
                name = name.substring(0, name.length() - 1);
            }
            pageNames[i]=name;
        }

        List<Page> pages = new ArrayList<>();
        for(int i=0;i<array.size();i++){
            JsonObject item = array.get(i).getAsJsonObject();
            String configStr = item.get("list").toString();
            pages.add(new Page(pageNames[i], configStr, array2String(pageNames), i));
        }
        return pages;
    }
    private static String array2String(String [] arr){
        String temp = "{";
        for(int i=0;i<arr.length;i++){
            temp += i+":'"+arr[i]+"',";
        }
        temp = temp.substring(0, temp.length()-1);
        temp +=",'pageNum':"+arr.length+"}";
        return temp;
    }
}
