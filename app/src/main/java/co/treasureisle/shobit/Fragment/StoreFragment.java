package co.treasureisle.shobit.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Activity.SubStoreActivity;
import co.treasureisle.shobit.Adapter.EventPagerAdapter;
import co.treasureisle.shobit.Adapter.PostThumbAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Event;
import co.treasureisle.shobit.Model.Hashtag;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Store;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 2. 17..
 */

public class StoreFragment extends Fragment{
    private static final String TAG = StoreFragment.class.getSimpleName();
    private Context context;
    private FragmentActivity activity;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapterEditorsPick;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapterTodaySeller;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapterHot1;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapterHot2;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapterHot3;

    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Post> postsEditorsPick = new ArrayList<>();
    private ArrayList<Post> postsTodaySeller = new ArrayList<>();
    private ArrayList<Post> postsHot1 = new ArrayList<>();
    private ArrayList<Post> postsHot2 = new ArrayList<>();
    private ArrayList<Post> postsHot3 = new ArrayList<>();
    private ArrayList<Hashtag> hashtags = new ArrayList<>();

    private Store store;

    private ViewPager pagerEvent;
    private RecyclerView listEditorsPick;
    private RecyclerView listTodaySeller;
    private RecyclerView listHot1;
    private RecyclerView listHot2;
    private RecyclerView listHot3;

    private TextView textEditorsPick;
    private TextView textTodaySeller;
    private TextView textHot1;
    private TextView textHot2;
    private TextView textHot3;

    private Button btnMoreEditorsPick;
    private Button btnMoreTodaySeller;
    private Button btnMoreHot1;
    private Button btnMoreHot2;
    private Button btnMoreHot3;

