package co.treasureisle.shobit.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.treasureisle.shobit.Adapter.PostThumbAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.EndlessRecyclerOnScrollListener;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 29..
 */

public class SearchPostsActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = SearchPostsActivity.class.getSimpleName();
    public static final int SEARCH_TYPE_TITLE = 0;
    public static final int SEARCH_TYPE_HASHTAG = 1;

    private ArrayList<Post> posts;
    private int searchType = 0;

    private EditText textSearch;
    private RecyclerView postList;
    private PostThumbAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_posts);
        posts = new ArrayList<>();

        searchType = getIntent().getIntExtra(IntentTag.SEARCH_TYPE, 0);

        textSearch = (EditText) findViewById(R.id.text_search);
        postList = (RecyclerView) findViewById(R.id.post_list);

        adapter = new PostThumbAdapter(this, posts);
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager(this, 3);
        postList.setLayoutManager(layoutManager);
        postList.setAdapter(adapter);

        textSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        fetchPosts();
                    }
                } else if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchPosts();
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View view){
        fetchPosts();
    }

    public void fetchPosts() {
        String keyword = textSearch.getText().toString();
        HashMap<String,String> params = new HashMap<>();

        params.put("keyword",keyword);

        if (searchType == SEARCH_TYPE_TITLE) {
            params.put("search_type", "title");
        } else if (searchType == SEARCH_TYPE_HASHTAG) {
            params.put("search_type", "hashtag");
        }

        MultipartRequest req = new MultipartRequest(this, com.android.volley.Request.Method.POST, "/search_post", params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            posts.clear();

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


        VolleySingleTon.getInstance(this).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}
