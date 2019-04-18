package com.mustafa.sar.instagramthesis.utilities;

import android.os.Environment;

public class FileDirectory {
    //"storage/emulated/0 "
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String CAMERA = ROOT_DIR + "/DCIM/Camera";
    public String WHATS_APP = ROOT_DIR + "/WhatsApp/Media/WhatsApp Images";
    public String PICTURES = ROOT_DIR + "/Pictures";

    public String STORAGE_PHOTO_PATH = "photos/users/";

}
