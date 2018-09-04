package com.ecity.cswatersupply.emergency.model;

import java.io.Serializable;

public class EmergencyPlanModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int STATUS_NOT_DOWNLOAD = 0;
	public static final int STATUS_CONNECTING = 1;
	public static final int STATUS_CONNECT_ERROR = 2;
	public static final int STATUS_DOWNLOADING = 3;
	public static final int STATUS_PAUSED = 4;
	public static final int STATUS_DOWNLOAD_ERROR = 5;
	public static final int STATUS_COMPLETE = 6;
	
	private String id;
	private String name;
	private String createtime;
	public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    private String enter;
	private String entername;
	private String type;
	private String typeName;
	private String doc;
	private String docUrl;
	private String uploadTime;
	private String uploader;
	private String savePath;
	private String image;
	private int progress;
	private String downloadPerSize;
	private int status;
	private String thumbnail;
	private String describe;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return name;
	}

	public void setPackageName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return docUrl;
	}

	public void setUrl(String url) {
		this.docUrl = url;
	}

	public String getDownloadPerSize() {
		return downloadPerSize;
	}

	public void setDownloadPerSize(String downloadPerSize) {
		this.downloadPerSize = downloadPerSize;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getStatus() {
		return status;
	}

	
	public String getEnter() {
        return enter;
    }

    public void setEnter(String enter) {
        this.enter = enter;
    }

    public String getEntername() {
        if(null == entername || entername.equalsIgnoreCase("")){
            entername = "default";
        }
        return entername;
    }

    public void setEntername(String entername) {
        this.entername = entername;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStatusText() {
		switch (status) {
		case STATUS_NOT_DOWNLOAD:
			return "未下载";
		case STATUS_CONNECTING:
			return "正在连接";
		case STATUS_CONNECT_ERROR:
			return "连接失败";
		case STATUS_DOWNLOADING:
			return "下载中";
		case STATUS_PAUSED:
			return "已暂停";
		case STATUS_DOWNLOAD_ERROR:
			return "下载失败";
		case STATUS_COMPLETE:
			return "下载完成";
		default:
			return "未下载";
		}
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
