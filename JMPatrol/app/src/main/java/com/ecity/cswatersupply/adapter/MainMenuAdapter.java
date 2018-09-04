package com.ecity.cswatersupply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.ui.widght.NotificationBadgeView;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.PropertyChangeManager;
import com.ecity.cswatersupply.xg.model.NotificationType;
import com.z3app.android.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/***
 * 
 * @author SunShan'ai
 *
 */
public class MainMenuAdapter extends BaseAdapter {
    private List<AppMenu> items;
    private LayoutInflater mInflater;
    private Context mContext;
    public NotificationBadgeView badgeView;

    public NotificationBadgeView getBadgeView() {
        return badgeView;
    }
    

    public void setBadgeView(NotificationBadgeView badgeView) {
        this.badgeView = badgeView;
    }

    public MainMenuAdapter(Context context, List<AppMenu> items) {
        this.items = items;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null == items ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_main_menu_grid, null);
        ViewHolder myHolder = new ViewHolder(convertView);
        convertView.setTag(myHolder);
        myHolder.main_menu_item_iconImageView.setBackgroundResource(items.get(position).getMenuIconResourceId());
        myHolder.main_menu_item_nameTextView.setText(items.get(position).getName());
        String subName = items.get(position).getSubName();
        addBadgeView(subName, myHolder);

        return convertView;
    }

    private void addBadgeView(String subName, ViewHolder myHolder) {
        String[] subNamesArray = HostApplication.getApplication().getResources().getStringArray(R.array.main_meun_subnames); 
        List<String> subNames = ListUtil.toList(subNamesArray);
        if (!subNames.contains(subName)) {
            return;
        }

        badgeView = new NotificationBadgeView(mContext, myHolder.main_menu_item_iconImageView);
        // 绑定tag
        String badgeViewTag = "";
        if (subName.equals(getMainMeunSubname(R.string.main_meun_subname_workorder))) {
            badgeViewTag = NotificationType.workOrder.toString();
        } else if (subName.equals(getMainMeunSubname(R.string.main_meun_subname_patrolPlanTask))) {
            badgeViewTag = NotificationType.xj_badge_view_key.toString();
        } else if (subName.equals(getMainMeunSubname(R.string.main_meun_subname_flexflowPlanTask))) {
            badgeViewTag = NotificationType.yh_badge_view_key.toString();
        }
        badgeView.setTag(badgeViewTag);
        int lastCount = PreferencesUtil.getInt(HostApplication.getApplication().getApplicationContext(), badgeViewTag + HostApplication.getApplication().getCurrentUser().getId(), -1);
        PropertyChangeManager.getInstance().addPropertyChangeListener(badgeView);

        // 如果上次有未读任务，显示之
        if (lastCount > 0) {
            badgeView.setText(String.valueOf(lastCount));
            badgeView.show();
        }
    }

    private String getMainMeunSubname(int strId){
        return HostApplication.getApplication().getResources().getString(strId);
    }

    public static AppMenu NULL_ITEM = new AppMenu(null);

    public List<AppMenu> getItems() {
        return items;
    }

    public List<AppMenu> getNotNULLItems() {
        List<AppMenu> notNULLItems = new ArrayList<AppMenu>();
        for (AppMenu item : items) {
            if (!isNullItem(item))
                notNULLItems.add(item);
        }
        return notNULLItems;
    }

    public void setItems(List<AppMenu> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public AppMenu getNullItem() {
        return NULL_ITEM;
    }

    public boolean isNullItem(AppMenu item) {
        return item == NULL_ITEM;
    }

    static class ViewHolder {
        private TextView main_menu_item_nameTextView;
        private ImageView main_menu_item_iconImageView;

        public ViewHolder(View view) {
            main_menu_item_iconImageView = (ImageView) view.findViewById(R.id.main_menu_item_icon);
            main_menu_item_nameTextView = (TextView) view.findViewById(R.id.main_menu_item_name);
        }
    }
}
