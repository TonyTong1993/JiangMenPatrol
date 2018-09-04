package com.ecity.cswatersupply.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.contact.model.Contact;
import com.ecity.cswatersupply.utils.PhoneUtil;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.activity.BaseActivity;

/**
 * Created by Administrator on 2017/5/17.
 */

public class TakePhoneActivity extends BaseActivity {
    public final static String EMERENCY_CONTACT = "EMERENCY_CONTACT";

    private TextView tvName;
    private TextView name;
    private TextView phone;
    private TextView mobilePhone;
    private TextView position;
    private TextView department;
    private TextView address;
    private ImageView callPhone;
    private ImageView callMobilePhone;
    private ImageView messageImg;
    private ImageView backImg;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_take_phone);
        handleIntent();
        initUI();
        initData();
        bindEvent();
    }

    private void handleIntent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(EMERENCY_CONTACT)) {
            contact = (Contact) bundle.getSerializable(EMERENCY_CONTACT);
        }
    }

    private void initUI() {
        tvName = (TextView) this.findViewById(R.id.tv_name);
        name = (TextView) this.findViewById(R.id.contact_name_value);
        phone = (TextView) this.findViewById(R.id.contact_phone_value);
        mobilePhone = (TextView) this.findViewById(R.id.contact_mobile_phone_value);
        position = (TextView) this.findViewById(R.id.contact_position_value);
        department = (TextView) this.findViewById(R.id.contact_dept_value);
        address = (TextView) this.findViewById(R.id.contact_address_value);
        callPhone = (ImageView) this.findViewById(R.id.iv_call_phone);
        callMobilePhone = (ImageView) this.findViewById(R.id.iv_call_mobile_phone);
        messageImg = (ImageView) this.findViewById(R.id.iv_message);
        backImg = (ImageView) this.findViewById(R.id.iv_back_arrow);
    }

    private void initData() {
        tvName.setText(contact.getName());
        name.setText(contact.getName());
        if(StringUtil.isBlank(contact.getTel())) {
            phone.setText("无");
            callPhone.setVisibility(View.GONE);
        } else {
            phone.setText(contact.getTel());
            callPhone.setVisibility(View.VISIBLE);
        }
        mobilePhone.setText(contact.getMobile());
        position.setText(contact.getPosition());
        department.setText(contact.getType() + " > " + contact.getDepartment());
        StringBuffer buffer = new StringBuffer();
        buffer.append(contact.getCity());
        buffer.append(contact.getRegion());
        buffer.append(contact.getVillage());
        buffer.append(contact.getCommunity());
        address.setText(buffer.toString());
    }

    private void bindEvent() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("call_back", 100);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneUtil.call(TakePhoneActivity.this, contact.getTel().toString());
            }
        });

        callMobilePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneUtil.call(TakePhoneActivity.this, contact.getMobile().toString());
            }
        });

        messageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + contact.getMobile());
                Intent intentMessage = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intentMessage);
            }
        });
    }


}
