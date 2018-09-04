package com.ecity.android.map.core.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class QueryLayerIDs implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6357302517128516186L;

    /**
     * 元数据列表
     */
    private static DbMetaSet metaSet = new DbMetaSet();

    private static QueryLayerIDs instance;

    /**
     * net列表中所有的layerID
     */
    private static int[] netlayerIDs = null;
    /**
     * layer描述信息，key为layer，value为metainfo的descript
     */
    private static Map<String, Object> layerDescripe = new Hashtable<String, Object>();
    /**
     * layer名字信息，key为layer，value为metainfo的layername
     */
    private static Map<String, Object> layerName = new Hashtable<String, Object>();
    /**
     * layer对应的class信息，key为layer，value为net中对应的的clsssname
     */
    private static Map<String, Object> netName = new Hashtable<String, Object>();
    /**
     * layer对应的显示名称信息，key为layer，value为net中对应的的dname
     */
    private static Map<String, Object> netDispName = new Hashtable<String, Object>();
    /**
     * dno对应的class信息,key为net字段中的dno，value为net中对应的的clsssname
     */
    private static Map<String, Object> dnoLayernName = new Hashtable<String, Object>();

    /**
     * 保存所有包含net的DbMeataInfo，用于填充空间查询图层选择
     */
    private static ArrayList<DbMetaInfo> mylistDbMetaInfo = new ArrayList<DbMetaInfo>();

    /**
     * 包含所有管网的DbMetaInfo例如常熟外网服务的DMA
     */
    private static List<DbMetaInfo> allListDbMetaInfo = new ArrayList<DbMetaInfo>();

    /**
     * 管段layer id
     */
    private static int pipeLayerID = -1;

    public static QueryLayerIDs getInstance() {
        if (instance == null) {
            instance = new QueryLayerIDs();
        }
        return instance;
    }

    /**
     * @return 获取net列表中所有的layer
     */
    public static int[] getQueryNetlayerIDs() {
        return netlayerIDs;
    }

    /**
     * 获取该layer在metainfo中的descripte字段信息
     * 
     * @param layerid
     * @return
     */
    public String getLayerDescripeByID(int layerid) {
        try {
            String key = String.valueOf(layerid);
            Object obj = layerDescripe.get(key);
            return String.valueOf(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取该layer在metainfo中的layername字段信息
     * 
     * @param layerid
     * @return
     */
    public String getLayerNameByID(int layerid) {
        try {
            String key = String.valueOf(layerid);
            Object obj = layerName.get(key);
            return String.valueOf(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 此方法描述的是： 根据dno取Layer对应的classname
     * 
     * @author:
     * @version: 2015年6月13日 下午4:32:50
     */

    public String getLayerNameByDno(String dno) {
        try {
            String layerName = (String) dnoLayernName.get(dno);
            return layerName;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * layer对应的显示名称信息，net中对应的的dname字段
     * 
     * @param layerid
     * @return
     */
    public String getNetDispNameByID(int layerid) {
        try {
            String key = String.valueOf(layerid);
            Object obj = netDispName.get(key);
            return String.valueOf(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取layer在net中对应的的clsssname字段
     * 
     * @param layerid
     * @return
     */
    public String getNetNameByID(int layerid) {
        try {
            String key = String.valueOf(layerid);
            Object obj = netName.get(key);
            return String.valueOf(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据metainfo中的layername字段:net中定义的dname字段组合取出对应的layerID
     * 
     * @param groupAndname
     * @return
     */
    public int getLayerIDbyLayerNameAndNetname(String groupAndname) {
        int id = -1;
        String[] layerNameAndNet = groupAndname.split(":");
        if (layerNameAndNet.length == 2) {
            String tlayerName = "";
            String tnetName = "";
            tlayerName = layerNameAndNet[0];
            tnetName = layerNameAndNet[1];
            if (metaSet != null) {
                List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
                listDbMetaInfo = metaSet.getListDbMetaInfo();
                for (int i = 0; i < listDbMetaInfo.size(); i++) {
                    DbMetaInfo tmtinfo = listDbMetaInfo.get(i);
                    if ((tmtinfo.getLayername()).equalsIgnoreCase(tlayerName)) {
                        List<DbMetaNet> net = tmtinfo.getNet();
                        if (net.size() > 0) {
                            for (DbMetaNet tn : net) {
                                if ((tn.getDname()).equalsIgnoreCase(tnetName)) {
                                    id = tn.getLayerid();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return id;
    }

    /***
     * 
     * 根据metainfo中的code字段获得图层ID
     * 
     * @author:
     * @version: 2015年8月17日 下午5:37:46
     */
    public int[] getLayerIDbyCode(String code) {
        if (null == code || code.equalsIgnoreCase("")) {
            return null;
        }

        ArrayList<Integer> tids = new ArrayList<Integer>();
        if (metaSet != null) {
            List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
            listDbMetaInfo = metaSet.getListDbMetaInfo();
            for (int i = 0; i < listDbMetaInfo.size(); i++) {
                DbMetaInfo tmtinfo = listDbMetaInfo.get(i);
                if ((tmtinfo.getCode()).equalsIgnoreCase(code)) {
                    List<DbMetaNet> net = tmtinfo.getNet();
                    if (net.size() > 0) {
                        for (DbMetaNet tn : net) {
                            tids.add(tn.getLayerid());
                        }
                    }
                }
            }
        }

        int[] layerIDs = new int[tids.size()];
        for (int k = 0; k < tids.size(); k++) {
            layerIDs[k] = tids.get(k);
        }
        return layerIDs;
    }

    public String getPortralLayerNameByIDObject(Object layerid) {
        try {
            int layerId = Integer.parseInt(String.valueOf(layerid));
            String portralLayerName = getLayerNameByID(layerId) + ":"
                    + getNetNameByID(layerId);
            return portralLayerName;
        } catch (Exception e) {
            return null;
        }
    }

    public String getLayerNameByIDObject(Object layerid) {
        try {
            int layerId = Integer.parseInt(String.valueOf(layerid));
            String portralLayerName = getLayerNameByID(layerId);
            return portralLayerName;
        } catch (Exception e) {
            return "";
        }
    }

    public QueryLayerIDs() {

    }

    public QueryLayerIDs(DbMetaSet dbMetaSet) {
        init(dbMetaSet);
    }

    /**
     * 此方法描述的是： 初始化元数据
     * 
     * @author:
     * @version: 2015年4月10日 下午6:21:59
     */

    public void init(DbMetaSet dbMetaSet) {
        metaSet = dbMetaSet;
        if (mylistDbMetaInfo.size() != 0) {
            mylistDbMetaInfo.clear();
        }
        if (allListDbMetaInfo.size() != 0) {
            allListDbMetaInfo.clear();
        }
        if (metaSet != null) {
            List<DbMetaInfo> listDbMetaInfo = metaSet.getListDbMetaInfo();
            allListDbMetaInfo = metaSet.getListDbMetaInfo();
            List<Integer> tids = new ArrayList<Integer>();
            List<Integer> devicePointTids = new ArrayList<Integer>();
            String tlayerDescripe = "";
            String tlayerName = "";
            String tnetDispName = "";
            String tnetClassName = "";
            String tkey = "";
            int dno = 0;
            for (int i = 0; i < listDbMetaInfo.size(); i++) {
                DbMetaInfo tmtinfo = listDbMetaInfo.get(i);
                tlayerDescripe = tmtinfo.getDescripe();
                tlayerName = tmtinfo.getLayername();
                List<DbMetaNet> net = tmtinfo.getNet();
                if (net.size() > 0) {
                    mylistDbMetaInfo.add(tmtinfo);
                    for (DbMetaNet tn : net) {
                        dno = tn.getDid();
                        tids.add(tn.getLayerid());
                        if (tn.getGeo_type() != 0) {
                            devicePointTids.add(tn.getLayerid());
                        }
                        tkey = String.valueOf(tn.getLayerid());
                        if (tn.getDname() == null)
                            tnetDispName = "";
                        else
                            tnetDispName = tn.getDname();

                        if (tn.getClsname() == null)
                            tnetClassName = "";
                        else
                            tnetClassName = tn.getClsname();

                        layerDescripe.put(tkey, tlayerDescripe);
                        layerName.put(tkey, tlayerName);
                        netName.put(tkey, tnetClassName);
                        dnoLayernName.put(dno + "", tnetClassName);
                        netDispName.put(tkey, tnetDispName);
                        if (tn.getDname().equals("管段")) {
                            setPipeLayerID(tn.getLayerid());
                        }
                    }
                }
            }
            // 为啥是这么用的，而不是直接用toArray呢
            netlayerIDs = new int[tids.size()];
            for (int k = 0; k < tids.size(); k++) {
                netlayerIDs[k] = tids.get(k);
            }
        }
    }

    /**
     * @return 获取所有包含net属性的metainfo
     */
    public ArrayList<DbMetaInfo> getDbMetaInfoswithNet() {
        return mylistDbMetaInfo;
    }

    /**
     * 得到所有的DbMetaInfo
     * 
     * @author WangFeng<br/>
     *         Create at 20162016-12-9上午9:39:05
     */
    public List<DbMetaInfo> getAllDbMetaInfos() {
        return allListDbMetaInfo;
    }

    /**
     * @return 获取管段layer的ID
     */
    public static int getPipeLayerID() {
        return pipeLayerID;
    }

    public static void setPipeLayerID(int pipeLayerID) {
        QueryLayerIDs.pipeLayerID = pipeLayerID;
    }

    /**
     * 此方法描述的是： 根据图层ID查询官网Dname
     * 
     * @author:
     * @version: 2015年1月18日 下午4:20:49
     */

    public static String getDnamebyLayerId(int layerId) {
        try {
            if (mylistDbMetaInfo.size() > 0) {
                for (int i = 0; i < mylistDbMetaInfo.size(); i++) {
                    DbMetaInfo dbMetaInfo = mylistDbMetaInfo.get(i);
                    if (dbMetaInfo.getNet().size() > 0) {
                        for (int j = 0; j < dbMetaInfo.getNet().size(); j++) {
                            DbMetaNet dbmetaNet = dbMetaInfo.getNet().get(j);
                            if (layerId == dbmetaNet.getLayerid()) {
                                return dbmetaNet.getDname();
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据layerId得到geotype,geotype为0为管线否则为节点
     * 
     * @author WangFeng<br/>
     *         Create at 20162016-12-14下午2:45:30
     */
    public static int getGeoTypebyLayerId(int layerId) {
        try {
            if (mylistDbMetaInfo.size() > 0) {
                for (int i = 0; i < mylistDbMetaInfo.size(); i++) {
                    DbMetaInfo dbMetaInfo = mylistDbMetaInfo.get(i);
                    if (dbMetaInfo.getNet().size() > 0) {
                        for (int j = 0; j < dbMetaInfo.getNet().size(); j++) {
                            DbMetaNet dbmetaNet = dbMetaInfo.getNet().get(j);
                            if (layerId == dbmetaNet.getLayerid()) {
                                return dbmetaNet.getGeo_type();
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 此方法描述的是： 根据官网Dname查询图层ID
     * 
     * @author:
     * @version: 2015年5月7日 下午5:24:42
     */
    public static int getLayerIdbyDname(String dName) {
        int layerId = -1;
        try {

            if (mylistDbMetaInfo.size() > 0) {
                for (int i = 0; i < mylistDbMetaInfo.size(); i++) {
                    DbMetaInfo dbMetaInfo = mylistDbMetaInfo.get(i);
                    if (dbMetaInfo.getNet().size() > 0) {
                        for (int j = 0; j < dbMetaInfo.getNet().size(); j++) {
                            DbMetaNet dbmetaNet = dbMetaInfo.getNet().get(j);
                            if (dbmetaNet.getDname().equals(dName)) {
                                layerId = dbmetaNet.getLayerid();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layerId;
    }
}
