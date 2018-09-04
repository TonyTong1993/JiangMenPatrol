package com.ecity.cswatersupply.emergency.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.emergency.test.Textwriter;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.z3app.android.util.FileUtil;

public class InspectFileManager {
    public static InspectFileManager instance;
    //上报的压缩文件路径
    private static String zipPath;
    //表单数据文件夹路径
    private static String srcPath;
    //表单文件路径
    private static String inspectPath;
    //下载的所有历史文件储存文件夹路径
    private static String srcPath4Update;
    //历史zip包文件路径
    private static String zipPath4Update;
    //历史表单文件路径
    private static String inspectPath4Update;

    //现场调查历史表单文件路径
    private static String xcdcFilePath;
    private static String xcdcJsonFilePath;
    private static String xcdcZipFilePath;

    public String getInspectPath4Update() {
        return inspectPath4Update;
    }

    public String getZipPath4Update() {
        return zipPath4Update;
    }

    public void setZipPath4Update(String zipName) {
        InspectFileManager.zipPath4Update = srcPath4Update + "/" + zipName;
    }

    public String getZipPath() {
        return zipPath;
    }

    public String getSrcPath4Update() {
        return srcPath4Update;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public String getInspectPath() {
        return inspectPath;
    }

    public static String getXcdcFilePath() {
        return xcdcFilePath;
    }

    public static String getXcdcJsonFilePath() {
        return xcdcJsonFilePath;
    }

    public static String getXcdcZipFilePath() {
        return xcdcZipFilePath;
    }

    private InspectFileManager() {

    }

    public static InspectFileManager getInstance() {
        if (null == instance) {
            instance = new InspectFileManager();
        }
        return instance;
    }

    /**
     * 初始化文件地址信息
     *
     * @param did 每个地震的编号id  默认该id为唯一的
     */
    public static void initInspectFile(int did) {
        inspectPath = FileUtil.getInstance(null).getRootPath() + "//inspect_" + did + "//inspects.json";
        zipPath = FileUtil.getInstance(null).getRootPath() + "//inspect_" + did + "//inspects.zip";
        srcPath = FileUtil.getInstance(null).getRootPath() + "//inspect_" + did;

        File dir = new File(srcPath);
        if (dir.exists()) {
            dir.delete();
        }
        dir.mkdirs();
    }

    /**
     * 初始化文件地址信息(现场调查)
     *
     * @param model 每个地震的编号id  默认该id为唯一的
     */
    public static void initInspectFile(EarthQuakeQuickReportModel model) {
        xcdcFilePath = FileUtil.getInstance(null).getRootPath() + "//xcdcInspect_" + model.getGid();
        xcdcJsonFilePath = FileUtil.getInstance(null).getRootPath() + "//xcdcInspect_" + model.getGid() + "//inspects.json";
        xcdcZipFilePath = FileUtil.getInstance(null).getRootPath() + "//xcdcInspect_" + model.getGid() + "//inspects.zip";
        File dir = new File(xcdcFilePath);
        if (dir.exists()) {
            dir.delete();
        }
        dir.mkdirs();
    }


    public static void deleteXCDCSrc(){
        File dir = new File(xcdcFilePath);
        if (dir.exists()) {
            deleteDirectory(xcdcFilePath);
        }
        dir.mkdirs();
    }

    /**
     * @param gid 为每个历史速报的gid
     */
    public static void initFilePath4UpdateInspect(int gid) {
        inspectPath4Update = FileUtil.getInstance(null).getRootPath() + "//download//inspect_" + gid + "//inspects.json";
        zipPath = FileUtil.getInstance(null).getRootPath() + "//download//inspect_" + gid + "//inspects.zip";
        srcPath4Update = FileUtil.getInstance(null).getRootPath() + "//download//inspect_" + gid;
        File downloadDir = new File(srcPath4Update);
        if (downloadDir.exists()) {
            downloadDir.delete();
        }
        downloadDir.mkdirs();
    }
    /**
     * @param gid 为每个历史速报的gid(现场调查)
     */
    public static void initFilePath4UpdateInspectXC(int gid) {
        inspectPath4Update = FileUtil.getInstance(null).getRootPath() + "//download//xcdcinspect_" + gid + "//inspects.json";
        zipPath = FileUtil.getInstance(null).getRootPath() + "//download//xcdcinspect_" + gid + "//inspects.zip";
        srcPath4Update = FileUtil.getInstance(null).getRootPath() + "//download//xcdcinspect_" + gid;
        File downloadDir = new File(srcPath4Update);
        if (downloadDir.exists()) {
            downloadDir.delete();
        }
        downloadDir.mkdirs();
    }


    /***
    * 删除文件夹以及目录下的文件
    * @param   filePath 被删除目录的文件路径
    * @return  目录删除成功返回true，否则返回false
    */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     * 写入表单数据到文件
     *
     * @param item
     */
    public static void wirteInspectIntoFile(InspectItem item, String inspectFilePath) {
        JSONObject jsonObject = FromPackageUtil.buildJson(item);
        if (!StringUtil.isBlank(inspectFilePath)) {
            Textwriter.write(inspectFilePath, jsonObject.toString());
        }
    }

    /**
     * 删除路径下的文件夹
     */
    public static void deleteInspectFile() {
        FileUtil.getInstance(null).deleteDir(srcPath);
        zipPath = null;
        srcPath = null;
        inspectPath = null;
    }

    public static void deleteFile4update() {
        FileUtil.getInstance(null).deleteDir(srcPath4Update);
        srcPath4Update = null;
        zipPath4Update = null;
        inspectPath4Update = null;
    }

    /**
     * 删除单个文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (null == file || !file.exists()) {
            return;
        }
        FileUtil.deleteFile(file);
    }

    /**
     * 从文件内读取表单数据
     *
     * @return
     */
    public static InspectItem readInspectItemFromFile(String path) {
        File file = new File(path);

        if (null == file || !file.exists()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            //从文件读取字符串
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(0);
            byte[] buffer = new byte[(int) raf.length()];
            raf.read(buffer);
            String contentStr = new String(buffer, "UTF-8");
            if (StringUtil.isBlank(contentStr)) {
                return null;
            }
            //字符串转JSON
            JSONObject objJson = new JSONObject(contentStr);
            InspectItem item = new InspectItem();
            List<InspectItem> items = new ArrayList<InspectItem>();
            //JSON转InspectItem 
            items = FromPackageUtil.analysisJson(objJson, item, items);
            return items.get(0);
        } catch (Exception e) {
        LogUtil.e(HostApplication.getApplication(), e);
        return null;
    } finally {
        try {
            raf.close();
        } catch (IOException e) {
            LogUtil.e(HostApplication.getApplication(), e);
            return null;
        }
    }
    }

    /**
     * @param srcPath  需要打包的路径
     * @param destPath 打包之后形成的zip文件
     * @return
     */
    public static boolean zipInspectFile(String srcPath, String destPath) {
        try {
            File file = new File(destPath);
            if (file.exists()) {

                file.delete();
            }
            ZipUtil.zip(srcPath, destPath);
            return true;
        } catch (IOException e) {
            LogUtil.e(HostApplication.getApplication(), e);
            return false;
        }
    }

    /**
     * 解压文件
     */
    public static boolean unZipInspectFile(String zipFileName, String outputDirectory) {
        try {
            ZipUtil.unzip(zipFileName, outputDirectory);
            return true;
        } catch (IOException e) {
            LogUtil.e(HostApplication.getApplication(), e);
            return false;
        }
    }
}
