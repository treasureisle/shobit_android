package co.treasureisle.shobit.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.NotificationAdapter;
import co.treasureisle.shobit.Adapter.UserAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Model.Notification;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class NotificationActivity extends BaseActivity {
    public static final String TAG = NotificationActivity.class.getSimpleName();

    private RecyclerView notificationList;
    private NotificationAdapter adapter;

    private ArrayList<Notification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationList = (RecyclerView)findViewById(R.id.notification_list);

        notifications = new ArrayList<>();

        adapter = new NotificationAdapter(this, notifications);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 1);
        notificationList.setLayoutManager(layoutManager);
        notificationList.setAdapter(adapter);

        fetchNotification();

    }

    private void fetchNotification(){
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/notification", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray notificationArray = response.getJSONArray("notification");

                    for (int i = 0; i < notificationArray.length(); i++) {
                        Notification notification = new Notification(notificationArray.getJSONObject(i));
                        notifications.add(notification);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });

        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }
}
