package com.ecity.cswatersupply.project.model.db;

import com.ecity.android.db.model.ASQLiteBean;
import com.ecity.cswatersupply.utils.GsonUtil;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author Ma Qianli
 *
 */
public abstract class ABaseBeanXtd extends ASQLiteBean implements Comparable<ABaseBeanXtd> {
    public static final String UNIQUE_ID_COLUM_NAME = "_rid";
    private static final long serialVersionUID = 6568037669712829924L;
    protected int uniqueId;

    /**
     * Only available after reading from database. This value is intialized by buildFromCursor method.
     * @return
     */
    public int getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean buildFromJson(JSONObject json) {
        return false;
    }

    @Override
    public void copyTo(ASQLiteBean bean) {
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    @Override
    public Map<String, String> toMap() {
        return null;
    }

    @Override
    public int compareTo(ABaseBeanXtd another) {
        if (another == null) {
            return 1;
        }

        return getUniqueId() - another.getUniqueId();
    }
}
