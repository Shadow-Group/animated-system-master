package com.osama.project34.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.*;

import com.osama.project34.R;

/**
 * Created by patriot on 7/1/17.
 */

public class EmailView extends Activity{
    @Override
    public boolean onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState,Menu menu) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_view);
        MenuInflater inflater1 = getMenuInflater();
        inflater1.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
}
