package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.R;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 17..
 */

public class FeedImageViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = FeedImageViewHolder.class.getSimpleName();

    public NetworkImageView imageView;

    public FeedImageViewHolder(View itemView) {
        super(itemView);
        imageView = (NetworkImageView) itemView.findViewById(R.id.feed_image);
    }

    private void drawLayout(final Activity activity, final String url) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, url);
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageUrl(url, imageLoader);
    }

    public void onBind(final Activity activity, final String url) {
        drawLayout(activity, url);
    }
}

