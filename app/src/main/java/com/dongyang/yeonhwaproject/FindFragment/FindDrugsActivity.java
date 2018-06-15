package com.dongyang.yeonhwaproject.FindFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.dongyang.yeonhwaproject.Adapter.FindDrugsAdapter;
import com.dongyang.yeonhwaproject.Common.GlobalInfo;
import com.dongyang.yeonhwaproject.DetailActivity.SettingDetailLocationActivity;
import com.dongyang.yeonhwaproject.LoginActivity;
import com.dongyang.yeonhwaproject.POJO.FindDrugsPOJO;
import com.dongyang.yeonhwaproject.R;
import com.dongyang.yeonhwaproject.SettingLocationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Kim Jong-Hwa on 2018-05-26.
 */

public class FindDrugsActivity extends AppCompatActivity {

    ImageButton exit_btn;
    AutoCompleteTextView drug_edit_view;
    ListView listView;
    FindDrugsAdapter adapter;
    TextView drug_name, drug_company;
    LinearLayout resultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_drugs_main);

        resultLayout = findViewById(R.id.find_drugs_result_layout);

        listView = findViewById(R.id.find_drugs_listview);
        adapter = new FindDrugsAdapter();
        listView.setAdapter(adapter);

        drug_name = findViewById(R.id.find_drugs_name);
        drug_company = findViewById(R.id.find_drugs_company);

        exit_btn = findViewById(R.id.find_drugs_cancel);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        drug_edit_view = findViewById(R.id.find_drugs_auto_textview);
        drug_edit_view.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, GlobalInfo.drug_name));
        drug_edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drug_edit_view.showDropDown();
            }
        });

        drug_edit_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        String seletedDrugStr = drug_edit_view.getText().toString();
                        int selectedDrugIdx = -1;

                        for(int i=0; i<GlobalInfo.drug_name.length; i++){
                            if(seletedDrugStr.equals(GlobalInfo.drug_name[i])){
                                selectedDrugIdx = i;
                                break;
                            }
                        }

                        if (selectedDrugIdx == -1){
                            Snackbar.make(v, "검색 결과가 없습니다.", Snackbar.LENGTH_SHORT).show();
                            if(resultLayout.getVisibility() == View.VISIBLE)   resultLayout.setVisibility(View.GONE);
                        }
                        else {
                            getDrugItemInfo(selectedDrugIdx);
                        }
                }
                return true;
            }
        });

        drug_edit_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String seletedDrugStr = drug_edit_view.getText().toString();
                int selectedDrugIdx = -1;

                for(int i=0; i<GlobalInfo.drug_name.length; i++){
                    if(seletedDrugStr.equals(GlobalInfo.drug_name[i])){
                        selectedDrugIdx = i;
                        break;
                    }
                }

                if (selectedDrugIdx == -1){
                    Snackbar.make(view, "검색 결과가 없습니다.", Snackbar.LENGTH_SHORT).show();
                    if(resultLayout.getVisibility() == View.VISIBLE)   resultLayout.setVisibility(View.GONE);
                }
                else {
                    getDrugItemInfo(selectedDrugIdx);
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

    private void getDrugItemInfo(int idx) {
        AQuery aQuery = new AQuery(FindDrugsActivity.this);
        String login_url = GlobalInfo.SERVER_URL + "drugs/getItem";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("drug_id", GlobalInfo.drug_id[idx]);

        aQuery.ajax(login_url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                ArrayList<FindDrugsPOJO> alist = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_code = jsonObject.get("result").toString();

                    if(result_code.equals("0001")){
                        Toast.makeText(FindDrugsActivity.this, "아이디 전달 오류", Toast.LENGTH_SHORT).show();
                        if(resultLayout.getVisibility() == View.VISIBLE)   resultLayout.setVisibility(View.GONE);
                    } else if (result_code.equals("0010")) {
                        Toast.makeText(FindDrugsActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        if(resultLayout.getVisibility() == View.VISIBLE)   resultLayout.setVisibility(View.GONE);
                    } else {
                        JSONObject jObject = new JSONObject(jsonObject.getString("drug_info"));

                        drug_name.setText(jObject.getString("name"));
                        drug_company.setText(jObject.getString("entp_name"));

                        FindDrugsPOJO pojo = new FindDrugsPOJO();
                        pojo.setExplain("분류");
                        pojo.setMsg(jObject.getString("class_no"));
                        alist.add(pojo);

                        pojo = new FindDrugsPOJO();
                        pojo.setExplain("성상");
                        pojo.setMsg(jObject.getString("chart"));
                        alist.add(pojo);

                        pojo = new FindDrugsPOJO();
                        pojo.setExplain("원료 성분");
                        pojo.setMsg(jObject.getString("material_name"));
                        alist.add(pojo);

                        pojo = new FindDrugsPOJO();
                        pojo.setExplain("저장 방법");
                        pojo.setMsg(jObject.getString("storage_method"));
                        alist.add(pojo);

                        pojo = new FindDrugsPOJO();
                        pojo.setExplain("유효 기간");
                        pojo.setMsg(jObject.getString("valid_term"));
                        alist.add(pojo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    adapter.setList(alist);
                    if(resultLayout.getVisibility() == View.GONE)   resultLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
