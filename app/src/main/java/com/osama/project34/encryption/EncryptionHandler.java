package com.osama.project34.encryption;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.osama.project34.MailApplication;
import com.osama.project34.data.Key;
import com.osama.project34.firebase.FirebaseHandler;
import com.osama.project34.utils.ConfigManager;
import com.osama.project34.utils.FileUtils;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.openpgp.PGPKeyRingGenerator;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPSecretKey;
import org.spongycastle.openpgp.PGPSecretKeyRing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;



public class EncryptionHandler {
    @SuppressLint("StaticFieldLeak")
    public static void generateKeys(final String username, final String password, final OnKeysGenerated listener) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                char[] pass = password.toCharArray();
                try {
                    Log.d("bullhead", "start generating keys");
                    PGPKeyRingGenerator keyRingGenerator = new KeyManagement().generateKey(username, pass);
                    PGPPublicKeyRing publicKeys = keyRingGenerator.generatePublicKeyRing();
                    PGPSecretKeyRing secretKeys = keyRingGenerator.generateSecretKeyRing();

                    //output keys in ascii armored format
                    File file = new File(MailApplication.getInstance().getFilesDir(), "pub.asc");
                    ArmoredOutputStream pubOut = new ArmoredOutputStream(new FileOutputStream(file));
                    publicKeys.encode(pubOut);
                    pubOut.close();

                    file = new File(MailApplication.getInstance().getFilesDir(), "sec.asc");
                    ArmoredOutputStream secOut = new ArmoredOutputStream(new FileOutputStream(file));
                    secretKeys.encode(secOut);
                    secOut.close();

                    //insert user public key on server database
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(outputStream);
                    publicKeys.encode(armoredOutputStream);
                    armoredOutputStream.close();

                    Key key = new Key();
                    key.setText(outputStream.toString());
                    key.setUser(username);
                    FirebaseHandler.getInstance().saveKey(key);
                    return true;


                } catch (Exception e) {
                    Log.d("bullhead", "Error generating keys");
                    e.printStackTrace();
                    return false;

                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    ConfigManager.setKeysGenerated();
                    listener.onSuccess();
                } else {
                    listener.onError();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public static void encryptFile(final File inputFile, final File outputFile, final File pubKey, final OnFileEncrypted listener) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {

                    return EncryptionWrapper.encryptFile(inputFile, outputFile, pubKey, true);

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    listener.onSuccess();
                } else {
                    listener.onError();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public static void decryptFile(final File encryptedFile, final File decryptedFile,
                                   final File publicKey, final File secKey, final String password, final OnFileDecrypted callback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Log.d("Message is: ", "doInBackground: " + FileUtils.getStringFromFile(encryptedFile));
                    return EncryptionWrapper.decryptFile(encryptedFile, decryptedFile, publicKey, new FileInputStream(secKey), password.toCharArray());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void importKeys(final String secKeyFileName, final OnKeysGenerated callback) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    PGPSecretKey key = MyPGPUtil.readSecretKey(secKeyFileName);
                    // if secret key is correct get the public key from it and save it
                    PGPPublicKey pub = key.getPublicKey();
                    //output keys in ascii armored format
                    File file = new File(MailApplication.getInstance().getFilesDir(), "pub.asc");
                    ArmoredOutputStream pubOut = new ArmoredOutputStream(new FileOutputStream(file));
                    pub.encode(pubOut);
                    pubOut.close();


                    file = new File(MailApplication.getInstance().getFilesDir(), "sec.asc");
                    ArmoredOutputStream secOut = new ArmoredOutputStream(new FileOutputStream(file));
                    key.encode(secOut);
                    secOut.close();

                    //insert user public key on server database
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(outputStream);
                    pub.encode(armoredOutputStream);
                    armoredOutputStream.close();

                    Key user = new Key();
                    user.setText(outputStream.toString());
                    user.setUser(ConfigManager.getEmail());
                    FirebaseHandler.getInstance().saveKey(user);
                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    ConfigManager.setKeysGenerated();
                    callback.onSuccess();
                } else {
                    callback.onError();
                }
            }
        }.execute();

    }


    public interface OnFileDecrypted {
        void onSuccess();

        void onError();
    }

    public interface OnFileEncrypted {
        void onSuccess();

        void onError();
    }

    public interface OnKeysGenerated {
        void onSuccess();

        void onError();
    }

}
