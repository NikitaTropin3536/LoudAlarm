package com.example.loudalarm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.loudalarm.Fragments.AddFragment;
import com.example.loudalarm.Fragments.HomeFragment;
import com.example.loudalarm.Fragments.SettingsFragment;
import com.example.loudalarm.Room.AlarmDAO;
import com.example.loudalarm.Room.AlarmEntity;
import com.example.loudalarm.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    public AlarmDAO alarmDatabaseDAO;
    List<AlarmEntity> alarms;


    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(HomeFragment.newInstance(alarms));
                        return true;
                    case R.id.navigation_settings:
                        loadFragment(SettingsFragment.newInstance((App.getDefaultMusicUri())));
                        return true;
                    case R.id.navigation_add:
                        loadFragment(AddFragment.newInstance(new AlarmEntity(App.getDefaultMusicUri().toString())));
                        return true;
                }
                return false;
            };


    public void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(() -> {
            alarmDatabaseDAO = App.getDatabase().alarmDAO();
            alarms = alarmDatabaseDAO.getAll();
            runOnUiThread(() -> {
                        loadFragment(HomeFragment.newInstance(alarms));
                    }
            );
        }).start();

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setItemHorizontalTranslationEnabled(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}