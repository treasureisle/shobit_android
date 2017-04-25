package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Model.Message;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Utils;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = MessageViewHolder.class.getSimpleName();

    private TextView textMessage;
    private FrameLayout balloon;
    private FrameLayout innerBalloon;

    public MessageViewHolder(View itemView) {
        super(itemView);

        textMessage = (TextView) itemView.findViewById(R.id.message_text);
        balloon = (FrameLayout) itemView.findViewById(R.id.balloon);
        innerBalloon = (FrameLayout) itemView.findViewById(R.id.inner_balloon);

    }

    private void drawLayout(final Activity activity, final Message message) {

        final User user = message.getSender();
        SharedPreferences pref = Utils.getPrefs(activity);
        final int myId = Integer.parseInt(pref.getString(PrefTag.USER_ID, null));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,10,10,20);

        if(user.getId() == myId){
            innerBalloon.setBackgroundColor(Color.parseColor("#eecccc"));

            params.gravity = Gravity.RIGHT;
        }

        balloon.setLayoutParams(params);

        textMessage.setText(message.getMessage());

    }

    public void onBind(final Activity activity, final Message message) {
        drawLayout(activity, message);
    }
}
