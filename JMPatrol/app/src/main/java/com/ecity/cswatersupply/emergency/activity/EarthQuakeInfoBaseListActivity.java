package com.ecity.cswatersupply.emergency.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.emergency.adapter.ExpertAdapter;
import com.ecity.cswatersupply.emergency.adapter.QuakeInfoListAdapter;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoDistributionOperaterXtd;
import com.ecity.cswatersupply.emergency.menu.QuakeInfoSearchOperater;
import com.ecity.cswatersupply.emergency.menu.UnUsualReportOperater;
import com.ecity.cswatersupply.emergency.model.EarthQuakeInfoModel;
import com.ecity.cswatersupply.emergency.model.EmergencyItem;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.model.SearchType;
import com.ecity.cswatersupply.emergency.network.request.GetEarthQuakeParameter;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.emergency.utils.CommonTool;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.RequestCode;
import com.ecity.cswatersupply.model.ResultCode;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.ui.activities.CustomReportActivity1;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.CustomViewInflater;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.utils.UIHelper;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/***
 * 地震信息列表
 * @author hzb
 */
public class EarthQuakeInfoBaseListActivity extends BaseActivity{
    private ListView ltv_download;
    private List<EmergencyItem> models;
    private ExpertAdapter adapter;
    // private List<EmergencyItem> list;
    //EmergencyPlanModel
    private List<EmergencyItem> list;
    private final static int GET_DATA_SUCCEED = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourXmlLayput());
        initView();
        list = new ArrayList<EmergencyItem>();
        initData();
    }

    public Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            ltv_download = (ListView)findViewById(R.id.ltv_download);
            adapter = new ExpertAdapter(EarthQuakeInfoBaseListActivity.this,list);
            ltv_download.setAdapter(adapter);
            ltv_download.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EmergencyItem item = list.get(position);
                    String detailUrl = item.getDetailUrl();
                    Intent intent = new Intent(EarthQuakeInfoBaseListActivity.this, DetailWebActivity.class);
                    intent.putExtra("url", detailUrl);
                    startActivity(intent);
                }
            });
            LoadingDialogUtil.dismiss();
        }
    };

    private void initView(){
        CustomTitleView titleView = (CustomTitleView)findViewById(R.id.customTitleView);
        titleView.setVisibility(View.VISIBLE);
        titleView.setTitleText("地震信息");
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {//http://www.csi.ac.cn/   <a[^>]*>([^<]*)<a>
                String result = CommonTool.getRequest(ServiceUrlManager.GetNewsBulletinBoardService(), "utf-8");
                String a = "<a href=\"([^\"]*)\">([^\"]*)</a><span>([^</span>]*)</span>";
                parseHtmlData(result, a);

                mHandler.sendMessage(mHandler.obtainMessage(GET_DATA_SUCCEED, list));

            }

        }).start();
    }

    private void parseHtmlData(String result, String format) {
        if(StringUtil.isBlank(result) || StringUtil.isBlank(format) ) {
            return;
        }

        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            EmergencyItem model = new EmergencyItem();
            //发生的时间和地名
            model.setDetailUrl(matcher.group(1).trim());
            //具体的时间
            model.setImgurl(matcher.group(2).trim());
            // 具体的地址
            model.setTitle(matcher.group(3).trim());
            list.add(model);
        }
    }

    /**
     * EventBus methods begin.
     */

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.TOAST:
                if (event.isForTarget(this)) {
                    ToastUtil.showShort(event.getMessage());
                }
                break;
            case UIEventStatus.NOTIFICATION_EXPERTOPINION:
                parsingDatas(event);
                break;
            default:
                break;
        }
    }
    private void parsingDatas(UIEvent event) {
        models = new ArrayList<EmergencyItem>();
        if (null == event) {
            return;
        }
        List<EmergencyPlanModel> data = event.getData();
        for (int i = 0; i < data.size(); i++) {
            EmergencyItem tmp = new EmergencyItem();
            tmp.setTitle(data.get(i).getName());
            tmp.setDetailUrl(data.get(i).getCreatetime());
            tmp.setImgurl(data.get(i).getDescribe());
            models.add(tmp);
        }
    }
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
    }
    protected int getResourXmlLayput() {
        return R.layout.fragment_downloadlistview;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
