package com.osama.project34.utils;

import android.os.Environment;
import android.provider.MediaStore;

import com.osama.project34.MailApplication;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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

    public static File createFromString(String message,final String filename) throws IOException{
        File file=new File(OUR_DIR+filename);
        BufferedWriter stream=new BufferedWriter(new FileWriter(file));
        stream.write(message);
        stream.flush();
        stream.close();
        return file;
    }

    public static File createTempFile() {
            return new File(OUR_DIR+"temp.file");
    }

    public static String getStringFromFile(File outputFile){
        try {
            FileInputStream fis = new FileInputStream(outputFile);
            byte[] data = new byte[(int) outputFile.length()];
            fis.read(data);
            fis.close();

            String str = new String(data, "UTF-8");

            return str;
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }
    }
}
