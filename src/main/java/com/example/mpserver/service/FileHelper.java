package com.example.mpserver.service;

import com.example.mpserver.controller.HTMLParserController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanghd16 on 2018/5/30.
 */
public class FileHelper {
    private static final Logger LOG = Logger.getLogger(FileHelper.class.getName());
    private static final String DEF_REGEX="\\[\\{(.+?)\\}\\]";
    public static final String pZipDir = "userZip"+File.separator;

    public String fileRead(String fileName) {
        File file = new File(fileName);
        String str = "";
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String s="";
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
            bufferedReader.close();
            str = sb.toString();
        }catch (Exception ex){
            LOG.log(Level.WARNING, "file reader failed.");
        }
//        System.out.println(str);
        return str;
    }

    public boolean replaceTemplate2File(String sourceDir,String templateName, String destDir, String fileName, Map<String, String> data){
        //先读取template,替换后写成对应的文件
        String template = fileRead(sourceDir + templateName);
        String result = render(template, data);
        LOG.log(Level.INFO, "Render page: "+fileName);
        //检查目录是否存在，不存在则新建
        File dirs = new File(destDir);
        if(!dirs.exists()){
            dirs.mkdirs();
        }
        //模板文件写入对应的文件
        return saveAsFile(destDir+fileName, result);
    }

    private String render(String template, Map<String, String> data) {
        return render(template,data,DEF_REGEX);
    }

    private String render(String template, Map<String, String> data,String regex) {
        if(StringUtils.isBlank(template)){
            return "";
        }
        if(StringUtils.isBlank(regex)){
            return template;
        }
        if(data == null || data.size() == 0){
            return template;
        }
        try {
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(template);
            while (matcher.find()) {
                String name = matcher.group(1);// 键名
                String value = data.get(name);// 键值
                if (value == null) {value = "";}
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }
    //覆盖的另存为fileName
    public boolean saveAsFile(String fileName, String content){
//        LOG.log(Level.INFO, fileName+" "+ content);
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(fileName);
            fileWriter.write(content);
        }catch (IOException ex){
            LOG.log(Level.WARNING, "File write failed.");
            return false;
        }finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException ex){
                LOG.log(Level.WARNING, "File flush or close failed.");
                return false;
            }
        }
        return true;
    }
    //递归删除目录中文件
    public static boolean deleteDir(File dir, List<String> extras) {
        if (dir.isDirectory()) {
            if(null!=extras && extras.contains(dir.getName())){
                return true;
            }
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]), extras);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        if(null!=extras && extras.contains(dir.getName())){
            return true; //排除一些不需要删除的文件
        }else {
            return dir.delete();
        }
    }
    //下载
    public static void downloadZip(HttpServletResponse resp,String fileDir, String username){

        ZipManager zipManager= new ZipManager();
        String zipFile = pZipDir + username+"_" + System.currentTimeMillis() + ".zip";
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
    //文件拷贝
    public static void copyFile(String sourceFile,String destDir, String destFile) {

//        System.out.println(sourceFile);
        try{
            File sfile = new File(sourceFile);
            if(!sfile.exists()){
                //源文件不存在，直接忽略
                return;
            }
//            System.out.println(destDir);
            //首先判断目录是否存在，不存在则新建
            File dir = new File(destDir);
            if(!dir.exists()){
                dir.mkdirs();
            }
            //目标文件已经存在，不用copy
            File dfile = new File(destDir+destFile);
            if(dfile.exists()){
                return ;
            }
//            System.out.println(destDir+destFile);

            FileInputStream input  = new FileInputStream(sourceFile);
            FileOutputStream output = new FileOutputStream(destDir+destFile);

            int in  = input.read();

            while(in!=-1){
                output.write(in);
                in = input.read();
            }
            input.close();
            output.close();

        }catch(IOException e){
//            e.printStackTrace();
            LOG.log(Level.WARNING, "File copy exception.");
        }
    }

    // 递归方法
    public static void copyDirs(File file, File file2) {
        // 当找到目录时，创建目录
        if (file.isDirectory()) {
            file2.mkdir();
            File[] files = file.listFiles();
            for (File file3 : files) {
                // 递归
                copyDirs(file3, new File(file2, file3.getName()));
            }
            //当找到文件时
        } else if (file.isFile()) {
            File file3 = new File(file2.getAbsolutePath());
            try {
                file3.createNewFile();
                copyDatas(file.getAbsolutePath(), file3.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 复制文件数据的方法
    public static void copyDatas(String filePath, String filePath1) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 字节流
            fis = new FileInputStream(filePath);
            fos = new FileOutputStream(filePath1);
            byte[] buffer = new byte[1024];
            while (true) {
                int temp = fis.read(buffer, 0, buffer.length);
                if (temp == -1) {
                    break;
                } else {
                    fos.write(buffer, 0, temp);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }


}
