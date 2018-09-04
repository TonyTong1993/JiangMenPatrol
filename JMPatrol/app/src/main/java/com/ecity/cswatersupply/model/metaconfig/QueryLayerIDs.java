package com.ecity.cswatersupply.model.metaconfig;

import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.MetaDownloadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class QueryLayerIDs {
	private static DbMetaSet metaSet = new DbMetaSet();
	private static QueryLayerIDs instance;
	// 管网图层
	private static int[] netlayerIDs = null;

	private static Map<String, Object> layerDescripe = new Hashtable<String, Object>();
	private static Map<String, Object> layerName = new Hashtable<String, Object>();
	private static Map<String, Object> netName = new Hashtable<String, Object>();
	private static Map<String, Object> netDispName = new Hashtable<String, Object>();
	private static Map<String, Object> dnoLayernName = new Hashtable<String, Object>();

	// 保存所有包含net的DbMeataInfo，用于填充空间查询图层选择
	private static ArrayList<DbMetaInfo> mylistDbMetaInfo = new ArrayList<DbMetaInfo>();
	// 管段
	private static int pipeLayerID = -1;

	public static QueryLayerIDs getInstance() {
		if (instance == null)
			instance = new QueryLayerIDs();
		return instance;
	}

	public static int[] getQueryNetlayerIDs() {
		return netlayerIDs;
	}

	public String getLayerDescripeByID(int layerid) {
		try {
			String key = String.valueOf(layerid);
			Object obj = layerDescripe.get(key);
			return String.valueOf(obj);
		} catch (Exception e) {
			return null;
		}
	}

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
	 * 此方法描述的是： 根据dno取Layername
	 * 
	 * @author: wangliu94@163.com
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

	//
	public String getNetDispNameByID(int layerid) {
		try {
			String key = String.valueOf(layerid);
			Object obj = netDispName.get(key);
			return String.valueOf(obj);
		} catch (Exception e) {
			return null;
		}
	}

	public String getNetNameByID(int layerid) {
		try {
			String key = String.valueOf(layerid);
			Object obj = netName.get(key);
			return String.valueOf(obj);
		} catch (Exception e) {
			return null;
		}
	}

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

	public Map<String, List<DbMetaNet>> getDbMetaNets() {
		Map<String, List<DbMetaNet>> dbMetaNets = new HashMap<String, List<DbMetaNet>>();
		if (metaSet != null) {
			List<DbMetaInfo> listDbMetaInfos = new ArrayList<DbMetaInfo>();
			listDbMetaInfos = metaSet.getListDbMetaInfo();
			for(DbMetaInfo dbMetaInfo : listDbMetaInfos) {
				List<DbMetaNet> net = dbMetaInfo.getNet();
				if (net.size() > 0) {
					dbMetaNets.put(dbMetaInfo.getDescripe(), net);
				}
			}
		}
		return dbMetaNets;
	}

	/***
	 * 
	 * 此方法描述的是： 根据元数据要说编码获得图层ID
	 * 
	 * @author: wangliu94@163.com
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
		metaSet = dbMetaSet;
		if (mylistDbMetaInfo.size() != 0) {
			mylistDbMetaInfo.clear();
		}
		if (metaSet != null) {
			List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
			listDbMetaInfo = metaSet.getListDbMetaInfo();
			List<Integer> tids = new ArrayList<Integer>();
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
			netlayerIDs = new int[tids.size()];
			for (int k = 0; k < tids.size(); k++) {
				netlayerIDs[k] = tids.get(k);
			}

		}
	}

	/**
	 * 此方法描述的是： 初始化元数据
	 * 
	 * @author: wangliu94@163.com
	 * @version: 2015年4月10日 下午6:21:59
	 */

	public void init(DbMetaSet dbMetaSet) {
		metaSet = dbMetaSet;
		if (mylistDbMetaInfo.size() != 0) {
			mylistDbMetaInfo.clear();
		}
		if (metaSet != null) {
			List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
			listDbMetaInfo = metaSet.getListDbMetaInfo();
			List<Integer> tids = new ArrayList<Integer>();
			String tlayerDescripe = "";
			String tlayerName = "";
			String tnetDispName = "";
			String tnetClassName = "";
			String tkey = "";
			for (int i = 0; i < listDbMetaInfo.size(); i++) {
				DbMetaInfo tmtinfo = listDbMetaInfo.get(i);
				tlayerDescripe = tmtinfo.getDescripe();
				tlayerName = tmtinfo.getLayername();
				List<DbMetaNet> net = tmtinfo.getNet();

				if (net.size() > 0) {
					mylistDbMetaInfo.add(tmtinfo);
					for (DbMetaNet tn : net) {

						tids.add(tn.getLayerid());
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
						netDispName.put(tkey, tnetDispName);
						if (tn.getDname().equals("管段")) {
							setPipeLayerID(tn.getLayerid());
						}
					}
				}
			}
			netlayerIDs = new int[tids.size()];
			for (int k = 0; k < tids.size(); k++) {
				netlayerIDs[k] = tids.get(k);
			}

		}
	}

	public ArrayList<DbMetaInfo> getDbMetaInfoswithNet() {
		return mylistDbMetaInfo;
	}

	public static int getPipeLayerID() {
		return pipeLayerID;
	}

	public static void setPipeLayerID(int pipeLayerID) {
		QueryLayerIDs.pipeLayerID = pipeLayerID;
	}

	/**
	 * 此方法描述的是： 根据图层ID查询官网Dname
	 * 
	 * @author: wangliu94@163.com
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
	

	public static int getLayerIdByTableName(String tableName) {
		try {
			if (null != metaSet && null != metaSet.getListDbMetaInfo()
					&& !metaSet.getListDbMetaInfo().isEmpty()) {
				int size = metaSet.getListDbMetaInfo().size();
				for (int i = 0; i < size; i++) {
					DbMetaInfo dbMetaInfo = metaSet.getListDbMetaInfo().get(i);
					if (dbMetaInfo.getLayername().equalsIgnoreCase(tableName)) {
						return dbMetaInfo.getLayerid();
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
	 * @author: wangliu94@163.com
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
	
	public static int getLayerIdbyLayerName(String layerName) {
	        int layerId = -1;
	        try {
	            if (metaSet != null) {
	                List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
	                listDbMetaInfo = metaSet.getListDbMetaInfo();
	                for (int i = 0; i < listDbMetaInfo.size(); i++) {
	                    DbMetaInfo tmtinfo = listDbMetaInfo.get(i);
	                    if (tmtinfo.getLayername().equals(layerName)) {
                            layerId = tmtinfo.getLayerid();
                            break;
                        }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return layerId;
	    }

    /***
     * 获得某个设备的所有字段
     * @param dName
     * @return
     */
    public static List<FieldModel> getFieldByDname(String dName) {
        try {

            if (mylistDbMetaInfo.size() > 0) {
                for (int i = 0; i < mylistDbMetaInfo.size(); i++) {
                    DbMetaInfo dbMetaInfo = mylistDbMetaInfo.get(i);
                    if (dbMetaInfo.getNet().size() > 0) {
                        for (int j = 0; j < dbMetaInfo.getNet().size(); j++) {
                            DbMetaNet dbmetaNet = dbMetaInfo.getNet().get(j);
                            if (dbmetaNet.getDname().equalsIgnoreCase(dName)) {
                               return dbmetaNet.getFields();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 判断地图配置的元数据是否加载
	 * @return
	 */
	public boolean isMetaSetNull() {
		if(null == metaSet || ListUtil.isEmpty(mylistDbMetaInfo) || !MetaDownloadUtil.isMetasLoaded) {
			return true;
		} else {
			return false;
		}
	}
}
