package com.facecards;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizPageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.quiz_page_activity);

        Intent startingIntent = getIntent();

        final List<String> names = new ArrayList<String>(Arrays.asList(startingIntent.getStringArrayExtra("names")));
        final List<String> fb_ids = new ArrayList<String>(Arrays.asList(startingIntent.getStringArrayExtra("uids")));

//        String[] names = new String[] {"John", "Bob", "Sarah"};
//        String[] fb_ids = new String[] {"618833", "4"};

        final ArrayAdapter names_adapter = new ArrayAdapter<String>(this, R.layout.quiz_name, names);
        final GridView names_view = (GridView) findViewById(R.id.quiz_names);
        names_view.setAdapter(names_adapter);
        names_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View child_view, int i, long l) {
                CharSequence drag_content = ((TextView) child_view).getText().toString();
                ClipData.Item item = new ClipData.Item(drag_content);
                String[] content_types = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(drag_content, content_types, item);
                View.DragShadowBuilder myShadow = new NameDragShadowBuilder(child_view);
                child_view.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0          // flags (not currently used, set to 0)
                );
                return true;
            }
        });

        final GridView photos_view = (GridView) findViewById(R.id.quiz_photos);

        class ProfilePictureArrayAdapter<String> extends ArrayAdapter<String> {
            public ProfilePictureArrayAdapter(Context c, int r, List<String> items) {
                super(c, r, items);
            }

            public View getView(final int position, View view, ViewGroup group) {
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.quiz_photo, null);
                    view.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {
                            if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
                                CharSequence drag_text = dragEvent.getClipData().getItemAt(0).getText();
                                boolean correct = view.getTag().equals(drag_text);
                                Toast.makeText(getContext(), correct ? "Correct!" : "Nope", 2).show();
                                Log.i("AMK", "Dropped " + drag_text + " into " + view.getTag());
                                if (correct) {
                                    ProfilePictureView v = (ProfilePictureView) view;
                                    ProfilePictureArrayAdapter.this.remove((String)v.getProfileId());
                                    ProfilePictureArrayAdapter.this.notifyDataSetChanged();
                                    photos_view.invalidateViews();
                                    names_adapter.remove(view.getTag());
                                    names_adapter.notifyDataSetChanged();
                                    names_view.invalidateViews();
                                }
                                return true;
                            } else if (dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                                return true;   // Yeah, I'm down to take a drop event.
                            } else {
                                return false;
                            }
                        }
                    });
                }
                // save correct name on view
                Log.i("AMK", "Saving #" + Integer.toString(position) + " (name " + names.get(position) + ")");
                Log.i("AMK", "Previous Tag: " + view.getTag());
                view.setTag(names.get(position));

                ProfilePictureView pic_view = (ProfilePictureView) view;
                pic_view.setProfileId((java.lang.String)this.getItem(position));
                return pic_view;
            }
        }

        ProfilePictureArrayAdapter<String> photos_adapter = new ProfilePictureArrayAdapter<String>(
            this, R.layout.quiz_photo, fb_ids);
        photos_view.setAdapter(photos_adapter);
    }
}
