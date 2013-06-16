
package com.facecards;

import android.util.Log;
import android.os.Bundle;

import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class FBRequests {

    private static final String TAG = "FBRequests";

    public static class Friend {
        public Friend(long uid, String name) {
            this.uid = uid;
            this.name = name;
        }

        public final long uid;
        public final String name;
    }

    public interface RecentlyAddedFriendsCallback {
        public void onSuccess(List<Friend> recentUsers);
    }

    static abstract class HelperCallback implements Request.Callback {
        protected boolean logErrorsAndReport(Response res) {
            FacebookRequestError err = res.getError();
            if (err == null) {
                return false;
            }

            Log.e(TAG, err.getRequestResult().toString());

            return true;
        }
    }

    static private void appendNewFriends(long meuid,
            JSONArray data,
            ArrayList<Friend> newFriendUids) {
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            JSONObject tags = obj.optJSONObject("story_tags");

            if (// Isn't a status update
                    !obj.optString("status_type").equals("approved_friend") ||
                    // Not from me
                    obj.optJSONObject("from").optLong("id") != meuid ||
                    // Doesn't have story tags
                    tags == null
               ) {
                continue;
               }

            Log.i(TAG, "Examining status item: " + obj.toString());

            Iterator tagkeys = tags.keys();
            for (Iterator tagKeys = tags.keys(); tagKeys.hasNext(); ) {
                String k = (String)tagKeys.next();
                JSONArray arr = tags.optJSONArray(k);

                for (int j = 0; j < arr.length(); j++) {
                    JSONObject tagVal = arr.optJSONObject(j);

                    if (tagVal == null) {
                        Log.i(TAG, "story_tags key with no value " + k);
                        continue;
                    }

                    String type = tagVal.optString("type");
                    if (!type.equals("user")) {
                        Log.i(TAG, "rejected type: " + type);
                        continue;
                    }

                    long id = tagVal.optLong("id");
                    if (id == meuid) {
                        Log.i(TAG, "rejected id: " + id);
                        continue;
                    }

                    String name = tagVal.optString("name");
                    Log.i(TAG, "Found new friend: " + tagVal.optString("name"));
                    newFriendUids.add(new Friend(id, name));
                }
            }
        }
    }

    public static void getRecentlyAddedFriends(final Session session,
            final RecentlyAddedFriendsCallback cb)
    {
        Request req = new Request(session, "me", null, HttpMethod.GET);
        req.setCallback(new HelperCallback() {
            @Override
            public void onCompleted(Response res) {

                JSONObject meobj = res.getGraphObject().getInnerJSONObject();
                final long meuid = meobj.optLong("id");

                Request req = new Request(session, "me/feed", null, HttpMethod.GET);
                req.setCallback(new HelperCallback() {
                    @Override
                    public void onCompleted(Response res) {
                        if (logErrorsAndReport(res)) {
                            return;
                        }

                        ArrayList<Friend> newFriendUids = new ArrayList<Friend>();
                        JSONArray data = (JSONArray)res.getGraphObject().getProperty("data");

                        appendNewFriends(meuid, data, newFriendUids);
                        cb.onSuccess(newFriendUids);
                    }
                });

                req.executeAsync();
            }
        });

        req.executeAsync();
    }
}
