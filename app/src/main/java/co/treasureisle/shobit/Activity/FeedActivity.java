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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.FeedTabPagerAdapter;
import co.treasureisle.shobit.Adapter.HomeTabPagerAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 3. 15..
 */

public class FeedActivity extends BaseActivity {
    private ArrayList<Post> mPosts;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPosts = new ArrayList<>();

        notificationButton = (Button)findViewById(R.id.btn_notification);
        searchButton = (Button)findViewById(R.id.btn_search);
        homeButton = (Button)findViewById(R.id.homebutton);
        feedButton = (Button)findViewById(R.id.feedbutton);
        uploadButton = (Button)findViewById(R.id.uploadbutton);
        cartButton = (Button)findViewById(R.id.cartbutton);
        profileButton = (Button)findViewById(R.id.profilebutton);

        View.OnClickListener searchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchPopup();
            }
        };

        View.OnClickListener homeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, HomeActivity.class));
                finish();
            }
        };

        View.OnClickListener feedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    startActivity(new Intent(FeedActivity.this, LoginPopupActivity.class));
                } else {
                    startActivity(new Intent(FeedActivity.this, CartActivity.class));
                }

            }
        };

        View.OnClickListener profileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = Utils.getPrefs(FeedActivity.this);
                String userId = pref.getString(PrefTag.USER_ID, null);
                Intent i = new Intent(FeedActivity.this, ProfileActivity.class);
                i.putExtra(IntentTag.USER, userId);
                startActivity(i);
            }
        };

        homeButton.setOnClickListener(homeListener);
        feedButton.setOnClickListener(feedListener);
        uploadButton.setOnClickListener(uploadListener);
        cartButton.setOnClickListener(cartListener);
        profileButton.setOnClickListener(profileListener);
        searchButton.setOnClickListener(searchListener);

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.sliding_menu);
        tabLayout.addTab(tabLayout.newTab().setText("판매"));
        tabLayout.addTab(tabLayout.newTab().setText("일반"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        pager = (ViewPager) findViewById(R.id.viewpager);

        // Creating TabPagerAdapter adapter

        FeedTabPagerAdapter pagerAdapter = new FeedTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

    private void showUploadPopup(){
        SharedPreferences pref = Utils.getPrefs(FeedActivity.this);

        userId = pref.getString(PrefTag.USER_ID, null);
        if (userId == null) {
            startActivityForResult(new Intent(FeedActivity.this, LoginPopupActivity.class), RequestCode.REQ_LOGIN);
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

                Intent i = new Intent(FeedActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_SELL);
                startActivity(i);
            }
        });

        btnUploadBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(FeedActivity.this, UploadActivity.class);
                i.putExtra(IntentTag.POST_TYPE, UploadActivity.POST_TYPE_BUY);
                startActivity(i);
            }
        });

        btnUploadReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent i = new Intent(FeedActivity.this, UploadActivity.class);
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
                Intent i = new Intent(FeedActivity.this, SearchPostsActivity.class);
                i.putExtra(IntentTag.SEARCH_TYPE, SearchPostsActivity.SEARCH_TYPE_TITLE);
                startActivity(i);
            }
        });

        btnSearchHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(FeedActivity.this, SearchPostsActivity.class);
                i.putExtra(IntentTag.SEARCH_TYPE, SearchPostsActivity.SEARCH_TYPE_HASHTAG);
                startActivity(i);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent i = new Intent(FeedActivity.this, SearchUserActivity.class);
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
}
