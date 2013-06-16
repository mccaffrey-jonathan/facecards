package com.facecards;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.facebook.widget.ProfilePictureView;

import java.util.List;

public class QuizPageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.quiz_page_activity);

        String[] names = new String[] {"John", "Bob", "Sarah"};
        ArrayAdapter names_adapter = new ArrayAdapter<String>(this,
            R.layout.quiz_name, names);
        GridView names_view = (GridView) findViewById(R.id.quiz_names);
        names_view.setAdapter(names_adapter);

        String[] fb_ids = new String[] {"618833", "4"};
        ArrayAdapter photos_adapter = new ArrayAdapter<String>(this,
            R.layout.quiz_photo, fb_ids);
        GridView photos_view = (GridView) findViewById(R.id.quiz_photos);
        photos_view.setAdapter(photos_adapter);

    }

}
