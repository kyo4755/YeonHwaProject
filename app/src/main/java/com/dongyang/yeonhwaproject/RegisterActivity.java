package com.dongyang.yeonhwaproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton cancel_btn;
    EditText edit_id, edit_name, edit_pw, edit_re_pw, edit_email, edit_phone;
    Button register_btn;
    Spinner email_spinner;

    private String email_form = "naver.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        cancel_btn = findViewById(R.id.register_cancel);
        cancel_btn.setOnClickListener(this);

        edit_id = findViewById(R.id.register_edittext_id);
        edit_name = findViewById(R.id.register_edittext_name);
        edit_pw = findViewById(R.id.register_edittext_pw);
        edit_re_pw = findViewById(R.id.register_edittext_re_pw);
        edit_email = findViewById(R.id.register_edittext_email);
        edit_phone = findViewById(R.id.register_edittext_phone);

        register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(this);

        email_spinner = findViewById(R.id.register_email_spinner);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_cancel:
                finish();
                overridePendingTransition(R.anim.not_move_animation, R.anim.right_out_animation);
                break;
            case R.id.register_btn:
                registerProcess();
                break;
        }
    }

    private void registerProcess() {
        AQuery aQuery = new AQuery(RegisterActivity.this);
        String register_url = GlobalInfo.SERVER_URL + "users/register";

        Map<String, String> params = new LinkedHashMap<>();

        params.put("id", edit_id.getText().toString());
        params.put("pw", edit_pw.getText().toString());
        params.put("name", edit_name.getText().toString());
        params.put("phone", edit_phone.getText().toString());
        params.put("email", edit_email.getText().toString() + "@" + email_form);

        aQuery.ajax(register_url, params, String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){

                    } else if(result_code.equals("0002")){

                    } else if(result_code.equals("0003")){

                    } else if(result_code.equals("0004")){

                    } else if(result_code.equals("0005")){

                    } else if(result_code.equals("0010")){

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
