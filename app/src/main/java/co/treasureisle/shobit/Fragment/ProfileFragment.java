package co.treasureisle.shobit.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.EndlessRecyclerOnScrollListener;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 2. 17..
 */

public class ProfileFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    public static ProfileFragment newInstance() {return new ProfileFragment();}

    @Override
    public void onRefresh() { refreshPosts(); }

    public void fetchPosts() {

    }

    public void refreshPosts() {

    }
}