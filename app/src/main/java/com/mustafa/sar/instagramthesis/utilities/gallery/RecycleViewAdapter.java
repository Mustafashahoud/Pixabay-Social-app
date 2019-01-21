package com.mustafa.sar.instagramthesis.utilities.gallery;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.UniversalImageLoader;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private int layoutResource;
    private String append;
    private ArrayList<String> imgURLs;
    private UniversalImageLoader universalImageLoader;
     ProgressBar progressBar;
     SquareImageViewForScaling img;

    public RecycleViewAdapter(Context mContext, int layoutResource, String append, ArrayList<String> imgURLs) {
        super();
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.append = append;
        this.imgURLs = imgURLs;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(layoutResource, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapter.ViewHolder viewHolder, int i) {

        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String imgURL = getItem(i);

        universalImageLoader = new UniversalImageLoader(mContext);

        universalImageLoader.setImage(  imgURL, img, progressBar, append );

//        ImageLoader imageLoafer = ImageLoader.getInstance();
//        imageLoafer.displayImage(append + imgURL, img, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                if (progressBar != null) {
//                    progressBar.setVisibility(View.VISIBLE);
//
//                }
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                if (progressBar != null) {
//                    progressBar.setVisibility(View.GONE);
//
//                }
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                if (progressBar != null) {
//                    progressBar.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                if (progressBar != null) {
//                    progressBar.setVisibility(View.GONE);
//                }
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return imgURLs.size();
    }

    public String getItem(int position) {
        return imgURLs.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ProgressBar progressBar;
//        private SquareImageViewForScaling img;

        public ViewHolder(View view) {
            super(view);

            progressBar = (ProgressBar) view.findViewById(R.id.progressGridView);
            img = (SquareImageViewForScaling) view.findViewById(R.id.imageGridView);
        }
    }
}
