package com.ecity.cswatersupply.ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.inpsectitem.ContactInspectItemViewXtd;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2017-3-23
 */
public class FZFeedBackCustomReportActivity extends CustomReportActivity1 {

    private List<String> itemNames =  new ArrayList<>();
    private InspectItem linkInspectItem;

    protected void submitInfo(List<InspectItem> items) {
        List<InspectItem> inspectItems = setLinkInspectItemValue(items);
        if (InspectItemUtil.hasEmptyItem(inspectItems)) {
            Toast.makeText(this, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isInspectItemContentValid()) {
            return;
        }

        if (null != mCommonReportOperator) {
            SessionManager.currentInspectItems = inspectItems;
            mCommonReportOperator.submit2Server(inspectItems);
        }
    }

    protected void internalFillDatas(List<InspectItem> inspectItems) {
        if (ListUtil.isEmpty(inspectItems)) {
            return;
        }

        List<InspectItem> items = InspectItemUtil.mergeAllItemsInline(inspectItems);
        List<InspectItem> merginLinkedinspectItems = new ArrayList<>();
        if (null != layoutContainerView) {
            layoutContainerView.removeAllViews();
        }

        for(InspectItem item : items) {
            itemNames.add(item.getName());
        }
        if(itemNames.contains("FEEDINFO") && itemNames.contains("wentimiaoshu")) {
            merginLinkedinspectItems = merginLinkInspectItems(items);
        }

        mCustomViewInflater = new CustomViewInflater(this);
        layoutContainerView = (LinearLayout) findViewById(R.id.ll_container);

        for (InspectItem item : merginLinkedinspectItems) {
            if (!item.isVisible() && (!item.getType().equals(EInspectItemType.GROUP))) {
                continue;
            }

            if (isFromTaskFunction) {
                addGroupTitleView(getLayoutInflater(), layoutContainerView, item);
            }
            if (item.getName().equalsIgnoreCase("SHEBETYPE")) {
                item.setEdit(false);
                item.setValue(SessionManager.currentPointPartIntMapOpretor.getType());
            }
            if(item.getName().equalsIgnoreCase("XUNJMAN")) {
                User currentUser = HostApplication.getApplication().getCurrentUser();
                String value = currentUser.getId() + ContactInspectItemViewXtd.CONTACT_USER_INFO_SEPARATOR + currentUser.getLoginName();
                JSONArray jsonArrays = new JSONArray();
                jsonArrays.put(value);
                item.setValue(jsonArrays.toString());
            }

            layoutContainerView.addView(mCustomViewInflater.inflate(item));
        }
    }

    private List<InspectItem> merginLinkInspectItems(List<InspectItem> inspectItems) {
        List<InspectItem> items = new ArrayList<>();
        String linkValues = "";
        for(InspectItem item : inspectItems) {
            if(item.getName().equals("FEEDINFO")) {
                item.setType(EInspectItemType.LINKDROPDOWNLIST);
                item.setGroupName(getFirstGroupName(item, mInspectItems));
                item.setLinkName("wentimiaoshu");
                item.setLinkAlias(ResourceUtil.getStringById(R.string.planningtask_wentimiaoshu));
                if(!StringUtil.isBlank(item.getValue())) {
                    linkValues = item.getValue() + ",";
                }
                linkInspectItem = item;
            }
            if(item.getName().equals("wentimiaoshu")) {
                if(!StringUtil.isBlank(item.getValue())) {
                    linkValues += item.getValue();
                }
                linkInspectItem.setValue(linkValues);
                continue;
            }
            items.add(item);
        }

        return items;
    }

    private List<InspectItem> setLinkInspectItemValue(List<InspectItem> inspectItems) {
        List<InspectItem> items = new ArrayList<>();
        String values = linkInspectItem.getValue();
        for(InspectItem item : inspectItems) {
            if(item.getName().equals("FEEDINFO")) {
                item.setType(EInspectItemType.DROPDOWNLIST);
                if(values.contains(",")) {
                    String[] vals = values.split(",");
                    if(null != vals && vals.length >= 1) {
                        item.setValue(vals[0]);
                    }
                } else {
                    item.setValue(values);
                }
            } else if(item.getName().equals("wentimiaoshu")) {
                if(values.contains(",")) {
                    String[] vals = values.split(",");
                    if(null != vals && vals.length == 2 ) {
                        item.setValue(vals[1]);
                    } else {
                        item.setValue("");
                    }
                } else {
                    item.setValue("");
                }
            }
            items.add(item);
        }

        return items;
    }

}
