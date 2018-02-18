package com.tpoint.ui.MainActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tpoint.R;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // remove theme's activity background for better performance
        getWindow().setBackgroundDrawable(null);
        // add fragment
        if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_activity_container, fragment).commit();
        }
    }
}