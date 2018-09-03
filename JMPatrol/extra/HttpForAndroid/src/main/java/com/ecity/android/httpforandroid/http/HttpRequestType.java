package com.ecity.android.httpforandroid.http;

public enum HttpRequestType {
	GET,/*GET方式请求*/
	POST,/*POST方式请求*/
	AUTO;/*自动判断，判断标准是字符串长度超过2000字节*/
}
