package com.dongyang.yeonhwaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by JongHwa on 2018-06-13.
 */

public class ReviewInsertActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ImageButton cancel_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_insert_main);

        toolbar = findViewById(R.id.review_insert_toolbar);
        setSupportActionBar(toolbar);

        cancel_btn = findViewById(R.id.review_insert_cancel);
        cancel_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.review_insert_cancel:
                finish();
                overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
                break;
            case R.id.login_btn:
                break;
        }
    }
}
