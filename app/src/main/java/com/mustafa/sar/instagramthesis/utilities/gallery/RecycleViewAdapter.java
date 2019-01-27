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
/*    private ProgressBar progressBar;
    private SquareImageViewForScaling img;*/
    private CustomOnItemClickListener clickListener;

    public RecycleViewAdapter(Context mContext, int layoutResource, String append, ArrayList<String> imgURLs, CustomOnItemClickListener clickListener) {
        super();
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        this.layoutResource = layoutResource;
        this.append = append;
        this.imgURLs = imgURLs;
        this.clickListener = clickListener;
    }
    //onCreateViewHolderis called whenever a new instance of our ViewHolder class is created
    //This method is called right when the adapter is created and is used to initialize your ViewHolder(s)
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(layoutResource, viewGroup, false);
        return new ViewHolder(view);
    }

    //is called when the SO binds the view with the data — or, in other words, the data is shown in the UI
    //This method is called for each ViewHolder to bind it to the adapter. This is where we will pass our data to our ViewHolder.
    @Override
    public void onBindViewHolder(final RecycleViewAdapter.ViewHolder viewHolder, int i) {

        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final String imgURL = getItem(i);

        //Calling set Image that calls ImageLoader.getInstance() and it calls displayImage
        UniversalImageLoader.setImage(imgURL, viewHolder.img, viewHolder.progressBar, append);

        /*1)))))*/
        /*Either you put the listener here or down in the ViewHolder */
//        viewHolder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickListener.onClick(v, viewHolder.getAdapterPosition());
//            }
//        });


    }
    public interface CustomOnItemClickListener {
        public void onClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return imgURLs.size();
    }

    public String getItem(int position) {
        return imgURLs.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private SquareImageViewForScaling img;

        /*2)))))*/
        /*Either you put the listener here or Up in the onBindViewHolder */
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(v, getAdapterPosition());
                }
            });

            progressBar = (ProgressBar) view.findViewById(R.id.progressGridView);
            img = (SquareImageViewForScaling) view.findViewById(R.id.imageGridView);
        }
    }
}
