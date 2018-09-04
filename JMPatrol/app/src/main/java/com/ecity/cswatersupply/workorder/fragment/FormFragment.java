package com.ecity.cswatersupply.workorder.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.GsonUtil;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.workorder.view.FormActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *Created by Gxx on 2017/4/13.
 */
@SuppressLint("ValidFragment")
public class FormFragment extends Fragment {
    private FormActivity formActivity;
    private LinearLayout mLlContainer;
    private List<InspectItem> items;
    private List<View> itemViews;
    private CustomViewInflater customViewInflater;
    private static Map<String, FormFragment> tabName2Instances = new HashMap<String, FormFragment>();

    public static FormFragment getInstance(String key, List<InspectItem> items) {
        FormFragment instance = tabName2Instances.get(key);
        if (instance == null) {
            instance = new FormFragment();
            tabName2Instances.put(key, instance);
        }

        instance.items = items;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBusUtil.register(this);
        View convertView = inflater.inflate(R.layout.fragment_workorder_base_info, null);
        mLlContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
        formActivity = (FormActivity) getActivity();
        itemViews = new ArrayList<View>();
        refreshUI();

        return convertView;
    }

    public void onBackButtonClicked(View view) {
        formActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    private void refreshUI() {
        mLlContainer.removeAllViews();
        itemViews.clear();
        if (ListUtil.isEmpty(items)) {
            return;
        }
       customViewInflater = new CustomViewInflater(getActivity());
        for (InspectItem item : items) {
            View view  = customViewInflater.inflate(item);
            itemViews.add(view);
            mLlContainer.addView(view);
        }
    }

    public void onEventMainThread(UIEvent event) {

        switch (event.getId()) {
            case UIEventStatus.REFRESH_CASCAED_INSPECTITEM:
                refreshCascadeInspectItem(event);
                break;
            default:
                break;
        }
    }

    private void refreshCascadeInspectItem(UIEvent event) {
        List<InspectItemSelectValue> childSelectValues = new ArrayList<InspectItemSelectValue>();

        InspectItem parentInspectItem = event.getData();
        if (parentInspectItem.getValue().equals("")) {
            childSelectValues = InspectItemUtil.parseSelectValues1(parentInspectItem).get(0).getChildSelectValue();
        } else {
            childSelectValues = getSelectValueChild(parentInspectItem);
        }

        for (int i = 0; i < items.size(); i++) {
            InspectItem item = items.get(i);
            if(item.getCascadeGroupSn().equals("1") && (null != item.getCascadeGroupName())) {
                itemViews.remove(i);
                mLlContainer.removeViewAt(i);

                item.setSelectValues(GsonUtil.toJson(childSelectValues));
                View view = customViewInflater.inflate(item);
                itemViews.add(i, view);
                mLlContainer.addView(view, i);
            }
        }
    }

    /***
     * 根据父检查项的值查找子检查项的SelectValue
     * @return
     */
    private List<InspectItemSelectValue> getSelectValueChild(InspectItem inspectItem) {
        List<InspectItemSelectValue> selectValueLists = InspectItemUtil.parseSelectValues1(inspectItem);
        if (inspectItem.getValue().equals("")) {
            return selectValueLists.get(0).getChildSelectValue();
        }
        String value = inspectItem.getValue();
        List<InspectItemSelectValue> child = new ArrayList<InspectItemSelectValue>();
        for (InspectItemSelectValue selectValue : selectValueLists) {
            if (selectValue.getGid().equals(value)) {
                child = selectValue.getChildSelectValue();
                break;
            }
        }

        return child;
    }
}
