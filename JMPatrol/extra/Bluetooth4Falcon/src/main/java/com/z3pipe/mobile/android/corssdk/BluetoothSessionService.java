package com.z3pipe.mobile.android.corssdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.z3pipe.mobile.android.corssdk.model.Constants;

public class BluetoothSessionService {
	private static final String TAG = "BluetoothSessionService";
	public static final int STATE_NONE = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;

	public static String UUID = "00001101-0000-1000-8000-00805F9B34FB";
	private String msg = "";
	private StringBuilder mStringBuilder = new StringBuilder();

	private Handler mHandler;
	private int mState;
	private BluetoothAdapter mAdapter;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;

	public BluetoothSessionService(Context context, Handler handler) {
		this.mAdapter = BluetoothAdapter.getDefaultAdapter();
		this.mHandler = handler;
		this.mState = STATE_NONE;
	}

	public synchronized int getState() {
		return this.mState;
	}

	private synchronized void setState(int state) {
		if (Constants.DEBUG){
			Log.d(TAG, "setState() " + mState + " -> " + state);
		}
		mState = state;

		// Give the new state to the Handler so the UI Activity can update
		mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1)
				.sendToTarget();
	}

	public synchronized void start() {
		if (Constants.DEBUG) {
			Log.e(TAG, "start");
		}
	}

	public synchronized void connect(BluetoothDevice device) {
		if (Constants.DEBUG) {
			Log.d(TAG, "connect to" + device);
		}
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		if (device.getBondState() == BluetoothDevice.BOND_NONE) {
			try {
				Method method = BluetoothDevice.class.getMethod("createBond");
				method.invoke(device);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
			mConnectThread = new ConnectThread(device);
			mConnectThread.start();
			setState(STATE_CONNECTING);
		}
	}

	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {
		if (Constants.DEBUG){
			Log.d(TAG, "connected to" + device);
		}

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI Activity
		Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString(Constants.DEVICE_NAME, device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		setState(STATE_CONNECTED);
	}

	private class ConnectThread extends Thread {
		// private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;

			try {
				BluetoothSocket tmp = device.createRfcommSocketToServiceRecord(java.util.UUID
						.fromString(UUID));

				CORSSDK.getInstance().setSocket(tmp);
			} catch (Exception e) {
				Log.e(TAG, "create() failed", e);
			}
		}

		@Override
		public void run() {
			setName("ConnectThread");
			mAdapter.cancelDiscovery();
			try {
				CORSSDK.getInstance().getSocket().connect();
			} catch (Exception e) {
				Log.d("ConnectThread", e.getMessage());
				if (null != mConnectThread) {
					mConnectThread.cancel();
				}
				connectionFailed();
				return;
			}
			synchronized (BluetoothSessionService.this) {
				mConnectThread = null;
			}
			
			connected(CORSSDK.getInstance().getSocket(), mmDevice);
		}

		public void cancel() {
			try {
				if (null != CORSSDK.getInstance().getSocket()) {
					CORSSDK.getInstance().getSocket().close();
				}
			} catch (Exception e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInputStream;
		private final OutputStream mmOutputStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (Exception e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInputStream = tmpIn;
			mmOutputStream = tmpOut;
		}

		@Override
		public void run() {
			byte[] buffer = new byte[512];
			int bytes;

			byte[] sendCmd = { 02, 00, 06, 00, 03, 03 };
			try {
				mmOutputStream.write(sendCmd);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			while (true) {
				try {
					bytes = mmInputStream.read(buffer, 0, buffer.length);

					if (bytes >= 1) {
						mStringBuilder.append(new String(buffer, 0, bytes));
						msg = mStringBuilder.toString();

						if (msg.lastIndexOf("\r\n") >= 0
								&& msg.indexOf("$") >= 0) {
							String msgSend = msg.substring(msg.indexOf("$"),
									msg.lastIndexOf("\r\n") + 1);
							mHandler.obtainMessage(Constants.MESSAGE_READ,
									Constants.GGA, 0, msgSend)
									.sendToTarget();

							Log.i(TAG, "待解析:" + msgSend);
							mStringBuilder.delete(0,
									msg.lastIndexOf("\r\n") + 1);
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					if(null == mmInputStream || (e instanceof  IOException) ) {
						Log.e(TAG, "disconnected", e);
						connectionLost();
						try {
							mmInputStream.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						return;
					}
				}
			}
		}

		public void cancel() {
			try {
				mmInputStream.close();
				mmSocket.close();
			} catch (Exception e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	private void connectionFailed() {
		Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(Constants.TOAST, "连接异常！");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	private void connectionLost() {
		Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(Constants.TOAST, "设备已断开！");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (Constants.DEBUG){
			Log.d(TAG, "stop");
		}

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_NONE);
	}
}
