package com.z3pipe.mobile.android.corssdk.util;

import java.io.File;
import java.io.FileFilter;

public class XmlFileFilter implements FileFilter {

	/*
	 * @author mercy
	 * 
	 * @文件过滤器
	 * 
	 * @date 2015-04-16
	 */
	String suffix;

	public XmlFileFilter(String suffix) {
		// TODO Auto-generated constructor stub
		this.suffix = suffix;
	}

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		String filename = pathname.getName();
		if (filename.lastIndexOf(suffix) != -1) {
			return true;
		}
		return false;
	}

}
