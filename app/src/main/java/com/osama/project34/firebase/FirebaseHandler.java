package com.osama.project34.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.osama.project34.data.Key;

import java.util.ArrayList;
import java.util.Random;



public class FirebaseHandler {
    private static final String TAG ="bullhead";
    private DatabaseReference databaseReference;
    private static FirebaseHandler instance;
    private ArrayList<Key> keys;

    private FirebaseHandler() {
        databaseReference = FirebaseDatabase.getInstance().getReference(DatabaseKeys.ROOT_REFERENCE);
    }

    public static FirebaseHandler getInstance() {
        if (instance == null) {
            instance = new FirebaseHandler();
            instance.keys = new ArrayList<>();
        }
        return instance;
    }

    public void saveKey(Key key) {
        Log.d(TAG, "saveKey: started public key upload");
        String username= key.getUser();
        if (username.contains(".")){
            Log.d(TAG, "saveKey: username has \".\" in his/her name. Can't put with \".\" in db");
           username= username.replace(".","");
        }
        if (username.contains("@")){
            username=username.replace("@","");
        }
        databaseReference.child(username).setValue(key);
    }

    public void loadAllKeys() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Key key = child.getValue(Key.class);
                    if (key != null) {
                        Log.d(TAG, "onDataChange: user"+key.getUser()+" has key: "+key.getText());
                        keys.add(key);
                    } else {
                        Log.d(TAG, "onDataChange: key is null");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Key> getKeys() {
        return keys;
    }
}
