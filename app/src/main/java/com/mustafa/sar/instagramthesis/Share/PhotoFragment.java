package com.mustafa.sar.instagramthesis.Share;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.mustafa.sar.instagramthesis.Profile.AccountSettingActivity;
import com.mustafa.sar.instagramthesis.R;
import com.mustafa.sar.instagramthesis.utilities.Permission;
import java.io.ByteArrayOutputStream;

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
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

                if(((ShareActivity)getActivity()).getCurrentTabNumber() == REQUEST_IMAGE_CAPTURE){
                    if (((ShareActivity)getActivity()).checkPermission(Permission.CAMERA_PERMISSION)) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                          //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("MyImageCapture")));
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                        }
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
        //that means we wanna share a photo from camera
        if (isRootTask()){
            if (requestCode == REQUEST_IMAGE_CAPTURE ) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                /*TODO it is not as it executes very slowly*/
                Uri tempUri = getImageUri(getActivity(), imageBitmap);
                Log.d(TAG, "onActivityResult: " + tempUri);
                String url = getRealPathFromUri(getActivity(), tempUri);

                Intent intent = new Intent(getActivity(), SelectedImgActivity.class);
                intent.putExtra("photo_URL_bitmap", url);
                startActivity(intent);
            }
        }
        //that means we wanna change the profile photo from camera
        else{
            // Make sure the request was successful
            // Check which request we're responding to
            if (requestCode == REQUEST_IMAGE_CAPTURE ) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                /*TODO it is not as it executes very slowly*/
                Uri tempUri = getImageUri(getActivity(), imageBitmap);
                Log.d(TAG, "onActivityResult: " + tempUri);
                String url = getRealPathFromUri(getActivity(), tempUri);

                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                intent.putExtra("photo_URL_bitmap", url);
                intent.putExtra("return_to_fragment", getString(R.string.edit_profile_fragment));

                // Finish the activity that holds the fragment to get the back arrow work properly
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Is root task boolean.
     *
     * @return  true if we are coming from shareActivity willing to share a photo directly NOT by pressing changeProfilePhoto willing to change the profile photo
     */
    public boolean isRootTask(){
        if (((ShareActivity)getActivity()).getIntentFlag() == 0){
            return  true;
        }
        else return false;
    }

}


