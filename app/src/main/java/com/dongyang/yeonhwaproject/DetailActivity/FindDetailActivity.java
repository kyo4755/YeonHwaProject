package com.dongyang.yeonhwaproject.DetailActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.dongyang.yeonhwaproject.Adapter.FindDetailTabPagerAdapter;
import com.dongyang.yeonhwaproject.R;
import com.dongyang.yeonhwaproject.ReviewInsertActivity;

/**
 * Created by Kim Jong-Hwa on 2018-05-27.
 */

public class FindDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;

    Button detail_toolbar_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_find_main);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);

        Intent intent = getIntent();
        toolbar.setTitle(intent.getStringExtra("prefab_name"));

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        TabLayout tabLayout = findViewById(R.id.detail_toolbar_tab);
        tabLayout.addTab(tabLayout.newTab().setText("정보"));
        tabLayout.addTab(tabLayout.newTab().setText("리뷰"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.detail_viewPager);

        FindDetailTabPagerAdapter pagerAdapter = new FindDetailTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        detail_toolbar_call = findViewById(R.id.detail_toolbar_call);
        detail_toolbar_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+intent.getStringExtra("prefab_tel"))));
            }
        });

        FloatingActionButton fab = findViewById(R.id.detail_floating_action_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent it = new Intent(FindDetailActivity.this, ReviewInsertActivity.class);
            startActivity(it);
            overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
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
