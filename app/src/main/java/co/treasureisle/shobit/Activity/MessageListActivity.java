package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.MessageListAdapter;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageListActivity extends BaseActivity {
    public static final String TAG = MessageListActivity.class.getSimpleName();

    private RecyclerView userList;
    private MessageListAdapter adapter;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        userList = (RecyclerView)findViewById(R.id.user_list);

        users = new ArrayList<>();

        adapter = new MessageListAdapter(this, users);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 1);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(adapter);

        fetchMessageList();

    }

    private void fetchMessageList(){
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/message_list", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray userArray = response.getJSONArray("users");

                    for (int i = 0; i < userArray.length(); i++) {
                        User user = new User(userArray.getJSONObject(i));
                        users.add(user);
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
