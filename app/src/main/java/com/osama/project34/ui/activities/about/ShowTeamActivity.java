package com.osama.project34.ui.activities.about;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.osama.project34.R;
import com.osama.project34.ui.activities.BaseActivity;


public class ShowTeamActivity extends BaseActivity {
    private String mfirstDevelporName = "Osama Bin Omar";
    private String msecondDevelporsName = "Hamza Muhammad Latif";
    private String mthirdDevelporsName = "Muahmmad Ahmad Nasim";
    private String mworkFirstDevelpor = "Encryption Specialis";
    private String mworkSecondDevelpor = "Design And Implementation";
    private String mworkThirdDevelpor = "Database specialist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView fdtextview = (TextView) findViewById(R.id.firstdevelpor_textview);
        TextView sdtextview = (TextView) findViewById(R.id.seconddevelpor_textview);
        TextView tdtextview = (TextView) findViewById(R.id.thirddevelpor_textview);
        TextView fdworktextview = (TextView) findViewById(R.id.work_first_develpor_textview);
        TextView sdworktextview = (TextView) findViewById(R.id.work_second_develpor_textview);
        TextView tdworktextview = (TextView) findViewById(R.id.work_Third_develpor_textview);

        fdtextview.setText(mfirstDevelporName);
        fdworktextview.setText(mworkFirstDevelpor);
        sdtextview.setText(msecondDevelporsName);
        sdworktextview.setText(mworkSecondDevelpor);
        tdtextview.setText(mthirdDevelporsName);
        tdworktextview.setText(mworkThirdDevelpor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Developers");
        }
    }
}
