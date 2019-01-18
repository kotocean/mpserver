package com.example.mpserver.controller;

import com.example.mpserver.model.ResponseData;
import com.example.mpserver.service.ZipManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zhanghd16 on 2018/5/21.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger LOG = Logger.getLogger(FileController.class.getName());

//    String targetDir = "E:\\ui\\mpvueDemos\\mpvue-weui-1.0.3\\src\\utils\\";
    private String mpDir = "E:\\ui\\mpvueDemos"; //所有模板小程序的存放目录

    @PostMapping("/update")
    public ResponseData update(@RequestParam String fileName, @RequestParam String data, @RequestParam String mpName){
//        System.out.println(data);
        String name = mpName;//"mpvue-weui-1.0.3"; //小程序的名字
        String content = "var data="+data;
        content += "\n export default{data: data}"; //目前只配置数据，方法TODO
        String targetDir = mpDir+"\\"+name+"\\src\\utils\\";
        boolean result = saveAsFile(targetDir+fileName, content);
        if(result){
            return new ResponseData(ResponseData.Code.SUCCESS, "update success.");
        }else{
            return new ResponseData(ResponseData.Code.FAILDED, "update failed.");
        }
    }

    @GetMapping("/download")
    public void download(HttpServletResponse resp, @RequestParam String mpName){
        ZipManager zipManager= new ZipManager();
        String fileDir = mpDir+"\\"+ mpName +"\\dist";
        String zipFile = "zipTemp_" + System.currentTimeMillis() + ".zip";
        zipManager.zipMultiFile(fileDir, zipFile, true);

        File file = new File(zipFile);
        resp.setHeader("content-type", "application/octet-stream");
        resp.setContentType("application/octet-stream");
//        resp.setHeader("Content-Length", String.valueOf(file.length()));
        resp.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        byte[] buff = new byte[2048];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, e.toString());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, e.toString());
                }
            }
        }
    }

    @PostMapping("/build")
    public ResponseData build(@RequestParam String mpName){
        String [] cmd={"cmd","/C","npm run build"};
        String dir = mpDir+"\\" + mpName;
        File targetDir = new File(dir+"\\dist");

        Process p = null;

        try {
//            long buildStartTime = System.currentTimeMillis();//1526953706695
            //构建之前先删除已经存在的dist,为检查是否build成功提供条件
            if(targetDir.exists()){
                deleteDir(targetDir);
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
                    Thread.sleep(1000);
                }catch (Exception e){
                    LOG.log(Level.WARNING, "Thread sleep exception.");
                }
            }
        }
        if(count>=maxCount){
            return new ResponseData(ResponseData.Code.FAILDED, "update failed.");
        }else {
            return new ResponseData(ResponseData.Code.SUCCESS, "update success.");
        }
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    //覆盖的另存为fileName
    private boolean saveAsFile(String fileName, String content){
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
        }catch (IOException ex){
//            System.out.println("文件写入失败");
            LOG.log(Level.WARNING, "File write failed.");
            return false;
        }finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException ex){
//                System.out.println("文件flush或close失败");
                LOG.log(Level.WARNING, "File flush or close failed.");
                return false;
            }
        }
//        System.out.println("文件写入成功！");
        return true;
    }
}
