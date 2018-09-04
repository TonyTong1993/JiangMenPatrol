package com.ecity.cswatersupply.model.metaconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hancuiyan
 *
 */
public class DbMetaSet implements Serializable {
    private static final long serialVersionUID = -2535554390263131634L;
    private List<DbMetaGWFL> listDbMetaGWFL = new ArrayList<DbMetaGWFL>();
    private List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
    private List<String> ListdbConnInfo = new ArrayList<String>();

    public List<DbMetaGWFL> getListDbMetaGWFL() {
        return listDbMetaGWFL;
    }

    public void setListDbMetaGWFL(List<DbMetaGWFL> listDbMetaGWFL) {
        this.listDbMetaGWFL = listDbMetaGWFL;
    }

    public List<DbMetaInfo> getListDbMetaInfo() {
        return listDbMetaInfo;
    }

    public void setListDbMetaInfo(List<DbMetaInfo> listDbMetaInfo) {
        this.listDbMetaInfo = listDbMetaInfo;
    }

    public List<String> getListdbConnInfo() {
        return ListdbConnInfo;
    }

    public void setListdbConnInfo(List<String> listdbConnInfo) {
        ListdbConnInfo = listdbConnInfo;
    }
}
