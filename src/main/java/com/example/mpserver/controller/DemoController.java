package com.example.mpserver.controller;

import com.example.mpserver.model.*;
import com.example.mpserver.service.TaskHelper;
import com.example.mpserver.service.TaskQueue;
import com.example.mpserver.service.TestTask;
import javafx.concurrent.Task;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by zhanghd16 on 2018/5/30.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/")
    public ComponentData getData(@RequestParam int id, @RequestParam String name){
        System.out.println("======>>>>:"+id+";"+name);
        List<ListData> listData = new ArrayList<>();
        listData.add(new ListData("statics/mountains.jpg","First stop","MountainsMountains"));
        listData.add(new ListData("statics/parallax2.jpg","Third stop","Famous Bridge"));
        return new ComponentData("新闻列表","http://10.136.19.228/demo/list/more/1", listData);
    }
    @RequestMapping("/new")
    public ComponentData getNewData(){
        List<ListData> listData = new ArrayList<>();
//        listData.add(new ListData("statics/parallax1.jpg","Second stop","Famous City"));
        listData.add(new ListData("statics/parallax2.jpg","Third stop","Famous Bridge"));
        listData.add(new ListData("statics/mountains.jpg","First stop","MountainsMountains"));
        return new ComponentData("新闻列表","http://10.136.19.228/demo/list/more/1", listData);
    }

    @RequestMapping("/list")
    public ComponentData getListItemData(){
        List<ListItem> ListItems = new ArrayList<>();
        ListItems.add(new ListItem("/list?id=10","statics/mountains.jpg","Must-see places 1","The world in which we live is full of wonderful places that most of us do not know they really exist. Here you can see some of those breathtaking places  around the world. Enjoy!", 3));
        ListItems.add(new ListItem("/list?id=20","statics/parallax1.jpg","Must-see places 2","The world in which we live is full of wonderful places that most of us do not know they really exist. Here you can see some of those breathtaking places  around the world. Enjoy!", 2));
        return new ComponentData("最新资讯","http://10.136.19.228/demo/list/more/1", ListItems);
    }
    @RequestMapping("/list/more/{id}")
    public MoreData getMoreItems(@PathVariable int id){
        //每次更多项目的返回，都要构造下次更多返回的url
        List<ListItem> ListItems = new ArrayList<>();
        if(id==1) {
            ListItems.add(new ListItem("/list?id="+id, "statics/mountains.jpg", "Must-see places 1", "The world in which we live is full of wonderful places that most of us do not know they really exist. Here you can see some of those breathtaking places  around the world. Enjoy!", 3));
        }else if(id==2) {
            ListItems.add(new ListItem("/list?id="+id, "statics/parallax1.jpg", "Must-see places 2", "The world in which we live is full of wonderful places that most of us do not know they really exist. Here you can see some of those breathtaking places  around the world. Enjoy!", 2));
        }else if(id==3) {
            ListItems.add(new ListItem("/list?id="+id, "statics/parallax2.jpg", "Must-see places 3", "The world in which we live is full of wonderful places that most of us do not know they really exist. Here you can see some of those breathtaking places  around the world. Enjoy!", 2));
        }
        return new MoreData("http://10.136.19.228/demo/list/more/"+(id+1), ListItems);
    }
    @RequestMapping("/list/detail")
    public Article getArticle(@RequestParam int id){
        return new Article(id,"跟宁哥学Go语言（"+id+"）：Go语言数据类型视频教程","李宁","2018/6/6 17:17","<p>本课程是《跟宁哥学Go语言》系列专题的第3个课程，主要面向Go语言的初学者 Go语言可以做很多东西，如命令行工具、Web应用、桌面应用、移动应用、区块链、深度学习等。《跟宁哥学Go语言》系列专题是学习用Go语言编写各种类型应用的基础，是必学的课程。</p>");
    }
    @RequestMapping("/submit")
    public ResponseData submitHandle(@RequestParam int id, @RequestParam String name){
        System.out.println("===========>: " + id+ ";"+name);
        return new ResponseData(ResponseData.Code.SUCCESS, "submit success!");
    }

    @RequestMapping("/task")
    public int commitTask(@RequestParam String username){
        TaskHelper taskHelper = TaskHelper.getInstance();

        int num = taskHelper.add(new TestTask(username));//执行添加任务到任务列表
        System.out.println("======>>> --------- taskQueue size: " + num);
        return num;
    }

    @RequestMapping("/os")
    public void getOsInfo(){
        Properties props=System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        String osArch = props.getProperty("os.arch"); //操作系统构架
        String osVersion = props.getProperty("os.version"); //操作系统版本

        System.out.println("=====>>> ---  osName:"+osName);
        System.out.println("=====>>> ---  osArch:"+osArch);
        System.out.println("=====>>> ---  osVersion:"+osVersion);
    }
}
