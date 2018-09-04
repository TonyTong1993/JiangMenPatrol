package com.ecity.cswatersupply.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.ConditionAndCodeQueryOperator;
import com.ecity.cswatersupply.menu.map.IMapOperationContext;
import com.ecity.cswatersupply.model.metaconfig.DbMetaInfo;
import com.ecity.cswatersupply.model.metaconfig.DbMetaNet;
import com.ecity.cswatersupply.model.metaconfig.FieldModel;
import com.ecity.cswatersupply.model.metaconfig.QueryLayerIDs;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.esri.android.map.MapView;
import com.zzz.ecity.android.applibrary.view.CustomSpinner;

import java.util.ArrayList;
import java.util.List;

public class ConditionQueryDialog extends Dialog {
    private Context context;
    private TextView dialogTitle;
    private TextView conditionSelect;
    private EditText conditionEdt;
    private TextView deviceTxt, attrTxt, conditionTxt;
    private Button positiveBtn, negativeBtn;
    private LinearLayout TitleLayout;
    private LinearLayout selectDeviceLayout;
    private LinearLayout selectAttrLayout;
    private LinearLayout selectConditionLayout;
    private String deviceType, deviceAttr, querycondition;
    private ArrayList<String> deviceTypes;
    private FieldModel[] fields;
    private String[] conditions;
    private MapView mapView;
    private IMapOperationContext fragment;
    private boolean isDeviceQuery;
    private CustomSpinner deviceSpn;
    private CustomSpinner attrSpn;
    private CustomSpinner conditionSpn;

    public ConditionQueryDialog(Context context, MapView mapView, IMapOperationContext fragment, boolean isDeviceQuery) {
        super(context, R.style.customDialog);
        this.context = context;
        this.mapView = mapView;
        this.fragment = fragment;
        this.isDeviceQuery = isDeviceQuery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_query_layer_control);

        dialogTitle = (TextView) findViewById(R.id.title);
        TitleLayout = (LinearLayout) findViewById(R.id.ll_query_title);
        conditionSelect = (TextView) findViewById(R.id.condition_select);

        selectDeviceLayout = (LinearLayout) findViewById(R.id.ll_select_device);
        selectAttrLayout = (LinearLayout) findViewById(R.id.ll_select_attr);
        selectConditionLayout = (LinearLayout) findViewById(R.id.ll_select_condition);

        deviceTxt = (TextView) findViewById(R.id.spn_value_device);
        attrTxt = (TextView) findViewById(R.id.spn_value_name);
        conditionTxt = (TextView) findViewById(R.id.spn_value_condition);

        conditionEdt = (EditText) findViewById(R.id.condition);
        positiveBtn = (Button) findViewById(R.id.custom_dialog_ok);
        negativeBtn = (Button) findViewById(R.id.custom_dialog_cancel);
        conditions = ResourceUtil.getArrayById(R.array.map_query_conditions);

        if (isDeviceQuery) {
            initDeviceQueryView();
        } else {
            initConditionQueryView();
        }

