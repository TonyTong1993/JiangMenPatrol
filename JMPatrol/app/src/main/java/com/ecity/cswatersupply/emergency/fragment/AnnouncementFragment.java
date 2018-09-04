package com.ecity.cswatersupply.emergency.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.activity.NoticeDetailActivity;
import com.ecity.cswatersupply.emergency.adapter.NoticeListAdapter;
import com.ecity.cswatersupply.emergency.network.response.GetNoticeListResponse;
import com.ecity.cswatersupply.emergency.network.response.NoticeModel;
import com.ecity.cswatersupply.emergency.service.EmergencyService;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.shizhefei.fragment.LazyFragment;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @author 49136
 *         公告
 */

public class AnnouncementFragment extends LazyFragment {
    public static final String INTENT_KEY_GID = "INTENT_KEY_GID";

    private static int pageNo = 1;
    private static int pageSize = 1000;
    private ListView lv_notice;
    private NoticeListAdapter adapter;
    private List<NoticeModel> features;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_downloadlistview);
        EventBusUtil.register(this);
        initView();
    }

    private void initView() {

        lv_notice = (ListView) findViewById(R.id.ltv_download);
        adapter = new NoticeListAdapter(getActivity());
        lv_notice.setAdapter(adapter);

        requestNoticeList();

        setOnClickListener();

//        String html = "<html> <body> <p>\\n    <img id=\\\"44024737\\\" alt=\\\"\\\" src=\\\"http://imgsports.gmw.cn/attachement/jpg/site2/20161226/f44d305ea65019cab22242.jpg\\\" title=\\\"11大明星跑龙套瞬间：周星驰欧阳震华出现在同一部剧\\\"/>\\n</p> </body> </html>";
        final String html = "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p style=\"margin-top: 12px; margin-bottom: 12px; padding: 0px; font-family: 微软雅黑; white-space: normal; background-color: rgb(255, 255, 255);\">“老一辈”明星，在大红大紫之前，基本上都是从跑龙套开始的。</p>\n" +
                "<p style=\"margin-top: 12px; margin-bottom: 12px; padding: 0px; font-family: 微软雅黑; white-space: normal; background-color: rgb(255, 255, 255);\">　　周星驰是从TVB的训练班毕业的，因此在不少剧中也跑过龙。在1982年的《苏乞儿》里，只是一个默默无闻站后面的甲乙丙丁▼</p>\n" +
                "<p style=\"margin-top: 12px; margin-bottom: 12px; padding: 0px; font-family: 微软雅黑; white-space: normal; background-color: rgb(255, 255, 255);\">\n" +
                "    <img id=\"44024737\" alt=\"\" src=\"http://imgsports.gmw.cn/attachement/jpg/site2/20161226/f44d305ea65019cab22242.jpg \n" +
                "\n" +
                "\" title=\"11大明星跑龙套瞬间：周星驰欧阳震华出现在同一部剧\"/>\n" +
                "</p>\n" +
                "<p style=\"margin-top: 12px; margin-bottom: 12px; padding: 0px; font-family: 微软雅黑; white-space: normal; background-color: rgb(255, 255, 255);\">　\n" +
                "    　<br/>\n" +
                "</p>\n" +
                "<p>\n" +
                "    <br/>\n" +
                "</p>\n" +
                "</body>\n" +
                "</html>\n";
        Spanned sp = Html.fromHtml(html, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
                InputStream is = null;
                try {
                    is = (InputStream) new URL(source).getContent();
                    Drawable d = Drawable.createFromStream(is, "src");
                    d.setBounds(0, 0, d.getIntrinsicWidth(),
                            d.getIntrinsicHeight());
                    is.close();
                    return d;
                } catch (Exception e) {
                    return null;
                }
            }
        }, null);
//        view1.loadUrl("http://192.168.8.229:8080/web/module/JMProject/ProcessManage/test.html");
    }

    private void requestNoticeList() {
        User currentUser = HostApplication.getApplication().getCurrentUser();
        LoadingDialogUtil.show(getActivity(), "正在查询消息通告");
        EmergencyService.getInstance().getNoticeList(currentUser, pageNo, pageSize);
    }

    private void handleGetNoticeList(ResponseEvent event) {
        GetNoticeListResponse data = event.getData();
        features = data.getFeatures();
        adapter.setList(features);
    }

    private void setOnClickListener() {
        lv_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra(INTENT_KEY_GID, features.get(position).getGid());
                startActivity(intent);
            }
        });
    }


    //    /**
//     * EventBus methods begin.
//     */
//    
//    public void onEventMainThread(UIEvent event) {
//        switch (event.getId()) {
//            case UIEventStatus.TOAST:
//                if (event.isForTarget(this)) {
//                    ToastUtil.showShort(event.getMessage());
//                }
//                break;
//            case UIEventStatus.NOTIFICATION_EXPERTOPINION:
//                parsingDatas(event);
//                break;
//            default:
//                break;
//        }
//    }
//    
//    
//
    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }
        switch (event.getId()) {
            case ResponseEventStatus.EMERGENCY_GET_NOTICE_LIST:
                LoadingDialogUtil.dismiss();
                handleGetNoticeList(event);
                break;

            default:
                break;
        }
    }
//    
}
