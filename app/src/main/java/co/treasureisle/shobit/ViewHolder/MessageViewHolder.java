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

import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Message;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = MessageViewHolder.class.getSimpleName();

    private LinearLayout wrapperProfile;
    private NetworkImageView imgProfileThumb;
    private TextView textUsername;
    private TextView textComment;


    public MessageViewHolder(View itemView) {
        super(itemView);

        wrapperProfile = (LinearLayout) itemView.findViewById(R.id.wrapper_profile);
        imgProfileThumb = (NetworkImageView) itemView.findViewById(R.id.profile_thumbnail);
        textUsername = (TextView) itemView.findViewById(R.id.username_text);
        textComment = (TextView) itemView.findViewById(R.id.usercomment_text);

    }

    private void drawLayout(final Activity activity, final Message message) {

        final User user = message.getSender();

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        imgProfileThumb.setVisibility(View.VISIBLE);
        imgProfileThumb.setImageUrl(user.getProfileThumbUrl(), imageLoader);

        wrapperProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProfileActivity.class);
                Log.e(TAG, "userId: " + user.getId());
                i.putExtra(IntentTag.USER, String.valueOf(user.getId()));
                activity.startActivity(i);
            }
        });

        textUsername.setText(user.getUsername());
        textComment.setText(user.getIntroduce());


    }

    public void onBind(final Activity activity, final Message message) {
        drawLayout(activity, message);
    }
}
