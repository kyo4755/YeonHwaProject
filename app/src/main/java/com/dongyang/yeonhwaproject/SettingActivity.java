package com.dongyang.yeonhwaproject;

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

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton cancel_btn;
    TextView edit_id;
    ImageView setting_img;
    EditText edit_name,  edit_pw, edit_re_pw, edit_email, edit_phone;
    Button setting_btn;
    Spinner email_spinner;

    public RequestManager glideManager;

    private String email_form = "naver.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        glideManager = Glide.with(this);

        cancel_btn = findViewById(R.id.setting_cancel);
        cancel_btn.setOnClickListener(this);

        setting_img = findViewById(R.id.setting_profile);
        edit_id = findViewById(R.id.setting_edittext_id);
        edit_name = findViewById(R.id.setting_edittext_name);
        edit_email = findViewById(R.id.setting_edittext_email);
        edit_phone = findViewById(R.id.setting_edittext_phone);
        edit_phone.addTextChangedListener(new TextWatcher() {
            int prevL = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prevL < length) && (length == 3 || length == 8)) {
                    String data = edit_phone.getText().toString();
                    edit_phone.setText(data + "-");
                    edit_phone.setSelection(length + 1);
                }

            }
        });

        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);

        email_spinner = findViewById(R.id.setting_email_spinner);
        email_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                email_form = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(GlobalInfo.isLogin){
            edit_id.setText(GlobalInfo.user_name);
            edit_name.setText(GlobalInfo.user_name);
            //edit_pw.setText(GlobalInfo.user_pw);
            edit_email.setText(GlobalInfo.user_email);
            edit_phone.setText(GlobalInfo.user_phone);

            if(GlobalInfo.user_image.equals("null")){
                glideManager.load(R.drawable.default_user)
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(SettingActivity.this))
                        .into(setting_img);
            } else {
                String img_url = GlobalInfo.SERVER_URL + "users/getPhoto?id=" + GlobalInfo.user_image;
                glideManager.load(img_url)
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(SettingActivity.this))
                        .into(setting_img);
            }
        }
        else {
           /* Toast.makeText(this,"로그인해주세요",Toast.LENGTH_LONG);
            finish();*/
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_cancel:
                finishActivity();
                break;
            case R.id.setting_btn:
                if(edit_id == null) {
                    settingProcess(v);
                }
                break;
        }
    }
    private void settingProcess(final View view) {
        AQuery aQuery = new AQuery(SettingActivity.this);
        String setting_url = GlobalInfo.SERVER_URL + "users/setting";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("id", edit_id.getText().toString());
        params.put("pw", edit_pw.getText().toString());
        params.put("name", edit_name.getText().toString());
        params.put("phone", edit_phone.getText().toString());
        params.put("email", edit_email.getText().toString() + "@" + email_form);

        aQuery.ajax(setting_url, params, String.class, new AjaxCallback<String>(){
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
                    } else if(result_code.equals("0003")){
                        Snackbar.make(view, "닉네임을 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0004")){
                        Snackbar.make(view, "전화번호를 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0005")){
                        Snackbar.make(view, "이메일을 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0010")){
                        Snackbar.make(view, "중복되는 아이디가 존재합니다.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "개인설정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
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
