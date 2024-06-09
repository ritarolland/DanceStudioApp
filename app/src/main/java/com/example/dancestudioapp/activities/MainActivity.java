package com.example.dancestudioapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dancestudioapp.R;
import com.example.dancestudioapp.databinding.ActivityMainBinding;
import com.example.dancestudioapp.fragments.HomeFragment;
import com.example.dancestudioapp.fragments.NotificationFragment;
import com.example.dancestudioapp.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navigationBar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
                return true;
            } else if (item.getItemId() == R.id.notifications) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new NotificationFragment()).commit();
                return true;
            }else if (item.getItemId() == R.id.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new ProfileFragment()).commit();
                return true;
            }
            return false;
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
    }
}