package com.mustafa.sar.instagramthesis.utilities;

import android.Manifest;

public class Permission {
    public static final String[] PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE ,
            Manifest.permission.READ_EXTERNAL_STORAGE ,
            Manifest.permission.CAMERA
    };
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static final String WRITE_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
}
