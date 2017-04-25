package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.MessageList;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.MessageListViewHolder;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = MessageListAdapter.class.getSimpleName();
    private ArrayList<MessageList> messageLists;
    private Activity activity;

    public MessageListAdapter(Activity activity, ArrayList<MessageList> messageLists) {
        this.activity = activity;
        this.messageLists = messageLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.message_list, parent, false);

        return new MessageListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageListViewHolder) {
            MessageList messageList = getMessageList(position);
            ((MessageListViewHolder) holder).onBind(activity, messageList);
        }
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    private MessageList getMessageList(int position) {
        return messageLists.get(position);
    }

}
