package com.osama.project34.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.osama.project34.data.Key;

/**
 * Created by bullhead on 9/7/17.
 *
 */

public class FirebaseHandler {
    private static final String TAG=FirebaseHandler.class.getCanonicalName();

    private DatabaseReference databaseReference;
    private static FirebaseHandler instance;

    private FirebaseHandler(){
        databaseReference=FirebaseDatabase.getInstance().getReference(DatabaseKeys.ROOT_REFERENCE);
    }
    public static FirebaseHandler getInstance() {
        if (instance==null){
            instance=new FirebaseHandler();
        }
        return instance;
    }

    public void saveKey(Key key){
        databaseReference.setValue(key);
    }

}
