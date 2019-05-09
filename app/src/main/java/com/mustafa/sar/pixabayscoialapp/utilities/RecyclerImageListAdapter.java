package com.mustafa.sar.pixabayscoialapp.utilities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.databinding.ImageItemBinding;
import com.mustafa.sar.pixabayscoialapp.models.Image;
import com.mustafa.sar.pixabayscoialapp.viewmodels.ImageViewModel;

import java.util.List;


public class RecyclerImageListAdapter extends RecyclerView.Adapter<RecyclerImageListAdapter.MyViewHolder> {

    private List<Image> imageList;

     static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageItemBinding imageItemBinding;

        private MyViewHolder(View v) {
            super(v);
            imageItemBinding = ImageItemBinding.bind(v);
        }
    }

    public RecyclerImageListAdapter(List<Image> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      holder.imageItemBinding.setViewmodel((new ImageViewModel(imageList.get(position))));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
