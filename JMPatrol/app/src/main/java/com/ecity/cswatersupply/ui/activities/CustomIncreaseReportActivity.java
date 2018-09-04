package com.ecity.cswatersupply.ui.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.IncreaseInspectItemAdapter;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.AlertStyle;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

/**
 * @author SunShan'ai
 * 当通用上报界面中有可以多次添加的条目时,即increase为true时,调用这个界面(一般情况下,不需要使用者单独调用),由于increase不是EInspectItemType中的类型,
 * 所以没有继承CustomReportActivity
 */
public class CustomIncreaseReportActivity extends BaseActivity {
    private ListView mListView;
    private IncreaseInspectItemAdapter mAdapter;
    private InspectItem mParentItem;
    private List<InspectItem> mChildItems;
    private List<InspectItem> mDatas;
    private String mJsonDatas;
    private CustomTitleView mCustomTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_form_item_increase);
        initData();
        initUI();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.REQUEST_CHILD_ITEM:
                if (null != data && null != mAdapter && null != mDatas) {
                    mChildItems = (List<InspectItem>) data.getSerializableExtra(CustomViewInflater.REPORT_CHILD_ITEMS);
                    int positionId = data.getIntExtra(CustomViewInflater.REPORT_MULTI_CHILD_IDENTIFY, -1);
                    if (positionId >= 0 && positionId < mDatas.size()) {
                        mDatas.get(positionId).setChilds(mChildItems);
                        mAdapter.setList(mDatas);
                    } else {
                        InspectItem item = new InspectItem();
                        cloneParentItem(item);
                        item.setChilds(mChildItems);
                        mDatas.add(item);

                        mAdapter.setList(mDatas);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onFinish(View view) {
        Intent intent = new Intent();
        intent.putExtra(CustomViewInflater.REPORT_TITLE_PARENT, mParentItem);
        intent.putExtra(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) mDatas);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mParentItem = (InspectItem) bundle.getSerializable(CustomViewInflater.REPORT_TITLE_PARENT);
            mChildItems = (List<InspectItem>) bundle.getSerializable(CustomViewInflater.REPORT_CHILD_ITEMS);
            if (null == mParentItem || null == mChildItems) {
                return;
            }

            if (!StringUtil.isBlank(mParentItem.getValue())) {
                mJsonDatas = mParentItem.getValue();
                mDatas = adaptDatasFromJsonString(mJsonDatas);
            }

            mAdapter = new IncreaseInspectItemAdapter(CustomIncreaseReportActivity.this, mParentItem);
            mAdapter.setAddPrompt(String.format(getString(R.string.event_report_add_material), mParentItem.getAlias()));
            if (!ListUtil.isEmpty(mDatas)) {
                mAdapter.setList(mDatas);
            } else {
                mDatas = new ArrayList<InspectItem>();
            }
        }
    }

    private void initUI() {
        mListView = (ListView) findViewById(R.id.lv_can_add_item);
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(new MyAddListViewOnItemClickListener());
        mListView.setOnItemLongClickListener(new MyAddListViewOnItemLongClickListener());
        mCustomTitleView = (CustomTitleView) findViewById(R.id.view_title_report_event);
        mCustomTitleView.setTitleText(mParentItem.getAlias());
        mCustomTitleView.setBtnStyle(BtnStyle.ONLY_BACK);
    }

    private ArrayList<InspectItem> adaptDatasFromJsonString(String jsonString) {
        ArrayList<InspectItem> datas = new ArrayList<InspectItem>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                InspectItem item = new InspectItem();
                cloneParentItem(item);
                ArrayList<InspectItem> datasTemp = new ArrayList<InspectItem>();
                JSONArray jsonTempArray = jsonArray.optJSONArray(i);
                for (int j = 0; j < jsonTempArray.length(); j++) {
                    datasTemp.add(InspectItemAdapter.adaptItem(jsonTempArray.optJSONObject(j)));
                }
                item.setChilds(datasTemp);
                datas.add(item);
            }

        } catch (JSONException e) {
            LogUtil.e(this, e);
        }

        return datas;
    }

    private void cloneParentItem(InspectItem item) {
        if (null != mParentItem) {
            item.setAlias(mParentItem.getAlias());
            item.setDefaultValue(mParentItem.getDefaultValue());
            item.setGeoType(mParentItem.getGeoType());
            item.setIncrease(mParentItem.isIncrease());
            item.setLongText(mParentItem.isLongText());
            item.setName(mParentItem.getName());
            item.setRequired(mParentItem.isRequired());
            item.setSelectValues(mParentItem.getSelectValues());
            item.setType(mParentItem.getType());
            item.setValue(mParentItem.getValue());
        }
    }

    private class MyAddListViewOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < mDatas.size()) {
                Intent intent = new Intent(CustomIncreaseReportActivity.this, CustomChildReportActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CustomViewInflater.REPORT_TITLE_PARENT, mParentItem);
                bundle.putInt(CustomViewInflater.REPORT_MULTI_CHILD_IDENTIFY, position);
                bundle.putSerializable(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) mDatas.get(position).getChilds());
                bundle.putString(CustomViewInflater.REPORT_COMFROM, EventReportOperator1.class.getName());
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCode.REQUEST_CHILD_ITEM);
            }
        }
    }

    private class MyAddListViewOnItemLongClickListener implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            if (position < mDatas.size()) {
                AlertView dialog = new AlertView(CustomIncreaseReportActivity.this, getString(R.string.dialog_title_prompt), getString(R.string.event_report_delete_item_tips),
                        new OnAlertViewListener() {

                            @Override
                            public void back(boolean result) {
                                if (result) {
                                    mDatas.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }, AlertStyle.OK_CANCEL);
                dialog.show();
            }

            return true;
        }
    }
}
