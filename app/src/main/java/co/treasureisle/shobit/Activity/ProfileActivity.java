package co.treasureisle.shobit.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.HomeTabPagerAdapter;
import co.treasureisle.shobit.Adapter.MessageAdapter;
import co.treasureisle.shobit.Adapter.ProfileTabPagerAdapter;
import co.treasureisle.shobit.Adapter.PurchaseListAdapter;
import co.treasureisle.shobit.Adapter.SellListAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Model.Follow;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.Model.UserDetail;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.View.RoundedNetworkImageView;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 22..
 */

public class ProfileActivity  extends BaseActivity {
    private static final  String TAG = ProfileActivity.class.getSimpleName();
    private ViewPager pager;
    private TabLayout tabLayout;

    private Button notificationButton;
    private Button messageButton;
    private Button purchaseListButton;
    private Button settingButton;
    private Button followButton;
    private RoundedNetworkImageView profileThumbnail;
    private TextView usernameText;
    private TextView followingText;
    private TextView followerText;

    private UserDetail user;
    private String userId;
    private ArrayList<Follow> followings;
    private ArrayList<Follow> followers;
    private boolean isFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getIntent().getStringExtra(IntentTag.USER);

        setContentView(R.layout.activity_profile);

        followers = new ArrayList<Follow>();
        followings = new ArrayList<Follow>();

        notificationButton = (Button)findViewById(R.id.btn_notification);
        messageButton = (Button)findViewById(R.id.btn_message);
        purchaseListButton = (Button)findViewById(R.id.btn_purchase_list);
        settingButton = (Button)findViewById(R.id.btn_setting);
        followButton = (Button)findViewById(R.id.btn_follow);

        profileThumbnail = (RoundedNetworkImageView)findViewById(R.id.profile_thumbnail);
        usernameText = (TextView)findViewById(R.id.username_text);
        followingText = (TextView)findViewById(R.id.following_text);
        followerText = (TextView)findViewById(R.id.follower_text);

        View.OnClickListener notificationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
            }
        };

        View.OnClickListener messageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isMe()) {
                    Intent i = new Intent(ProfileActivity.this, MessageListActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(ProfileActivity.this, MessageActivity.class);
                    i.putExtra(IntentTag.USER, user);
                    startActivity(i);
                }
            }
        };

        View.OnClickListener purchaseListListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getSellerLevel() == 0){
                    startActivity(new Intent(ProfileActivity.this, PurchaseListActivity.class));
                } else {
                    showPurchaseListPopup();
                }

            }
        };

        View.OnClickListener settingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, SettingActivity.class);
                i.putExtra(IntentTag.USER, user);
                startActivity(i);
            }
        };

        View.OnClickListener followListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        };

        notificationButton.setOnClickListener(notificationListener);
        messageButton.setOnClickListener(messageListener);
        purchaseListButton.setOnClickListener(purchaseListListener);
        settingButton.setOnClickListener(settingListener);
        followButton.setOnClickListener(followListener);

        fetchProfile();

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.sliding_menu);
        tabLayout.addTab(tabLayout.newTab().setText("라이크"));
        tabLayout.addTab(tabLayout.newTab().setText("리스트"));
        tabLayout.addTab(tabLayout.newTab().setText("스토어"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        pager = (ViewPager) findViewById(R.id.viewpager);

        // Creating TabPagerAdapter adapter
        ProfileTabPagerAdapter pagerAdapter = new ProfileTabPagerAdapter(userId, getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPurchaseListPopup(){
        final PopupWindow popup = new PopupWindow(purchaseListButton);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_purchase_list, null);

        Button btnPurchaseList = (Button) view.findViewById(R.id.btn_buy_list);
        Button btnSellList = (Button) view.findViewById(R.id.btn_sell_list);

        btnPurchaseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                startActivity(new Intent(ProfileActivity.this, PurchaseListActivity.class));
            }
        });

        btnSellList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                startActivity(new Intent(ProfileActivity.this, SellListActivity.class));
            }
        });


        popup.setContentView(view);
        popup.setWindowLayoutMode(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(purchaseListButton);

    }

    private void fetchProfile() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/user_detail/" + userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    user = new UserDetail(response.getJSONObject("user_detail"));

                    ImageLoader imageLoader = VolleySingleTon.getInstance(ProfileActivity.this).getImageLoader();
                    profileThumbnail.setVisibility(View.VISIBLE);
                    profileThumbnail.setImageUrl(user.getProfileThumbUrl(), imageLoader);

                    usernameText.setText(user.getUsername());

                    if (user.isMe()) {
                        followButton.setVisibility(View.INVISIBLE);
                    } else {
                        fetchIsFollow();
                        notificationButton.setVisibility(View.INVISIBLE);
                        settingButton.setVisibility(View.INVISIBLE);
                        purchaseListButton.setVisibility(View.INVISIBLE);
                    }

                    fetchFollows();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    private void fetchFollows() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/follow/" + userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject json = response.getJSONObject("follow");
                    JSONArray followingsJSON = json.getJSONArray("followings");
                    JSONArray followersJSON = json.getJSONArray("followers");

                    for (int i=0; i<followingsJSON.length(); i++) {
                        Follow follow = new Follow(followingsJSON.getJSONObject(i));
                        followings.add(follow);
                    }

                    for (int i=0; i<followersJSON.length(); i++) {
                        Follow follow = new Follow(followersJSON.getJSONObject(i));
                        followers.add(follow);
                    }

                    followingText.setText(String.valueOf(followings.size()));
                    followerText.setText(String.valueOf(followers.size()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    private void fetchIsFollow() {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/is_following/" + userId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                followButton.setText("언팔로우");
                isFollowing = true;
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null) {
                        Log.e(TAG, error.networkResponse.toString());
                        if (error.networkResponse.statusCode == 404) {
                            followButton.setText("팔로우");
                            isFollowing = false;
                        }
                    }
                }
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    private void follow(){
        if (isFollowing) {
            final ShobitRequest req = new ShobitRequest(this, Request.Method.DELETE, "/follow/" + userId, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    followButton.setText("팔로우");
                    isFollowing = false;

                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                                followButton.setText("팔로우");
                                isFollowing = false;
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(this).addToRequestQueue(req);
        } else {
            final ShobitRequest req = new ShobitRequest(this, Request.Method.POST, "/follow/" + userId, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    followButton.setText("언팔로우");
                    isFollowing = true;

                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                                followButton.setText("언팔로우");
                                isFollowing = true;
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(this).addToRequestQueue(req);
        }
    }
}
