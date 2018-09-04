package com.ecity.cswatersupply.contact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.contact.adater.ContactAdapter;
import com.ecity.cswatersupply.contact.adater.GroupContactAdapter;
import com.ecity.cswatersupply.contact.model.Contact;
import com.ecity.cswatersupply.contact.model.ContactGroup;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.utils.ListUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class ContactFragment extends Fragment {
    private ListView groupListView;
    private ListView contactListView;
    private View divider;
    private ContactGroup contactGroup;
    private GroupContactAdapter groupContactAdapter;
    private ContactAdapter contactAdapter;

    public ContactFragment() {
    }

    @SuppressLint("ValidFragment")
    public ContactFragment(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        groupListView = (ListView) view.findViewById(R.id.grouplistView);
        contactListView = (ListView) view.findViewById(R.id.contactlistView);
        initData();
        bindEvent();
        return view;
    }

    private void initData() {
        if (ListUtil.isEmpty(contactGroup.getChildGroup()) && ListUtil.isEmpty(contactGroup.getContacts())) {
            return;
        }

        if (!ListUtil.isEmpty(contactGroup.getChildGroup())) {
            groupListView.setVisibility(View.VISIBLE);
            List<ContactGroup> contactGroups = contactGroup.getChildGroup();
            groupContactAdapter = new GroupContactAdapter(getActivity());
            groupContactAdapter.setList(contactGroups);
            groupListView.setAdapter(groupContactAdapter);
        } else {
            groupListView.setVisibility(View.GONE);
        }

        if (!ListUtil.isEmpty(contactGroup.getContacts())) {
            contactListView.setVisibility(View.VISIBLE);
            List<Contact> contacts = contactGroup.getContacts();
            contactAdapter = new ContactAdapter(getActivity());
            contactAdapter.setList(contacts);
            contactListView.setAdapter(contactAdapter);
        } else {
            contactListView.setVisibility(View.GONE);
        }
    }

    private void bindEvent() {
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactGroup contactGroup = (ContactGroup) groupContactAdapter.getItem(i);
                ContactFragment fragment = new ContactFragment(contactGroup);
                UIEvent event = new UIEvent(UIEventStatus.EMERGENCY_CONTACT_PUSH_STACK, contactGroup);
                EventBusUtil.post(event);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                tx.replace(R.id.fragment_container, fragment, "");
                tx.addToBackStack(contactGroup.getGroupName());
                tx.commit();
            }
        });

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactFragment fragment = new ContactFragment(contactGroup);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                tx.replace(R.id.fragment_container, fragment, "");
                tx.addToBackStack(null);
                tx.commit();
                Contact contact = (Contact) contactAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), TakePhoneActivity.class);
                intent.putExtra(TakePhoneActivity.EMERENCY_CONTACT, contact);
                getActivity().startActivityForResult(intent,1);
            }
        });
    }
}
