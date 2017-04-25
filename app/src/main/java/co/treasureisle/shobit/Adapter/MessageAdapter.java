package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.Message;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.MessageViewHolder;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = MessageAdapter.class.getSimpleName();
    private ArrayList<Message> messages;
    private Activity activity;

    public MessageAdapter(Activity activity, ArrayList<Message> messages) {
        this.activity = activity;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.message, parent, false);

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder) {
            Message message = getMessage(position);
            ((MessageViewHolder) holder).onBind(activity, message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private Message getMessage(int position) {
        return messages.get(position);
    }

}
