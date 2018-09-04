package com.ecity.medialibrary.model;

import java.io.Serializable;

public class AudioModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int OVER = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    
    private boolean delete;
    
    String filePath = "";
    String tempPath = "";
    String name = "";
    String length = "";
    boolean isRecording = false;
    //默认没有播放
    int playState = OVER;

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
