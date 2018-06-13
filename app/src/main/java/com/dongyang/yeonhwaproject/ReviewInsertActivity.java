package com.dongyang.yeonhwaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by JongHwa on 2018-06-13.
 */

public class ReviewInsertActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ImageButton cancel_btn;
    RatingBar ratingBar;
    EditText editText;
    Button sendBtn;

    float rate = 0.0f;

    String hpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_insert_main);

        toolbar = findViewById(R.id.review_insert_toolbar);
        setSupportActionBar(toolbar);

        Intent it = getIntent();
        hpid = it.getStringExtra("hpid");

        ratingBar = findViewById(R.id.review_insert_ratingbar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });

        editText = findViewById(R.id.review_insert_edittext);

        sendBtn = findViewById(R.id.review_insert_btn);
        sendBtn.setOnClickListener(this);
        cancel_btn = findViewById(R.id.review_insert_cancel);
        cancel_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.review_insert_cancel:
                finishActivity();
                break;
            case R.id.review_insert_btn:
                tryReviewSend(v);
                break;
        }
    }

    private void tryReviewSend(final View view) {
        AQuery aQuery = new AQuery(ReviewInsertActivity.this);
        String reviewInsertUrl = GlobalInfo.SERVER_URL + "review/insert";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("hpid", hpid);
        params.put("user_id", GlobalInfo.user_id);
        params.put("user_name", GlobalInfo.user_name);
        params.put("user_point", String.valueOf(rate));
        params.put("user_msg", editText.getText().toString());
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis()));
        params.put("user_date", time);

        aQuery.ajax(reviewInsertUrl, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Snackbar.make(view, "HPID 값이 없습니다.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0002")){
                        Snackbar.make(view, "사용자 ID 값이 없습니다.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0003")){
                        Snackbar.make(view, "사용자 닉네임 값이 없습니다.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0004")){
                        Snackbar.make(view, "사용자 점수 값이 없습니다.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0005")){
                        Snackbar.make(view, "내용을 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        Toast.makeText(ReviewInsertActivity.this, "리뷰가 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
    }
}
