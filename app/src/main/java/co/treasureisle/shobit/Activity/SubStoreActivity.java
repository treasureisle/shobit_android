package co.treasureisle.shobit.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.PostThumbAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 30..
 */

public class SubStoreActivity extends BaseActivity{
    private static final String TAG = SubStoreActivity.class.getSimpleName();
    private String url = "";
    private String title = "";
    private int id = 0;
    private ArrayList<Post> posts;

    private TextView textTitle;
    private Button notificationButton;
    private Button searchButton;
    private Button homeButton;
    private Button feedButton;
    private Button uploadButton;
    private Button cartButton;
    private Button profileButton;
    private String userId;

    private RecyclerView postList;
    private PostThumbAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getIntent().getStringExtra(IntentTag.URL);
        id = getIntent().getIntExtra(IntentTag.ID, 0);
        title = getIntent().getStringExtra(IntentTag.TITLE);
        
        setContentView(R.layout.activity_store_detail);
        posts = new ArrayList<>();

        notificationButton = (Button)findViewById(R.id.btn_notification);
        textTitle = (TextView)findViewById(R.id.text_title);
        searchButton = (Button)findViewById(R.id.btn_search);
        homeButton = (Button)findViewById(R.id.homebutton);
        feedButton = (Button)findViewById(R.id.feedbutton);
        uploadButton = (Button)findViewById(R.id.uploadbutton);
        cartButton = (Button)findViewById(R.id.cartbutton);
        profileButton = (Button)findViewById(R.id.profilebutton);
        postList = (RecyclerView)findViewById(R.id.post_list);

        textTitle.setText(title);

        adapter = new PostThumbAdapter(this, posts);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 3);
        postList.setLayoutManager(layoutManager);
        postList.setAdapter(adapter);

        View.OnClickListener homeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubStoreActivity.this, HomeActivity.class));
                finish();
            }
        };

        View.OnClickListener feedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = Utils.getPrefs(SubStoreActivity.this);
                userId = pref.getString(PrefTag.USER_ID, null);
                if (userId == null) {
                    startActivity(new Intent(SubStoreActivity.this, LoginPopupActivity.class));
                } else {
                    startActivity(new Intent(SubStoreActivity.this, FeedActivity.class));
                    finish();
                }
            }
        };

        View.OnClickListener uploadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadPopup();
            }
        };

        View.OnClickListener cartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        View.OnClickListener profileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = Utils.getPrefs(SubStoreActivity.this);
                userId = pref.getString(PrefTag.USER_ID, null);
                if (userId == null) {
                    startActivity(new Intent(SubStoreActivity.this, LoginPopupActivity.class));
                } else {
                    Intent i = new Intent(SubStoreActivity.this, ProfileActivity.class);
                    i.putExtra(IntentTag.USER, userId);
                    startActivity(i);
                }
            }
        };

        View.OnClickListener searchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchPopup();
            }
        };

        homeButton.setOnClickListener(homeListener);
        feedButton.setOnClickListener(feedListener);
        uploadButton.setOnClickListener(uploadListener);
        cartButton.setOnClickListener(cartListener);
        profileButton.setOnClickListener(profileListener);
        searchButton.setOnClickListener(searchListener);

        fetchPosts();
    }

    private void showUploadPopup(){
        SharedPreferences pref = Utils.getPrefs(SubStoreActivity.this);

        userId = pref.getString(PrefTag.USER_ID, null);
        if (userId == null) {
            startActivity(new Intent(SubStoreActivity.this, LoginPopupActivity.class));
            return;
        }

        final PopupWindow popup = new PopupWindow(uploadButton);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_upload, null);

        Button btnUploadSell = (Button) view.findViewById(R.id.btn_upload_sell);
        Button btnUploadBuy = (Button) view.findViewById(R.id.btn_upload_buy);
        Button btnUploadReview = (Button) view.findViewById(R.id.btn_upload_review);

        btnUploadSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(SubStoreActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_SELL);
                startActivity(i);
            }
        });

        btnUploadBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(SubStoreActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_BUY);
                startActivity(i);
            }
        });

        btnUploadReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(SubStoreActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_REVIEW);
                startActivity(i);
            }
        });


        popup.setContentView(view);
        popup.setWindowLayoutMode(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        uploadButton.getLocationOnScreen(location);

        popup.showAtLocation(uploadButton, Gravity.NO_GRAVITY, location[0], location[1]);
    }

    private void showSearchPopup(){
        final PopupWindow popup = new PopupWindow(searchButton);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_search, null);

        Button btnSearchTitle = (Button) view.findViewById(R.id.btn_search_title);
        Button btnSearchHashtag = (Button) view.findViewById(R.id.btn_search_hashtag);
        Button btnSearchUser = (Button) view.findViewById(R.id.btn_search_user);

        btnSearchTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(SubStoreActivity.this, SearchPostsActivity.class);
                i.putExtra(IntentTag.SEARCH_TYPE, SearchPostsActivity.SEARCH_TYPE_TITLE);
                startActivity(i);
            }
        });

        btnSearchHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(SubStoreActivity.this, SearchPostsActivity.class);
                i.putExtra(IntentTag.SEARCH_TYPE, SearchPostsActivity.SEARCH_TYPE_HASHTAG);
                startActivity(i);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(SubStoreActivity.this, SearchUserActivity.class);
                startActivity(i);
            }
        });


        popup.setContentView(view);
        popup.setWindowLayoutMode(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(searchButton);

    }

    private void fetchPosts() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, url + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray postArray = response.getJSONArray("posts");

                    for (int i = 0; i < postArray.length(); i++) {
                        Post post = new Post(postArray.getJSONObject(i));
                        posts.add(post);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }
}
