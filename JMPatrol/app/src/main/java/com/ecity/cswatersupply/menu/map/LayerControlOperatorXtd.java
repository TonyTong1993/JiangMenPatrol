package com.ecity.cswatersupply.menu.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.EQModuleConfig;
import com.ecity.cswatersupply.emergency.EarthQuakeDisplayUtil;
import com.ecity.cswatersupply.network.response.loginresponse.MobileConfig;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.RestoreManager;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledServiceLayer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

public class LayerControlOperatorXtd extends AMapMenuCommand {

    private static LayerControlOperatorXtd instanceControlMenu;
    private LayerControlListAdapter layerControlListAdapter = null;
    private MapView mapView;
    private IMapOperationContext mapFragment;
    private PopupWindow popupWindow = null;
    public boolean isshowing = false;
    public boolean[] isChecked;

    enum loadType {
        ONLINE, OFFLINE
    }

    public static LayerControlOperatorXtd getInstance() {
        if (null == instanceControlMenu) {
            instanceControlMenu = new LayerControlOperatorXtd();
        }
        return instanceControlMenu;
    }

    public void ClearPopwindow() {
        if (null == popupWindow) {
            return;
        }
        popupWindow.dismiss();
    }

    @Override
    public boolean execute(MapView mapView, IMapOperationContext fragment) {
        this.mapFragment = fragment;
        this.mapView = mapView;
        List<String> sourceList = getMapLayers();
        if (null == sourceList) {
            return false;
        }
        // 弹出图层选择窗口
        if (isshowing) {
            popupWindow.dismiss();
        } else {
            showWindow(sourceList);
        }
        return true;
    }

    private List<String> getMapLayers() {
        List<String> sourceList = new ArrayList<String>();
        MobileConfig mobileConfig = RestoreManager.getInstance().restoreMobileMapConfig();
        if (ListUtil.isEmpty(mobileConfig.getSourceConfigArrayList())) {
            return null;
        }

        Layer[] layers = mapView.getLayers();

        if(null == layers ) {
            return null;
        }

        try {
            for (int index = 0; index < layers.length; index++) {
                Layer layer = layers[index];
                String layerName = String.valueOf(layer.getName());
                boolean visible = layer.isVisible();
                if (layer instanceof ArcGISDynamicMapServiceLayer) {
                    sourceList.add(layerName + "#" + layerName + "#" + visible);
                } else if (layer instanceof TiledServiceLayer) {
                    sourceList.add(layerName + "#" + layerName + "#" + visible);
                } else if (layer instanceof ArcGISTiledMapServiceLayer) {
                    sourceList.add(layerName + "#" + layerName + "#" + visible);
                } else if (layer instanceof ArcGISLocalTiledLayer) {
                    sourceList.add(layerName + "#" + layerName + "#" + visible);
                } else if(layer instanceof GraphicsLayer) {
                    if(EQModuleConfig.EQLAYERNAME.equalsIgnoreCase(layerName)){
                        sourceList.add(layerName + "#" + EarthQuakeDisplayUtil.getEarthQuickLayerName() + "#" + visible);
                    } else if(EQModuleConfig.STATIONLAYERNAME.equalsIgnoreCase(layerName)){
                        sourceList.add(layerName + "#" + EarthQuakeDisplayUtil.getStationLayerName() + "#" + visible);
                    } else if(EQModuleConfig.REFUGENAME.equalsIgnoreCase(layerName)){
                        sourceList.add(layerName + "#" + EarthQuakeDisplayUtil.getRefugeLayerName() + "#" + visible);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mapFragment.getContext(), mapFragment.getContext().getString(R.string.no_spatial_layer), Toast.LENGTH_LONG).show();
            return null;
        }
        return sourceList;
    }

    @SuppressWarnings("deprecation")
    private void showWindow(List<String> layerSource) {
        DisplayMetrics dm = new DisplayMetrics();
        mapFragment.getContext().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mapFragment.getContext()).inflate(R.layout.dialog_layercontrol, null);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(linearLayout, screenWidth, LayoutParams.WRAP_CONTENT);

        }
        ListView listView = (ListView) linearLayout.findViewById(R.id.layerList);
        layerControlListAdapter = new LayerControlListAdapter(mapFragment.getContext(), layerSource);
        listView.setAdapter(layerControlListAdapter);
        layerControlListAdapter.notifyDataSetChanged();

        // 点击外部时，弹窗消失
        popupWindow.setOutsideTouchable(true);
        // 点击返回键时，弹窗消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //
        popupWindow.setFocusable(true);
        // 设置弹出和消失动画
        popupWindow.setAnimationStyle(R.style.layercontrolwin_anim_style);
        // 设置背景Activity半透明
        /*
         * WindowManager.LayoutParams lParams =
         * mainFrame.getWindow().getAttributes(); lParams.alpha = 0.5f;
         * mainFrame.getWindow().setAttributes(lParams);
         */
        // 关闭时，修改isShowing的值
        popupWindow.setOnDismissListener(onDismissListener);
        popupWindow.showAsDropDown(mapFragment.getContext().findViewById(R.id.view_title_map), 5, 30);
        popupWindow.update();
        isshowing = true;

    }

