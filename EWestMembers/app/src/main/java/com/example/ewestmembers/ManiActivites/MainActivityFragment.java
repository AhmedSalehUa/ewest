package com.example.ewestmembers.ManiActivites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ewestmembers.ManiActivites.Attendance.AttendanceFragment;
import com.example.ewestmembers.ManiActivites.Maintaince.MaintainceFragment;
import com.example.ewestmembers.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityFragment extends Fragment {
    BottomNavigationView bottomNav;
    public MainActivityFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main,container,false);
        bottomNav = (BottomNavigationView) root.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_bot_Attendance:
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();
                        break;

                    case R.id.nav_bot_fixed:
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new MaintainceFragment()).commit();
                        break;
                }
                return true;
            }
        });
        bottomNav.setVisibility(View.VISIBLE);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();

        //customizing back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if (bottomNav.getSelectedItemId() != R.id.nav_bot_Attendance) {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();
                    bottomNav.setSelectedItemId(R.id.nav_bot_Attendance);
                }  else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return root;
    }
}
