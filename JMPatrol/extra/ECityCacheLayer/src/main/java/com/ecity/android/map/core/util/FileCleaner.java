package com.ecity.android.map.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * 按需清理文件空间 先清理限定时间和限定的单个文件大小以外的日志（今天的除外） 如果剩余的文件总量大于给定的文件限制空间，清除最早的那个文件
 * 
 * @author gaokai
 */
public class FileCleaner {
    private int MAX_DIR_SIZE_MB;// 文件路径所占最大空间
    private int MAX_FILE_EXSIST_DAY;// 单个文件存在的最长时间（天）
    private int MAX_SINGLE_FILE_SIZE_MB;// 单个文件最大空间

    private long maxSize = 0;
    private String filePath = "";

    /**
     * @param path
     *            需要清理的文件路径
     * 
     * @param dirSize
     *            文件路径所占最大空间
     * @param fileExsistDay
     *            单个文件存在的最长时间（天）
     * @param fileSize
     *            单个文件最大空间
     */
    public FileCleaner(String path, int dirSize, int fileExsistDay, int fileSize) {
        this.filePath = path;
        this.MAX_DIR_SIZE_MB = dirSize;
        this.MAX_FILE_EXSIST_DAY = fileExsistDay;
        this.MAX_SINGLE_FILE_SIZE_MB = fileSize;
    }

    public void work() {
        ArrayList<CleanFile> logFiles = new ArrayList<CleanFile>();
        logFiles = scanAndCleanFiles(filePath, logFiles);
        if (logFiles.size() == 0) {
            return;
        }
        if (maxSize > MAX_DIR_SIZE_MB) {
            deleteFirstExisistZipFile(logFiles);
        }
    }

    /**
     * 删除时间最久的日志
     * 
     * @param logFiles
     */
    private void deleteFirstExisistZipFile(ArrayList<CleanFile> logFiles) {
        String logPath = FileHelper.getInstance().getLogPath();
        Collections.sort(logFiles, new ModifiedTimeComparator());
        String path = logPath + logFiles.get(0).name;
        FileHelper.deleteSDFile(path);
    }

    /**
     * 扫描并清理超期和超量的文件
     * 
     * @param path
     * @param day
     * @param logFiles
     * @return
     */
    public ArrayList<CleanFile> scanAndCleanFiles(String path,
            ArrayList<CleanFile> logFiles) {
        File file = new File(path);
        if (file.isFile()) {
            if (isNeedDelete(file))
                file.delete();
            else {
                maxSize += FileHelper.formetFileSize(file.length(), "MB");
                CleanFile logFile = new CleanFile(file.getName(),
                        file.length(), file.lastModified());
                logFiles.add(logFile);
            }
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            for (File f : childFile) {
                scanAndCleanFiles(f.getPath(), logFiles);
            }
            if (isNeedDelete(file))
                file.delete();
        }

        return logFiles;
    }

    private boolean isNeedDelete(File file) {
        long tf = file.lastModified();
        Date nowDate = new Date();
        long tn = nowDate.getTime();
        long millis = tn - tf;
        int offset = (int) (millis / (1000 * 60 * 60 * 24));
        long singleLogSize = FileHelper.formetFileSize(file.length(), "MB");
        // 今天的文件不删
        if (offset > 1) {
            // 大于限定时间或者大于单个文件的限定大小，删！
            if (offset >= MAX_FILE_EXSIST_DAY
                    || singleLogSize > MAX_SINGLE_FILE_SIZE_MB)
                return true;

        }
        return false;
    }

    // 待清理的文件模型
    public static class CleanFile {
        CleanFile(String name, long size, long lastModifiedTime) {
            this.name = name;
            this.size = size;
            this.lastModifiedTime = lastModifiedTime;
        }

        public String name;
        public long size;
        public long lastModifiedTime;
    }

    // 根据最后的修改时间排序
    public static class ModifiedTimeComparator implements Comparator<CleanFile> {

        @Override
        public int compare(CleanFile lhs, CleanFile rhs) {
            if (lhs != null && rhs != null) {
                if (lhs.lastModifiedTime < rhs.lastModifiedTime) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        }

    }

}
