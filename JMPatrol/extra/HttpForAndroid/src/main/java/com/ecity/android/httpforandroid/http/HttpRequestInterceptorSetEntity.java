package com.ecity.android.httpforandroid.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

public class HttpRequestInterceptorSetEntity implements HttpResponseInterceptor
{
	@Override
	public void process(HttpResponse response, HttpContext context)
			throws HttpException, IOException {
	    HttpEntity localHttpEntity = response.getEntity();
	    Header localHeader = localHttpEntity.getContentEncoding();
	    if (localHeader != null) {
	      HeaderElement[] arrayOfHeaderElement = localHeader.getElements();
	      for (int i = 0; i < arrayOfHeaderElement.length; i++) {
              if (arrayOfHeaderElement[i].getName().equalsIgnoreCase("gzip")) {
                  response.setEntity(new HttpRequest.httpEntityWrapperClient(response.getEntity()));
                  return;
              }
          }
	    }		
	}
}