        positiveBtn.setOnClickListener(new OnQueryBtnClickListener());
        negativeBtn.setOnClickListener(new DismissListener());
    }

    private void initConditionQueryView() {
        TitleLayout.setVisibility(View.GONE);
        dialogTitle.setText(ResourceUtil.getStringById(R.string.map_query_net_line));
        conditionSelect.setText(ResourceUtil.getStringById(R.string.map_query_net_sel_line));

        getFields(getPipeLineDName());

        if(null == fields || 0 == fields.length) {
            ToastUtil.showLong(ResourceUtil.getStringById(R.string.map_query_no_metas));
            return;
        }
        ArrayList<String> attrs = getFieldStrs(fields);
        attrSpn = new CustomSpinner(context, selectAttrLayout, attrTxt, new MySpinnerListener(attrTxt), attrs, 0);
        deviceAttr = attrs.get(0);
        conditionSpn = new CustomSpinner(context, selectConditionLayout, conditionTxt, new MySpinnerListener(conditionTxt), parse2List(conditions), 0);
        querycondition = parse2List(conditions).get(0);
    }

    private void initDeviceQueryView() {
        TitleLayout.setVisibility(View.VISIBLE);
        dialogTitle.setText(ResourceUtil.getStringById(R.string.map_query_net_device));
        conditionSelect.setText(ResourceUtil.getStringById(R.string.map_query_net_sel_device));
        deviceTypes = getDeviceTypes();
        if (null == deviceTypes || ListUtil.isEmpty(deviceTypes)) {
            ToastUtil.showLong(ResourceUtil.getStringById(R.string.map_query_no_metas));
            return;
        }
        getFields(deviceTypes.get(0));
        if(null == fields || 0 == fields.length) {
            ToastUtil.showLong(ResourceUtil.getStringById(R.string.map_query_no_metas));
            return;
        }
        ArrayList<String> attrs = getFieldStrs(fields);
        deviceSpn = new CustomSpinner(context, selectDeviceLayout, deviceTxt, new MySpinnerListener(deviceTxt), deviceTypes, 0);
        deviceType = deviceTypes.get(0);
        attrSpn = new CustomSpinner(context, selectAttrLayout, attrTxt, new MySpinnerListener(attrTxt), attrs, 0);
        deviceAttr = attrs.get(0);
        conditionSpn = new CustomSpinner(context, selectConditionLayout, conditionTxt, new MySpinnerListener(conditionTxt), parse2List(conditions), 0);
        querycondition = parse2List(conditions).get(0);
    }

    private ArrayList<String> getFieldStrs(FieldModel[] models) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < models.length; i++) {
            result.add(models[i].getAlias());
        }
        return result;
    }

    private ArrayList<String> parse2List(String[] items) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < items.length; i++) {
            result.add(items[i]);
        }
        return result;
    }

    private void getFields(String dName) {
        if (null == dName) {
            return;
        }

        List<FieldModel> fieldModels = QueryLayerIDs.getInstance().getFieldByDname(dName);

        if (null != fieldModels) {
            int size = fieldModels.size();
            fields = new FieldModel[size];
            for (int i = 0; i < size; i++) {
                fields[i] = fieldModels.get(i);
            }
        }
    }

    private ArrayList<String> getDeviceTypes() {
        ArrayList<String> deviceTypes = new ArrayList<>();
        List<DbMetaInfo> dbMetaInfos = QueryLayerIDs.getInstance().getDbMetaInfoswithNet();
        for (int i = 0; i < dbMetaInfos.size(); i++) {
            DbMetaInfo tmtinfo = dbMetaInfos.get(i);
            List<DbMetaNet> net = tmtinfo.getNet();
            if (net.size() > 0) {
                for (DbMetaNet tn : net) {
                    if (isDeviceQuery) {
                        if (tn.getGeo_type() == 1) {
                            deviceTypes.add(tn.getDname());
                        }
                    } else {
                        if (tn.getGeo_type() == 0) {
                            deviceTypes.add(tn.getDname());
                        }
                    }
                }
            }
        }
        return deviceTypes;
    }

    private int getGXLayerId() {
        List<DbMetaInfo> dbMetaInfos = QueryLayerIDs.getInstance().getDbMetaInfoswithNet();
        for (int i = 0; i < dbMetaInfos.size(); i++) {
            DbMetaInfo tmtinfo = dbMetaInfos.get(i);
            List<DbMetaNet> net = tmtinfo.getNet();
            if (net.size() > 0) {
                for (DbMetaNet tn : net) {

                    if (tn.getGeo_type() == 0) {
                        return tn.getLayerid();
                    }
                }
            }
        }
        return -1;
    }

    private String getPipeLineDName() {
        List<DbMetaInfo> dbMetaInfos = QueryLayerIDs.getInstance().getDbMetaInfoswithNet();
        for (int i = 0; i < dbMetaInfos.size(); i++) {
            DbMetaInfo tmtinfo = dbMetaInfos.get(i);
            List<DbMetaNet> net = tmtinfo.getNet();
            if (net.size() > 0) {
                for (DbMetaNet tn : net) {
                    if (tn.getGeo_type() == 0) {
                        return tn.getDname();
                    }
                }
            }
        }
        return "";
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

    private class OnQueryBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String condition = conditionEdt.getText().toString();
            String where = "";
            deviceType = deviceTxt.getText().toString();
            deviceAttr = attrTxt.getText().toString();
            if (querycondition.equals(ResourceUtil.getStringById(R.string.map_query_like))) {
                where = deviceAttr + " like '%" + condition + "%'";
            } else {
                if(StringUtil.isBlank(condition)) {
                    ToastUtil.showLong(R.string.please_input_exact_condition);
                    return;
                } else {
                    if (querycondition.equals(ResourceUtil.getStringById(R.string.map_query_equal))) {
                        where = deviceAttr + "=" + condition;
                    } else if (querycondition.equals(ResourceUtil.getStringById(R.string.map_query_larger))) {
                        where = deviceAttr + " > " + condition;
                    } else if (querycondition.equals(ResourceUtil.getStringById(R.string.map_query_smaller))) {
                        where = deviceAttr + " < " + condition;
                    } else if (querycondition.equals(ResourceUtil.getStringById(R.string.map_query_larger_equal))) {
                        where = deviceAttr + " >= " + condition;
                    } else if (querycondition.equals(ResourceUtil.getStringById(R.string.map_query_smaller_equal))) {
                        where = deviceAttr + " <= " + condition;
                    }
                }
            }
            ConditionAndCodeQueryOperator operator = new ConditionAndCodeQueryOperator(where);
            operator.setMapviewOption(mapView, fragment);
            int layerId = -1;
            if (isDeviceQuery) {
                layerId = QueryLayerIDs.getLayerIdbyDname(deviceType);
            } else {
                layerId = getGXLayerId();
            }

            operator.setLayerId(layerId);
            operator.startQuery();
            dismiss();
        }
    }

    private class DismissListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            dismiss();
        }
    }

    private class MySpinnerListener implements CustomSpinner.OnSpinnerListener {
        private TextView textView;

        public MySpinnerListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void back(int position) {
            switch (textView.getId()) {
                case R.id.spn_value_device:
                    deviceType = deviceTypes.get(position);
                    getFields(deviceType);
                    if(null == fields || 0 == fields.length) {
                        ToastUtil.showLong(ResourceUtil.getStringById(R.string.map_query_no_metas));
                        return;
                    }
                    deviceTxt.setText(deviceType);

                    ArrayList<String> attrs = getFieldStrs(fields);
                    attrSpn = new CustomSpinner(context, selectAttrLayout, attrTxt, new MySpinnerListener(attrTxt), attrs, 0);
                    attrTxt.setText(attrs.get(0));
                    deviceAttr = attrs.get(0);
                    break;
                case R.id.spn_value_name:
                    deviceAttr = fields[position].getAlias();
                    attrTxt.setText(deviceAttr);
                    break;
                case R.id.spn_value_condition:
                    querycondition = conditions[position];
                    conditionTxt.setText(querycondition);
                    break;
                default:
                    break;
            }

        }
    }

}
