package com.ecity.cswatersupply.ui.inpsectitem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.activity.EarthQuakeIncreaseReportActivity;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.model.checkitem.PatrolPosition;
import com.ecity.cswatersupply.ui.activities.CustomChildReportActivity1;
import com.ecity.cswatersupply.ui.activities.CustomIncreaseReportActivity;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.z3app.android.util.StringUtil;

public class GroupInspectItemViewXtd extends ABaseInspectItemView {
    private Map<String, TextView> mHashMapButtons = new HashMap<String, TextView>();
    private TextView tvGroupLeft;
    private TextView tvGroupRight;

    @Override
    protected void setup(View contentView) {
        contentView.findViewById(R.id.ll_item_title).setVisibility(View.GONE);
        tvGroupLeft = (TextView) contentView.findViewById(R.id.tv_group_value_left);
        if (!StringUtil.isBlank(mInspectItem.getAlias())) {
            tvGroupLeft.setText(mInspectItem.getAlias());
        } else {
            tvGroupLeft.setText(mInspectItem.getName());
        }
        tvGroupRight = (TextView) contentView.findViewById(R.id.tv_group_value_right);
        if (!StringUtil.isBlank(mInspectItem.getValue())) {
            tvGroupRight.setText(mInspectItem.getValue());
        } else if (!StringUtil.isBlank(mInspectItem.getDefaultValue())) {
            tvGroupRight.setText(mInspectItem.getDefaultValue());
        }

        setGroupRightDefultValue(mInspectItem);
        mHashMapButtons.put(mInspectItem.getName(), tvGroupRight);
        tvGroupRight.setOnClickListener(new MyChildItemsOnClickListener(mInspectItem));
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_group;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.REQUEST_CHILD_ITEM) {
            if (null != data) {
                InspectItem parentItem = (InspectItem) data.getSerializableExtra(CustomViewInflater.REPORT_TITLE_PARENT);
                @SuppressWarnings("unchecked")
                List<InspectItem> childItems = (List<InspectItem>) data.getSerializableExtra(CustomViewInflater.REPORT_CHILD_ITEMS);
                setChildItemsValue(parentItem, childItems);
            }
        }
    }

    /***
     * 检查项如果有值或默认值就显示组内各检查项的值或默认值，没有则显示组内各检查项的别名
     *
     * @param inspectItem
     */
    private void setGroupRightDefultValue(InspectItem inspectItem) {
        List<InspectItem> childs = inspectItem.getChilds();
        if(ListUtil.isEmpty(childs)) {
            return;
        }

        // 获取组内各检查项的值或默认值，空格连接
        StringBuilder valueDisplay = new StringBuilder("");
        if (isChildItemsHasGroup(childs)) {
            valueDisplay = getGroupValueDisplay(childs);
        } else {
            for (InspectItem itemTemp : childs) {
                getChildValueDisplay(itemTemp, valueDisplay);
            }
        }

        // 判断检查项是否有值，决定显示的数据
        if (null != valueDisplay && !StringUtil.isBlank(valueDisplay.toString())) {
            tvGroupRight.setText(valueDisplay.toString());
        } else {
            StringBuilder sb = new StringBuilder("");
            if (!ListUtil.isEmpty(mInspectItem.getChilds())) {
                for (InspectItem itemChild : mInspectItem.getChilds()) {
                    sb.append(itemChild.getAlias()).append(" ");
                }
            }
            tvGroupRight.setText(sb.toString());
        }
    }

    private class MyChildItemsOnClickListener implements OnClickListener {
        private InspectItem item;

        public MyChildItemsOnClickListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (item.isIncrease()) {
                Intent intent = null;
                if (EInspectItemType.TEXT.equals(item.getChilds().get(0).getType()) || EInspectItemType.TEXTEXT.equals(item.getChilds().get(0).getType())) {
                    intent = new Intent(context, EarthQuakeIncreaseReportActivity.class);
                } else {
                    intent = new Intent(context, CustomIncreaseReportActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(CustomViewInflater.REPORT_TITLE_PARENT, item);
                bundle.putSerializable(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) item.getChilds());
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCode.REQUEST_CHILD_ITEM);
            } else {
                Intent intent = new Intent(context, CustomChildReportActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CustomViewInflater.REPORT_TITLE_PARENT, item);
                bundle.putSerializable(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) item.getChilds());
                bundle.putSerializable(CustomViewInflater.REPORT_COORDINATE, new PatrolPosition(true, 31.774588853418408, 120.99370401185207, ""));
                bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
                bundle.putBoolean(CustomViewInflater.KEY_IS_EARTHQUAKE_INSPECTITEMS, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCode.REQUEST_CHILD_ITEM);
            }
        }
    }

    private boolean isChildItemsHasGroup(List<InspectItem> childItems) {
        for (InspectItem itemTemp : childItems) {
            if (itemTemp.getType() == EInspectItemType.GROUP) {
                return true;
            } else {
                continue;
            }
        }
        return false;

    }

    private void setChildItemsValue(InspectItem parentItem, List<InspectItem> childItems) {
        if ((null == parentItem) || ListUtil.isEmpty(childItems)) {
            return;
        }
        StringBuilder valueDisplay = new StringBuilder();
        //检查子表单内是否含有group 
        if (isChildItemsHasGroup(childItems)) {
            valueDisplay = getGroupValueDisplay(childItems);
        } else {
            for (InspectItem itemTemp : childItems) {
                getChildValueDisplay(itemTemp, valueDisplay);
            }
        }

        setInspectValueByChildItems(parentItem, childItems, valueDisplay);
    }

    private StringBuilder getGroupValueDisplay(List<InspectItem> items) {
        StringBuilder valueDisplay = new StringBuilder();

        for(InspectItem item : items) {
            if(!ListUtil.isEmpty(item.getChilds())) {
                valueDisplay.append(getGroupValueDisplay(item.getChilds()));
            } else {
                getChildValueDisplay(item, valueDisplay);
            }
        }

        return valueDisplay;
    }

    private void getChildValueDisplay(InspectItem item, StringBuilder valueDisplay) {
        if (item.getType() == EInspectItemType.DROPDOWNLIST || item.getType() == EInspectItemType.DROPDOWNLISTEXT || item.getType() == EInspectItemType.RADIO
                || item.getType() == EInspectItemType.CHECKBOX) {
            List<InspectItemSelectValue> selectValueLists = InspectItemUtil.parseSelectValues(item);
            if (ListUtil.isEmpty(selectValueLists)) {
                return;
            }

            if (!StringUtil.isEmpty(item.getValue())) {
                boolean hasValue = false;
                for (InspectItemSelectValue inspectItemSelectValue : selectValueLists) {
                    if (item.getValue().contains(inspectItemSelectValue.gid)) {
                        hasValue = true;
                        String valueDes = inspectItemSelectValue.name;
                        valueDisplay.append(valueDes).append(" ");
                    }
                }
                if (!hasValue && item.getType() == EInspectItemType.DROPDOWNLISTEXT) {
                    valueDisplay.append(item.getValue()).append(" ");
                }
            } else if (!StringUtil.isEmpty(item.getDefaultValue())) {
                boolean hasValue = false;
                for (InspectItemSelectValue inspectItemSelectValue : selectValueLists) {
                    if (item.getValue().contains(inspectItemSelectValue.gid)) {
                        hasValue = true;
                        String valueDes = inspectItemSelectValue.name;
                        valueDisplay.append(valueDes).append(" ");
                    }
                }
                if (!hasValue && item.getType() == EInspectItemType.DROPDOWNLISTEXT) {
                    valueDisplay.append(item.getValue()).append(" ");
                }
            } else {
                // no logic to do.
            }
        } else {
            if (!StringUtil.isEmpty(item.getValue())) {
                valueDisplay.append(item.getValue()).append(" ");
            } else if (!StringUtil.isEmpty(item.getDefaultValue())) {
                valueDisplay.append(item.getDefaultValue()).append(" ");
            } else {
                // no logic to do.
            }
        }
    }

    private void setInspectValueByChildItems(InspectItem parentItem, List<InspectItem> childItems, StringBuilder valueDisplay) {
        if (!mInspectItem.getName().equalsIgnoreCase(parentItem.getName())) {
            return;
        }

        if (!ListUtil.isEmpty(childItems)) {
            if (mInspectItem.isIncrease()) {
                mInspectItem.setValue(generateIncreaseJsonString(childItems));
                mInspectItem.setChilds(childItems);
            } else {
                mInspectItem.setValue(generateNoIncreaseJsonString(childItems));
                mInspectItem.setChilds(childItems);
            }
        } else {
            mInspectItem.setValue("");
        }

        if (null != valueDisplay && !StringUtil.isBlank(valueDisplay.toString())) {
            mHashMapButtons.get(parentItem.getName()).setText(valueDisplay);
        }
    }

    private String generateNoIncreaseJsonString(List<InspectItem> dataSources) {
        JSONArray jsonArray = new JSONArray();
        for (InspectItem itemTemp : dataSources) {
            JSONObject jsonObject = inspectItem2Json(itemTemp);
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    private String generateIncreaseJsonString(List<InspectItem> dataSources) {
        String jsonResult = "";
        if (ListUtil.isEmpty(dataSources)) {
            return jsonResult;
        }
        JSONArray jsonArray = new JSONArray();
        for (InspectItem itemTemp : dataSources) {
            JSONArray jsonTempArray = new JSONArray();
            if (EInspectItemType.GROUP.equals(itemTemp.getType())) {
                List<InspectItem> childItems = itemTemp.getChilds();
                if (ListUtil.isEmpty(childItems)) {
                    return itemTemp.getValue();
                }
                for (InspectItem inspectItem : childItems) {
                    JSONObject jsonObject = inspectItem2Json(inspectItem);
                    jsonTempArray.put(jsonObject);
                }
                jsonArray.put(jsonTempArray);
            } else {
                JSONObject jsonObject = inspectItem2Json(itemTemp);
                jsonArray.put(jsonObject);
            }
        }
        jsonResult = jsonArray.toString();

        return jsonResult;
    }

    private String generateIncreaseGroupJsonString(InspectItem dataSources) {
        String jsonResult = "";
        JSONArray jsonTempArray = new JSONArray();
        List<InspectItem> childItems = dataSources.getChilds();
        if (ListUtil.isEmpty(childItems)) {
            return dataSources.getValue();
        }
        for (InspectItem inspectItem : childItems) {
            JSONObject jsonObject = inspectItem2Json(inspectItem);
            jsonTempArray.put(jsonObject);
        }
        jsonResult = jsonTempArray.toString();

        return jsonResult;
    }

    private JSONObject inspectItem2Json(InspectItem inspectItem) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", inspectItem.getName());
            jsonObject.put("alias", inspectItem.getAlias());
            jsonObject.put("type", inspectItem.getType());
            jsonObject.put("required", inspectItem.isRequired() ? 1 : 0);
            jsonObject.put("defaultValue", inspectItem.getDefaultValue());
            jsonObject.put("value", inspectItem.getValue());
            jsonObject.put("len", inspectItem.isLongText() ? 1 : 0);
            jsonObject.put("increase", inspectItem.isIncrease() ? 1 : 0);
            jsonObject.put("selectValues", inspectItem.getSelectValues());
            jsonObject.put("childs", generateIncreaseGroupJsonString(inspectItem));
            jsonObject.put("visible", inspectItem.isVisible() ? 1 : 0);
            jsonObject.put("edit", inspectItem.isEdit() ? 1 : 0);
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }

        return jsonObject;
    }
}
