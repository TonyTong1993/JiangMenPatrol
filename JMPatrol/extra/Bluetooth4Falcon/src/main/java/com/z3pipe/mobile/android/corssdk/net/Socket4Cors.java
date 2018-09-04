package com.z3pipe.mobile.android.corssdk.net;

import android.util.Base64;

public class Socket4Cors {
	public static String CreateHttpRequset(String mountPoint, String userId, String password) {
		String msg = "GET /" + mountPoint + " HTTP/1.0\r\n";
		msg += "User-Agent: NTRIP GNSSInternetRadio/1.4.11\r\n";
		msg += "Accept: */*\r\n";
		msg += "Connection: close\r\n";

		String tempString = (userId + ":" + password);
		byte[] buf;
		buf = tempString.getBytes();

		String code = Base64.encodeToString(buf, Base64.NO_WRAP);
		msg += "Authorization: Basic " + code + "\r\n";
		msg += "\r\n";

		return msg;
	}

	public static String Request2NtripServer() {
		return "GET / HTTP/1.0\r\nUser-Agent: NTRIP GNSSInternetRadio/1.4.11\r\nAccept: */*\r\nConnection: close\r\n\r\n";
	}
}
