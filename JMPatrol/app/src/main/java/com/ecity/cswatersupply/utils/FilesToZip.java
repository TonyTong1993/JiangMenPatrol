package com.ecity.cswatersupply.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.medialibrary.utils.MediaHelper;
import com.z3app.android.util.FileUtil;

public class FilesToZip {
    private static volatile FilesToZip instance;
    private EZipType mZipType = EZipType.IMAGE;

    public enum EZipType {
        IMAGE, AUDIO, VIDEO
    }

    public void setZipType(EZipType mZipType) {
        this.mZipType = mZipType;
    }

    public static FilesToZip getInstance() {
        if (null == instance) {
            synchronized (FilesToZip.class) {
                if (null == instance) {
                    instance = new FilesToZip();
                }
            }
        }

        return instance;
    }

    /**
     * Compress a single file into a zip file.
     * @param filePath 需要解压的文件路径
     * @return 解压完成后的zip包所在路径
     */
    public String zip(String filePath) {
        ArrayList<File> files = new ArrayList<File>();

        files.add(new File(filePath));

        String path = FileUtil.getInstance(null).getMediaPath() + UUID.randomUUID() + ".zip";
        if (zip(files.toArray(new File[files.size()]), path)) {
            return path;
        } else {
            return "";
        }
    }

    /**
     * Compress multiple files into a zip file.
     * @param fileList 需要解压的文件清单
     * @return zip包所在路径
     */
    public String zip(List<String> fileList) {
        return zip(fileList, "");
    }

    /**
     * 
     * @param fileList 需要压缩的文件路径
     * @param id 一个表单对应一个id，区别命名用
     * @return
     */
    public String zip(List<String> fileList, String id) {
        ArrayList<File> files = new ArrayList<File>();

        for (String file : fileList) {
            files.add(new File(file));
        }

        String path = FileUtil.getInstance(null).getMediaPath() + UUID.randomUUID() + ".zip";
        if (zip(files.toArray(new File[files.size()]), path, id)) {
            return path;
        } else {
            return "";
        }
    }

    private boolean zip(File[] sourceFiles, String zipFilePath) {
        boolean flag = false;

        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            File zipFile = new File(zipFilePath);

            if (zipFile.exists()) {
                zipFile.delete();
            } else {
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));

                for (int i = 0; i < sourceFiles.length; i++) {
                    ZipEntry zipEntry = null;
                    byte[] buffer = null;
                    switch (mZipType) {
                        case IMAGE:
                            buffer = decodeFile(sourceFiles[i]);
                            zipEntry = new ZipEntry(i + ".jpg");
                            break;
                        case AUDIO:
                            buffer = getFileBuffer(sourceFiles[i]);
                            zipEntry = new ZipEntry(i + ".mp3");
                            break;
                        case VIDEO:
                            buffer = getFileBuffer(sourceFiles[i]);
                            zipEntry = new ZipEntry(i + ".mp4");
                            break;
                        default:
                            break;
                    }
                    zos.putNextEntry(zipEntry);
                    zos.write(buffer, 0, buffer.length);
                    zipEntry.setSize(buffer.length);
                }

                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    private boolean zip(File[] sourceFiles, String zipFilePath, String id) {
        boolean flag = false;

        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            File zipFile = new File(zipFilePath);

            if (zipFile.exists()) {
                zipFile.delete();
            } else {
                fos = new FileOutputStream(zipFile);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));

