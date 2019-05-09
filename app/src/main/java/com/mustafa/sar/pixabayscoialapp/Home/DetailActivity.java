package com.mustafa.sar.pixabayscoialapp.Home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.Share.SelectedImgActivity;
import com.mustafa.sar.pixabayscoialapp.databinding.ActivityDetailsBinding;
import com.mustafa.sar.pixabayscoialapp.models.Image;
import com.mustafa.sar.pixabayscoialapp.viewmodels.ImageViewModel;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDetailsBinding mActivityDetailsBinding;
    public final static String PIXABAY_IMAGE = "PIXABAY_IMAGE";
    public final static String IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_LOW_QUALITY = "Image From Pixabay to be uploaded low quality";
    public final static String IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_High_QUALITY = "Image From Pixabay to be uploaded high quality";
    private Image image;
    private TextView shareTextView;
    private String imgUrlLowQuality = "";
    private String imgUrlHighQuality = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        initImage();
        shareTextView = findViewById(R.id.shareTextView);
        shareTextView.setOnClickListener(this);
       mActivityDetailsBinding.setViewmodel(new ImageViewModel(image));
    }
    private void initImage() {
        //Deserializes the Gson(String) into an Image obj
        image = new Gson().fromJson(getIntent().getStringExtra(PIXABAY_IMAGE), Image.class);
    }

    @Override
    public void onClick(View v) {
        imgUrlLowQuality = image.getPreviewUrl();
        imgUrlHighQuality = image.getWebFormatUrl();
        Intent intent = new Intent(this, SelectedImgActivity.class);
        intent.putExtra(IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_LOW_QUALITY, imgUrlLowQuality);
        intent.putExtra(IMAGE_FROM_PIXABAY_TO_BE_UPLOADED_High_QUALITY, imgUrlHighQuality);
        startActivity(intent);
    }
}
