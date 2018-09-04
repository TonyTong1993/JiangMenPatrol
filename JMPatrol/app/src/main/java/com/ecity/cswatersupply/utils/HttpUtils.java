package com.ecity.cswatersupply.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.ecity.android.log.LogUtil;

public class HttpUtils {
    private static final String TAG = HttpUtils.class.getSimpleName();

    public interface FileDownloadCallback {
        /**
         * callback method when file downloading succeeds
         */
        void onSuccess();

        /**
         * callback method when file downloading fails
         */
        void onError(Throwable throwable);

        /**
         * @param progress download progress. If current progress is 75%, value of progress is 75.
         */
        void onProgressUpdate(double progress);
    }

    public static InputStream getInputStream(String path) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = getConnection(path);

        try {
            if (200 == httpURLConnection.getResponseCode()) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                LogUtil.e(TAG, "response code=" + httpURLConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            LogUtil.e(TAG, e);
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        }

        return inputStream;
    }

    public static InputStream getInputStream(HttpURLConnection connection) {
        if (connection == null) {
            return null;
        }

        InputStream inputStream = null;

        try {
            if (200 == connection.getResponseCode()) {
                inputStream = connection.getInputStream();
            } else {
                LogUtil.e(TAG, "response code=" + connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            LogUtil.e(TAG, e);
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        }

        return inputStream;
    }

    public static HttpURLConnection getConnection(String path) {
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(path);
            if (null != url) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
            }
        } catch (MalformedURLException e) {
            LogUtil.e(TAG, e);
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        }

        return httpURLConnection;
    }

    public static boolean saveInputStream(InputStream inputStream, String saveToPath) {
        boolean isSuccess = true;
        byte[] data = new byte[1024];
        int len = 0;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(saveToPath);
            while (-1 != (len = inputStream.read(data))) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e);
            isSuccess = false;
        } finally {
            releaseCloseable(inputStream);
            releaseCloseable(fileOutputStream);
        }

        return isSuccess;
    }

    public static void saveInputStream(int totalSize, InputStream inputStream, String saveToPath, FileDownloadCallback callback) {
        byte[] data = new byte[1024];
        int len = 0;
        double downloadedSize = 0.0;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(saveToPath);
            while (-1 != (len = inputStream.read(data))) {
                fileOutputStream.write(data, 0, len);
                downloadedSize += len;
                if ((callback != null) && (totalSize > 0)) {
                    double progress = (downloadedSize / totalSize) * 100;
                    callback.onProgressUpdate(progress);
                }
            }
            if (callback != null) {
                callback.onSuccess();
            }
        } catch (IOException e) {
            if (callback != null) {
                callback.onError(e);
            }
            LogUtil.e(TAG, e);
        } finally {
            releaseCloseable(inputStream);
            releaseCloseable(fileOutputStream);
            callback = null;
        }
    }

    public static String convertStreamToString(InputStream inStream) {
        /*  
          * To convert the InputStream to String we use the BufferedReader.readLine()  
          * method. We iterate until the BufferedReader return null which means  
          * there's no more data to read. Each line will appended to a StringBuilder  
          * and returned as String.  
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private static void releaseCloseable(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                LogUtil.e(TAG, e);
            }
        }
    }
}