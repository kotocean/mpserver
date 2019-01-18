package com.example.mpserver.service;

import com.example.mpserver.controller.FileController;
import com.example.mpserver.model.ResponseData;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zhanghd16 on 2018/6/4.
 */
public class CommandHelper {

    private static final Logger LOG = Logger.getLogger(CommandHelper.class.getName());

    public static boolean build(String command, String platform, String dir, String userDir){
        String [] cmd=new String[3];
//        String[] cmd={"/bin/sh","-c", command};
        if(platform.toLowerCase().contains("windows")){
            cmd[0] = "cmd";
            cmd[1] = "/C";
        }else if(platform.toLowerCase().contains("linux")){
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
        }else{
            LOG.log(Level.WARNING, "Unknown platform! Please choose right platform.");
            return false;
        }
        cmd[2] = command;
        File targetDir = new File(dir+"dist");

        Process p = null;

        try {
//            long buildStartTime = System.currentTimeMillis();//1526953706695
            //构建之前先删除已经存在的dist,为检查是否build成功提供条件
            if(targetDir.exists()){
                FileHelper.deleteDir(targetDir, new ArrayList<>());
            }
            p = Runtime.getRuntime().exec(cmd, null, new File(dir));
        }catch (Exception e){
            LOG.log(Level.WARNING,"command run exception: "+e.toString());
        }
        boolean start = true;
        int count = 0;
        int maxCount = 30; //最大sleep次数，也就是build构建超时时间timeout
        while(start){
            if(targetDir.exists() && targetDir.isDirectory()){
//                System.out.println(targetDir.lastModified());//1526953713905-1526953706695=7210
                start = false;
            }else{
                count ++;
                if(count>=maxCount){
                    start = false; //超过最大等待时间，停止
                    if(p!=null){//强制终止进程
                        p.destroyForcibly();
                    }
                }
                try {
                    Thread.sleep(500);
                }catch (Exception e){
                    LOG.log(Level.WARNING, "Thread sleep exception.");
                }
            }
        }
        if(count>=maxCount){
            return false;
        }else {
            //构建成功后，copy到username目录
            File userDistDir = new File(dir+userDir+File.separator+"dist");
            //先清除历史构建文件
            FileHelper.deleteDir(userDistDir, new ArrayList<>());
            //copy完成后，检查是否已成功copy
            FileHelper.copyDirs(new File(dir+"dist"), userDistDir);
            if(userDistDir.exists()) {
                return true;
            }else{
                return false;
            }
        }
    }
}
