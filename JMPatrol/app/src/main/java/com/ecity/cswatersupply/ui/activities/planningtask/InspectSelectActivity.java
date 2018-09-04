package com.ecity.cswatersupply.ui.activities.planningtask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectSelectListAdapter;
import com.ecity.cswatersupply.model.checkitem.ChildrenItem;
import com.ecity.cswatersupply.model.checkitem.Group;
import com.ecity.cswatersupply.model.checkitem.GroupItem;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

public class InspectSelectActivity extends BaseActivity {
    private ExpandableListView listView;
    //适配器
    private InspectSelectListAdapter listAdapter;
    //所有的分组信息
    private List<GroupItem> dataList = new ArrayList<GroupItem>();
    private ArrayList<Group> allGroups;
    private CustomTitleView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_select);
        initUI();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        Bundle attrBundle = getIntent().getExtras();
        allGroups = (ArrayList<Group>) attrBundle.getSerializable("groups");
        getDataList();
        listAdapter = new InspectSelectListAdapter(InspectSelectActivity.this, dataList);
        listView.setAdapter(listAdapter);
    }

    private void getDataList() {
        for (Group group : allGroups) {
            if (ListUtil.isEmpty(group.getGroups())) {
                GroupItem item = new GroupItem(group.getName(), group.getAlias(), true, null);
                dataList.add(item);
            } else {
                List<ChildrenItem> childrenItems = new ArrayList<ChildrenItem>();
                for (Group childGroup : group.getGroups()) {
                    ChildrenItem item = new ChildrenItem(childGroup.getName(), childGroup.getAlias(), true);
                    childrenItems.add(item);
                }
                GroupItem item = new GroupItem(group.getName(), group.getAlias(), true, childrenItems);
                dataList.add(item);
            }
        }
    }

    @SuppressLint("NewApi")
    private void initUI() {
        title = (CustomTitleView) findViewById(R.id.customTitleView);
        title.setTitleText(this.getResources().getString(R.string.inspect_screen_title));
        title.setBtnStyle(CustomTitleView.BtnStyle.RIGHT_ACTION);
        title.tv_title.setBackground(ResourceUtil.getDrawableResourceById(R.drawable.selector_titleview_titletxt_shape));
        title.tv_rightSingle.setText(R.string.ok);

        listView = (ExpandableListView) findViewById(R.id.el_inspects);
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    public void onActionButtonClicked(View v) {
        ArrayList<Group> didSelectedGroups = getDidSelectedGroupList();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bundle.putSerializable(CustomViewInflater.INTENT_GROUPS, didSelectedGroups);
        intent.putExtras(bundle);
        setResult(CustomViewInflater.GROUPS, intent);
        finish();
    }

    public ArrayList<Group> getDidSelectedGroupList() {
        ArrayList<Group> didSelectedGroups = new ArrayList<Group>();
        //得到被选择的组
        Map<String, Integer> groupCheckedStateMap = listAdapter.getGroupCheckedStateMap();
        //得到被选择的每个分组
        List<String> checkedChildren = listAdapter.getCheckedChildren();
        if (ListUtil.isEmpty(checkedChildren)) {

        }
        for (Group group : allGroups) {
            if (ListUtil.isEmpty(group.getGroups())) {
                int state = groupCheckedStateMap.get(group.getName());
                switch (state) {
                    case 1:
                        group.setVisible(true);
                        break;
                    case 3:
                        group.setVisible(false);
                        break;
                    default:
                        break;
                }
                didSelectedGroups.add(group);
            } else {
                int state = groupCheckedStateMap.get(group.getName());
                switch (state) {
                    case 1:
                        group.setVisible(true);
                        ArrayList<Group> itemGroups_one = (ArrayList<Group>) group.getGroups();
                        for (Group item : itemGroups_one) {
                            item.setVisible(true);
                        }
                        break;
                    case 2:
                        //此种情况比较复杂  每个组包含被选中的项和没有被选中的项
                        group.setVisible(true);
                        ArrayList<Group> itemGroups_twe = (ArrayList<Group>) group.getGroups();
                        for (Group item : itemGroups_twe) {
                            if (checkedChildren.contains(item.getName())) {
                                item.setVisible(true);
                            }else{
                                item.setVisible(false);
                            }
                        }
                        break;
                    case 3:
                        group.setVisible(false);
                        ArrayList<Group> itemGroups_three = (ArrayList<Group>) group.getGroups();
                        for (Group item : itemGroups_three) {
                            item.setVisible(false);
                        }
                        break;
                    default:
                        break;
                }
                didSelectedGroups.add(group);
            }
        }

        return didSelectedGroups;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
