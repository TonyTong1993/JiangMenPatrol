package com.ecity.cswatersupply.ui.widght;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.map.AMapMenu;
import com.ecity.cswatersupply.menu.map.AMapMenuCommand;
import com.ecity.cswatersupply.menu.map.EmptyMapCommandXtd;
import com.ecity.cswatersupply.menu.map.TabMenuTools;
import com.ecity.cswatersupply.ui.activities.MapActivity;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.esri.android.map.MapView;
import com.z3app.android.util.StringUtil;

/**
 * 
 * @author SunShan'ai
 *
 */
public class CustomPopupToolView {
    private List<TabMenuTools> tabMenus;//TabMenus contains AppMenus
    private ViewPager viewPager;
    private LayoutInflater inflater;
    private PopupWindow popupWindow;
    private List<View> viewLists;
    private TextView tvTool1;
    private View viewRoot;
    private ColorStateList csl1;
    private ColorStateList csl2;
    private MapView mapView;
    private MapActivity mapActivity;

    public interface onChangePage {
        void changePage(int pageNum);
    }

    public CustomPopupToolView(MapView mapView,MapActivity mapActivity) {
        this.mapView = mapView;
        this.mapActivity = mapActivity;
        inflater = LayoutInflater.from(mapActivity);
        tabMenus = new ArrayList<TabMenuTools>();
        viewLists = new ArrayList<View>();
        analyzeMenuXML();
        viewRoot = inflater.inflate(R.layout.view_map_popup_menu, null);
        viewPager = (ViewPager) viewRoot.findViewById(R.id.viewPager_tools);
        viewPager.setFocusableInTouchMode(true);
        viewPager.setFocusable(true);
        initTabPage();
        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.addOnPageChangeListener(new MyViewPagerOnChangeListener());
        popupWindow = new PopupWindow(viewRoot, LayoutParams.WRAP_CONTENT, mapActivity.getResources().getDimensionPixelSize(R.dimen.map_popmenu_h));
    }

    public void show(View parent, Context context) {
        popupWindow.setWidth(getScreenWidth() - 25);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.css_popup_menu_background));
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, parent.getHeight() + 20);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        mapActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        return screenW;
    }

    private void initTabPage() {
        if (null != tabMenus) {
            csl1 = mapActivity.getResources().getColorStateList(R.color.map_popup_tab);
            csl2 = mapActivity.getResources().getColorStateList(R.color.font_level_4);
            for (int i = 0; i < tabMenus.size(); i++) {
                viewLists.add(inflater.inflate(R.layout.view_popup_menu_grid, null));
                switch (i) {
                    case 0:
                        tvTool1 = (TextView) viewRoot.findViewById(R.id.tv_tool1);
                        tvTool1.setOnClickListener(new MyTextViewOnClickListener());
                        tvTool1.setText(tabMenus.get(0).getName());
                        tvTool1.setTextColor(csl1);
                        break;
                    default:
                        break;
                }

            }
        }
    }

    private class MyTextViewOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_tool1:
                    viewPager.setCurrentItem(0);
                    tvTool1.setTextColor(csl1);
                    break;
                default:
                    break;
            }
        }

    }

    private class MyViewPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewLists.get(position);
            GridView gridView = (GridView) view.findViewById(R.id.myGridView);
            final List<AMapMenu> menus = tabMenus.get(position).getMenus();
            gridView.setAdapter(new MyGridViewAdapter(menus));
            gridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    menus.get(position).execute(mapView,mapActivity);
                    dismiss();
                }
            });
            container.addView(view);

            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewLists.get(position));
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    private class MyViewPagerOnChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO no logic to do
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO no logic to do
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    tvTool1.setTextColor(csl1);
                    break;
                default:
                    break;
            }
        }
    }

    private class MyGridViewAdapter extends BaseAdapter {
        private List<AMapMenu> items;

        public MyGridViewAdapter(List<AMapMenu> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items == null ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder myHolder = null;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.view_popup_menu_grid_item, null);
                myHolder = new ViewHolder(convertView);
                convertView.setTag(myHolder);
            } else {
                myHolder = (ViewHolder) convertView.getTag();
            }
            myHolder.iv.setImageResource(ResourceUtil.getDrawableResourceId(items.get(position).getIconName()));
            myHolder.tv.setText(items.get(position).getName());

            return convertView;
        }

        private class ViewHolder {
            private ImageView iv;
            private TextView tv;

            public ViewHolder(View view) {
                iv = (ImageView) view.findViewById(R.id.imgbtn_img);
                tv = (TextView) view.findViewById(R.id.imgbtn_text);
            }
        }
    }

    private void analyzeMenuXML() {
        try {
            XmlResourceParser parser = mapActivity.getResources().getXml(R.xml.map_btns);
            int eventType = parser.getEventType();
            TabMenuTools tabMenu = null;
            List<AMapMenu> menuLists = null;
            AMapMenu appMenu = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Category".equalsIgnoreCase(tagName)) {
                            tabMenu = new TabMenuTools(null, null, false);
                            tabMenu.setName(parser.getAttributeValue(null, "name"));
                            menuLists = new ArrayList<AMapMenu>();
                        } else if ("Button".equalsIgnoreCase(tagName)) {
                            appMenu = new AMapMenu();
                        } else if ("Name".equalsIgnoreCase(tagName)) {
                            appMenu.setName(parser.nextText());
                        } else if ("IconName".equalsIgnoreCase(tagName)) {
                            appMenu.setIconName(parser.nextText());
                        } else if ("Command".equalsIgnoreCase(tagName)) {
                            appMenu.setMapMenuCommand(getAMapMenuCommand(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Button".equalsIgnoreCase(tagName)) {
                            if (appMenu != null) {
                                menuLists.add(appMenu);
                            }
                            appMenu = null;
                        } else if ("Category".equalsIgnoreCase(tagName)) {
                            if (menuLists != null && tabMenu != null) {
                                tabMenu.setMenus(menuLists);
                                tabMenus.add(tabMenu);
                            }
                            menuLists = null;
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(this, e);
        }

    }

    private AMapMenuCommand getAMapMenuCommand(String text) {
        if (StringUtil.isEmpty(text)) {
            return new EmptyMapCommandXtd();
        }
        AMapMenuCommand command = null;
        try {
            command = (AMapMenuCommand) Class.forName(text).newInstance();
            return command;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(this, e);
        }

        return new EmptyMapCommandXtd();
    }

}
