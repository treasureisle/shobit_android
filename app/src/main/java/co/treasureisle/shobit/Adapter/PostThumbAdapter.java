package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.PostThumbViewHolder;

/**
 * Created by pgseong on 2017. 3. 13..
 */

public class PostThumbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = PostThumbAdapter.class.getSimpleName();
    private ArrayList<Post> mPosts;

    private Activity mActivity;

    public PostThumbAdapter(Activity activity, ArrayList<Post> mPosts) {
        this.mActivity = activity;
        this.mPosts = mPosts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.post_thumb, parent, false);

        return new PostThumbViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostThumbViewHolder) {
            Post post = getPost(position);
            ((PostThumbViewHolder) holder).onBind(mActivity, post);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    private Post getPost(int position) {
        return mPosts.get(position);
    }
}
