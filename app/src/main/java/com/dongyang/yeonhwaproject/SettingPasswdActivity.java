package com.dongyang.yeonhwaproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Kim Jong-Hwa on 2018-05-26.
 */

public class SettingPasswdActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton cancel_btn;
    EditText old_pw, new_pw, re_pw;
    Button setting_pw_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_passwd);
        cancel_btn = findViewById(R.id.setting_cancel);
        cancel_btn.setOnClickListener(this);
        setting_pw_btn = findViewById(R.id.setting_pw_btn);
        setting_pw_btn.setOnClickListener(this);

        old_pw = findViewById(R.id.setting_old_pw);
        new_pw = findViewById(R.id.setting_new_pw);
        re_pw = findViewById(R.id.setting_re_pw);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_cancel:
                finishActivity();
                break;
            case R.id.setting_pw_btn:
                if(new_pw.getText().toString().equals(re_pw.getText().toString())){
                    settingPasswd(v);
                }
               else
                    Snackbar.make(v, "비밀번호 확인이 일치하지 않습니다.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                break;
        }
    }
    private void settingPasswd(final View view) {
        AQuery aQuery = new AQuery(SettingPasswdActivity.this);
        String settingPasswd_url = GlobalInfo.SERVER_URL + "users/changePassword";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("id", GlobalInfo.user_id);
        params.put("old_pw", old_pw.getText().toString());
        params.put("new_pw", new_pw.getText().toString());

        aQuery.ajax(settingPasswd_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Snackbar.make(view, "아이디 오류.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0002")){
                        Snackbar.make(view, "기존 비밀번호를 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0003")){
                        Snackbar.make(view, "변경할 비밀번호를 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0010")){
                        Snackbar.make(view, "에러.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0000")){
                        Toast.makeText(SettingPasswdActivity.this, "비밀번호 변경이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
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
