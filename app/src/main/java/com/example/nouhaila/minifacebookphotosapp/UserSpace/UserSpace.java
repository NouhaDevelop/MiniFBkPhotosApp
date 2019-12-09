package com.example.nouhaila.minifacebookphotosapp.UserSpace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.nouhaila.minifacebookphotosapp.Database.SharedPrefManager;
import com.example.nouhaila.minifacebookphotosapp.MainActivity;
import com.example.nouhaila.minifacebookphotosapp.R;
import com.example.nouhaila.minifacebookphotosapp.UserSpace.Home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class UserSpace extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_deconx:
                    SharedPrefManager.getInstance(getApplicationContext()).logout();
                    finish();
                    startActivity(new Intent(UserSpace.this, MainActivity.class));
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_space);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //if user not logged in then Go to MainActivity

        if(!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //set First fragment to show as Home Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
    }

}
