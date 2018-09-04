package com.ecity.cswatersupply.model.metaconfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * 设备信息配置
 * 
 * @author ZiZhengzhuan
 * 
 */
public class DbMetaSetConfig {
    private static DbMetaSetConfig instance = null;
    // 图层及管网设备信息
    private DbMetaSet dbMetaSet;

    public static DbMetaSetConfig getInstance() {
        if (instance == null) {
            return new DbMetaSetConfig();
        }
        return instance;
    }

    /**
     * 存取管网设备信息
     * 
     * @param
     * @return
     */
    public DbMetaSet getDbMetaSet() {
        return dbMetaSet;
    }

    public void setDbMetaSet(DbMetaSet dbMetaSet) {
        this.dbMetaSet = dbMetaSet;
    }

    public static DbMetaSetConfig fromJsonInputStream(InputStream stream) {
        DbMetaSetConfig dbMetaSetConfig = null;
        JSONObject rootObj;
        try {
            String json = inputStream2String(stream);
            rootObj = new JSONObject(json);
            DbMetaSet dbMetaSet = new DbMetaSet();
            JSONArray gwflArr = null;
            if (!rootObj.isNull("gwfl"))
                gwflArr = rootObj.getJSONArray("gwfl");
            if (gwflArr != null) {//获得 gwfl
                List<DbMetaGWFL> listDbMetaGWFL = new ArrayList<DbMetaGWFL>();
                for (int i = 0; i < gwflArr.length(); i++) {
                    DbMetaGWFL dbMetaGWFL = new DbMetaGWFL();
                    JSONObject obj = gwflArr.getJSONObject(i);
                    if (!obj.isNull("type"))
                        dbMetaGWFL.setType(obj.getString("type"));
                    if (!obj.isNull("code"))
                        dbMetaGWFL.setCode(obj.getString("code"));
                    if (!obj.isNull("remark"))
                        dbMetaGWFL.setRemark(obj.getString("remark"));
                    listDbMetaGWFL.add(dbMetaGWFL);
                }
                dbMetaSet.setListDbMetaGWFL(listDbMetaGWFL);
            }
            JSONArray metainfoArr = null;
            if (!rootObj.isNull("metainfo"))
                metainfoArr = rootObj.getJSONArray("metainfo");
            if (metainfoArr != null) {//获得 metainfo
                List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
                for (int i = 0; i < metainfoArr.length(); i++) {
                    DbMetaInfo dbMetaInfo = new DbMetaInfo();
                    JSONObject obj = metainfoArr.getJSONObject(i);
                    if (!obj.isNull("code"))
                        dbMetaInfo.setCode(obj.getString("code"));
                    if (!obj.isNull("type"))
                        dbMetaInfo.setType(obj.getInt("type"));
                    if (!obj.isNull("remark"))
                        dbMetaInfo.setRemark(obj.getString("remark"));
                    if (!obj.isNull("layername"))
                        dbMetaInfo.setLayername(obj.getString("layername"));
                    if (!obj.isNull("descripe"))
                        dbMetaInfo.setDescripe(obj.getString("descripe"));
                    if (!obj.isNull("layerid"))
                        dbMetaInfo.setLayerid(obj.getInt("layerid"));

                    List<DbMetaNet> listNet = new ArrayList<DbMetaNet>();

                    JSONArray netArr = null;
                    if (!obj.isNull("net"))
                        netArr = obj.getJSONArray("net");
                    for (int j = 0; j < netArr.length(); j++) {
                        DbMetaNet net = new DbMetaNet();
                        JSONObject netObj = netArr.getJSONObject(j);
                        net.setDname(netObj.optString("dname"));

                        net.setClsid(netObj.optInt("clsid"));

                        net.setClsname(netObj.optString("clsname"));

                        net.setGeo_type(netObj.optInt("geotype"));

                        net.setLayerid(netObj.optInt("layerid"));

                        net.setBs_prop(netObj.optString("bsprop"));

                        if (!netObj.isNull("fields")) {
                            ArrayList<FieldModel> fieldModles = new ArrayList<FieldModel>();
                            try {
                                JSONArray fields = netObj.getJSONArray("fields");
                                if (null != fields && fields.length() > 0) {
                                    FieldModel fieldModel = null;
                                    for (int k = 0; k < fields.length(); k++) {
                                        fieldModel = new FieldModel();
                                        JSONObject fieldObj = fields.getJSONObject(k);
                                        fieldModel.setAlias(fieldObj.optString("alias", ""));
                                        fieldModel.setName(fieldObj.optString("name", ""));
                                        fieldModel.setType(fieldObj.optString("type", ""));
                                        fieldModles.add(fieldModel);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            net.setFields(fieldModles);
                        }

                        listNet.add(net);
                    }
                    dbMetaInfo.setNet(listNet);
                    listDbMetaInfo.add(dbMetaInfo);
                }
                dbMetaSet.setListDbMetaInfo(listDbMetaInfo);
            }
            dbMetaSetConfig = new DbMetaSetConfig();
            dbMetaSetConfig.setDbMetaSet(dbMetaSet);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("异常：", "获取元数据失败！");
            dbMetaSetConfig = null;
        }
        return dbMetaSetConfig;
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}