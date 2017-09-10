package com.osama.project34.ui.activities.about;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.osama.project34.R;
import com.osama.project34.ui.activities.BaseActivity;
import com.osama.project34.utils.ConfigManager;


public class ShowTeamActivity extends BaseActivity {
    private String mfirstDevelporName = "Osama Bin Omar (bullhead)";
    private String msecondDevelporsName = "Hamza Muhammad Latif (shadow)";
    private String mthirdDevelporsName = "Muahammad Ahmad Nasim (patriot)";
    private String mworkFirstDevelpor = "Encryption library implementation.";
    private String mworkSecondDevelpor = "UI designer and application interface architect ";
    private String mworkThirdDevelpor = "IMAP library design and implementation";

    private String firstGithub="https://github.com/mosamabinomar";
    private String secondGithub="https://github.com/Shadow-Group";
    private String thirdGithub="https://github.com/patriot321";

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
        findViewById(R.id.first_developer_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAlertForGitHub(firstGithub);
            }
        });
        findViewById(R.id.second_developer_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAlertForGitHub(secondGithub);
            }
        });
        findViewById(R.id.third_developer_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForGitHub(thirdGithub);
            }
        });
    }
    private void openDeveloperGithub(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    private void showAlertForGitHub(final String url){
        new AlertDialog.Builder(this, ConfigManager.isDarkTheme()?R.style.DialogStyleDark:R.style.DialogStyleLight)
                .setMessage("Would you like to check developer's profile on GitHub")
                .setTitle("GitHub")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openDeveloperGithub(url);
                    }
                })
                .show();
    }
}
