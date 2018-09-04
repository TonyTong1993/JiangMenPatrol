package com.ecity.medialibrary.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.ecity.medialibrary.model.AudioModel;
import com.z3app.android.util.FileUtil;

public class MediaHelper {

    private static final String AUDIO_FILE_PREFIX = "Audio_";
    private static final String MPEG_FILE_PREFIX = "VID_";
    private static final String MPEG_FILE_SUFFIX = ".mp4";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
 
    public static File createAudioFile(Context context, String suffix) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File audioF = new File(FileUtil.getInstance(context).getMediaPath() + AUDIO_FILE_PREFIX + timeStamp + suffix);
        audioF.createNewFile();

        return audioF;
    }

    /**
     * 将File转成AudioModel,并且避免在audioModels中重复添加
     * @param files 源List
     * @param audioModels 目标List
     */
    public static void transFile2AudioModel(List<File> files, List<AudioModel> audioModels) {
        removeDuplicateMediaFile(files, audioModels);
        for (File file : files) {
            AudioModel audioModel = new AudioModel();
            audioModel.setFilePath(file.getAbsolutePath());
            audioModel.setPlayState(AudioModel.OVER);
            audioModel.setRecording(false);
            audioModel.setName(file.getName());
            audioModel.setLength(getAudioLength(file.getAbsolutePath()));
            audioModels.add(audioModel);
        }
    }

    public static String getAudioLength(String filePath) {
        String length = "00:00";
        int milliseconds = -1;
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.prepare();
            milliseconds = player.getDuration();
            Date date = new Date(milliseconds);
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
            length = dateFormat.format(date);
        } catch (Exception e) {
            Log.e("MediaHelper", e.toString());
        }

        return length;
    }

    /**
     * 去除files中与audioModels重复的文件
     * @param files
     * @param audioModels
     */
    public static void removeDuplicateMediaFile(List<File> files, List<AudioModel> audioModels) {
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();
            for (AudioModel audioModel : audioModels) {
                if (audioModel.getFilePath().equalsIgnoreCase(file.getAbsolutePath())) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public static File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = MPEG_FILE_PREFIX + timeStamp;
        File albumF = new File(getVideoPath());
        File videoF = File.createTempFile(videoFileName, MPEG_FILE_SUFFIX, albumF);
        return videoF;
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp;
        File albumF = new File(FileUtil.getInstance(null).getMediaPath());
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    
    public static String getVideoPath(){
    	String path = FileUtil.getInstance(null).getMediaPath()+ "videos/"; 
    	FileUtil.getInstance(null).hasFileDir(path);
    	return path;
    }

    public static String getImagePath(){
        String path = FileUtil.getInstance(null).getMediaPath()+ "images/"; 
        FileUtil.getInstance(null).hasFileDir(path);
        return path;
    }
    
    public static String getAudioPath(){
        String path = FileUtil.getInstance(null).getMediaPath()+ "audios/"; 
        FileUtil.getInstance(null).hasFileDir(path);
        return path;
    }
}
