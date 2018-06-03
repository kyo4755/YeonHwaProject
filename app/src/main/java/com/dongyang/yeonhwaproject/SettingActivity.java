package com.dongyang.yeonhwaproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by Kim Jong-Hwa on 2018-05-26.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton cancel_btn;
    EditText edit_it, edit_pw, edit_re_pw, edit_email, edit_address;
    Button setting_btn;
    Spinner email_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        cancel_btn = findViewById(R.id.setting_cancel);
        cancel_btn.setOnClickListener(this);

        edit_it = findViewById(R.id.setting_edittext_id);
        edit_pw = findViewById(R.id.setting_edittext_pw);
        edit_re_pw = findViewById(R.id.setting_edittext_re_pw);
        edit_email = findViewById(R.id.setting_edittext_email);
        edit_address = findViewById(R.id.setting_edittext_address);

        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);

        email_spinner = findViewById(R.id.setting_email_spinner);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_cancel:
                finish();
                overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
                break;
        }
    }
}
