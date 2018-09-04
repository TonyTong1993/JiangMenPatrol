package com.ecity.mobile.android.crossanalysis.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.ecity.mobile.android.crossanalysis.PipeColorBean;

public class DataUtil {
    private static final String Default_Config_Path = "/Ecity/DataJson/";
    private static final String Default_pipeColor_config = "pipeColorCfg.xml";

    /**
     * 得到所有的管线颜色集合
     * 
     * @author WangFeng<br/>
     *         Create at 20172017-1-18下午1:48:44
     */
    public static List<PipeColorBean> getColorBeans() {
        String filePath = getPipeColorCfgPath();
        File file = new File(filePath);
        List<PipeColorBean> pipeColorBeans = new ArrayList<PipeColorBean>();
        try {
            // 创建一个文档构建工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            // 通过工厂生产DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 将指定file的内容解析 返回一个Document的对象
            Document doc = builder.parse(file);
            NodeList nodeList = doc.getElementsByTagName("pipe");
            int len = nodeList.getLength();
            for (int i = 0; i < len; i++) {
                PipeColorBean bean = new PipeColorBean();
                int len2 = nodeList.item(i).getChildNodes().getLength();
                for (int j = 0; j < len2; j++) {
                    Node node1 = nodeList.item(i).getChildNodes().item(j);
                    if (node1.getNodeType() == 1) {
                        String content = node1.getFirstChild().getNodeValue();
                        String nodeName = node1.getNodeName();
                        if (nodeName.equalsIgnoreCase("code")) {
                            bean.setCode(content);
                        } else if (nodeName.equalsIgnoreCase("name")) {
                            bean.setName(content);
                        } else if (nodeName.equalsIgnoreCase("color")) {
                            bean.setColor(Color.parseColor(content));
                        } else if (nodeName
                                .equalsIgnoreCase("pipeAltitudeType")) {
                            bean.setPipeAltitudeType(content);
                        }
                    }
                }
                pipeColorBeans.add(bean);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pipeColorBeans;
    }

    /**
     * 得到管线颜色配置文件路径
     * 
     * @author WangFeng<br/>
     *         Create at 20172017-1-17下午6:01:04
     */
    public static String getPipeColorCfgPath() {
        String fieldAlais = getDataSavePath();
        if (!TextUtils.isEmpty(fieldAlais)) {
            return fieldAlais + Default_pipeColor_config;
        }
        return null;
    }

    /**
     * @return 获取数据文件的存放路径，带“/”结尾
     */
    public static String getDataSavePath() {
        String sdDir = getSdCardDir();
        if (sdDir != null) {
            return sdDir + Default_Config_Path;
        }
        return null;

    }

    private static final String getSdCardDir() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            String filePath = Environment.getExternalStorageDirectory()
                    .getPath();// sd卡路径
            return filePath;
        } else {
            return null;
        }
    }
}
