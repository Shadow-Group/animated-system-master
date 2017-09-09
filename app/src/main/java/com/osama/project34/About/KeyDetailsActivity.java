
package com.osama.project34.About;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.osama.project34.R;
import com.osama.project34.encryption.MyPGPUtil;
import com.osama.project34.utils.ConfigManager;

import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPPublicKey;

import java.io.IOException;

public class KeyDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_details);
        Toolbar toolbar= (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Key Details");
     ((TextView)findViewById(R.id.key_details_name_textview)).setText(ConfigManager.getEmail());
    fillKeyDetails();
}

    private void fillKeyDetails() {
        try {
            PGPPublicKey key= MyPGPUtil.readPublicKey(getFilesDir()+"/"+"pub.asc");
            ((TextView)findViewById(R.id.key_details_keyid_textview)).setText(""+key.getKeyID());
            ((TextView)findViewById(R.id.key_details_keysize_textview)).setText(""+key.getBitStrength());
            if(key.getValidSeconds()==0){
                ((TextView)findViewById(R.id.key_details_keyalgo_textview)).setText("Key does not expire");
            }else{
                ((TextView)findViewById(R.id.key_details_keyalgo_textview)).setText(""+key.getValidSeconds());
            }
            ((TextView)findViewById(R.id.key_details_keytime_textview)).setText(""+key.getCreationTime());
        } catch (IOException | PGPException e) {
            e.printStackTrace();
        }
    }
}
