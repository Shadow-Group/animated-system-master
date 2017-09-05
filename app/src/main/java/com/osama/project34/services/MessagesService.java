package com.osama.project34.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by bullhead on 9/5/17.
 *
 */

public class MessagesService extends Service {
    private IBinder mBinder=new LocalBinder();

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return START_NOT_STICKY;
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class LocalBinder extends Binder{
        public MessagesService getInstance(){
            return MessagesService.this;
        }
    }
}
