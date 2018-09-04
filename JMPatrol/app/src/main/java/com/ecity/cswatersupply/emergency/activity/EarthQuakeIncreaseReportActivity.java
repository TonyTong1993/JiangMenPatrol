package com.ecity.cswatersupply.emergency.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.EarthquakeIncreaseInspectItemAdapter;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.menu.EventReportOperator1;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.activities.CustomChildReportActivity1;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.AlertStyle;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

/**
 * 对应地震调查的房屋破坏列表
 * @author ml
 *
 */
public class EarthQuakeIncreaseReportActivity extends BaseActivity {
    private ListView mListView;
    private EditText et_remark_value;
    private EarthquakeIncreaseInspectItemAdapter mAdapter;
    private InspectItem mParentItem;
    private List<InspectItem> mChildItems;
    //描述的
    private InspectItem mTextItems;
    private List<InspectItem> mDatas;
    private CustomTitleView mCustomTitleView;
    private int IncreaseNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_form_increase);
        initData();
        initUI();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.REQUEST_CHILD_ITEM:
                if (null != data && null != mAdapter && null != mDatas) {
                    List<InspectItem> mItems = (List<InspectItem>) data.getSerializableExtra(CustomViewInflater.REPORT_CHILD_ITEMS);
                    int positionId = data.getIntExtra(CustomViewInflater.REPORT_MULTI_CHILD_IDENTIFY, -1);
                    if (positionId >= 0 && positionId < mDatas.size()) {
                        mDatas.get(positionId).setChilds(mItems);
                        mAdapter.setList(mDatas);
                    } else {
                        InspectItem item = new InspectItem();
                        cloneClildItem(item,true);
                        item.setChilds(mItems);
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

        intent.putExtra(CustomViewInflater.REPORT_CHILD_ITEMS, (Serializable) initBackData());
        setResult(RESULT_OK, intent);
        finish();
    }

    //回传数据组装
    public List<InspectItem> initBackData() {
        InspectItem dataItem = new InspectItem();
        cloneClildItem(dataItem,false);
        dataItem.setChilds(mDatas);
        List<InspectItem> childItems = new ArrayList<InspectItem>();
        childItems.add(mTextItems);
        childItems.add(dataItem);

        return childItems;
    }

    public void onActionButtonClicked(View view) {
        onFinish(view);
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            IncreaseNum = 0;
            mParentItem = (InspectItem) bundle.getSerializable(CustomViewInflater.REPORT_TITLE_PARENT);
            mChildItems = (List<InspectItem>) bundle.getSerializable(CustomViewInflater.REPORT_CHILD_ITEMS);
            if (null == mParentItem || null == mChildItems) {
                return;
            }

            if (!StringUtil.isBlank(mParentItem.getValue())) {
                String mJsonDatas = mParentItem.getValue();
                mChildItems = adaptChildItemsFromJsonString(mJsonDatas);
            }
            
            if (ListUtil.isEmpty(mChildItems) == false) {
                for (int i = 0; i < mChildItems.size(); i++) {
                    switch (mChildItems.get(i).getType()) {
                        case TEXT:
                            mTextItems = mChildItems.get(i);
                            break;
                        case GROUP:
                            mDatas = mChildItems.get(i).getChilds();
                            break;
                        default:
                            break;
                    }
                }
            }

            mAdapter = new EarthquakeIncreaseInspectItemAdapter(EarthQuakeIncreaseReportActivity.this, mParentItem);
            mAdapter.setAddPrompt(String.format(getString(R.string.event_report_add_material), mChildItems.get(0).getAlias()));
            if(!mChildItems.get(0).isEdit()) {
                for(InspectItem item : InspectItemUtil.mergeAllItemsInline(mDatas)) {
                    item.setEdit(false);
                }
            }

            if (!ListUtil.isEmpty(mDatas)) {
                mAdapter.setList(mDatas);
            } else {
                mDatas = new ArrayList<InspectItem>();
            }
        }
    }

    private void initUI() {
        et_remark_value = (EditText) findViewById(R.id.et_remark_value);
        if (null != mTextItems) {
            et_remark_value.setText(mTextItems.getValue());
        }
        if(mTextItems.isEdit()) {
            et_remark_value.addTextChangedListener(new MyEditTextListener(mTextItems));
        } else {
            et_remark_value.setText(mTextItems.getValue());
            et_remark_value.setEnabled(false);
        }
        mListView = (ListView) findViewById(R.id.lv_can_add_item);
        if (null != mAdapter) {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(new MyAddListViewOnItemClickListener());
        mListView.setOnItemLongClickListener(new MyAddListViewOnItemLongClickListener());
        mCustomTitleView = (CustomTitleView) findViewById(R.id.view_title_report_event);
        mCustomTitleView.setTitleText(mParentItem.getAlias());
        mCustomTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
        mCustomTitleView.setRightActionBtnText(R.string.str_emergency_finish);
    }

    private ArrayList<InspectItem> adaptChildItemsFromJsonString(String jsonString) {
        ArrayList<InspectItem> childItems = new ArrayList<InspectItem>();

        try {

            JSONArray jsonArray = new JSONArray(jsonString);
            if (null == jsonArray || jsonArray.length() == 0) {
                return null;
            }
            //描述
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            if (null != jsonObject) {
                childItems.add(InspectItemAdapter.adaptItem(jsonObject));
            }
            if (!jsonArray.isNull(1)) {
                InspectItem item = new InspectItem();
                cloneParentItem(item);
                item.setChilds(adaptDatasFromJsonArray(jsonArray.optJSONArray(1)));
                childItems.add(item);
            }
        } catch (JSONException e) {
            LogUtil.e(this, e);
        }
        return childItems;
    }

    private ArrayList<InspectItem> adaptDatasFromJsonArray(JSONArray jsonArray) {
        if (null == jsonArray || jsonArray.length() == 0) {
            return null;
        }
        ArrayList<InspectItem> datas = new ArrayList<InspectItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
            /*InspectItem item = new InspectItem();
            cloneParentItem(item);
            datasTemp.add(InspectItemAdapter.adaptIncreaseItem(jsonArray.optJSONObject(i)));
            item.setChilds(datasTemp);
            datas.add(item);*/
            InspectItem item = InspectItemAdapter.adaptIncreaseItem(jsonArray.optJSONObject(i));
            datas.add(item);
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
    

    private void cloneClildItem(InspectItem item,boolean isAliasIncrease) {
        if (null != mDatas.get(0)) {
            if (isAliasIncrease) {
                item.setAlias(mDatas.get(0).getAlias()+IncreaseNum);
                item.setName(mDatas.get(0).getName()+IncreaseNum);
                IncreaseNum++;
            }else{
                item.setAlias(mDatas.get(0).getAlias());
                item.setName(mDatas.get(0).getName());
            }
            item.setDefaultValue(mDatas.get(0).getDefaultValue());
            item.setGeoType(mDatas.get(0).getGeoType());
            item.setIncrease(mDatas.get(0).isIncrease());
            item.setLongText(mDatas.get(0).isLongText());
            item.setRequired(mDatas.get(0).isRequired());
            item.setSelectValues(mDatas.get(0).getSelectValues());
            item.setType(mDatas.get(0).getType());
            item.setValue(mDatas.get(0).getValue());
        }
    }

    private class MyAddListViewOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < mDatas.size()) {
                Intent intent = new Intent(EarthQuakeIncreaseReportActivity.this, CustomChildReportActivity1.class);
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
                AlertView dialog = new AlertView(EarthQuakeIncreaseReportActivity.this, getString(R.string.dialog_title_prompt), getString(R.string.event_report_delete_item_tips),
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

    private class MyEditTextListener implements TextWatcher {
        private InspectItem item;

        public MyEditTextListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no logic to do.
        }

        @Override
        public void afterTextChanged(Editable s) {
            item.setValue(s.toString());
        }
    }

}
