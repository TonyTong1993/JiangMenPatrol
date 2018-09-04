/**
 * @company 武汉易思迪信息科技有限公司（ECity）
 * @package com.ecity.android.cityrevisionpo.data
 * @file NetMtFld.java
 * @author FangJianming
 * @version 2014-6-24
 */
package com.ecity.android.map.core.local;

import java.util.ArrayList;
import java.util.Map;

/**
 * @version 2014-6-24
 * @author FangJianming
 */
public class NetmtFld {
    private int dno;
    private String dname;
    private String dalias;
    private int geo_type;
    private int bs_prop;
    private int sid;
    private byte[] bArrayPic;

    private ArrayList<MTFldModel> mtFldModelList;
    private ArrayList<MTFldModel> editableMtFldModels;
    private ArrayList<MTFldModel> visiableMtFldModels;
    private ArrayList<String> dispNameList;
    private ArrayList<String> editNameList;

    /**
     * dno
     * 
     * @return the dno
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getDno() {
        return dno;
    }

    /**
     * @param dno
     *            the dno to set
     */

    public void setDno(int dno) {
        this.dno = dno;
    }

    /**
     * dname
     * 
     * @return the dname
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getDname() {
        return dname;
    }

    /**
     * @param dname
     *            the dname to set
     */

    public void setDname(String dname) {
        this.dname = dname;
    }

    /**
     * dalias
     * 
     * @return the dalias
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getDalias() {
        return dalias;
    }

    /**
     * @param dalias
     *            the dalias to set
     */

    public void setDalias(String dalias) {
        this.dalias = dalias;
    }

    /**
     * geo_type
     * 
     * @return the geo_type
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getGeo_type() {
        return geo_type;
    }

    /**
     * @param geo_type
     *            the geo_type to set
     */

    public void setGeo_type(int geo_type) {
        this.geo_type = geo_type;
    }

    /**
     * bs_prop
     * 
     * @return the bs_prop
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getBs_prop() {
        return bs_prop;
    }

    /**
     * @param bs_prop
     *            the bs_prop to set
     */

    public void setBs_prop(int bs_prop) {
        this.bs_prop = bs_prop;
    }

    /**
     * sid
     * 
     * @return the sid
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public int getSid() {
        return sid;
    }

    /**
     * @param sid
     *            the sid to set
     */

    public void setSid(int sid) {
        this.sid = sid;
    }

    /**
     * bArrayPic
     * 
     * @return the bArrayPic
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public byte[] getbArrayPic() {
        return bArrayPic;
    }

    /**
     * @param bArrayPic
     *            the bArrayPic to set
     */

    public void setbArrayPic(byte[] bArrayPic) {
        this.bArrayPic = bArrayPic;
    }

    /**
     * mtFldModelList
     * 
     * @return the mtFldModelList
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public ArrayList<MTFldModel> getMtFldModelList() {
        return mtFldModelList;
    }

    /**
     * @param mtFldModelList
     *            the mtFldModelList to set
     */

    public void setMtFldModelList(ArrayList<MTFldModel> mtFldModelList) {
        this.mtFldModelList = mtFldModelList;
    }

    /**
     * dispNameList
     * 
     * @return the dispNameList
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public ArrayList<String> getDispNameList() {
        return dispNameList;
    }

    /**
     * @param dispNameList
     *            the dispNameList to set
     */

    public void setDispNameList(ArrayList<String> dispNameList) {
        this.dispNameList = dispNameList;
    }

    /**
     * editNameList
     * 
     * @return the editNameList
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public ArrayList<String> getEditNameList() {
        return editNameList;
    }

    /**
     * @param editNameList
     *            the editNameList to set
     */

    public void setEditNameList(ArrayList<String> editNameList) {
        this.editNameList = editNameList;
    }

    /**
     * @comments 构造函数，初始化操作
     */
    public NetmtFld() {
        mtFldModelList = new ArrayList<MTFldModel>();
    }

    /**
     * @comments 构造函数，初始化操作
     * @param bean
     * @throws Exception
     */
    public NetmtFld(SQLiteRecordBean bean) throws Exception {
        mtFldModelList = new ArrayList<MTFldModel>();
        visiableMtFldModels = new ArrayList<MTFldModel>();
        editableMtFldModels = new ArrayList<MTFldModel>();
        dispNameList = new ArrayList<String>();
        editNameList = new ArrayList<String>();
        Map<String, Object> attribute = bean.getAttribute();
        dno = (Integer) attribute.get("dno");
        dname = (String) attribute.get("dname");
        dalias = (String) attribute.get("dalias");
        geo_type = (Integer) attribute.get("geo_type");
        bs_prop = (Integer) attribute.get("bs_prop");
        sid = (Integer) attribute.get("sid");
        // initByteArrayPicture(sid);
    }

