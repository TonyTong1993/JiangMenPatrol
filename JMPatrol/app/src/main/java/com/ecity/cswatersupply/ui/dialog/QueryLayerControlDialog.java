package com.ecity.cswatersupply.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.QueryLayerAdapter;
import com.ecity.cswatersupply.model.metaconfig.DbMetaNet;
import com.ecity.cswatersupply.model.metaconfig.QueryDbMetaNet;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.ui.activities.LoginSettingActivity;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.SettingsManager;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryLayerControlDialog extends Dialog {
    private Context context;
    private ListView layerListView;
    private Button positiveBtn, negativeBtn;
    private ArrayList<String> dbMetaNames;
    private List<QueryDbMetaNet> currentDbMetaNetList;
    private Map<String, List<QueryDbMetaNet>> dbMetaNetMap;
    private QueryLayerAdapter adapter;
    private OnDialogBtnClickListener listener;
    private ArrayList<Integer> ids;
    private LinearLayout mLlSpinnerEnvironment;
    private TextView mTvEnvironment;
    private CustomSpinner mCustomSpinner;
    private MyEnvironmentOnSpinnerListener mEnvironmentSpinnerListener;

    public interface OnDialogBtnClickListener {
        public void onback(boolean result, ArrayList<Integer> ids);
    }


    public QueryLayerControlDialog(Context context, OnDialogBtnClickListener listener) {
        super(context, R.style.customDialog);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_query_layers);

        layerListView = (ListView) findViewById(R.id.layerList);
        positiveBtn = (Button) findViewById(R.id.custom_dialog_ok);
        negativeBtn = (Button) findViewById(R.id.custom_dialog_cancel);
        mLlSpinnerEnvironment = (LinearLayout) findViewById(R.id.ll_spinner_environment);
        mTvEnvironment = (TextView) findViewById(R.id.tv_environment);
        ids = new ArrayList<>();
        dbMetaNames = new ArrayList<String>();
        currentDbMetaNetList = new ArrayList<>();
        dbMetaNetMap = new HashMap<String, List<QueryDbMetaNet>>();

        adapter = new QueryLayerAdapter(context);
        layerListView.setAdapter(adapter);
        getLayerList();
        initSpn();
        if(!ListUtil.isEmpty(dbMetaNames)) {
            currentDbMetaNetList = dbMetaNetMap.get(dbMetaNames.get(0));
            adapter.setList(currentDbMetaNetList);
        }

        layerListView.setOnItemClickListener(new MyItemClickListener());
        positiveBtn.setOnClickListener(clickOkListener);
        negativeBtn.setOnClickListener(mDismissListener);
    }

    private void getLayerList() {
        Map<String, List<DbMetaNet>> dbMetaInfos = QueryLayerIDs.getInstance().getDbMetaNets();
        for (Map.Entry<String, List<DbMetaNet>> entry : dbMetaInfos.entrySet()) {
            dbMetaNames.add(entry.getKey());
            List<DbMetaNet> nets = entry.getValue();
            List<QueryDbMetaNet> queryNets = new ArrayList<QueryDbMetaNet>();
            for (DbMetaNet net : nets) {
                QueryDbMetaNet layerMeta = new QueryDbMetaNet();
                layerMeta.setLayerid(net.getLayerid());
                layerMeta.setDid(net.getDid());
                layerMeta.setDname(net.getDname());
                layerMeta.setClsname(net.getClsname());
                layerMeta.setDalias(net.getDalias());
                queryNets.add(layerMeta);
            }
            dbMetaNetMap.put(entry.getKey(), queryNets);
        }
    }

    private void initSpn() {
        mLlSpinnerEnvironment = (LinearLayout) findViewById(R.id.ll_spinner_environment);
        mTvEnvironment = (TextView) findViewById(R.id.tv_environment);
        mEnvironmentSpinnerListener = new MyEnvironmentOnSpinnerListener();
        mCustomSpinner = new CustomSpinner(context, mLlSpinnerEnvironment, mTvEnvironment, mEnvironmentSpinnerListener, dbMetaNames, 0);
    }

    @Override
    public void show() {
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            lp.width = height / 10 * 8;
        } else {
            lp.width = width / 10 * 8;
        }
        mWindow.setAttributes(lp);
        super.show();
    }

    private View.OnClickListener clickOkListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            QueryLayerControlDialog.this.dismiss();
            listener.onback(true, ids);
        }
    };

    private View.OnClickListener mDismissListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            QueryLayerControlDialog.this.dismiss();
            listener.onback(false, null);
        }
    };

    private class MyItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            QueryDbMetaNet net = currentDbMetaNetList.get(position);
            if (net.isSelected()) {
                net.setSelected(false);
            } else {
                net.setSelected(true);
            }
            ids.add(net.getLayerid());
            adapter.notifyDataSetChanged();
        }
    }

    private class MyEnvironmentOnSpinnerListener implements CustomSpinner.OnSpinnerListener {

        @Override
        public void back(int position) {
            String dbMetaName = dbMetaNames.get(position);
            currentDbMetaNetList = dbMetaNetMap.get(dbMetaName);
            adapter.setList(currentDbMetaNetList);
            adapter.notifyDataSetChanged();
        }
    }

}
