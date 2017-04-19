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

import co.treasureisle.shobit.Adapter.MessageAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Message;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageActivity extends BaseActivity {
    public static final String TAG = MessageActivity.class.getSimpleName();

    private int userId = 0;
    private RecyclerView messageList;
    private MessageAdapter adapter;

    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userId = getIntent().getIntExtra(IntentTag.USER, 0);

        messageList = (RecyclerView)findViewById(R.id.message_list);

        messages = new ArrayList<>();

        adapter = new MessageAdapter(this, messages);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 1);
        messageList.setLayoutManager(layoutManager);
        messageList.setAdapter(adapter);

        fetchMessage();

    }

    private void fetchMessage(){
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/message/" + userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray messageArray = response.getJSONArray("messages");

                    for (int i = 0; i < messageArray.length(); i++) {
                        Message message = new Message(messageArray.getJSONObject(i));
                        messages.add(message);
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
