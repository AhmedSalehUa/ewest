package com.example.ewestmembers;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.ewestmembers.ManiActivites.Attendance.AttendanceFragment;
import com.example.ewestmembers.ManiActivites.Maintaince.MaintainceFragment;
import com.example.ewestmembers.SideActivites.Messeges.MessagesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView leftView;
    BottomNavigationView bottomNav;
    DrawerLayout drawer;


    int BottomMargin; // margin fragments above bottom nav

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checking api url saved correctly
        SharedPreferences sharedPreferences = this.getSharedPreferences("MainActivity", MODE_PRIVATE);
        if (sharedPreferences.getString("API", "no").equals("no")) {
            sharedPreferences.edit().putString("API", "http://41.178.166.108/ewest/api/");
            sharedPreferences.edit().commit();
        }

        //config left Navigations
        FrameLayout framaeLayouat = (FrameLayout) findViewById(R.id.fragment_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        leftView = findViewById(R.id.nav_view);
        leftView.setNavigationItemSelectedListener(this);

        //config Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        //config bottomNavigations
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_bot_Attendance:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();
                        break;

                    case R.id.nav_bot_fixed:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MaintainceFragment()).commit();
                        break;
                }
                return true;
            }
        });
        bottomNav.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();


        //customizing back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if (bottomNav.getSelectedItemId() != R.id.nav_bot_Attendance) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();
                    bottomNav.setSelectedItemId(R.id.nav_bot_Attendance);
                } else if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    //gettingApi main Yrl For All Application
    public static String getAPIHEADER(Context athis) {
        if (athis == null) {
            Log.e("api", "error");
        }
        SharedPreferences sharedPreferences = athis.getSharedPreferences("MainActivity", MODE_PRIVATE);
        return sharedPreferences.getString("API", "http://41.178.166.108/ewest/api/");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_left_Home:
                FrameLayout framaeLayouat = (FrameLayout) findViewById(R.id.fragment_container);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) framaeLayouat.getLayoutParams();
                params.setMargins(0, 0, 0, BottomMargin);
                framaeLayouat.setLayoutParams(params);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();
                bottomNav.setVisibility(View.VISIBLE);
                bottomNav.setSelectedItemId(R.id.nav_bot_Attendance);
                getSupportActionBar().show();
                break;
            case R.id.nav_left_Messages:
                FrameLayout framaeLayout = (FrameLayout) findViewById(R.id.fragment_container);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessagesFragment(framaeLayout)).commit();
                bottomNav.setVisibility(View.INVISIBLE);
                getSupportActionBar().hide();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}