package com.example.ewestmembers;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ewestmembers.ManiActivites.MainActivityFragment;
import com.example.ewestmembers.ManiActivites.Menu.CenteredTextFragment;
import com.example.ewestmembers.ManiActivites.Menu.DrawerAdapter;
import com.example.ewestmembers.ManiActivites.Menu.DrawerItem;
import com.example.ewestmembers.ManiActivites.Menu.SimpleItem;
import com.example.ewestmembers.SideActivites.Messeges.MessagesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{


    private static final int POS_HOME = 0;
    private static final int POS_MESSAGES = 1;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private SlidingRootNav slidingRootNav;


    int BottomMargin; // margin fragments above bottom nav

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vew);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.lest_nav)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_MESSAGES)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);
        //checking api url saved correctly
        SharedPreferences sharedPreferences = this.getSharedPreferences("MainActivity", MODE_PRIVATE);
        if (sharedPreferences.getString("API", "no").equals("no")) {
            sharedPreferences.edit().putString("API", "http://41.178.166.108/ewest/api/");
            sharedPreferences.edit().commit();
        }
        ImageView userImage = findViewById(R.id.username_image_view);

        TextView userName =findViewById(R.id.username_view);
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("a","aa");
            }
        });

        MaterialIconView logOut=findViewById(R.id.logoutBtn);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggingOut();
            }
        });
        TextView logOutText =findViewById(R.id.logoutText);
        logOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggingOut();
            }
        });
    }
    void loggingOut(){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));

    }
    @Override
    public void onItemSelected(int position) {
        if(position == POS_MESSAGES){
            Fragment selectedScreen = new MessagesFragment();
            showFragment(selectedScreen);
            getSupportActionBar().setTitle("Messages");
        }else if(position == POS_HOME){
            Fragment selectedScreen = new MainActivityFragment();
            showFragment(selectedScreen);

            getSupportActionBar().setTitle("Home");
        }
        slidingRootNav.closeMenu();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.gray))
                .withTextTint(color(R.color.gray))
                .withSelectedIconTint(color(R.color.colorPrimary))
                .withSelectedTextTint(color(R.color.colorPrimary));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
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

}