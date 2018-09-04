package com.ecity.cswatersupply.utils;

import android.content.Context;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemSelectValueAdapter;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.z3app.android.util.StringUtil;
import com.z3pipe.mobile.android.corssdk.model.EQulityType;
import com.z3pipe.mobile.android.corssdk.model.SatelliteInfo;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FZInspectItemUtil {

    /***
     * 设置福州cors连接的解状态和水平精度
     *
     * @param inspectItems
     */
    public static void setSatelliteInfo(List<InspectItem> inspectItems) {
        List<InspectItem> items = InspectItemUtil.mergeAllItemsInline(inspectItems);
        for(InspectItem item : items) {
            if(null == SessionManager.satelliteInfo) {
                if("jiestate".equalsIgnoreCase(item.getName()) || "shuipingjingdu".equalsIgnoreCase(item.getName())) {
                    item.setValue(ResourceUtil.getStringById(R.string.item_empty));
                }
            } else {
                SatelliteInfo satelliteInfo = SessionManager.satelliteInfo;
                if("jiestate".equalsIgnoreCase(item.getName())) {
                    item.setValue(satelliteInfo.getQuality().getValue());
                }
                if("shuipingjingdu".equalsIgnoreCase(item.getName())) {
                    item.setValue(satelliteInfo.getHdop());
                }
            }
        }
    }
}
