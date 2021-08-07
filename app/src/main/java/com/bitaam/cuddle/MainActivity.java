package com.bitaam.cuddle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.bitaam.cuddle.ui.FeedFragment;
import com.bitaam.cuddle.ui.InterestedFragment;
import com.bitaam.cuddle.ui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    Fragment active;
    boolean homeFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        active  = new FeedFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,active,"TAG").commit();

        navView.setSelectedItemId(R.id.navigation_feed);

        onClickActivities();

    }

    private void onClickActivities(){

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment = new FeedFragment();


                switch(item.getItemId()){

                    case R.id.navigation_feed:
                        fragment = new FeedFragment();
                        homeFlag = true;
                        break;
                    case R.id.navigation_interested:
                        fragment = new InterestedFragment();
                        homeFlag = false;
                        break;
                    case R.id.navigation_profile:
                        fragment = new ProfileFragment();
                        homeFlag = false;
                        break;

                }

                //Bundle bundle = new Bundle();
                //bundle.putString("UserInfo",userModal.getRole());
                //assert fragment != null;
                //fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment,"TAG").remove(active).commit();
                active = fragment;//.hide(active)
                return true;
            }
        });

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        navView.setSelectedItemId(R.id.navigation_feed);

    }

    @Override
    public void onBackPressed() {

        if (homeFlag)
        {
            finish();
        }else{
            navView.setSelectedItemId(R.id.navigation_feed);
        }

    }


}