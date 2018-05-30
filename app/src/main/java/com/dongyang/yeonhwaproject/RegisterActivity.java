package com.dongyang.yeonhwaproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Created by JongHwa on 2018-05-29.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton cancel_btn;
    EditText edit_it, edit_pw, edit_re_pw, edit_email, edit_address;
    Button register_btn;
    Spinner email_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        cancel_btn = findViewById(R.id.register_cancel);
        cancel_btn.setOnClickListener(this);

        edit_it = findViewById(R.id.register_edittext_id);
        edit_pw = findViewById(R.id.register_edittext_pw);
        edit_re_pw = findViewById(R.id.register_edittext_re_pw);
        edit_email = findViewById(R.id.register_edittext_email);
        edit_address = findViewById(R.id.register_edittext_address);

        register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(this);

        email_spinner = findViewById(R.id.register_email_spinner);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_cancel:
                finish();
                overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
                break;
        }
    }
}
