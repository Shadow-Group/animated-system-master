package com.osama.project34.utils;

import android.os.Environment;

import com.osama.project34.MailApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bullhead on 9/6/17.
 *
 */

public final class FileUtils {
    private static final String OUR_DIR= MailApplication.getInstance().getFilesDir().getAbsolutePath()+"/";
    public static String createTempAttachment(InputStream in,String filename) throws IOException{
        File file=new File(OUR_DIR+filename);
        OutputStream out=new BufferedOutputStream(new FileOutputStream(file));
        byte[] buffer=new byte[2048];
        int data=-1;
        while ((data=in.read(buffer))!=-1){
            out.write(buffer,0,data);
        }
        in.close();
        out.close();
        return file.getAbsolutePath();
    }
}
