package co.treasureisle.shobit.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.FeedAdapter;
import co.treasureisle.shobit.Adapter.PostThumbAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.EndlessRecyclerOnScrollListener;
import co.treasureisle.shobit.View.ShobitSwipeLayout;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 2. 17..
 */

public class RecyclerFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{
    public static final  String TAG = RecyclerFragment.class.getSimpleName();
    public static final int ADAPTER_TYPE_HOME = 0;
    public static final int ADAPTER_TYPE_FEED = 1;

    private Context context;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private ArrayList<Post> posts;
    private String apiUrl;
    private ShobitSwipeLayout swipeLayout;
    private int adapterType;

    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    private RecyclerView listView;


    public static RecyclerFragment newInstance(String apiUrl, int adapterType) {

        Bundle args = new Bundle();
        RecyclerFragment fragment = new RecyclerFragment();
        args.putString(IntentTag.API_URL, apiUrl);
        fragment.setArguments(args);
        fragment.adapterType = adapterType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        this.posts = new ArrayList<>();
        this.apiUrl = getArguments().getString(IntentTag.API_URL);
        if (this.adapterType == ADAPTER_TYPE_HOME) {
            this.adapter = new PostThumbAdapter(getActivity(), posts);
        } else if (this.adapterType == ADAPTER_TYPE_FEED) {
            this.adapter = new FeedAdapter(getActivity(), posts);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        Activity activity = getActivity();

        listView = (RecyclerView) view.findViewById(R.id.list);

        GridLayoutManager layoutManager;

        if (this.adapterType == ADAPTER_TYPE_HOME) {
            layoutManager = new GridLayoutManager(context, 3);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(adapter);

            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    fetchPosts(current_page);
                }
            };
        } else if (this.adapterType == ADAPTER_TYPE_FEED) {
            layoutManager = new GridLayoutManager(context, 1);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(adapter);

            endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    fetchPosts(current_page);
                }
            };
        }



        listView.setOnScrollListener(endlessRecyclerOnScrollListener);

        swipeLayout = (ShobitSwipeLayout) view.findViewById(R.id.swipe);
        swipeLayout.setProgressViewOffset(false, Utils.convertDpToPx(activity, Constants.SWIPE_REFRESH_START_OFFSET), Utils.convertDpToPx(activity, Constants.SWIPE_REFRESH_END_OFFSET));
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });

        endlessRecyclerOnScrollListener.setOnLoadMore(true);
        fetchPosts(1);

        return view;
    }

    @Override
    public void onRefresh() { refreshPosts(); }

    public void fetchPosts(final int page) {
        final ShobitRequest req = new ShobitRequest(context, Request.Method.GET, apiUrl + "&page=" + page, new Response.Listener<JSONObject>() {
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
                swipeLayout.setRefreshing(false);
                endlessRecyclerOnScrollListener.setOnLoadMore(false);
                if (page == 1) {
                    listView.scrollToPosition(0);
                }
            }
        });

        VolleySingleTon.getInstance(context).addToRequestQueue(req);
    }

    public void refreshPosts() {
        posts.clear();
        fetchPosts(1);
    }
}
