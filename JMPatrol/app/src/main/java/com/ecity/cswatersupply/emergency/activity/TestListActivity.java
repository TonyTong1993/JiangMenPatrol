package com.ecity.cswatersupply.emergency.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.adapter.QuakeInfoListAdapter;
import com.ecity.cswatersupply.emergency.network.response.GetSelectTestResponse;
import com.ecity.cswatersupply.emergency.network.response.SelectModel;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;
import com.zzz.ecity.android.applibrary.view.CircleTextImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/12.
 */

public class TestListActivity extends BaseActivity {
    List<String> typeList;
    List<String> displayList;
    ListView lv_record;
    BaseAdapter adapter;
    Map<String, List<String>> map1,map2;
    List<String> deptList;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_information);
        EventBusUtil.register(this);
        initView();
        request();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unregister(this);
        super.onDestroy();
    }

    private void initView() {
        displayList = new ArrayList<>();

        MapActivityTitleView viewById = (MapActivityTitleView)findViewById(R.id.view_title);
        viewById.setBtnStyle(MapActivityTitleView.BtnStyle.ONLY_CONFIRM);
        viewById.setOnActionButtonClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page--;
                if (page==1){
                    displayList = typeList;
                }else if(page==2){
                    displayList = deptList;
                }
                adapter.notifyDataSetChanged();
                if (page==0){
                    page=1;
                }
            }
        });

        PullToRefreshListView pullList = (PullToRefreshListView) findViewById(R.id.pull_listview);
        lv_record = pullList.getRefreshableView();
        lv_record.setDivider(ResourceUtil.getDrawableResourceById(R.drawable.shape_list_divider));
        lv_record.setDividerHeight(ResourceUtil.getDimensionPixelSizeById(R.dimen.margin_spacing_level_4));

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return displayList.size();
            }

            @Override
            public Object getItem(int position) {
                return displayList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (null == convertView) {
                    convertView = LayoutInflater.from(TestListActivity.this).inflate(R.layout.item_list_quakeinfo, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                String str = displayList.get(position);
                holder.region.setText(str);

                return convertView;
            }
        };

        lv_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (page == 1) {
                     deptList = map1.get(displayList.get(position));
                    displayList = deptList;
                    adapter.notifyDataSetChanged();
                    page++;
                }else if(page==2){
                    List<String> nameList = map2.get(displayList.get(position));
                    displayList = nameList;
                    adapter.notifyDataSetChanged();
                    page++;
                }else {

                }
            }
        });
    }

    private class ViewHolder {
        private CircleTextImageView degree;
        private TextView region;
        private TextView time;
        private TextView difftime;

        private ViewHolder(View view) {
            this.degree = (CircleTextImageView) view.findViewById(R.id.tv_degree);
            this.region = (TextView) view.findViewById(R.id.tv_region);
            this.time = (TextView) view.findViewById(R.id.tv_time);
            this.difftime = (TextView) view.findViewById(R.id.tv_difftime);
        }
    }

    private void request() {
        LoadingDialogUtil.show(this, "正在查询");
//        EmergencyService.getInstance().getSelectPeopleList();
    }

    private void handle(ResponseEvent event) {
        GetSelectTestResponse data = event.getData();
        List<SelectModel> resultList = data.getResultList();

        List<String> list1 = null;
        List<String> list2 = null;
        map1 = new HashMap<>();
        map2 = new HashMap<>();
        for (SelectModel model : resultList) {
            if (!map1.containsKey(model.getType())) {
                list1 = new ArrayList<>();
            } else {
                list1 = map1.get(model.getType());
            }
            if (!list1.contains(model.getDept())){
                list1.add(model.getDept());
            }
            map1.put(model.getType(), list1);

            if (!map2.containsKey(model.getDept())) {
                list2 = new ArrayList<>();
            } else {
                list2 = map2.get(model.getDept());
            }
            if (!list2.contains(model.getName())){
                list2.add(model.getName());
            }
            map2.put(model.getDept(), list2);
        }
        String s = map1.toString();
        String s1 = map2.toString();
        typeList = new ArrayList<>();
        Set<String> typeSet = map1.keySet();
        for (String str : typeSet) {
            typeList.add(str);
        }

        displayList = typeList;
        lv_record.setAdapter(adapter);
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_REFUGE_INFO_1:
                LoadingDialogUtil.dismiss();
                handle(event);
                break;

            default:
                break;
        }
    }
}
