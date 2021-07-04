package com.ams.store.whatsappstatussaver2021.Utils;

import android.os.Environment;

import java.io.File;

public class MyConstants {


    public static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp/Media/.Statuses");
    public static final String APP_DIR = Environment.getExternalStorageDirectory() +File.separator +"WhatsAppSavedStatus";
    public static final File APp_DIRECTORY = new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsAppSavedStatus");
    public static final int THUMBSIZE= 128;


}
