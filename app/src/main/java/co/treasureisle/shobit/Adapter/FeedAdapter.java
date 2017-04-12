package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.FeedViewHolder;

/**
 * Created by pgseong on 2017. 3. 17..
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = PostThumbAdapter.class.getSimpleName();
    private ArrayList<Post> mPosts;

    private Activity mActivity;

    public FeedAdapter(Activity activity, ArrayList<Post> mPosts) {
        this.mActivity = activity;
        this.mPosts = mPosts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.feed, parent, false);

        return new FeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedViewHolder) {
            Post post = getPost(position);
            ((FeedViewHolder) holder).onBind(mActivity, post);
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
