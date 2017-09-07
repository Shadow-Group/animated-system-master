package com.osama.project34.encryption;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.osama.project34.MailApplication;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.openpgp.PGPKeyRingGenerator;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPSecretKeyRing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by bullhead on 9/7/17.
 *
 */

public class EncryptionHandler {
    public static void genererateKeys(final String username,final String password,final OnKeysGenerated listener){
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                char[] pass             = password.toCharArray();
                try {
                    Log.d("bullhead","start generating keys");
                    PGPKeyRingGenerator keyRingGenerator    = new KeyManagement().generateKey(username,pass);
                    PGPPublicKeyRing publicKeys             = keyRingGenerator.generatePublicKeyRing();
                    PGPSecretKeyRing secretKeys             = keyRingGenerator.generateSecretKeyRing();

                    //output keys in ascii armored format
                    File file                   = new File(MailApplication.getInstance().getFilesDir(),"pub.asc");
                    ArmoredOutputStream pubOut  = new ArmoredOutputStream(new FileOutputStream(file));
                    publicKeys.encode(pubOut);
                    pubOut.close();

                    file                        = new File(MailApplication.getInstance().getCacheDir(),"sec.asc");
                    ArmoredOutputStream secOut  = new ArmoredOutputStream(new FileOutputStream(file));
                    secretKeys.encode(secOut);
                    secOut.close();

                    return true;


                } catch (Exception e) {
                    Log.d("bullhead","Error generating keys");
                    e.printStackTrace();
                    return false;

                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    listener.onSuccess();
                }else{
                    listener.onError();
                }
            }
        }.execute();
    }
    public interface OnKeysGenerated{
        void onSuccess();
        void onError();
    }

}
