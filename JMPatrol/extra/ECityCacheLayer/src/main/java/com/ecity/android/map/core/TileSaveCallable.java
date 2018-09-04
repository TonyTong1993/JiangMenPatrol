package com.ecity.android.map.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/***
 * 瓦片请求过程
 * @author ZiZhengzhuan
 *
 */
@SuppressLint("DefaultLocale")
public class TileSaveCallable extends TileCallable {
	private int level;
	private int col;
	private int row;
	private String url;
	private TimeInfo timeinfo;
	private byte[] tileData;
	private String cachePath;
	public TileSaveCallable(int level, int col, int row,String url,TimeInfo timeinfo, byte[] tileData,String cachePath) {
		super(row, row, row, url, timeinfo);
		this.url = url;
		this.level = level;
		this.col = col;
		this.row = row;
		this.timeinfo = timeinfo;
		this.tileData = tileData;
		this.cachePath = cachePath;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public TimeInfo getTimeinfo() {
		return timeinfo;
	}

	public void setTimeinfo(TimeInfo timeinfo) {
		this.timeinfo = timeinfo;
	}
	
	public byte[] getTileData() {
		return tileData;
	}

	public void setTileData(byte[] tileData) {
		this.tileData = tileData;
	}

	public String getCachePath() {
		return cachePath;
	}

	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}

	@Override
	public Boolean call() throws Exception {
		if(null == getTileData()){
			return false;
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(getTileData(), 0, getTileData().length);
		return saveBitmapToFile(getLevel(),getCol(),getRow(),bitmap);
	}

	// 保存图片到本地
	private boolean saveBitmapToFile(int level, int col, int row, Bitmap bitmap)
			throws IOException {
		if (null == bitmap){
			throw new IOException("NULL Content");
		}
		
		if(null == cachePath || "".equalsIgnoreCase(cachePath)){
			throw new IOException("cachePath is empty");
		}
		boolean flg = false;
		BufferedOutputStream os = null;
		try {
			File pathfile = new File(cachePath);
			if (!pathfile.exists()){
				pathfile.mkdirs();
			}

			String tileFilePath = cachePath + row + ".zzz";
			File file = new File(tileFilePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			flg = true;
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			os = null;
		}
		
		return flg;
	}
}
