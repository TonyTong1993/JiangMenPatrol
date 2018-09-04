package com.ecity.cswatersupply.emergency.test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.ecity.cswatersupply.emergency.utils.FromPackageUtil;
import com.ecity.cswatersupply.emergency.utils.ZipUtil;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.z3app.android.util.FileUtil;

public class FromPackageTest {
    public static void test() {
        RandomAccessFile raf = null;
        File file = null;
        String mapServerJson = "";
        String path = FileUtil.getInstance(null).getRootPath() + "//inspect//form.json";
        file = new File(path);

        if (null == file || !file.exists()) {
            return;
        }
        try {
            //从文件读取字符串
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(0);
            byte[] buffer = new byte[(int) raf.length()];
            raf.read(buffer);
            String contentStr = new String(buffer);
            mapServerJson = contentStr;
            
            //字符串转JSON
            JSONObject objJson = new JSONObject(mapServerJson);
            List<InspectItem> items = new ArrayList<InspectItem>();
            InspectItem item = new InspectItem();
            //JSON转InspectItem 
            items = FromPackageUtil.analysisJson(objJson, item, items);
            items.size();
            //InspectItem 转JSON
            JSONObject jsonObject = FromPackageUtil.buildJson(items.get(0));
            //JSON转到文件
            String path2 = FileUtil.getInstance(null).getRootPath() + "//event//001//form1.json";
            Textwriter.write(path2, jsonObject.toString());
            //压缩文件路径
            String zipPath = FileUtil.getInstance(null).getRootPath() + "//event//001.zip";
            //表单数据文件夹路径
            String srcPath = FileUtil.getInstance(null).getRootPath() + "//event//001";
            //压缩
            ZipUtil.zip(srcPath,zipPath);
            //解压
            ZipUtil.unzip(zipPath, FileUtil.getInstance(null).getRootPath() + "//event//002");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
