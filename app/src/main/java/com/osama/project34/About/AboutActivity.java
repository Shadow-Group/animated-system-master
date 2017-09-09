
package com.osama.project34.About;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.osama.project34.R;


public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.team).setOnClickListener(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("About");
        }
    }
    public void onLicenseClick(View view){


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.team:
                startActivity(new Intent(this,ShowTeamActivity.class));
                break;
        }
    }
}
