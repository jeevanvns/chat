package com.editsoft.ansh.mychat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.editsoft.ansh.mychat.R;
import com.editsoft.ansh.mychat.fragment.ChatContact;
import com.editsoft.ansh.mychat.fragment.ChatList;
import com.editsoft.ansh.mychat.utility.PreferenceUtils;
import com.editsoft.ansh.mychat.utility.ViewPagerAdapter;

public class MainActivity extends BaseActivity {

    private TabLayout tlTeams;
    private ViewPager vpTeams;
    private ViewPagerAdapter adapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tlTeams = (TabLayout) findViewById(R.id.tab_layout);
        vpTeams = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(vpTeams);
        tlTeams.setupWithViewPager(vpTeams);
        setUpToolbar();
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

    }

    @Override
    protected void initListener() {

    }


    @Override
    protected void bindDataWithUi() {

    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ChatList(), "CHATS");
        adapter.addFragment(new ChatContact(), "CONTACT");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chat:
                startActivity(new Intent(MainActivity.this, AddContact.class));
                break;
            case R.id.action_update_profile:
                startActivity(new Intent(MainActivity.this, UpdateProfile.class));
                break;
            case R.id.action_logout:
                PreferenceUtils.removeAll();
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


}