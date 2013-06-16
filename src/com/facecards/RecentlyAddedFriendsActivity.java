package com.facecards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class RecentlyAddedFriendsActivity extends Activity {

    private static final String TAG = "RecentlyAddedFriendsActivity";
    private UiLifecycleHelper uiHelper;

    private Activity getActivity() {
        return this;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        Log.i(TAG, "in onCreate of RecentlyAddedFriends!");

        Session sesh = Session.getActiveSession();

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.splash_activity);
    }

    @Override
        public void onResume() {
            super.onResume();
            uiHelper.onResume();
        }

    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiHelper.onActivityResult(requestCode, resultCode, data);
        }

    @Override
        public void onPause() {
            super.onPause();
            uiHelper.onPause();
        }

    @Override
        public void onDestroy() {
            super.onDestroy();
            uiHelper.onDestroy();
        }

    @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiHelper.onSaveInstanceState(outState);
        }
}
