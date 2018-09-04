package com.ecity.cswatersupply.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig.TaskServer;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.z3map.maploader.ModuleSetting;
import com.ecity.z3map.maploader.model.ECityRect;
import com.ecity.z3map.maploader.model.EMapLoadType;
import com.ecity.z3map.maploader.model.ESourceType;
import com.ecity.z3map.maploader.model.SourceConfig;
import com.esri.core.geometry.Envelope;
import com.z3app.android.util.FileUtil;
import com.z3app.android.util.StringUtil;

public class MobileConfigParser {

    public static MobileConfig fromXmlInputStream(Object data) {
        MobileConfig mobileConfig = null;
        InputStream isStream = null;
        SourceConfig sourceconfig = null;
        TaskServer taskServer = null;
        String mapDocPath = "";
        try {
            mapDocPath = FileUtil.getInstance(null).getLocalMapDocPath(); // 内部回初始化本地地图路径
        } catch (Exception e) {
            Log.e("MapLoadTool", e.getMessage(), e);
        }

        ModuleSetting.init(true);
        ModuleSetting mapLoadModuleSetting = ModuleSetting.getModuleSetting();

        try {
            isStream = new ByteArrayInputStream(data.toString().getBytes("UTF-8"));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(isStream, "UTF-8");
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        mobileConfig = new MobileConfig();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("cityName")) {
                            String envStr = parser.nextText();
                            mobileConfig.setCityName(envStr);

                        } else if (name.equalsIgnoreCase("cityId")) {
                            String envStr = parser.nextText();
                            mobileConfig.setCityId(envStr);

                        } else if (name.equalsIgnoreCase("cityRect")) {
                            String envStr = parser.nextText();
                            mobileConfig.setCityRect(envStr);

                        } else if (name.equalsIgnoreCase("initExtent")) {
                            String envStr = parser.nextText();
                            try {
                                String[] envArr = envStr.split(",");
                                if (envArr != null && envArr.length == 4) {
                                    Envelope envelope = new Envelope();
                                    envelope.setXMin(Double.parseDouble(envArr[0]));
                                    envelope.setYMin(Double.parseDouble(envArr[1]));
                                    envelope.setXMax(Double.parseDouble(envArr[2]));
                                    envelope.setYMax(Double.parseDouble(envArr[3]));
                                    mobileConfig.setInitExtent(envelope);
                                }
                            } catch (Exception e) {
                                LogUtil.e(MobileConfigParser.class, e);
                            }
                        } else if (name.equalsIgnoreCase("MaxResolution")) {
                            String envStr = parser.nextText();
                            mobileConfig.setMaxResolution(Double.valueOf(envStr));

                        } else if (name.equalsIgnoreCase("MinResolution")) {
                            String envStr = parser.nextText();
                            mobileConfig.setMinResolution(Double.valueOf(envStr));
                        } else if (name.equalsIgnoreCase("Sources")) {
                            mobileConfig.initSourceConfigArrayList();
                        } else if (name.equalsIgnoreCase("Source")) {
                            sourceconfig = new SourceConfig();
                            sourceconfig.sourceLocalPath = mapDocPath;
                        } else if (name.equalsIgnoreCase("SourceType") && sourceconfig != null) {
                            String v = String.valueOf(parser.nextText());
                            try {
                                sourceconfig.sourceType = ESourceType.valueOf(v.toUpperCase());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (name.equalsIgnoreCase("ID") && sourceconfig != null) {
                            sourceconfig.sourceID = parser.nextText();
                        } else if (name.equalsIgnoreCase("Name") && sourceconfig != null) {
                            sourceconfig.serverName = parser.nextText();
                        } else if (name.equalsIgnoreCase("Description")) {
                            sourceconfig.description = parser.nextText();
                        } else if (name.equalsIgnoreCase("LocalPath") && sourceconfig != null) {
                            String path = parser.nextText();
                            if(StringUtil.isBlank(path)) {
                                sourceconfig.sourceLocalPath = mapDocPath;
                            } else {
                                sourceconfig.sourceLocalPath = path;
                            }
                        } else if (name.equalsIgnoreCase("URL") && sourceconfig != null) {
                            sourceconfig.URL = parser.nextText();
                        } else if (name.equalsIgnoreCase("TpkUrl") && sourceconfig != null) {
                            sourceconfig.tpkUrl = parser.nextText();
                        } else if (name.equalsIgnoreCase("visible") && sourceconfig != null) {
                            sourceconfig.isVisible = parser.nextText();
                        } else if (name.equalsIgnoreCase("dispMaxScale") && sourceconfig != null) {
                            try {
                                sourceconfig.dispMaxScale = (int)Double.parseDouble(parser.nextText());
                            } catch (Exception e) {
                                LogUtil.e(MobileConfigParser.class, e);
                            }
                        } else if (name.equalsIgnoreCase("dispMinScale") && sourceconfig != null) {
                            try {
                                sourceconfig.dispMinScale = (int)Double.parseDouble(parser.nextText());
                            } catch (Exception e) {
                                LogUtil.e(MobileConfigParser.class, e);
                            }
                        } else if (name.equalsIgnoreCase("dispRect") && sourceconfig != null) {
                            String dispRectStr = parser.nextText();
                            try {
                                String[] dispRectStrArr = dispRectStr.split(",");
                                if (dispRectStrArr != null && dispRectStrArr.length == 4) {
                                    Envelope dispRectEnv = new Envelope();
                                    dispRectEnv.setXMin(Double.parseDouble(dispRectStrArr[0]));
                                    dispRectEnv.setYMin(Double.parseDouble(dispRectStrArr[1]));
                                    dispRectEnv.setXMax(Double.parseDouble(dispRectStrArr[2]));
                                    dispRectEnv.setYMax(Double.parseDouble(dispRectStrArr[3]));
                                    sourceconfig.dispRect = new ECityRect(dispRectEnv);
                                }
                            } catch (Exception e) {
                                LogUtil.e(MobileConfigParser.class, e);
                            }
                        } else if (name.equalsIgnoreCase("mapLoadType") && mobileConfig != null) {
                            try{
                                String value =  parser.nextText();
                                mobileConfig.setMapLoadType(EMapLoadType.valueOf(value.toUpperCase()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (name.equalsIgnoreCase("task")) {
                            taskServer = new TaskServer();
                        } else if (name.equalsIgnoreCase("key") && taskServer != null) {
                            taskServer.name = parser.nextText();
                        } else if (name.equalsIgnoreCase("value") && taskServer != null) {
                            taskServer.value = parser.nextText();
                        } else if (name.equalsIgnoreCase("dispMaxResolution") && mapLoadModuleSetting != null) {
                            try {
                                mapLoadModuleSetting.setMaxResolution( Double.parseDouble(parser.nextText()) );
                            } catch (Exception e) {
                                LogUtil.e(MobileConfigParser.class, e);
                            }
                        } else if (name.equalsIgnoreCase("dispMinResolution") && mapLoadModuleSetting != null) {
                            try {
                                mapLoadModuleSetting.setMinResolution( Double.parseDouble(parser.nextText()) );
                            } catch (Exception e) {
                                LogUtil.e(MobileConfigParser.class, e);
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("Source") && sourceconfig != null) {
                            mobileConfig.addSourceConfig(sourceconfig);
                            sourceconfig = null;
                        } else if (parser.getName().equalsIgnoreCase("task") && taskServer != null) {
                            String taskServerName = taskServer.name;
                            String taskServerValue = taskServer.value;
                            if (taskServerName.equalsIgnoreCase("SpacialSearchUrl")) {
                                ServiceUrlManager.getInstance().setSpacialSearchUrl(taskServerValue);
                            } else if (taskServerName.equalsIgnoreCase("PatrolFeatureServer")) {
                                ServiceUrlManager.getInstance().setPatrolFeatureServer(taskServerValue);
                            } else if (taskServerName.equalsIgnoreCase("PatrolServer")) {
                                ServiceUrlManager.getInstance().setPatrolServiceUrl(taskServerValue);
                            } else if (taskServerName.equalsIgnoreCase("WaiQinServer")) {
                                ServiceUrlManager.getInstance().setWaiQinServerUrl(taskServerValue);
                            } else if (taskServerName.equalsIgnoreCase("PatrolDeviceQueryIpPort")) {
                                ServiceUrlManager.getInstance().setPatrolDeviceQueryIpPort(taskServerValue);
                            } else if (taskServerName.equalsIgnoreCase("AddressSearchServerUrl")) {
                                ServiceUrlManager.getInstance().setPlanAndPipeAddress(taskServerValue);
                            }

                            taskServer = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            isStream.close();
        } catch (Exception e) {
            LogUtil.e(MobileConfigParser.class, e);
            mobileConfig = null;
        }
        return mobileConfig;
    }
}
