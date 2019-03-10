package com.mustafa.sar.instagramthesis.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mustafa.sar.instagramthesis.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class UniversalImageLoader {

    private static final int defaultImage = R.drawable.solid_white;

    private Context mContext;

    public UniversalImageLoader(Context mContext) {
        this.mContext = mContext;
    }

    public ImageLoaderConfiguration getConfig() {
        /*Caching is NOT enabled by default. If you want loaded images to be cached in memory and/or
         on disk then you should enable caching in DisplayImageOptions this way*/
        /*
         * Display Options (DisplayImageOptions) are local for every display task (ImageLoader.displayImage(...)).
         * Display Options can be applied to every display task (ImageLoader.displayImage(...) call).
         * ImageLoader.displayImage(new DisplayImageOptions)
         */
        BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheOnDisk(true)
                .cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(defaultImage)
                .considerExifParams(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .decodingOptions(decodingOptions)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        /*ImageLoader Configuration (ImageLoaderConfiguration) is global for application. You should set it once.*/
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs()
                .build();

        return config;

    }

    /**
     * This method can be used to set images that are static , it can't be used if the images are being changed
     * in the fragment/activity OR they are set in a list or a gridView
     *
     * @param imgURL
     * @param imageView
     * @param progressBar
     * @param append
     */
    public static void setImage(String imgURL, ImageView imageView, final ProgressBar progressBar, String append) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}
