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
import com.ecity.cswatersupply.emergency.activity.EarthQuakeIncreaseReportActivity;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.ui.activities.CustomChildReportActivity1;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;

public class EarthquakeIncreaseInspectItemAdapter extends ArrayListAdapter<InspectItem> {
    private static final int TYPE_FIRST = 0;
    private static final int TYPE_SECOND = 1;

    private EarthQuakeIncreaseReportActivity mActivity;
    private LayoutInflater mInflater;
    private InspectItem mParentItem;
    private String mAddPrompt;

    public EarthquakeIncreaseInspectItemAdapter(EarthQuakeIncreaseReportActivity activity, InspectItem parentItem) {
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
                    convertView = mInflater.inflate(R.layout.earthquake_house_damage_investigationform_item, null);
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
                holdFirst.tv_emergency_content.setText(des);
                break;
            case TYPE_SECOND:
                if (!StringUtil.isBlank(mAddPrompt)) {
                    holdSecond.tvAddMaterial.setText(mAddPrompt);
                }
                if (!ListUtil.isEmpty(mList)) {
                    holdSecond.tvAddMaterial.setOnClickListener(new MyAddMaterialOnClickListener(mList.get(0)));
                }
                break;
            default:
                break;
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_SECOND;
        } else {
            return TYPE_FIRST;
        }
    }

    static class ViewHolderFirst {
        
        /*private TextView tv_emergency_xuhao;
        private TextView tv_emergency_address;
        private TextView tv_emergency_pohuaichengdu;

        public ViewHolderFirst(View convertView) {
            tv_emergency_xuhao = (TextView) convertView.findViewById(R.id.tv_emergency_xuhao);
            tv_emergency_address = (TextView) convertView.findViewById(R.id.tv_emergency_address);
            tv_emergency_pohuaichengdu = (TextView) convertView.findViewById(R.id.tv_emergency_pohuaichengdu);
        }*/
        private TextView tv_emergency_content;
        public ViewHolderFirst(View convertView) {
            tv_emergency_content = (TextView)convertView.findViewById(R.id.tv_emergency_content);
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
                        //生成   名称:值
                        valueDisplay.append(itemTemp.getAlias()).append(" ");
                        String valueDes = inspectItemSelectValue.name;
                        valueDisplay.append(valueDes).append("\n");
                        break;
                    }
                }
            } else if (!StringUtil.isEmpty(itemTemp.getDefaultValue())) {
                for (InspectItemSelectValue inspectItemSelectValue : selectValueLists) {
                    if (inspectItemSelectValue.gid.equalsIgnoreCase(itemTemp.getDefaultValue())) {
                        valueDisplay.append(itemTemp.getAlias()).append(" ");
                        String valueDes = inspectItemSelectValue.name;
                        valueDisplay.append(valueDes).append("\n");
                        break;
                    }
                }
            } else {
                // no logic to do.
            }

        } else {
            if (!StringUtil.isBlank(itemTemp.getValue())) {
                valueDisplay.append(itemTemp.getAlias()).append(" ");
                valueDisplay.append(itemTemp.getValue()).append("\n");
            } else {
                valueDisplay.append(itemTemp.getAlias()).append(" ");
                valueDisplay.append(itemTemp.getDefaultValue()).append("\n");
            }
        }

        return valueDisplay.toString();
    }
}
