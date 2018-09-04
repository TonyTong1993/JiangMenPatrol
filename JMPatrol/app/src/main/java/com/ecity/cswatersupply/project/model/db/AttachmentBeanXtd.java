package com.ecity.cswatersupply.project.model.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.ecity.android.db.model.ASQLiteBean;

/**
 * @author Ma Qianli
 *
 */
public class AttachmentBeanXtd extends ABaseBeanXtd {
    private static final long serialVersionUID = -6796457443214072037L;

    /**
     * Status of notification. Not downloaded.
     */
    public static final String STATUS_NEW = "0";
    /**
     * Status of notification. Downloading.
     */
    public static final String STATUS_DOWNLOADING = "1";
    /**
     * Status of notification. Downloaded.
     */
    public static final String STATUS_DOWNLOADED = "2";

    private String url;
    private String localPath;
    private String fileName;
    private String uniqueName;
    private String status;
    private String downloadedOn;

    @Override
    public ASQLiteBean createInstance() {
        return new AttachmentBeanXtd();
    }

    @Override
    public ContentValues generateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("url", this.url);
        contentValues.put("uniqueName", this.uniqueName);
        contentValues.put("localPath", this.localPath);
        contentValues.put("fileName", this.fileName);
        contentValues.put("status", this.status);
        contentValues.put("downloadedOn", this.downloadedOn);

        return contentValues;
    }

    @Override
    public void buildFromCursor(Cursor cursor) {
        int index = 0;
        String[] columnNames = cursor.getColumnNames();
        for (String name : columnNames) {
            index = cursor.getColumnIndex(name);

            if (name.equalsIgnoreCase("_rid")) {
                uniqueId = cursor.getInt(index);
            } else if (name.equalsIgnoreCase("url")) {
                url = cursor.getString(index);
            } else if (name.equalsIgnoreCase("localPath")) {
                localPath = cursor.getString(index);
            } else if (name.equalsIgnoreCase("fileName")) {
                fileName = cursor.getString(index);
            } else if (name.equalsIgnoreCase("status")) {
                status = cursor.getString(index);
            } else if (name.equalsIgnoreCase("downloadedOn")) {
                downloadedOn = cursor.getString(index);
            } else if (name.equalsIgnoreCase("uniqueName")) {
                uniqueName = cursor.getString(index);
            } else {
                // Unknown column.
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownloadedOn() {
        return downloadedOn;
    }

    public void setDownloadedOn(String downloadedOn) {
        this.downloadedOn = downloadedOn;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}
