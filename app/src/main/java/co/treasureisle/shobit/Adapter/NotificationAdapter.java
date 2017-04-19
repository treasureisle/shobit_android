package co.treasureisle.shobit.Adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.Model.Notification;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.NotificationViewHolder;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = PostThumbAdapter.class.getSimpleName();
    private ArrayList<Notification> notifications;
    private Activity activity;

    public NotificationAdapter(Activity activity, ArrayList<Notification> notifications) {
        this.activity = activity;
        this.notifications = notifications;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.notification, parent, false);

        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationViewHolder) {
            Notification notification = getNotification(position);
            ((NotificationViewHolder) holder).onBind(activity, notification);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    private Notification getNotification(int position) {
        return notifications.get(position);
    }

}
