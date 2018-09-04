package com.ecity.android.contactmanchooser.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.android.contactmanchooser.R;
import com.ecity.android.contactmanchooser.model.ContactMan;
import com.yokeyword.indexablelistview.IndexBarAdapter;

public class ContactAdapter extends IndexBarAdapter<ContactMan> {
    private Context mContext;

    public ContactAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(IndexBarAdapter<ContactMan>.ViewHolder holder, ContactMan cityEntity) {
        ContactViewHolder myHolder = (ContactViewHolder) holder;
        myHolder.tvName.setText(cityEntity.getName().isEmpty()?"佚名":cityEntity.getName());
        myHolder.tvPhoneNumber.setText(cityEntity.getPhone().isEmpty()?"暂无号码":cityEntity.getPhone());
        boolean hasChoosed = cityEntity.isHasChoosed();
        Drawable d ;
        if (hasChoosed) {
            d = mContext.getResources().getDrawable(R.drawable.checkbox_hl);
        } else {
            d = mContext.getResources().getDrawable(R.drawable.checkbox_nor);
        }
        myHolder.imgHasChoosed.setImageDrawable(d);
    }

    @Override
    protected TextView onCreateTitleViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list_title, parent, false);
        return (TextView) view.findViewById(R.id.tv_title_name);
    }

    @Override
    protected IndexBarAdapter<ContactMan>.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_man, parent, false);
        return new ContactViewHolder(view);
    }

    class ContactViewHolder extends ViewHolder {
        private TextView tvName;
        private TextView tvPhoneNumber;
        private ImageView imgHasChoosed;

        public ContactViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvPhoneNumber = (TextView) view.findViewById(R.id.tv_mobile);
            imgHasChoosed = (ImageView) view.findViewById(R.id.img_haschoosed);
        }
    }
}