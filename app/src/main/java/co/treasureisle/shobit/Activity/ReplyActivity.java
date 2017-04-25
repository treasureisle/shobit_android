package co.treasureisle.shobit.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import co.treasureisle.shobit.Adapter.ReplyAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.DialogHelper;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Reply;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.View.RelativeLayoutDetectSoftKeyBoard;
import co.treasureisle.shobit.VolleySingleTon;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by pgseong on 2017. 3. 27..
 */

public class ReplyActivity extends BaseActivity implements RelativeLayoutDetectSoftKeyBoard.Listener {
    public static final String TAG = ReplyActivity.class.getSimpleName();

    public static final int COMMENT_ROW = 50;

    private EditText replyText;

    private String replyUrl;

    private String replyPostUrl;

    private ArrayList<Reply> replies;

    private ReplyAdapter adapter;

    private CircularProgressBar loading;
    private RecyclerView listView;

    private boolean isKeyboardShowing = false;

    private RelativeLayout replyHeaderAllWrapper;

    private int cursorId = 0;

    private int top = 0;

    private int parentId = 0;

    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Post post = getIntent().getParcelableExtra(IntentTag.POST);
        parentId = getIntent().getIntExtra(IntentTag.PARENT_ID, 0);

        replyPostUrl = String.format("/reply/%d", post.getId());

        if (parentId == 0) {
            replyUrl = String.format("/reply/%d", post.getId());
        } else {
            replyUrl = String.format("/reply_detail/%d/%d", post.getId(), parentId);
        }
        replies = new ArrayList<>();

        replyText = (EditText) findViewById(R.id.reply);
        loading = (CircularProgressBar) findViewById(R.id.loading);
        RelativeLayoutDetectSoftKeyBoard allWrapper = (RelativeLayoutDetectSoftKeyBoard) findViewById(R.id.replyHeaderAllWrapper);
        allWrapper.setListener(this);

        final Button replyBtn = (Button) findViewById(R.id.replyBtn);
        replyBtn.setEnabled(false);
        replyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                replyBtn.setEnabled(s.length() >= 1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidComment()) {
                    replyBtn.setEnabled(false);

                    HashMap<String,String> params = new HashMap<>();

                    params.put("text", replyText.getText().toString());
                    params.put("parent_id", parentId + "");

                    MultipartRequest req = new MultipartRequest(ReplyActivity.this, com.android.volley.Request.Method.POST, replyPostUrl, params, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    refreshReplies();
                                    replyBtn.setEnabled(true);
                                    setResult(replies.size(), new Intent());
                                    replyText.setText("");
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

                    VolleySingleTon.getInstance(ReplyActivity.this).addToRequestQueue(req, new DefaultRetryPolicy(
                            5 * 60 * 1000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                }
            }
        });

        adapter = new ReplyAdapter(this, replies, new ReplyAdapter.OnCreateHeaderView() {
            @Override
            public void onCreated(View headerView) {
                replyHeaderAllWrapper = (RelativeLayout) headerView;
            }
        }, new ReplyAdapter.OnClickMore() {
            @Override
            public void onClick() {
                replyHeaderAllWrapper.setVisibility(View.INVISIBLE);
                fetchReplies(cursorId);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);

        listView = (RecyclerView) findViewById(R.id.list);
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);

        fetchReplies(-1);
    }

    private void refreshReplies() {
        replies.clear();
        loading.setVisibility(View.VISIBLE);
        fetchReplies(-1);
    }

    private void fetchReplies(final int cursor) {
        if (cursor != -1) {
            View v = listView.getChildAt(1);
            top = (v == null) ? 0 : v.getTop();
        }

        String cursorParam = "";
        if (cursor != -1) {
            cursorParam = "&cursor=" + cursor;
        }
        ShobitRequest req = new ShobitRequest(this, Request.Method.GET, replyUrl + "?row=" + COMMENT_ROW + cursorParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray replysArray = response.getJSONArray("replies");

                    replyHeaderAllWrapper.setVisibility((replysArray.length() == COMMENT_ROW) ? View.VISIBLE : View.GONE);

                    final ArrayList<Reply> tempReplies = new ArrayList<>();

                    for (int i = 0; i < replysArray.length(); i++) {

                        Reply tempReply = new Reply(replysArray.getJSONObject(i));

                        tempReplies.add(tempReply);
                    }

                    for (int i = replysArray.length() - 1; i >= 0; i--) {
                        Reply tempReply = new Reply(replysArray.getJSONObject(i));
                        if (tempReply.getParentId() != 0) {
                            for(int j=0; j < replysArray.length(); j++) {
                                Reply parentReply = tempReplies.get(j);
                                if (tempReply.getParentId() == parentReply.getId()) {
                                    if (j >= replysArray.length() -1) {
                                        tempReplies.add(tempReply);
                                    } else {
                                        tempReplies.add(j + 1, tempReply);
                                    }
                                    tempReplies.remove(i);

                                }
                            }
                        }
                    }


                    Collections.reverse(tempReplies);

                    for (Reply replyCard : tempReplies) {
                        replies.add(0, replyCard);
                    }

                    if (replysArray.length() > 0) {
                        cursorId = replies.get(0).getId();
                    }
                    adapter.notifyDataSetChanged();
                    loading.setVisibility(View.GONE);

                    if (cursor == -1) {
                        scrollToBottom();
                    } else {
                        linearLayoutManager.scrollToPositionWithOffset(replysArray.length() + 1, top);
                    }

                    setResult(replies.size(), new Intent());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    private boolean isValidComment() {
        return replyText.getText().toString().length() != 0;
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

    public void replyDeleteCallback() {
        setResult(replies.size(), new Intent());
    }

    private void scrollToBottom() {
        listView.scrollToPosition(replies.size() - 1);
    }
}
