package com.enn.sop.global;

import java.io.Serializable;

/**
 * @author yongzhan
 * @date 2017/12/19
 */
public class StationInfoModel implements Serializable {

    /**
     * 场站id
     */
    private String stationId;

    /**
     * 场站名
     */
    private String stationName;

    /**
     * 场站ecode
     */
    private String stationEcode;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationEcode() {
        return stationEcode;
    }

    public void setStationEcode(String stationEcode) {
        this.stationEcode = stationEcode;
    }
}
