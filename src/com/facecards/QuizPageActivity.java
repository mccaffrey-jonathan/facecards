package com.facecards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        class ProfilePictureArrayAdapter<String> extends ArrayAdapter<String> {
            public ProfilePictureArrayAdapter(Context c, int r, String[] items) {
                super(c, r, items);
            }

            public View getView(int position, View view, ViewGroup group) {
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.quiz_photo, null);
                }
                ProfilePictureView pic_view = (ProfilePictureView) view;
                pic_view.setProfileId((java.lang.String)this.getItem(position));
                return pic_view;
            }
        }

        String[] fb_ids = new String[] {"618833", "4"};
        ProfilePictureArrayAdapter<String> photos_adapter = new ProfilePictureArrayAdapter<String>(
            this, R.layout.quiz_photo, fb_ids);
        GridView photos_view = (GridView) findViewById(R.id.quiz_photos);
        photos_view.setAdapter(photos_adapter);
    }

}
