package com.example.mpserver.service;

import com.example.mpserver.model.Page;
import com.example.mpserver.model.ResponseData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanghd16 on 2018/6/8.
 */
public class ProduceTask implements ITask {

    private String nebula;
    private String flyme;

    private String usersProject;
    private String buildTargetDir;
    private String username;

    private String pages;
    private String routeTemplate;
    private String pageTemplate;
    private String configTempalge;
    private String platform;

    public ProduceTask(String nebula, String flyme, String usersProject, String username, String pages, String routeTemplate, String pageTemplate, String configTempalge, String buildTargetDir, String platform) {
        this.nebula = nebula;
        this.flyme = flyme;
        this.usersProject = usersProject;
        this.username = username;
        this.pages = pages;
        this.routeTemplate = routeTemplate;
        this.pageTemplate = pageTemplate;
        this.configTempalge = configTempalge;
        this.buildTargetDir = buildTargetDir;
        this.platform = platform;
    }

    @Override
    public void run(){
        boolean result = produce();
        if(result){
            System.out.println(username + " produce 成功！");
        }else{
            System.out.println(username + " produce 失败！");
        }
    }

    private boolean produce(){
        //先清理user目录
        FileHelper.deleteDir(new File(nebula+usersProject+username),null);
        boolean result = true;
        List<Page> pageList = JsonHelper.getArray(pages);
        String routesChildren = "[";
        for(int i=0;i<pageList.size();i++){
            Page page = pageList.get(i);
            boolean res = producePage(page.getConfigList(), page.getName(),page.getPages(), page.getCurrentPage(), username);
            if(!res){
                result = false;
                break;
            }
            //构造route路由
            String routeName = (page.getName().split("\\."))[0];
            String routePath = routeName;
            if("index".equals(routeName)){
                routePath = "";
            }
            routesChildren += "{ path: '"+routePath+"', component: () => import('pages/"+routeName+"') },\n";
        }
        //生成routes
        FileHelper fileHelper = new FileHelper();
        Map<String, String> data = new HashMap<String, String>();
        data.put("placeholder", routesChildren+"]");
        fileHelper.replaceTemplate2File(nebula+"src"+File.separator+"router"+File.separator+"", routeTemplate, nebula+usersProject+username+""+File.separator+"src"+File.separator+"router"+File.separator+"","routes.js", data);
        //生成quasar.conf.js
        data = new HashMap<String, String>();
        data.put("placeholder", "/"+username+"/");
        fileHelper.replaceTemplate2File(nebula, configTempalge,nebula+usersProject+username+File.separator,"quasar.conf.js", data);


        if(result) {
            //拷贝之前先清理目录
            List<String> extras = new ArrayList<>();
            extras.add("template.vue");
            extras.add("404.vue");
            extras.add("quasar-logo.png");
            extras.add("icons");
            FileHelper.deleteDir(new File(nebula+"src"+File.separator+"pages"),extras);
            FileHelper.deleteDir(new File(nebula+"src"+File.separator+"statics"),extras);
            //递归拷贝目录文件
            FileHelper.copyDirs(new File(nebula+usersProject+username+File.separator+"src"), new File(nebula+"src"));
            //拷贝文件
            FileHelper.copyDirs(new File(nebula+usersProject+username+File.separator+"quasar.conf.js"), new File(nebula+"quasar.conf.js"));
//            return new ResponseData(ResponseData.Code.SUCCESS, "produce success.");
            //使用系统的cli构建工程
            boolean buildRes = CommandHelper.build("quasar build", platform, nebula, usersProject+username);
            //copy 构建后的工程到指定目录
            //该目录为web server服务目录
            FileHelper.copyDirs(new File(nebula+usersProject+username+File.separator+"dist"+File.separator+"spa-mat"), new File(buildTargetDir+username));
            return buildRes;
        }else{
            return false;
        }
    }

    //生成页面的处理逻辑
    private boolean producePage(String configList, String pageName, String pages, int currentPage, String username){
        //获取所有的image路径
        //statics\/([\u4E00-\u9FA5]|\w)+\.(png|jpg|jpeg|gif)
        String regex = "statics\\/([\\u4E00-\\u9FA5]|\\w)+\\.(png|jpg|jpeg|gif)";
        Pattern p  = Pattern.compile(regex);
        Matcher m   = p.matcher(configList);
        while(m.find()){
//            System.out.println(m.group()); //如statics/mountains.jpg
            String[] img = m.group().split("/");
            int i = 0;
            String targetDir="";
            while(i<img.length-1){
                targetDir += img[i]+File.separator;
                i++;
            }
            String filename=img[i];
            FileHelper.copyFile( flyme+"src"+File.separator+"statics"+File.separator+""+filename, nebula+usersProject+username+""+File.separator+"src"+File.separator+""+targetDir, filename);
        }

        FileHelper fileHelper = new FileHelper();
        Map<String, String> data = new HashMap<String, String>();
        data.put("placeholder", configList);
        data.put("pagesPlaceholder", pages);
        data.put("currentPage", ""+currentPage);
        return fileHelper.replaceTemplate2File(nebula+"src"+File.separator+"pages"+File.separator+"", pageTemplate,nebula+usersProject+username+""+File.separator+"src"+File.separator+"pages"+File.separator+"",  pageName, data);
    }
}
