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
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

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
                finishActivity();
                break;
            case R.id.login_btn:
                tryLogin(v);
                break;
            case R.id.login_register_btn:
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
                overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
                break;
        }
    }

    private void tryLogin(final View view) {
        AQuery aQuery = new AQuery(LoginActivity.this);
        String login_url = GlobalInfo.SERVER_URL + "users/login";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("id", edit_id.getText().toString());
        params.put("pw", edit_pw.getText().toString());

        aQuery.ajax(login_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Snackbar.make(view, "아이디를 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0002")){
                        Snackbar.make(view, "비밀번호를 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0010")){
                        Snackbar.make(view, "아이디와 비밀번호를 확인해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        JSONObject detailObj = new JSONObject(jsonObject.get("my_profile").toString());

                        GlobalInfo.isLogin = true;
                        GlobalInfo.user_id = detailObj.get("id").toString();
                        GlobalInfo.user_name = detailObj.get("name").toString();
                        GlobalInfo.user_phone = detailObj.get("phone").toString();
                        GlobalInfo.user_email = detailObj.get("email").toString();
                        GlobalInfo.user_image = detailObj.get("img_num").toString();

                        Toast.makeText(LoginActivity.this, "성공적으로 로그인이 되었습니다.", Toast.LENGTH_SHORT).show();
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
