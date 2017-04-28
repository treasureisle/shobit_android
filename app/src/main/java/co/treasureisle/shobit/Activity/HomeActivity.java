package co.treasureisle.shobit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.HomeTabPagerAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Session;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.SessionHelper;
import co.treasureisle.shobit.ShobitFirebaseMessagingHelper;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 2. 14..
 */

public class HomeActivity extends BaseActivity{
    private static final  String TAG = HomeActivity.class.getSimpleName();
    private ViewPager pager;
    private TabLayout tabLayout;
    private Button notificationButton;
    private Button searchButton;
    private Button homeButton;
    private Button feedButton;
    private Button uploadButton;
    private Button cartButton;
    private Button profileButton;
    private String userId;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        notificationButton = (Button)findViewById(R.id.btn_notification);
        searchButton = (Button)findViewById(R.id.btn_search);
        homeButton = (Button)findViewById(R.id.homebutton);
        feedButton = (Button)findViewById(R.id.feedbutton);
        uploadButton = (Button)findViewById(R.id.uploadbutton);
        cartButton = (Button)findViewById(R.id.cartbutton);
        profileButton = (Button)findViewById(R.id.profilebutton);

        View.OnClickListener homeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };

        View.OnClickListener feedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == null) {
                    startActivityForResult(new Intent(HomeActivity.this, LoginPopupActivity.class), RequestCode.REQ_LOGIN);
                } else {
                    startActivity(new Intent(HomeActivity.this, FeedActivity.class));
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
                if (userId == null) {
                    startActivityForResult(new Intent(HomeActivity.this, LoginPopupActivity.class), RequestCode.REQ_LOGIN);
                } else {
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                }

            }
        };

        View.OnClickListener profileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == null) {
                    startActivityForResult(new Intent(HomeActivity.this, LoginPopupActivity.class), RequestCode.REQ_LOGIN);
                } else {
                    Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                    i.putExtra(IntentTag.USER, userId);
                    startActivity(i);
                }
            }
        };

        View.OnClickListener notificationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
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
        notificationButton.setOnClickListener(notificationListener);

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.sliding_menu);
        tabLayout.addTab(tabLayout.newTab().setText("판매"));
        tabLayout.addTab(tabLayout.newTab().setText("구해줘"));
        tabLayout.addTab(tabLayout.newTab().setText("후기"));
        tabLayout.addTab(tabLayout.newTab().setText("스토어"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        pager = (ViewPager) findViewById(R.id.viewpager);

        // Creating TabPagerAdapter adapter
        HomeTabPagerAdapter pagerAdapter = new HomeTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);
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

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Firebase token: "+ token);

        tryLogin();

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

    public void tryLogin() {
        JSONObject obj = new JSONObject();
        try {
            SharedPreferences pref = Utils.getPrefs(HomeActivity.this);
            userId = pref.getString(PrefTag.USER_ID, null);
            accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);

            if (userId != null) {
                obj.put("id", userId);
                obj.put("access_token", accessToken);

                final ShobitRequest req = new ShobitRequest(this, Request.Method.POST, "/session", obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "login succeed");
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            if (error.networkResponse != null) {
                                if (error.networkResponse.statusCode == 403) {
                                    SessionHelper sessionHelper = new SessionHelper(HomeActivity.this);
                                    sessionHelper.clearToken();
                                    Utils.showToast(HomeActivity.this, "다른 기기에서 로그인하여 로그아웃됩니다.");
                                    finish();
                                    startActivity(getIntent());

                                } else if (error.networkResponse.statusCode == 404) {
                                    SessionHelper sessionHelper = new SessionHelper(HomeActivity.this);
                                    sessionHelper.clearToken();
                                    Utils.showToast(HomeActivity.this, "알수없는 오류가 발생하였습니다. 다시 로그인해주세요.");
                                }
                            }
                        }
                    }
                });

                VolleySingleTon.getInstance(this).addToRequestQueue(req);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showUploadPopup(){
        SharedPreferences pref = Utils.getPrefs(HomeActivity.this);

        userId = pref.getString(PrefTag.USER_ID, null);
        if (userId == null) {
            startActivityForResult(new Intent(HomeActivity.this, LoginPopupActivity.class), RequestCode.REQ_LOGIN);
            return;
        }

        FrameLayout layoutPopupLocation = (FrameLayout) findViewById(R.id.layout_popup_location);

        final PopupWindow popup = new PopupWindow(layoutPopupLocation);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_upload, null);

        Button btnUploadSell = (Button) view.findViewById(R.id.btn_upload_sell);
        Button btnUploadBuy = (Button) view.findViewById(R.id.btn_upload_buy);
        Button btnUploadReview = (Button) view.findViewById(R.id.btn_upload_review);

        btnUploadSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(HomeActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_SELL);
                startActivity(i);
            }
        });

        btnUploadBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(HomeActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_BUY);
                startActivity(i);
            }
        });

        btnUploadReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(HomeActivity.this, UploadActivity.class);
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
        layoutPopupLocation.getLocationOnScreen(location);

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
                Intent i = new Intent(HomeActivity.this, SearchPostsActivity.class);
                i.putExtra(IntentTag.SEARCH_TYPE, SearchPostsActivity.SEARCH_TYPE_TITLE);
                startActivity(i);
            }
        });

        btnSearchHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(HomeActivity.this, SearchPostsActivity.class);
                i.putExtra(IntentTag.SEARCH_TYPE, SearchPostsActivity.SEARCH_TYPE_HASHTAG);
                startActivity(i);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(HomeActivity.this, SearchUserActivity.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQ_LOGIN:
                tryLogin();
                break;
        }

    }
}
