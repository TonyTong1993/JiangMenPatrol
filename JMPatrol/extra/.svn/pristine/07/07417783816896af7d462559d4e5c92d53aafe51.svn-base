package com.ecity.android.map.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.ecity.android.map.core.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FileHelper {

	private Context context;
	private boolean hasSD         = false;
	private boolean hasPermission = false;

	private String SDPATH; // sd卡路径
	private String localMapDocPath;
	private String MapCachePath;
	private String rootPath;
	private String FILESPATH;
	private String skin_welcomimg_Path;
	private String media_Path;
	private String media_temp_path;
	private String logPath;
	private String conf_Path;
	private String downloadedPath;

	private static FileHelper instance;
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	private FileHelper() {
		initPaths();
		hasFileDir(skin_welcomimg_Path);
		hasFileDir(media_Path);
		hasFileDir(media_temp_path);
		hasFileDir(logPath);
		hasFileDir(conf_Path);
		hasFileDir(localMapDocPath);
		hasFileDir(MapCachePath);
		hasFileDir(downloadedPath);
	}

	public static FileHelper getInstance() {
		if (instance == null) {
			instance = new FileHelper();
		}
		return instance;
	}

	private void initPaths() {
		hasSD = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		//hasPermission = hasExternalStoragePermission(MainApplication.getApplication().getApplicationContext());
		hasPermission = true;
		SDPATH = (hasSD && hasPermission) ? Environment.getExternalStorageDirectory().getPath() : Environment.getRootDirectory().getPath();
		skin_welcomimg_Path = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "//skin//welcome//welcome.png";

		media_Path = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "/Media/";
		media_temp_path = media_Path + "Temp";
		logPath = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "//Log//";
		conf_Path = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "//conf//";
		rootPath = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "//";
		localMapDocPath = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "//map//";
		MapCachePath = localMapDocPath + "//cache//";
		downloadedPath = SDPATH + "//Z3SDK//" + Constants.PRJ_DIR_NAME + "//document//";
	}

	public File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	public static File createFile(String path) {
		File file = new File(path);
		try {
			if (file.exists()) {
				deleteFile(file);
			}
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}

	public static boolean deleteSDFile(String fileName) {
		File file = new File(fileName);
		if (file == null || !file.exists() || file.isDirectory()) {
			return false;
		}
		return file.delete();
	}

	public boolean hasFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return file.isFile();
		}
		return false;
	}

	public boolean hasFileDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.exists();
	}

	public static void RecursionDeleteFile(String dirc) {
		File file = new File(dirc);
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f.getPath());
			}
			file.delete();
		}
	}

	public static void DeleteFileInDirectoryWithBeforeDays(String path, int day) {
		File file = new File(path);
		if (file.isFile()) {
			if (isNeedDelete(file, day)) {
				file.delete();
			}
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			for (File f : childFile) {
				DeleteFileInDirectoryWithBeforeDays(f.getPath(), day);
			}
			if (isNeedDelete(file, day)) {
				file.delete();
			}
		}
	}

	private static boolean isNeedDelete(File file, int day) {
		long tf = file.lastModified();
		Date nowDate = new Date();
		long tn = nowDate.getTime();
		long millis = tn - tf;
		int offset = (int) (millis / (1000 * 60 * 60 * 24));
		if (offset >= day) {
			return true;
		} else {
			return false;
		}
	}

	public String getFILESPATH(Context context) {
		this.context = context;
		FILESPATH = this.context.getFilesDir().getPath();
		return FILESPATH;
	}

	public String getSDPATH() {
		return SDPATH;
	}

	public String getRootPath() {
		return rootPath;
	}

	public boolean hasSD() {
		return hasSD;
	}

	public String getSkinWelcomeImgPath() {
		return skin_welcomimg_Path;
	}

	public String getLocalMapDocPath() {
		return localMapDocPath;
	}

	public String getMediaPath() {
		return media_Path;
	}

	public String getMediaTempPath() {
		return media_temp_path;
	}

	public String getConfPath() {
		return conf_Path;
	}

	public String getLogPath() {
		return logPath;
	}

	public String getMapCachePath() {
		return MapCachePath;
	}

	public String getDownloadedPath() {
		return downloadedPath;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	public static String getSDPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = fileS + " B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + " K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + " M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + " G";
		}
		return fileSizeString;
	}

	public static long formetFileSize(long fileS, String unit) {
		if (unit.equalsIgnoreCase("B")) {
			return fileS;
		} else if (unit.equalsIgnoreCase("KB")) {
			return (fileS / 1024);
		} else if (unit.equalsIgnoreCase("MB")) {
			return (fileS / 1048576);
		} else if (unit.equalsIgnoreCase("GB")) {
			return (fileS / 1073741824);
		}
		return -1;
	}

	public static String combinPath(String path, String fileName) {
		return path + (path.endsWith(File.separator) ? "" : File.separator) + fileName;
	}

	public static boolean copyFile(File src, File tar) throws Exception {
		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[1024 * 8];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
		}
		if (src.isDirectory()) {
			File[] f = src.listFiles();
			tar.mkdir();
			for (int i = 0; i < f.length; i++) {
				copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile() + File.separator + f[i].getName()));
			}
		}
		return true;
	}

	public static boolean moveFile(File src, File tar) throws Exception {
		if (copyFile(src, tar)) {
			deleteFile(src);
			return true;
		}
		return false;
	}

	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		f.delete();
	}

	public static String getMIMEType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp") || end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") ||
				   end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	public void clearTileCacheFiles() {
		try {
			File file = new File(MapCachePath);
			deleteFile(file);
		} catch (Exception e) {
		}
	}

	public String getMapPathforCache(String layerName, int level, int col) {
		String dir = MapCachePath + layerName + "//L" + level + "//C" + col + "//";
		hasFileDir(dir);
		return dir;
	}

	public void openFile(Context context, String filePath) {
		File file = new File(filePath);
		if (file != null && file.exists() && file.length() > 0) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			String type = FileHelper.getMIMEType(file.getName());
			intent.setDataAndType(Uri.fromFile(file), type);
			context.startActivity(intent);
		}
	}

	public static boolean hasTPKFiles(String path) {
		File directory = new File(path);
		FileNameSelector mfileFilter = new FileNameSelector(".tpk");
		File[] TPKFiles = directory.listFiles(mfileFilter);
		if (TPKFiles != null && 0 != TPKFiles.length) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是目录
	 *
	 * @param dirPath 目录路径
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isDir(String dirPath) {
		return isDir(getFileByPath(dirPath));
	}

	/**
	 * 判断是否是目录
	 *
	 * @param file 文件
	 * @return {@code true}: 是<br>{@code false}: 否
	 */
	public static boolean isDir(File file) {
		return isFileExists(file) && file.isDirectory();
	}

	/**
	 * 根据文件路径获取文件
	 *
	 * @param filePath 文件路径
	 * @return 文件
	 */
	public static File getFileByPath(String filePath) {
		return StringUtil.isEmpty(filePath) ? null : new File(filePath);
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param filePath 文件路径
	 * @return {@code true}: 存在<br>{@code false}: 不存在
	 */
	public static boolean isFileExists(String filePath) {
		return isFileExists(getFileByPath(filePath));
	}

	/**
	 * 判断文件是否存在
	 *
	 * @param file 文件
	 * @return {@code true}: 存在<br>{@code false}: 不存在
	 */
	public static boolean isFileExists(File file) {
		return file != null && file.exists();
	}

	/**
	 * 获取目录下所有文件
	 *
	 * @param dirPath     目录路径
	 * @param isRecursive 是否递归进子目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
		return listFilesInDir(getFileByPath(dirPath), isRecursive);
	}

	/**
	 * 获取目录下所有文件
	 *
	 * @param dir         目录
	 * @param isRecursive 是否递归进子目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(File dir, boolean isRecursive) {
		if (!isDir(dir)) {
			return null;
		}
		if (isRecursive) {
			return listFilesInDir(dir);
		}
		List<File> list = new ArrayList<>();
		File[] files = dir.listFiles();
		if (files != null && files.length != 0) {
			Collections.addAll(list, files);
		}
		return list;
	}

	/**
	 * 获取目录下所有文件包括子目录
	 *
	 * @param dirPath 目录路径
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(String dirPath) {
		return listFilesInDir(getFileByPath(dirPath));
	}

	/**
	 * 获取目录下所有文件包括子目录
	 *
	 * @param dir 目录
	 * @return 文件链表
	 */
	public static List<File> listFilesInDir(File dir) {
		if (!isDir(dir)) {
			return null;
		}
		List<File> list = new ArrayList<>();
		File[] files = dir.listFiles();
		if (files != null && files.length != 0) {
			for (File file : files) {
				list.add(file);
				if (file.isDirectory()) {
					list.addAll(listFilesInDir(file));
				}
			}
		}
		return list;
	}


	static class FileNameSelector implements FilenameFilter {

		private String type;

		public FileNameSelector(String type) {
			this.type = type;
		}

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return filename.endsWith(type);
		}
	}

}
