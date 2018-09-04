package com.ecity.medialibrary.model;

import java.io.File;

/**
 * 
 * @author SunShan'ai
 */
public class FileModel {

    private File mFile;

    private boolean isChecked;

	public boolean isSelected = false;

	public File getmFile() {
		return mFile;
	}

	public void setmFile(File mFile) {
		this.mFile = mFile;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