    /**
     * 此方法描述的是：
     * 
     * @author: gaokai
     * @version: 2016年1月13日 下午3:34:08
     */
    OnDismissListener onDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            isshowing = false;

        }
    };

    class LayerControlListAdapter extends BaseAdapter {
        private List<String> mSourceList;
        private LayoutInflater mInflater = null;
        private String[] nameArrays;
        private String[] dispNameArrays;

        public LayerControlListAdapter(Context mContext, List<String> sourceList) {
            super();
            this.mSourceList = sourceList;
            int size  = mSourceList.size();
            this.nameArrays = new String[size];
            this.dispNameArrays = new String[size];
            isChecked = new boolean[size];
            this.mInflater = LayoutInflater.from(mContext);
            int i = 0;
            for(String layerInfo : mSourceList){
                String[] stringarrays = layerInfo.split("#");
                nameArrays[i] = stringarrays[0];
                dispNameArrays[i] = stringarrays[1];
                isChecked[i] = stringarrays[2].equals("true");
                i++;
            }
        }

        @Override
        public int getCount() {
            return mSourceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSourceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.layer_conrol_listitem, null);
                holder = new ViewHolder();

                holder.imageView = (ImageView) convertView.findViewById(R.id.layer_icon);
                holder.layernameString = (TextView) convertView.findViewById(R.id.layer_name);
                holder.toggleButton = (ToggleButton) convertView.findViewById(R.id.tv_loadType);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            String layernameString = nameArrays[position];
            int resId = R.drawable.navigation_locate;
            if (layernameString.equals("管网")) {
                resId = R.drawable.navigation_locate;
            } else if (layernameString.equals("地形图")) {
                resId = R.drawable.arrived;
            }
            holder.imageView.setBackgroundResource(resId);
            holder.layernameString.setText(dispNameArrays[position]);
            // holder.toggleButton.setOnClickListener(new
            // ButtonClickListener(position, mapView));
            holder.toggleButton.setOnCheckedChangeListener(new ButtonClickListener(nameArrays[position], mapView));
            holder.toggleButton.setChecked(isChecked[position]);
            return convertView;
        }

    }

    class ViewHolder {
        private TextView layernameString;
        private ImageView imageView;
        private ToggleButton toggleButton;
    }

    class ButtonClickListener implements OnCheckedChangeListener {
        private String layerName;
        private MapView mapView;

        public ButtonClickListener(String layerName, MapView mapView) {
            super();
            this.layerName = layerName;
            this.mapView = mapView;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Layer[] layers = mapView.getLayers();
                        for (int index = 0; index < layers.length; index++) {
                            Layer layer = layers[index];
                            String tmpLayerName = String.valueOf(layer.getName());
                            if(tmpLayerName.equalsIgnoreCase(layerName)){
                                layer.setVisible(isChecked);
                                break;
                            }
                        }
                     }catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
