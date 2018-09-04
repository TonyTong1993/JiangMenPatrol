package com.ecity.cswatersupply.adapter.checkitem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.ui.activities.CustomChildReportActivity1;
import com.ecity.cswatersupply.ui.activities.CustomIncreaseReportActivity;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;

public class IncreaseInspectItemAdapter extends ArrayListAdapter<InspectItem> {
    private static final int TYPE_FIRST = 0;
    private static final int TYPE_SECOND = 1;

    private CustomIncreaseReportActivity mActivity;
    private LayoutInflater mInflater;
    private InspectItem mParentItem;
    private String mAddPrompt;

    public IncreaseInspectItemAdapter(CustomIncreaseReportActivity activity, InspectItem parentItem) {
        super(activity);
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.mParentItem = parentItem;
        this.mList = new ArrayList<InspectItem>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InspectItem item = null;
        if (!ListUtil.isEmpty(mList) && position < mList.size()) {
            item = mList.get(position);
        }
        ViewHolderFirst holdFirst = null;
        ViewHolderSecond holdSecond = null;
        int type = getItemViewType(position);
        if (null == convertView) {
            switch (type) {
                case TYPE_FIRST:
                    convertView = mInflater.inflate(R.layout.custom_form_subitem_one_increase, null);
                    holdFirst = new ViewHolderFirst(convertView);
                    convertView.setTag(holdFirst);
                    break;
                case TYPE_SECOND:
                    convertView = mInflater.inflate(R.layout.custom_form_subitem_two_increase, null);
                    holdSecond = new ViewHolderSecond(convertView);
                    convertView.setTag(holdSecond);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_FIRST:
                    holdFirst = (ViewHolderFirst) convertView.getTag();
                    break;
                case TYPE_SECOND:
                    holdSecond = (ViewHolderSecond) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (type) {
            case TYPE_FIRST:
                String des = getMaterialDes(item);
                String name = des.substring(0, des.indexOf(" "));
                String detail = des.substring(des.indexOf(" "), des.length() - 1);
                holdFirst.tvIncreaseLeft.setText(name);
                holdFirst.tvIncreaseRight.setText(detail);
                break;
            case TYPE_SECOND:
                if (!StringUtil.isBlank(mAddPrompt)) {
                    holdSecond.tvAddMaterial.setText(mAddPrompt);
                }
                holdSecond.tvAddMaterial.setOnClickListener(new MyAddMaterialOnClickListener(mParentItem));
                break;
            default:
                break;
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size() + 1;
        else
            return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return TYPE_SECOND;
        } else {
            return TYPE_FIRST;
        }
    }

    static class ViewHolderFirst {
        private TextView tvIncreaseLeft;
        private TextView tvIncreaseRight;

        public ViewHolderFirst(View convertView) {
            tvIncreaseLeft = (TextView) convertView.findViewById(R.id.tv_increase_value_left);
            tvIncreaseRight = (TextView) convertView.findViewById(R.id.tv_add);
        }
    }

    static class ViewHolderSecond {
        private TextView tvAddMaterial;

        public ViewHolderSecond(View convertView) {
            tvAddMaterial = (TextView) convertView.findViewById(R.id.tv_add_material);
        }
    }

    public String getAddPrompt() {
        return mAddPrompt;
    }

    public void setAddPrompt(String mAddPrompt) {
        this.mAddPrompt = mAddPrompt;
    }

    private class MyAddMaterialOnClickListener implements OnClickListener {
        private InspectItem item;

        public MyAddMaterialOnClickListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, CustomChildReportActivity1.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CustomViewInflater.REPORT_TITLE_PARENT, item);
            bundle.putSerializable(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) item.getChilds());
            //bundle.putString(CustomReportActivity.REPORT_COORDINATE, "31.774588853418408,120.99370401185207");
            bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
            intent.putExtras(bundle);
            mActivity.startActivityForResult(intent, RequestCode.REQUEST_CHILD_ITEM);
        }
    }

    private String getMaterialDes(InspectItem item) {
        StringBuilder des = new StringBuilder("");
        if (null == item || ListUtil.isEmpty(item.getChilds())) {
            return des.toString();
        }
        List<InspectItem> childs = item.getChilds();
        for (InspectItem itemTemp : childs) {
            des.append(getValue(itemTemp));
        }

        return des.toString();
    }

    private String getValue(InspectItem itemTemp) {
        StringBuilder valueDisplay = new StringBuilder("");
        if (null == itemTemp) {
            return valueDisplay.toString();
        }
        List<InspectItemSelectValue> selectValueLists = null;
        if (itemTemp.getType() == EInspectItemType.DROPDOWNLIST || itemTemp.getType() == EInspectItemType.RADIO || itemTemp.getType() == EInspectItemType.CHECKBOX) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(itemTemp.getSelectValues());
                selectValueLists = InspectItemSelectValueAdapter.adapt(jsonArray);
            } catch (JSONException e) {
                LogUtil.e(this, e);
            }

            if (ListUtil.isEmpty(selectValueLists)) {
                return valueDisplay.toString();
            }

            if (!StringUtil.isEmpty(itemTemp.getValue())) {
                for (InspectItemSelectValue inspectItemSelectValue : selectValueLists) {
                    if (inspectItemSelectValue.gid.equalsIgnoreCase(itemTemp.getValue())) {
                        String valueDes = inspectItemSelectValue.name;
                        valueDisplay.append(valueDes).append(" ");
                        break;
                    }
                }
            } else if (!StringUtil.isEmpty(itemTemp.getDefaultValue())) {
                for (InspectItemSelectValue inspectItemSelectValue : selectValueLists) {
                    if (inspectItemSelectValue.gid.equalsIgnoreCase(itemTemp.getDefaultValue())) {
                        String valueDes = inspectItemSelectValue.name;
                        valueDisplay.append(valueDes).append(" ");
                        break;
                    }
                }
            } else {
                // no logic to do.
            }

        } else {
            if (!StringUtil.isBlank(itemTemp.getValue())) {
                valueDisplay.append(itemTemp.getValue()).append(" ");
            } else {
                valueDisplay.append(itemTemp.getDefaultValue()).append(" ");
            }
        }

        return valueDisplay.toString();
    }
}
