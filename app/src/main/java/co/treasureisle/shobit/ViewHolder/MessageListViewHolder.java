package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Activity.MessageActivity;
import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.MessageList;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageListViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = MessageListViewHolder.class.getSimpleName();

    private LinearLayout wrapperProfile;
    private NetworkImageView imgProfileThumb;
    private TextView textUsername;
    private TextView textComment;


    public MessageListViewHolder(View itemView) {
        super(itemView);

        wrapperProfile = (LinearLayout) itemView.findViewById(R.id.wrapper_profile);
        imgProfileThumb = (NetworkImageView) itemView.findViewById(R.id.profile_thumbnail);
        textUsername = (TextView) itemView.findViewById(R.id.username_text);
        textComment = (TextView) itemView.findViewById(R.id.message_text);

    }

    private void drawLayout(final Activity activity, final MessageList messageList) {

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        imgProfileThumb.setVisibility(View.VISIBLE);
        imgProfileThumb.setImageUrl(messageList.getProfileThumbUrl(), imageLoader);
        wrapperProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MessageActivity.class);
                Log.e(TAG, "userId: " + messageList.getId());
                i.putExtra(IntentTag.USER, (User)messageList);
                activity.startActivity(i);
            }
        });

        textUsername.setText(messageList.getUsername());
        textComment.setText(messageList.getLastMessage());


    }

    public void onBind(final Activity activity, final MessageList messageList) {
        drawLayout(activity, messageList);
    }
}
