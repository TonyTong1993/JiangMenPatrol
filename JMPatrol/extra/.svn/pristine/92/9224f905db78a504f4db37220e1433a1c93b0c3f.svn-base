package com.enrique.bluetooth4falcon;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.z3pipe.mobile.android.corssdk.BluetoothSessionService;
import com.zzz.ecity.android.applibrary.view.TitleView;
import com.zzz.ecity.android.applibrary.view.TitleView.BtnStyle;
import com.enrique.bluetooth4falcon.R;
/**
 * 
 * @author zhangfusheng
 * @date 2011-09-21
 *
 */
public class DeviceListActivity extends Activity {
	private boolean unresponsive = false;
	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// Member fields
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private TitleView customTitleView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_cors_devicelist);

		// Initialize array adapters. One for already paired devices and
		// one for newly discovered devices
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.listview_item_device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.listview_item_device_name);

		// Find and set up the ListView for paired devices
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setSelector(android.R.color.transparent);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Find and set up the ListView for newly discovered devices
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);

		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				// 通过设备名称限制一下设备的类型
				// if (device.getName().trim().indexOf("HC-06") >= 0 ||
				// device.getName().trim().indexOf("SABER") >= 0) {
				// if (device.getUuids() != null && device.getUuids().length >=
				// 1) {
				// BluetoothSessionService.UUID =
				// device.getUuids()[0].toString();
				// }
				// }
				mPairedDevicesArrayAdapter.add(device.getName().trim() + "\n"
						+ device.getAddress());
			}
		} else {
			String noDevices = getResources().getText(R.string.str_cors_none_paired)
					.toString();
			mPairedDevicesArrayAdapter.add(noDevices);
		}

		customTitleView = (TitleView) findViewById(R.id.customTitleView);
		customTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
		customTitleView
				.setRightActionBtnText(getString(R.string.str_cors_update));
		customTitleView.setTitleText(getString(R.string.str_cors_scan_bluetooth));
	}

	public void onBackButtonClicked(View view) {
		finish();
	}

	public void onActionButtonClicked(View view) {
		doDiscovery();
		customTitleView.setBtnStyle(BtnStyle.ONLY_BACK);
	}
	
	private void doDiscovery() {
		// Indicate scanning in the title
		customTitleView.setTitleText(getString(R.string.str_cors_scanning));
		if(null != mNewDevicesArrayAdapter){
			mNewDevicesArrayAdapter.clear();
		}
		// Turn on sub-title for new devices
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// 确保点击某个设备之前已获取该设备的UUID
			if (!unresponsive) {
				// Cancel discovery because it's costly and we're about to
				// connect
				mBtAdapter.cancelDiscovery();

				// Get the device MAC address, which is the last 17 chars in the
				// View
				String info = ((TextView) v).getText().toString();
				String address = info.substring(info.length() - 17);

				// Create the result Intent and include the MAC address
				Intent intent = new Intent();
				intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

				// Set result and finish this Activity
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	};
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equalsIgnoreCase(BluetoothDevice.ACTION_FOUND)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					String dname = "";
					String address = "";
					try {
						dname = device.getName().trim();
					} catch (Exception e) {
						dname = "Unknown Name";
					}
					try {
						address = device.getAddress();
					} catch (Exception e) {
					}
					
					mNewDevicesArrayAdapter.add(dname+ "\n" + address);
				}
			} else if (action
					.equalsIgnoreCase(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				customTitleView.setTitleText(getString(R.string.str_cors_select_device));
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					String noDevices = getResources().getText(
							R.string.str_cors_none_found).toString();
					mNewDevicesArrayAdapter.add(noDevices);
				}
				customTitleView.setBtnStyle(BtnStyle.RIGHT_ACTION);
			} else if (action.equalsIgnoreCase(BluetoothDevice.ACTION_UUID)) {
				BluetoothDevice bd = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Parcelable[] uuidExtra = intent
						.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);

				if (bd != null && uuidExtra != null) {
					for (Parcelable parcelable : uuidExtra) {
						if (!BluetoothSessionService.UUID.equals(parcelable
								.toString())) {
							BluetoothSessionService.UUID = parcelable
									.toString();
						}
					}
					unresponsive = false;
				}
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		// Unregister broadcast listeners
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when UUID has find
		this.registerReceiver(mReceiver, new IntentFilter(
				BluetoothDevice.ACTION_UUID));
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
}
