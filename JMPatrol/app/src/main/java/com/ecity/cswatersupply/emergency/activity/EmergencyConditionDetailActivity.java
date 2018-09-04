package com.ecity.cswatersupply.emergency.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.emergency.model.EarthQuakeQuickReportModel;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class EmergencyConditionDetailActivity extends BaseActivity {

    private EarthQuakeQuickReportModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder_detail_flow_info);
        initData();
        initView();
    }

    public void onBackButtonClicked(View view) {
        this.finish();
    }

    private void initData() {
        data = (EarthQuakeQuickReportModel) getIntent().getSerializableExtra("report_item");
        SessionManager.attachmentUrl = data.getDirectory();
    }

    private void initView() {
        CustomTitleView title = (CustomTitleView) findViewById(R.id.view_tile_flow_info_detail);
        title.setTitleText("情况上报详情");

        LinearLayout layoutContainer = (LinearLayout) findViewById(R.id.ll_container);

        List<InspectItem> items = new ArrayList<>();

        InspectItem item1 = new InspectItem();
        InspectItem item2 = new InspectItem();
        InspectItem item3 = new InspectItem();
        InspectItem item4 = new InspectItem();
        InspectItem item5 = new InspectItem();
        InspectItem item6 = new InspectItem();
        item1.setEdit(false);
        item1.setValue(data.getSurveyPerson());
        item1.setAlias("调查人");
        item1.setVisible(true);
        item1.setType(EInspectItemType.TEXT);
        item2.setEdit(false);
        item2.setValue(data.getSurveyAddress());
        item2.setAlias("调查点");
        item2.setVisible(true);
        item2.setType(EInspectItemType.TEXTEXT);
        item3.setEdit(false);
        item3.setValue(data.getMemo());
        item3.setAlias("调查情况");
        item3.setVisible(true);
        item3.setType(EInspectItemType.TEXTEXT);
        item4.setEdit(false);
        item4.setValue(data.getImageUrl());
        item4.setAlias("照片");
        item4.setVisible(true);
        item4.setType(EInspectItemType.IMAGE);
        item5.setEdit(false);
        item5.setVisible(true);
        item5.setValue(data.getVideoUrl());
        item5.setAlias("视频");
        item5.setType(EInspectItemType.VIDEO);
        item6.setEdit(false);
        item6.setVisible(true);
        item6.setValue(data.getAudioUrl());
        item6.setAlias("音频");
        item6.setType(EInspectItemType.AUDIO);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);


        if (ListUtil.isEmpty(items)) {
//            ToastUtil.showShort(getString(R.string.project_no_detail_info))
            return;
        }

        boolean hasEditableItem = false;
        CustomViewInflater customViewInflater = new CustomViewInflater(this);
        for (int i = 0; i < items.size(); i++) {
            layoutContainer.addView(customViewInflater.inflate(items.get(i)));
            hasEditableItem = (hasEditableItem || items.get(i).isEdit());
        }
    }
}
