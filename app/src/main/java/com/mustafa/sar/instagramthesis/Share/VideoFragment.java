package com.mustafa.sar.instagramthesis.Share;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.Permission;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;


public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";
    private static final int FRAGMENT_VIDEO_NUM = 2;
    /*
    * The integer argument is a "request code" that identifies your request.
    * When you receive the result Intent, the callback provides the same request code
    * so that your app can properly identify the result and determine how to handle it.*/
    private static final int REQUEST_CODE = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        Log.d(TAG, "onCreateView: started.");


        Button btnLunchCamera = (Button) view.findViewById(R.id.btnLaunchCameraVideo);
        btnLunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ShareActivity)getActivity()).getCurrentTabNumber() == FRAGMENT_VIDEO_NUM){
                    if (((ShareActivity)getActivity()).checkPermission(Permission.CAMERA_PERMISSION)){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CODE);
                    }
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure the request was successful
        // Check which request we're responding to
        if(requestCode == REQUEST_CODE){
            // Make sure the request was successful
                Log.d(TAG, "onActivityResult: We've receive the video");
        }
    
    }
}


