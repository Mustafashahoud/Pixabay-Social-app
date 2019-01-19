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


public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";
    private static final int FRAGMENT_PHOTO_NUM = 0;
    /*
    * The integer argument is a "request code" that identifies your request.
    * When you receive the result Intent, the callback provides the same request code
    * so that your app can properly identify the result and determine how to handle it.*/
    private static final int REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");


        Button btnLunchCamera = (Button) view.findViewById(R.id.btnLaunchCameraPhoto);
        btnLunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((ShareActivity)getActivity()).getCurrentTabNumber() == FRAGMENT_PHOTO_NUM){
                    if (((ShareActivity)getActivity()).checkPermission(Permission.CAMERA_PERMISSION)){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, REQUEST_CODE);
                    }
                }else{
                    Intent intent = new Intent(getActivity(), ShareActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
                Log.d(TAG, "onActivityResult: We've receive the photo");

        }

    }
}


