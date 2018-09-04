package com.z3pipe.mobile.android.corssdk.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.Handler;

import com.z3pipe.mobile.android.corssdk.CORSSDK;
import com.z3pipe.mobile.android.corssdk.model.Constants;
import com.z3pipe.mobile.android.corssdk.util.NMEAParser;

public class NetWorkService {
	// message types
	public static final int MESSAGE_SOURCE_TABLE_OK = 1;
	public static final int MESSAGE_SOURCE_TABLE_ERR = 2;
	private static final String CMD_HEAD = "$FCMDB,";
	private static final String CMD_END = ",*FF\r\n";

	private String mIP, mPort, mUserID, mPwd, mMountpoint;
	private Handler mHandler;
	private Socket mSocket;
	private DataOutputStream dos, btDos;
	private DataInputStream dis;
	private UpdateSourceTableThread mUpdateSourceTableThread;
	public AcquireDataThread mAcquireDataThread;
	private ReportGGA2Service mReportGGA2Service = null;

	/**
	 * 用指定参数实例化NetWorkService
	 */
	public NetWorkService(String ip, String port, Handler handler) {
		mIP = ip;
		mPort = port;
		mHandler = handler;
	}

	/**
	 * 用指定参数实例化NetWorkService，注意obj参数的成员顺序
	 * 
	 * @param obj
	 *            依次为ip、userID、password，mounted-Point等4个成员
	 */
	public NetWorkService(String[] obj, Handler handler) {
		this(obj[0], obj[1], handler);
		mUserID = obj[2];
		mPwd = obj[3];
		mMountpoint = obj[4];
	}

	/**
	 * 获取挂载点列表，返回结果为ArrayList<String>
	 */
	public synchronized void connect2Server() {
		if (mUpdateSourceTableThread != null) {
			mUpdateSourceTableThread.cancle();
			mUpdateSourceTableThread = null;
		}

		mUpdateSourceTableThread = new UpdateSourceTableThread();
		mUpdateSourceTableThread.start();
	}

	private class UpdateSourceTableThread extends Thread {

		@Override
		public void run() {
			try {
				GetCorsServiceSocket(mIP, mPort);
				// send the httpRequest strings
				byte[] datas = Socket4Cors.Request2NtripServer().getBytes();
				if(null == datas) {
					cancle();
					return;
				}
				
				dos.write(datas);

				byte[] buffer = new byte[1024];
				StringBuilder sb = new StringBuilder();
				int len = -1;
				while ((len = dis.read(buffer, 0, buffer.length)) != -1) {
					String str = new String(buffer, 0, len);
					sb.append(str);
				}

				String sourceString = sb.toString();
				if (sourceString.startsWith("SOURCETABLE 200 OK")) {
					ArrayList<String> mountPoints = new ArrayList<String>();

					String[] linStrings = sourceString.split("\r\n");
					for (String line : linStrings) {
						if (line.startsWith("STR")) {
							String[] dataStrings = line.trim().split(";");
							mountPoints.add(dataStrings[1]);
						}
					}
					
					if( null != mHandler) {
						mHandler.obtainMessage(MESSAGE_SOURCE_TABLE_OK, mountPoints)
							.sendToTarget();
					}
				} else {
					if(null != mHandler){
						mHandler.obtainMessage(MESSAGE_SOURCE_TABLE_ERR,
							sourceString).sendToTarget();
					}
				}
				cancle();
				mUpdateSourceTableThread = null;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 停止当前操作
		 */
		public void cancle() {
			try {
				if (null != dos) {
					dos.close();
				}
				if (null != dis) {
					dis.close();
				}
				if (null != mSocket) {
					mSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取差分数据
	 */
	public synchronized void connect2server4data() {
		
		if(null != mReportGGA2Service) {
			mReportGGA2Service.Cancle();
		}
		
		if (mAcquireDataThread != null) {
			mAcquireDataThread.cancle();
			mAcquireDataThread = null;
		}
		mAcquireDataThread = new AcquireDataThread();
		mAcquireDataThread.start();
	}

	
	public synchronized void cancleAll() {
		
		if(null != mReportGGA2Service) {
			mReportGGA2Service.Cancle();
		}
		
		if (mAcquireDataThread != null) {
			mAcquireDataThread.cancle();
			mAcquireDataThread = null;
		}
		
		if (mUpdateSourceTableThread != null) {
			mUpdateSourceTableThread.cancle();
			mUpdateSourceTableThread = null;
		}
	}
	/*
	 * 获取差分数据线程
	 */
	private class AcquireDataThread extends Thread {
		private boolean _run = true;
		private byte[] buffer = new byte[256];

		@Override
		public void run() {
			if (mSocket != null) {
				mSocket = null;
			}
			try {
				GetCorsServiceSocket(mIP, mPort);
				dos.write(Socket4Cors.CreateHttpRequset(mMountpoint, mUserID,
						mPwd).getBytes());

				// write cors to Bluetooth socket
				if (CORSSDK.getInstance().getSocket() != null) {
					btDos = new DataOutputStream(
							CORSSDK.getInstance().getSocket().getOutputStream());
				}

				int len = -1;
				while (_run) {
					len = dis.read(buffer, 0, buffer.length);
					if (len >= 1) {
						String temp = new String(buffer);
						if (temp.startsWith("ICY 200 OK")) {
							if (mReportGGA2Service == null) {
								mReportGGA2Service = new ReportGGA2Service(dos);
								mReportGGA2Service.start();
							}
							sendMessage(
									Constants.MESSAGE_DIFFERENCE,
									Constants.LOGIN_INFO, 200);
						} else if (temp.contains("401 Unauthorized")) {
							sendMessage(
									Constants.MESSAGE_DIFFERENCE,
									Constants.LOGIN_INFO, 401);
						} else {
							sendMessage(
									Constants.MESSAGE_DIFFERENCE,
									Constants.DIFFERENCE_DATA_LENGTH, len);

							// 转发cors数据至设备
							if (btDos != null) {
								String head = CMD_HEAD
										+ String.valueOf(len + 17) + ",";
								btDos.write(head.getBytes());
								btDos.write(buffer, 0, len);
								btDos.write(CMD_END.getBytes());
							}
						}
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					dos.close();
					dis.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		private void sendMessage(int what, int arg1, int arg2){
			if(null == mHandler) {
				return;
			}
			
			try {
				mHandler.obtainMessage(what,arg1, arg2).sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void cancle() {
			try {
				_run = false;
				mSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 自动上报GGA语句类，每3分钟上报一次
	 */
	public class ReportGGA2Service extends Thread {
		private DataOutputStream dos = null;
		private boolean _run = false;

		public ReportGGA2Service(DataOutputStream dos) {
			this.dos = dos;
		}

		@Override
		public void run() {
			while (!_run) {
				try {
					dos.write(NMEAParser.getWholeGGAMsg().getBytes());
					Thread.sleep(1000 * 60 * 3);//
				} catch (Exception e) {
					e.printStackTrace();
					//Cancle();
				}
			}
		}

		public void Cancle() {
			try {
				_run = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 初始化网络socket，并得到其数据输入输出流
	 */
	private void GetCorsServiceSocket(String ip, String port) {
		try {
			if (null == mSocket) {
				InetAddress ipAddress = Inet4Address.getByName(ip);
				mSocket = new Socket(ipAddress, Integer.parseInt(port));
			}
			if (null == dos) {
				dos = new DataOutputStream(mSocket.getOutputStream());
			}
			if (null == dis) {
				dis = new DataInputStream(mSocket.getInputStream());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
