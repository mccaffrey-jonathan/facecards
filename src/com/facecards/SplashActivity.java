package com.facecards;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.splash_activity);
    }

}
