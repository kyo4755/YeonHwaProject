package com.dongyang.yeonhwaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by JongHwa on 2018-05-29.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ImageButton cancel_btn;
    EditText edit_id, edit_pw;
    Button login_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        cancel_btn = findViewById(R.id.login_cancel);
        edit_id = findViewById(R.id.login_edittext_id);
        edit_pw = findViewById(R.id.login_edittext_pw);
        login_btn = findViewById(R.id.login_btn);
        register_btn = findViewById(R.id.login_register_btn);

        cancel_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_cancel:
                finish();
                overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
                break;
            case R.id.login_btn:
                break;
            case R.id.login_register_btn:
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
                overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
                break;
        }
    }
}