    /**
     * @method initMTFldModelListByBeanList
     * @TODO 初始化显示字段
     * @param sameDnoBeanList
     */
    public void initMTFldModelListByBeanList(
            ArrayList<SQLiteRecordBean> sameDnoBeanList) {
        for (SQLiteRecordBean bean : sameDnoBeanList) {
            Map<String, Object> attribute = bean.getAttribute();
            MTFldModel mt = new MTFldModel();
            mt.setName((String) attribute.get("name"));
            mt.setAlias((String) attribute.get("alias"));
            mt.setDisptype((Integer) attribute.get("disptype"));
            mt.setDefval((String) attribute.get("defval"));
            mt.setFldval((String) attribute.get("fldval"));
            mt.setProp((String) attribute.get("prop"));
            mt.setVisible((Integer) attribute.get("visible"));
            mt.setEditable((Integer) attribute.get("editable"));
            mtFldModelList.add(mt);
        }
        // 可显示和可编辑的字段
        for (MTFldModel mtFldModel : mtFldModelList) {
            if (mtFldModel.getVisible() == 1) {
                dispNameList.add(mtFldModel.getName());
                visiableMtFldModels.add(mtFldModel);
            }
            if (mtFldModel.getEditable() == 1) {
                editNameList.add(mtFldModel.getName());
                editableMtFldModels.add(mtFldModel);
            }
        }
    }

    /**
     * ediableMtFldModels
     * 
     * @return the ediableMtFldModels
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public ArrayList<MTFldModel> getEdiableMtFldModels() {
        return editableMtFldModels;
    }

    /**
     * visiableMtFldModels
     * 
     * @return the visiableMtFldModels
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public ArrayList<MTFldModel> getVisiableMtFldModels() {
        return visiableMtFldModels;
    }
    // /**
    // * @method initByteArrayPicture
    // * @TODO 初始化管线和管点图标
    // * @param sid
    // * @return
    // * @throws Exception
    // */
    // public boolean initByteArrayPicture(int sid) throws Exception
    // {
    // if(sid<0||dname==null||dname.equalsIgnoreCase(""))
    // return false;
    // if(dname.contains("管段"))
    // {
    // bArrayPic = null;
    // }else
    // {
    // bArrayPic =
    // DBWorkSpace.getInstance().getGrpdataBytesBySymbolSid(sid);
    // }
    // return true;
    // }
    /**
     * @version 2014-6-24
     * @author FangJianming
     */
    /*
     * public class MTFldModel { private String name; private String alias; // 1
     * TextBox 2 日期 3 下拉框 private int disptype;
     * 
     * private String defval; private String fldval;//提供的选项 private String prop;
     * private int visible; private int editable;
     *//**
     * name
     * 
     * @return the name
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public String getName() { return name; }
     *//**
     * @param name
     *            the name to set
     */
    /*
     * 
     * public void setName(String name) { this.name = name; }
     *//**
     * alias
     * 
     * @return the alias
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public String getAlias() { return alias; }
     *//**
     * @param alias
     *            the alias to set
     */
    /*
     * 
     * public void setAlias(String alias) { this.alias = alias; }
     *//**
     * disptype
     * 
     * @return the disptype
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public int getDisptype() { return disptype; }
     *//**
     * @param disptype
     *            the disptype to set
     */
    /*
     * 
     * public void setDisptype(int disptype) { this.disptype = disptype; }
     *//**
     * defval
     * 
     * @return the defval
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public String getDefval() { return defval; }
     *//**
     * @param defval
     *            the defval to set
     */
    /*
     * 
     * public void setDefval(String defval) { this.defval = defval; }
     *//**
     * fldval
     * 
     * @return the fldval
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public String getFldval() { return fldval; }
     *//**
     * @param fldval
     *            the fldval to set
     */
    /*
     * 
     * public void setFldval(String fldval) { this.fldval = fldval; }
     *//**
     * prop
     * 
     * @return the prop
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public String getProp() { return prop; }
     *//**
     * @param prop
     *            the prop to set
     */
    /*
     * 
     * public void setProp(String prop) { this.prop = prop; }
     *//**
     * visible
     * 
     * @return the visible
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public int getVisible() { return visible; }
     *//**
     * @param visible
     *            the visible to set
     */
    /*
     * 
     * public void setVisible(int visible) { this.visible = visible; }
     *//**
     * editable
     * 
     * @return the editable
     * @since CodingExample Ver(编码范例查看) 1.0
     */
    /*
     * 
     * public int getEditable() { return editable; }
     *//**
     * @param editable
     *            the editable to set
     */
    /*
     * 
     * public void setEditable(int editable) { this.editable = editable; }
     * 
     * }
     */
}
