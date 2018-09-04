package com.ecity.cswatersupply.contact.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.contact.model.Contact;
import com.ecity.cswatersupply.contact.model.ContactGroup;
import com.ecity.cswatersupply.utils.ListUtil;

public class GroupContactAdapter extends ArrayListAdapter<ContactGroup> {
    private LayoutInflater inflater;

    public GroupContactAdapter(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ContactGroup contactGroup = getList().get(position);

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_list_contact_group, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.groupName.setText(contactGroup.getGroupName());

        if((ListUtil.isEmpty(contactGroup.getChildGroup()))) {
            if(null != contactGroup.getContacts()) {
                viewHolder.groupMemberNum.setText(String.valueOf(contactGroup.getContacts().size()));
            } else {
                viewHolder.groupMemberNum.setText("");
            }
        } else {
            viewHolder.groupMemberNum.setText(String.valueOf(contactGroup.getChildGroup().size()));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView groupName;
        private TextView groupMemberNum;

        private ViewHolder(View v) {
            groupName = (TextView) v.findViewById(R.id.tv_group_name);
            groupMemberNum = (TextView) v.findViewById(R.id.tv_group_num);
        }
    }
}