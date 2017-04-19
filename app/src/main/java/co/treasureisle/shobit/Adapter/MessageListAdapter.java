package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.MessageListViewHolder;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = MessageListAdapter.class.getSimpleName();
    private ArrayList<User> users;
    private Activity activity;

    public MessageListAdapter(Activity activity, ArrayList<User> users) {
        this.activity = activity;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.message_list, parent, false);

        return new MessageListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageListViewHolder) {
            User user = getUser(position);
            ((MessageListViewHolder) holder).onBind(activity, user);
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
