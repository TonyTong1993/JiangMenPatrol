package com.ecity.cswatersupply.emergency.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.z3app.android.util.StringUtil;

public class UniversalImageLoaderUtil {
    private static DisplayImageOptions options;

    static {
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(false).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }
    
    public static void loadImage(final Context context, String profileUrl, ImageView targetImageView) {
        profileUrl = getSafeImageUrl(profileUrl);

        View imageLayout = LayoutInflater.from(context).inflate(R.layout.item_pager_image, null, false);
        final ImageView loadingView = (ImageView) imageLayout.findViewById(R.id.loading);
        ImageLoader.getInstance().displayImage(profileUrl, targetImageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                loadingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Input/Output error";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                    default:
                        break;
                }
                LogUtil.e(context, message + ", imageUri=" + imageUri);
                message = context.getString(R.string.load_image_error);
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Load an image item in a grid view. <br>
     * The grid view view holder should use R.layout.item_grid_image which must consist of an ImageView and a ProgressBar.
     * @param context
     * @param profileUrl
     * @param holderImageView The image view in the view holder of an item.
     * @param holderProgressBar The progress bar in the view holder of an item.
     */
    public static void loadImage(final Activity context, String profileUrl, ImageView holderImageView, final ProgressBar holderProgressBar) {
        profileUrl = getSafeImageUrl(profileUrl);

        ImageLoader.getInstance().displayImage(profileUrl, holderImageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holderProgressBar.setProgress(0);
                holderProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holderProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holderProgressBar.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                holderProgressBar.setProgress(Math.round(100.0f * current / total));
            }
        });
    }

    public static void cleanImagCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }

    public static String getSafeImageUrl(String path) {
        if (StringUtil.isBlank(path) || path.length() < 5) {
            return "";
        }

        String prefix = path.substring(0, 5);
        if (!prefix.equalsIgnoreCase("http:") && !prefix.equalsIgnoreCase("https")) {
            path = "file:////" + path;
        }

        return path;
    }
}
