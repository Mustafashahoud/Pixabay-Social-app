package com.mustafa.sar.instagramthesis.utilities;

import android.os.Environment;

public class FileDirectory {
    //"storage/emulated/0 "
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String CAMERA = ROOT_DIR + "/DCIM/Camera";
    public String PICTURES = ROOT_DIR + "/Pictures";

}
