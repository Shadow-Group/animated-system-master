

package com.osama.project34.About;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.osama.project34.R;


public class ShowTeamActivity extends AppCompatActivity {
 private String mfirstDevelporName="Usama Bin Omar";
    private String msecondDevelporsName="Hamza Muhammad Latif";
    private String mthirdDevelporsName="Muahmmad Ahmad Nasim";
    private String mworkFirstDevelpor="Encryption Specialist";
    private String mworkSecondDevelpor="Design And Implementation";
    private String mworkThirdDevelpor="Database specialist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_team);
        TextView fdtextview=(TextView)findViewById(R.id.firstdevelpor_textview);
        TextView sdtextview=(TextView)findViewById(R.id.seconddevelpor_textview);
        TextView tdtextview=(TextView)findViewById(R.id.thirddevelpor_textview);
        TextView fdworktextview=(TextView)findViewById(R.id.work_first_develpor_textview);
        TextView sdworktextview=(TextView)findViewById(R.id.work_second_develpor_textview);
        TextView tdworktextview=(TextView)findViewById(R.id.work_Third_develpor_textview);

        fdtextview.setText(mfirstDevelporName);
        fdworktextview.setText(mworkFirstDevelpor);
        sdtextview.setText(msecondDevelporsName);
        sdworktextview.setText(mworkSecondDevelpor);
        tdtextview.setText(mthirdDevelporsName);
        tdworktextview.setText(mworkThirdDevelpor);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Developers");
        }
    }
}
