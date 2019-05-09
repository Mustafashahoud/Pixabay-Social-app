package com.mustafa.sar.pixabayscoialapp.viewmodels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mustafa.sar.pixabayscoialapp.Home.DetailActivity;
import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.models.Image;

public class ImageViewModel extends BaseObservable {
    private Image image;

    public ImageViewModel(Image image) {
        this.image = image;
    }

    public String getTags() {
        return image.getTags();
    }

    public String getImageUrl() {
        return image.getPreviewUrl();
    }

    public String getHighResImageUrl() {
        return image.getWebFormatUrl();
    }

    public String getLikes() {
        return image.getLikes();
    }

    public String getComments() {
        return image.getComments();
    }

    public String getFavorites() {
        return image.getFavorites();
    }

    public String getUserName() {
        return image.getUser();
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder_pix)
                .into(view);
    }

    public View.OnClickListener openDetails() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                alertDialog.setMessage(R.string.more_details).setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                                String serializedImg = new Gson().toJson(image);
                                intent.putExtra(DetailActivity.PIXABAY_IMAGE, serializedImg);
                                view.getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        };
    }
}