    public static StoreFragment newInstance() {

        Bundle args = new Bundle();

        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        this.activity = getActivity();

        this.adapterEditorsPick = new PostThumbAdapter(getActivity(), postsEditorsPick);
        this.adapterTodaySeller = new PostThumbAdapter(getActivity(), postsTodaySeller);
        this.adapterHot1 = new PostThumbAdapter(getActivity(), postsHot1);
        this.adapterHot2 = new PostThumbAdapter(getActivity(), postsHot2);
        this.adapterHot3 = new PostThumbAdapter(getActivity(), postsHot3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        listEditorsPick = (RecyclerView)view.findViewById(R.id.list_editors_pick);
        listTodaySeller = (RecyclerView)view.findViewById(R.id.list_today_seller);
        listHot1 = (RecyclerView)view.findViewById(R.id.list_hot1);
        listHot2 = (RecyclerView)view.findViewById(R.id.list_hot2);
        listHot3 = (RecyclerView)view.findViewById(R.id.list_hot3);

        btnMoreEditorsPick = (Button)view.findViewById(R.id.btn_more_editors_pick);
        btnMoreTodaySeller = (Button)view.findViewById(R.id.btn_more_today_seller);
        btnMoreHot1 = (Button)view.findViewById(R.id.btn_more_hot1);
        btnMoreHot2 = (Button)view.findViewById(R.id.btn_more_hot2);
        btnMoreHot3 = (Button)view.findViewById(R.id.btn_more_hot3);

        btnMoreEditorsPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSubStore("/store_detail/", store.getEditorsPickHashtagId(), store.getEditorsPickTitle());
            }
        });

        btnMoreTodaySeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSubStore("/user_posts/", store.getSellerId(), store.getTodaySellerTitle());
            }
        });

        btnMoreHot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSubStore("/store_detail/", hashtags.get(0).getId(), hashtags.get(0).getName());
            }
        });

        btnMoreHot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSubStore("/store_detail/", hashtags.get(1).getId(), hashtags.get(1).getName());
            }
        });

        btnMoreHot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToSubStore("/store_detail/", hashtags.get(2).getId(), hashtags.get(2).getName());
            }
        });

        textEditorsPick = (TextView)view.findViewById(R.id.text_editors_pick);
        textTodaySeller = (TextView)view.findViewById(R.id.text_today_seller);
        textHot1 = (TextView)view.findViewById(R.id.text_hot1);
        textHot2 = (TextView)view.findViewById(R.id.text_hot2);
        textHot3 = (TextView)view.findViewById(R.id.text_hot3);

        pagerEvent = (ViewPager)view.findViewById(R.id.pager_event);

        fetchStore();

        return view;
    }

    private void fetchStore() {
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/store", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    store = new Store(response.getJSONObject("store"));
                    events.clear();

                    if (store.getNumEvents() > 0 ) {
                        events.add(new Event(store.getEvent1HashtagId(), store.getEvent1ImgUrl(), store.getEvent1Title()));
                    }
                    if (store.getNumEvents() > 1 ) {
                        events.add(new Event(store.getEvent2HashtagId(), store.getEvent2ImgUrl(), store.getEvent2Title()));
                    }
                    if (store.getNumEvents() > 2 ) {
                        events.add(new Event(store.getEvent3HashtagId(), store.getEvent3ImgUrl(), store.getEvent3Title()));
                    }
                    if (store.getNumEvents() > 3 ) {
                        events.add(new Event(store.getEvent4HashtagId(), store.getEvent4ImgUrl(), store.getEvent4Title()));
                    }
                    if (store.getNumEvents() > 4 ) {
                        events.add(new Event(store.getEvent5HashtagId(), store.getEvent5ImgUrl(), store.getEvent5Title()));
                    }

                    EventPagerAdapter pagerAdapter = new EventPagerAdapter(activity.getSupportFragmentManager(), events);
                    pagerEvent.setAdapter(pagerAdapter);
                    pagerEvent.setOffscreenPageLimit(4);

                    textEditorsPick.setText(store.getEditorsPickTitle());
                    textTodaySeller.setText(store.getTodaySellerTitle());

                    fetchPosts();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }

    private void fetchPosts() {
        fetchEditorsPick();
        fetchTodaySeller();
        fetchBestHashtags();
    }

    private void fetchBestHashtags(){
        int categoryId = 0;

        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/hashtag_score/" + categoryId, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    hashtags.clear();

                    JSONArray hashtagArray = response.getJSONArray("hashtags");

                    for (int i = 0; i < hashtagArray.length(); i++) {
                        Hashtag hashtag = new Hashtag(hashtagArray.getJSONObject(i));
                        hashtags.add(hashtag);
                    }

                    if (hashtags.size() > 0 ){
                        textHot1.setText("#" + hashtags.get(0).getName());
                        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/store_detail/" + hashtags.get(0).getId(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray postArray = response.getJSONArray("posts");

                                    for (int i = 0; i < postArray.length(); i++) {
                                        Post post = new Post(postArray.getJSONObject(i));
                                        postsHot1.add(post);
                                    }

                                    GridLayoutManager layoutManager;
                                    adapterHot1 = new PostThumbAdapter(activity, postsHot1);
                                    layoutManager = new GridLayoutManager(context, postsHot1.size());
                                    listHot1.setLayoutManager(layoutManager);
                                    listHot1.setAdapter(adapterHot1);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        VolleySingleTon.getInstance(context).addToRequestQueue(req);
                    }

                    if (hashtags.size() > 1 ){
                        textHot2.setText("#" + hashtags.get(1).getName());
                        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/store_detail/" + hashtags.get(1).getId(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray postArray = response.getJSONArray("posts");

                                    for (int i = 0; i < postArray.length(); i++) {
                                        Post post = new Post(postArray.getJSONObject(i));
                                        postsHot2.add(post);
                                    }

                                    GridLayoutManager layoutManager;
                                    adapterHot2 = new PostThumbAdapter(activity, postsHot2);
                                    layoutManager = new GridLayoutManager(context, postsHot2.size());
                                    listHot2.setLayoutManager(layoutManager);
                                    listHot2.setAdapter(adapterHot2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        VolleySingleTon.getInstance(context).addToRequestQueue(req);
                    }

                    if (hashtags.size() > 2 ){
                        textHot3.setText("#" + hashtags.get(2).getName());
                        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/store_detail/" + hashtags.get(2).getId(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray postArray = response.getJSONArray("posts");

                                    for (int i = 0; i < postArray.length(); i++) {
                                        Post post = new Post(postArray.getJSONObject(i));
                                        postsHot3.add(post);
                                    }

                                    GridLayoutManager layoutManager;
                                    adapterHot3 = new PostThumbAdapter(activity, postsHot3);
                                    layoutManager = new GridLayoutManager(context, postsHot3.size());
                                    listHot3.setLayoutManager(layoutManager);
                                    listHot3.setAdapter(adapterHot3);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        VolleySingleTon.getInstance(context).addToRequestQueue(req);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }

    private void fetchEditorsPick() {
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/store_detail/" + store.getEditorsPickHashtagId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray postArray = response.getJSONArray("posts");

                    for (int i = 0; i < postArray.length(); i++) {
                        Post post = new Post(postArray.getJSONObject(i));
                        postsEditorsPick.add(post);
                    }

                    GridLayoutManager layoutManager;
                    adapterEditorsPick = new PostThumbAdapter(activity, postsEditorsPick);
                    layoutManager = new GridLayoutManager(context, postsEditorsPick.size());
                    listEditorsPick.setLayoutManager(layoutManager);
                    listEditorsPick.setAdapter(adapterEditorsPick);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }

    private void fetchTodaySeller() {
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, "/user_posts/" + store.getSellerId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray postArray = response.getJSONArray("posts");

                    for (int i = 0; i < postArray.length(); i++) {
                        Post post = new Post(postArray.getJSONObject(i));
                        postsTodaySeller.add(post);
                    }

                    GridLayoutManager layoutManager;
                    adapterTodaySeller = new PostThumbAdapter(activity, postsTodaySeller);
                    layoutManager = new GridLayoutManager(context, postsTodaySeller.size());
                    listTodaySeller.setLayoutManager(layoutManager);
                    listTodaySeller.setAdapter(adapterTodaySeller);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }

    private void jumpToSubStore(String url, int id, String title) {
        Intent i = new Intent(activity, SubStoreActivity.class);
        i.putExtra(IntentTag.URL, url);
        i.putExtra(IntentTag.ID, id);
        i.putExtra(IntentTag.TITLE, title);
        activity.startActivity(i);
    }
}