                for (int i = 0; i < sourceFiles.length; i++) {
                    byte[] buffer = null;
                    ZipEntry zipEntry = null;
                    switch (mZipType) {
                        case IMAGE:
                            buffer = decodeFile(sourceFiles[i]);
                            if (!StringUtil.isEmpty(id)) {
                                zipEntry = new ZipEntry(id + "_" + i + ".jpg");
                            } else {
                                zipEntry = new ZipEntry(i + ".jpg");
                            }
                            break;
                        case AUDIO:
                            buffer = getFileBuffer(sourceFiles[i]);
                            if (!StringUtil.isEmpty(id)) {
                                zipEntry = new ZipEntry(id + "_" + i + ".mp3");
                            } else {
                                zipEntry = new ZipEntry(i + ".mp3");
                            }
                            break;
                        case VIDEO:
                            buffer = getFileBuffer(sourceFiles[i]);
                            if (!StringUtil.isEmpty(id)) {
                                zipEntry = new ZipEntry(id + "_" + i + ".mp4");
                            } else {
                                zipEntry = new ZipEntry(i + ".mp4");
                            }
                            break;
                        default:
                            break;
                    }
                    zos.putNextEntry(zipEntry);
                    zos.write(buffer, 0, buffer.length);
                    zipEntry.setSize(buffer.length);
                }

                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != zos) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    public void delete(List<String> fileList) {
        for (String f : fileList) {
            File file = new File(f);

            if (file.exists()) {
                file.delete();
            }
        }
    }

    public String[] getFiles(String ext) {
        String pathRoot = FileUtil.getInstance(null).getMediaPath();
        File file = new File(pathRoot);
        String[] filePaths = file.list(new MyFilter(ext));
        for (int i = 0; i < filePaths.length; i++) {
            filePaths[i] = pathRoot + filePaths[i];
        }

        return filePaths;
    }

    private class MyFilter implements FilenameFilter {

        private String ext;

        public MyFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File dir, String filename) {
            return filename.endsWith(ext);
        }
    }

    private byte[] decodeFile(File file) {
        Bitmap bitmap = getBitmapFromFile(file, 1024, 768);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 80, baos);
        byte[] buffer = baos.toByteArray();
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
        }

        return buffer;
    }

    private byte[] getFileBuffer(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                baos.write(b, 0, n);
            }
            fis.close();
            baos.close();
            buffer = baos.toByteArray();
        } catch (Exception e) {
            LogUtil.e(this, e);
        }

        return buffer;
    }

    private Bitmap getBitmapFromFile(File file, int width, int height) {
        if (null != file && file.exists()) {
            try {
                BitmapFactory.Options opts = null;

                if (width > 0 && height > 0) {
                    opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getPath(), opts);
                    final int minSideLength = Math.min(width, height);
                    opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                    opts.inJustDecodeBounds = false;
                    opts.inInputShareable = true;
                    opts.inPurgeable = true;

                    return BitmapFactory.decodeFile(file.getPath(), opts);
                } else {
                    return getBitmapFromFile(file);
                }

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return getBitmapFromFile(file);
            }
        }

        return null;
    }

    private Bitmap getBitmapFromFile(File file) {
        BitmapFactory.Options opts = new BitmapFactory.Options();

        if (file.length() < 20480) { // 0-20k
            opts.inSampleSize = 1;
        } else if (file.length() < 51200) { // 20-50k
            opts.inSampleSize = 2;
        } else if (file.length() < 307200) { // 50-300k
            opts.inSampleSize = 4;
        } else if (file.length() < 819200) { // 300-800k
            opts.inSampleSize = 6;
        } else if (file.length() < 1048576) { // 800-1024k
            opts.inSampleSize = 8;
        } else {
            opts.inSampleSize = 10;
        }

        return BitmapFactory.decodeFile(file.getPath(), opts);
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * Deletes all files and subdirectories under "dir".
     * 
     * @param dir
     *            Directory to be deleted
     * @return boolean Returns "true" if all deletions were successful. If a
     *         deletion fails, the method stops attempting to delete and returns
     *         "false".
     */
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so now it can be smoked
        return dir.delete();
    }

    private long savedSize;

    public long getSavedSize() {
        return savedSize;
    }

    public void setSavedSize(long savedSize) {
        this.savedSize = savedSize;
    }

    public File zip2Small(File image) {
        File smallFile = null;
        try {
            if (image != null && image.exists()) {
                byte[] bytes = decodeFile(image);

                smallFile = MediaHelper.createImageFile();
                FileOutputStream fos = new FileOutputStream(smallFile);
                fos.write(bytes);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smallFile;
    }
}
