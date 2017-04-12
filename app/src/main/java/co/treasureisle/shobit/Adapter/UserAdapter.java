package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.PostThumbViewHolder;
import co.treasureisle.shobit.ViewHolder.UserViewHolder;

/**
 * Created by pgseong on 2017. 4. 12..
 */

public class UserAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = PostThumbAdapter.class.getSimpleName();
    private ArrayList<User> users;
    private Activity activity;

    public UserAdapter(Activity activity, ArrayList<User> users) {
        this.activity = activity;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.user, parent, false);

        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            User user = getUser(position);
            ((UserViewHolder) holder).onBind(activity, user);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    private User getUser(int position) {
        return users.get(position);
    }
}
