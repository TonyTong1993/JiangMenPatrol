package com.ecity.cswatersupply.contact.adater;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.contact.model.Contact;
import com.zzz.ecity.android.applibrary.view.CircleTextImageView;

public class ContactAdapter extends ArrayListAdapter<Contact> {
    private LayoutInflater inflater;
    private float textRatio;

    public ContactAdapter(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
        initTextRatio();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_contact_child, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null == mList || position > mList.size()) {
            return convertView;
        }

        Contact contact = mList.get(position);
        holder.title.setShowBoard(false);
        holder.title.setBGColor(R.color.lightblue);
        holder.title.setTextSize(textRatio);
        String name = contact.getName();
        if( name.length() > 1 ) {
            name = (name.substring(1, name.length())).trim();
        }
        holder.title.setText4CircleImage(name);
        holder.name.setText(contact.getName());
        holder.position.setText(contact.getPosition());

        return convertView;
    }

    private class ViewHolder {
        private CircleTextImageView title;
        private TextView name;
        private TextView position;

        private ViewHolder(View view) {
            this.title = (CircleTextImageView) view.findViewById(R.id.tv_title);
            this.name = (TextView) view.findViewById(R.id.tv_name);
            this.position = (TextView) view.findViewById(R.id.tv_position);
        }
    }

    private void initTextRatio(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager mWm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(displayMetrics);

        //2.计算与你开发时设定的屏幕大小的纵横比(这里假设你开发时定的屏幕大小是480*800)
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        float ratioWidth = (float)screenWidth / 480;
        float ratioHeight = (float)screenHeight / 800;

        textRatio = Math.min(ratioWidth, ratioHeight);
        textRatio = textRatio/10.f;
    }
}