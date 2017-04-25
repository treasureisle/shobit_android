package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import co.treasureisle.shobit.Adapter.MessageAdapter;
import co.treasureisle.shobit.Adapter.ReplyAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Model.Message;
import co.treasureisle.shobit.Model.MessageTimestamp;
import co.treasureisle.shobit.Model.Reply;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.View.RelativeLayoutDetectSoftKeyBoard;
import co.treasureisle.shobit.View.RoundedNetworkImageView;
import co.treasureisle.shobit.VolleySingleTon;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageActivity extends BaseActivity implements RelativeLayoutDetectSoftKeyBoard.Listener {
    public static final String TAG = MessageActivity.class.getSimpleName();

    private User user;

    private EditText textMessage;

    private CircularProgressBar loading;

    private boolean isKeyboardShowing = false;

    private RelativeLayout replyHeaderAllWrapper;

    private Button btnSend;

    private int cursorId = 0;

    private int top = 0;

    private int parentId = 0;

    private LinearLayoutManager linearLayoutManager;

    private LinearLayout wrapperProfile;

    private RoundedNetworkImageView profileThumbnail;

    private TextView textUsername;

    private RecyclerView messageList;

    private MessageAdapter adapter;

    private ArrayList<Message> messages;

    private Timer refreshTimer;
    private TimerTask refreshTask;

    private String timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        user = getIntent().getParcelableExtra(IntentTag.USER);

        ImageLoader imageLoader = VolleySingleTon.getInstance(this).getImageLoader();

        wrapperProfile = (LinearLayout) findViewById(R.id.wrapper_profile);
        profileThumbnail = (RoundedNetworkImageView) findViewById(R.id.profile_thumbnail);
        textUsername = (TextView) findViewById(R.id.text_username);

        textUsername.setText(user.getUsername());
        profileThumbnail.setVisibility(View.VISIBLE);
        profileThumbnail.setImageUrl(user.getProfileThumbUrl(), imageLoader);

        wrapperProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                Log.e(TAG, "userId: " + user.getId());
                i.putExtra(IntentTag.USER, String.valueOf(user.getId()));
                startActivity(i);
            }
        });

        messageList = (RecyclerView)findViewById(R.id.list_message);

        textMessage = (EditText) findViewById(R.id.text_message);
        loading = (CircularProgressBar) findViewById(R.id.loading);
        RelativeLayoutDetectSoftKeyBoard allWrapper = (RelativeLayoutDetectSoftKeyBoard) findViewById(R.id.message_all_wrapper);
        allWrapper.setListener(this);

        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messages = new ArrayList<>();

        adapter = new MessageAdapter(this, messages);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 1);
        messageList.setLayoutManager(layoutManager);
        messageList.setAdapter(adapter);

        linearLayoutManager = new LinearLayoutManager(this);

        fetchMessage();

        refreshTask = new TimerTask() {
            @Override
            public void run() {
                final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/message_timestamp", new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            SharedPreferences pref = Utils.getPrefs(context);
                            timestamp = pref.getString(PrefTag.MESSAGE_TIMESTAMP, null);

                            MessageTimestamp messageTimestamp = new MessageTimestamp(response.getJSONObject("timestamp"));

                            if (timestamp == null || timestamp.equals(messageTimestamp.getTimestamp()) == false) {
                                timestamp = messageTimestamp.getTimestamp();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(PrefTag.MESSAGE_TIMESTAMP, timestamp);
                                editor.apply();
                                fetchMessage();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading.setVisibility(View.GONE);
                    }
                });

                VolleySingleTon.getInstance(context).addToRequestQueue(req);
            }
        };

        refreshTimer = new Timer();
        refreshTimer.schedule(refreshTask, 10000, 10000);

    }

    private void fetchMessage(){
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/message/" + user.getId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray messageArray = response.getJSONArray("messages");
                    messages.clear();

                    for (int i = 0; i < messageArray.length(); i++) {
                        Message message = new Message(messageArray.getJSONObject(i));
                        messages.add(message);
                    }
                    scrollToBottom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
            }
        });

        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }

    private void sendMessage(){
        HashMap<String,String> params = new HashMap<>();

        params.put("message", textMessage.getText().toString());
        params.put("parent_id", parentId + "");

        MultipartRequest req = new MultipartRequest(context, com.android.volley.Request.Method.POST, "/message/" + user.getId(), params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fetchMessage();
                        btnSend.setEnabled(true);
                        setResult(messages.size(), new Intent());
                        textMessage.setText("");
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500) {
                            Log.d(TAG, error.networkResponse.toString());
                        } else {
                            Log.e(TAG, error.networkResponse.toString());
                        }
                    }
                }
            }
        });

        VolleySingleTon.getInstance(context).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onSoftKeyBoardShown(boolean isShowing) {
        if (isShowing) {
            if (!isKeyboardShowing) {
                isKeyboardShowing = true;
                scrollToBottom();
            }
        } else {
            if (isKeyboardShowing) {
                isKeyboardShowing = false;
            }
        }
    }

    private void scrollToBottom() {
        messageList.scrollToPosition(messages.size() - 1);
    }


}
