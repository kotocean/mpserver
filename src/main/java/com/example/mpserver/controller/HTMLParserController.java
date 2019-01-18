package com.example.mpserver.controller;

import com.example.mpserver.model.Page;
import com.example.mpserver.model.ResponseData;
import com.example.mpserver.service.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zhanghd16 on 2018/5/29.
 */
@RestController
@RequestMapping("/htmlparser")
public class HTMLParserController {
    private static final Logger LOG = Logger.getLogger(HTMLParserController.class.getName());

    @Value("${my.project.nebula}")
    private String nebula;
    @Value("${my.project.flyme}")
    private String flyme;
    @Value("${my.project.buildTargetDir}")
    private String buildTargetDir;

    @Value("${my.platform}")
    private String platform;

    private String pageTemplate="template.vue";
    private String routeTemplate="template.js";
    private String configTemplate = "quasar.conf.template.js";
    private String usersProject="usersProject"+File.separator;
    //图片上传
    @RequestMapping("/upload")
    public ResponseData handleFileUpload(@RequestParam("file") MultipartFile file){
        if(!file.isEmpty()){
            String name = System.currentTimeMillis()+"_"+ file.getOriginalFilename();
            try{
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(
                        new File( flyme+"src"+File.separator+"statics"+File.separator+"" + name)
                ));
                out.write(file.getBytes());
                out.flush();
                out.close();
            }catch (Exception ex){
                LOG.log(Level.WARNING, "upload exception: "+ ex.getMessage());
                return new ResponseData(ResponseData.Code.FAILDED, "upload failed: "+ ex.getMessage());
            }
            return new ResponseData(ResponseData.Code.SUCCESS, name);
        }
        return new ResponseData(ResponseData.Code.FAILDED,"upload failed: File empty.");
    }

    @RequestMapping("/replace")
    public void replace(){
        File input = new File(nebula+"src"+File.separator+"pages"+File.separator+"index.vue");
        try {
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Elements elements = doc.select("vddl-list");
//            System.out.println("==========>>>>: " + elements.html());
        }catch (Exception ex){
            LOG.log(Level.WARNING, "Html parser Exception.");
        }
    }

    @RequestMapping("/produce")
    public ResponseData produce(@RequestParam String pages, @RequestParam String username){
        //收到构建命令，则首先清空用户目录
        FileHelper.deleteDir(new File(nebula+usersProject+username+File.separator+"dist"), null);
        //再把任务加入执行队列中
        TaskHelper taskHelper = TaskHelper.getInstance();
        //用户的构建放入执行队列中，依次执行构建任务
        int num = taskHelper.add(new ProduceTask(nebula, flyme,usersProject,username, pages, routeTemplate, pageTemplate, configTemplate, buildTargetDir, platform));//执行添加任务到任务列表
        LOG.log(Level.INFO,"taskQueue size: " + num);

        return new ResponseData(ResponseData.Code.SUCCESS, "taskQueue num: " + num);
    }
    @RequestMapping("/downloadZip")
    private void downloadZip(HttpServletResponse resp,@RequestParam String username){
        String fileDir = nebula+usersProject+username+File.separator+"dist";
        File downloadDir = new File(fileDir);
        if(downloadDir.exists()) {
            FileHelper.downloadZip(resp, fileDir, username);
        }
    }
    @RequestMapping("/downloadZip/check")
    private ResponseData downloadZipCheck(@RequestParam String username){
        String fileDir = nebula+usersProject+username+File.separator+"dist";
        File downloadDir = new File(fileDir);
        if(downloadDir.exists()) {
            return new ResponseData(ResponseData.Code.SUCCESS, "build success!");
        }else{
            return new ResponseData(ResponseData.Code.FAILDED, "building ... or build failed.");
        }
    }
}
