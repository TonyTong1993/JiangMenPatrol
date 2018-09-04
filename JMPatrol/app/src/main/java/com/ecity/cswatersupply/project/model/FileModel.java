package com.ecity.cswatersupply.project.model;

import java.io.Serializable;

public class FileModel implements Serializable {
    //    "gid": 199,
    //    "datasize": 22081,
    //    "name": "定位 (1).png",
    //    "data": "",
    //    "content_type": "image/png",
    //    "type": "startPro",
    //    "remark": "",
    //    "adddate": "2016-12-23 17:47:02",
    //    "dbtype": 2,
    //    "filename": "startPro/定位 (1).png",
    //    "guid": "ee505f

    private static final long serialVersionUID = 3144576104914509687L;

    private String gid;
    private String datasize;
    private String name;
    private String data;
    private String content_type;
    private String type;
    private String remark;
    private String adddate;
    private String dbtype;
    private String filename;
    private String guid;

    public String getGid() {
        return gid;
    }

    public String getDatasize() {
        return datasize;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getContent_type() {
        return content_type;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public String getAdddate() {
        return adddate;
    }

    public String getDbtype() {
        return dbtype;
    }

    public String getFilename() {
        return filename;
    }

    public String getGuid() {
        return guid;
    }
}
