package com.hanlyjiang.learnandroid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * author:wangenzhao
 * date:2017/4/27 17:07
 */

public class FileUtil {

    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    public static String createFolder(String folder) {
        String str[] = folder.split("/");
        String path = "";
        for (int i = 0; i < str.length; i++) {
            path += "/" + str[i];
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
        }
        return folder;
    }

    /**
     * 创建文件
     * @param filePath
     * @return
     */
    public static String createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获得指定文件夹下所有的文件列表，递归获取子目录中的文件
     * @param allFiles 用于存储结果列表的List
     * @param root 需要查询的目录
     * @param filter 文件筛选
     */
    public static void listFiles(List<File> allFiles, File root, java.io.FileFilter filter){
        if(allFiles == null){
            //
        }
        File[] files = root.listFiles(filter);
        if( files != null ){
            for(File f : files){
                if(f.isFile()){
                    allFiles.add(f);
                }else if(f.isDirectory()){
                    listFiles(allFiles, f , filter );
                }
            }
        }else{//
        }
    }

    /* 拷贝Asset文件夹到sd卡 */
    public static void copyAssetsDir(Context context, String fromDir, String destDir) throws IOException{
        AssetManager assetManager = context.getAssets();
        String[] files = assetManager.list(fromDir);
        for(String f : files){
            String[] innder = assetManager.list(fromDir + File.separator + f);
            if(innder.length > 0){
                copyAssetsDir(context, fromDir + File.separator + f, destDir+ File.separator + f);
            }else {
                copyFile(assetManager.open(fromDir + File.separator + f), destDir + File.separator + f);
            }
        }
    }


    public static void copyFile(InputStream in, String newPath){
        try {
            int bytesum = 0;
            int byteread = 0;
            File newfile = new File(newPath);
            newfile.getParentFile().mkdirs();

            InputStream inStream = in; // 读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                fs.write(buffer, 0, byteread);
                fs.flush();
            }
            fs.close();
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 复制单个文件 ，若远文件存在则会覆盖原来的文件
     *
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
//			File newfile = new File(newPath);
            Log.e("sssss", newPath);
            // if (!newfile.exists()) { //文件不存在时
            InputStream inStream = new FileInputStream(oldPath); // 读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024 * 8];
//			int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                fs.write(buffer, 0, byteread);
                fs.flush();
            }
            inStream.close();
            fs.close();
            // }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制文件夹
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                copyFile( file[i].getPath(), targetDir+File.separator+file[i].getName());
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + File.separator+ file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir +File.separator + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }


    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath
     *            String
     * @throws FileNotFoundException
     * @throws IOException
     * @return boolean
     */
    public static boolean deletefile(String delpath) throws Exception {
        try {

            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + File.separator + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + File.separator + filelist[i]);
                    }
                }
                System.out.println(file.getAbsolutePath() + "删除成功");
                file.delete();
            }

        } catch (FileNotFoundException e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return true;
    }



    /**
     * 判断两个文件的内容是否相同，文件名要用绝对路径
     * @param fileName1 ：文件1的绝对路径
     * @param fileName2 ：文件2的绝对路径
     * @return 相同返回true，不相同返回false
     */
    public static boolean isSameFile(String fileName1,String fileName2){
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        try {
            fis1 = new FileInputStream(fileName1);
            fis2 = new FileInputStream(fileName2);
            int len1 = fis1.available();//返回总的字节数
            int len2 = fis2.available();
            if (len1 == len2) {//长度相同，则比较具体内容
                int a,b;
                while ((a = fis1.read()) != -1 && (b = fis2.read()) != -1) {
                    if (a != b) {
                        System.out.println("文件内容不一样");
                        return false;
                    }
                }
                System.out.println("两个文件完全相同");
                return true;
            } else {
                //长度不一样，文件肯定不同
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//关闭文件流，防止内存泄漏
            if (fis1 != null) {
                try {
                    fis1.close();
                } catch (IOException e) {
                    //忽略
                    e.printStackTrace();
                }
            }
            if (fis2 != null) {
                try {
                    fis2.close();
                } catch (IOException e) {
                    //忽略
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /**
     * 获取一个目录下所有文件目录列表
     * @param fileDirectory 文件目录
     * @param fileFilter 指定过滤器
     * @return File[] 文件列表数组
     */
    public static File[] filePath(String fileDirectory,String fileFilter){
        FileFilter filter = new FileFilter(fileFilter);
        File file=new File(fileDirectory);
        File[] filelist = file.listFiles(filter);
        return filelist;
    }

    static class FileFilter implements java.io.FileFilter {

        private String strEnds;

        public FileFilter(String strEnds) {
            this.strEnds = strEnds.toUpperCase();
            // this.strEnds = strEnds.split(",");
        }

        public boolean accept(File pathname) {
            if (strEnds.equals("*.*"))
                return true;
            if (pathname.isDirectory())
                return true;
            String filename = pathname.getAbsolutePath();
            if (filename.lastIndexOf(".") == -1)
                return false;
            String lastName = filename.substring(filename.lastIndexOf(".")).toUpperCase();
            return strEnds.indexOf(lastName) > -1;
        }

    }

}
