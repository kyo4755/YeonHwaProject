package com.dongyang.yeonhwaproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.FindFragment.FindDrugsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Kim Jong-Hwa on 2018-05-26.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton cancel_btn;
    TextView edit_id;
    ImageView setting_img;
    EditText edit_name, edit_email, edit_phone;
    Button setting_btn,setting_pw;
    Spinner email_spinner;
    private static final int SELECT_PICTURE = 1;
    public RequestManager glideManager;
    File selectedPhoto;


    private String email_form = "naver.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);

        glideManager = Glide.with(this);

        cancel_btn = findViewById(R.id.setting_cancel);
        cancel_btn.setOnClickListener(this);
        setting_pw = findViewById(R.id.setting_pw);
        setting_pw.setOnClickListener(this);

        setting_img = findViewById(R.id.setting_profile);
        edit_id = findViewById(R.id.setting_edittext_id);
        edit_name = findViewById(R.id.setting_edittext_name);
        edit_email = findViewById(R.id.setting_edittext_email);
        edit_phone = findViewById(R.id.setting_edittext_phone);

        //회원정보 가져오기

        edit_id.setText(GlobalInfo.user_id);
        edit_name.setText(GlobalInfo.user_name);
        edit_email.setText(GlobalInfo.user_email);
        edit_phone.setText(GlobalInfo.user_phone);

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
        setting_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // 사진 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        setting_btn = findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_cancel:
                finishActivity();
                break;
            case R.id.setting_btn:
                    settingProcess(v);
                break;
            case R.id.setting_pw:
                    Intent it = new Intent(SettingActivity.this, SettingPasswdActivity.class);
                    startActivity(it);
                    overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                File tmpCacheFile = new File(this.getCacheDir(), UUID.randomUUID() + ".jpg");

                if(fileCopy(selectedImageUri, tmpCacheFile)){
                    selectedPhoto = tmpCacheFile;

                    AQuery aQuery = new AQuery(this);
                    String url = GlobalInfo.SERVER_URL + "users/changePhoto";
                    Map<String, Object> params = new LinkedHashMap<>();
                    params.put("id", GlobalInfo.user_id);
                    params.put("image", selectedPhoto);

                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>(){
                        @Override
                        public void callback(String url, String result, AjaxStatus status) {
                            try{
                                JSONObject jsonObject = new JSONObject(result);
                                String result_value = jsonObject.get("result").toString();
                                String image_code = jsonObject.get("image").toString();

                                if(result_value.equals("0000")){
                                    String imgStr = GlobalInfo.SERVER_URL + "users/getPhoto?id=" + image_code;
                                    GlobalInfo.user_image = image_code;
                                    Glide.with(SettingActivity.this)
                                            .load(imgStr)
                                            .centerCrop()
                                            .bitmapTransform(new CropCircleTransformation(SettingActivity.this))
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .into(setting_img);
                                } else {
                                    Toast.makeText(SettingActivity.this, "서버와의 통신 중 오류가 발생했습니다. 나중에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e){

                            }
                        }
                    });
                }
            }
        }
    }
    protected boolean fileCopy(Uri in, File out) {
        try {
            File inFile = new File(in.getPath());
            InputStream is = new FileInputStream(inFile);
            // InputStream is =
            // context.getContentResolver().openInputStream(in);
            FileOutputStream outputStream = new FileOutputStream(out);

            BufferedInputStream bin = new BufferedInputStream(is);
            BufferedOutputStream bout = new BufferedOutputStream(outputStream);

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = bin.read(buffer, 0, 1024)) != -1) {
                bout.write(buffer, 0, bytesRead);
            }

            bout.close();
            bin.close();

            outputStream.close();
            is.close();
        } catch (IOException e) {
            InputStream is;
            try {
                is = this.getContentResolver().openInputStream(in);

                FileOutputStream outputStream = new FileOutputStream(out);

                BufferedInputStream bin = new BufferedInputStream(is);
                BufferedOutputStream bout = new BufferedOutputStream(outputStream);

                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = bin.read(buffer, 0, 1024)) != -1) {
                    bout.write(buffer, 0, bytesRead);
                }

                bout.close();
                bin.close();

                outputStream.close();
                is.close();


            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }

        }
        return true;
    }
    private void settingProcess(final View view) {
        AQuery aQuery = new AQuery(SettingActivity.this);
        String setting_url = GlobalInfo.SERVER_URL + "users/changeAll";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("id", edit_id.getText().toString());
        params.put("name", edit_name.getText().toString());
        params.put("email", edit_email.getText().toString());
        params.put("phone", edit_phone.getText().toString());

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
                        Snackbar.make(view, "닉네임을 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0003")){
                        Snackbar.make(view, "이메일을 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0004")){
                        Snackbar.make(view, "전화번호를 입력해 주세요.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0010")){
                        Snackbar.make(view, "에러.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if(result_code.equals("0000")){
                        GlobalInfo.user_name = edit_name.getText().toString();
                        GlobalInfo.user_phone = edit_phone.getText().toString();
                        GlobalInfo.user_email = edit_email.getText().toString();

                        Toast.makeText(SettingActivity.this, "설정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
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
