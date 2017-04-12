package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import co.treasureisle.shobit.Activity.DetailActivity;
import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Activity.ReplyActivity;
import co.treasureisle.shobit.Adapter.FeedImageAdapter;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.ColorSize;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.View.RoundedNetworkImageView;
import co.treasureisle.shobit.VolleySingleTon;

import static co.treasureisle.shobit.Activity.BaseActivity.context;

/**
 * Created by pgseong on 2017. 3. 17..
 */

public class FeedViewHolder  extends RecyclerView.ViewHolder {
    public static String TAG = PostThumbViewHolder.class.getSimpleName();

    private LinearLayout wrapper;
    private LinearLayout wrapperProfile;
    private RoundedNetworkImageView profileImage;
    private TextView usernameText;
    private RecyclerView imageListView;
    private Button cartButton;
    private Button likeButton;
    private Button replyButton;
    private Button shareButton;
    private Button likeImageButton;
    private TextView likeText;
    private Button replyImageButton;
    private TextView replyText;
    private TextView titleTextView;
    private TextView priceTextView;
    private FeedImageAdapter adapter;

    private ArrayList<String> imgUrls = new ArrayList<String>();
    private Post post = null;


    public FeedViewHolder(View itemView) {
        super(itemView);

        wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
        wrapperProfile = (LinearLayout) itemView.findViewById(R.id.wrapper_profile);
        profileImage = (RoundedNetworkImageView) itemView.findViewById(R.id.profile_thumbnail);
        usernameText = (TextView) itemView.findViewById(R.id.username_text);
        imageListView = (RecyclerView) itemView.findViewById(R.id.image_list);
        cartButton = (Button) itemView.findViewById(R.id.btn_feed_cart);
        likeButton = (Button) itemView.findViewById(R.id.btn_feed_like);
        replyButton = (Button) itemView.findViewById(R.id.btn_feed_reply);
        shareButton = (Button) itemView.findViewById(R.id.btn_feed_share);
        likeImageButton = (Button) itemView.findViewById(R.id.like_image_button);
        likeText = (TextView) itemView.findViewById(R.id.like_text);
        replyImageButton = (Button) itemView.findViewById(R.id.reply_image_button);
        replyText = (TextView) itemView.findViewById(R.id.reply_text);
        titleTextView = (TextView) itemView.findViewById(R.id.feed_title);
        priceTextView = (TextView) itemView.findViewById(R.id.feed_price);
    }

    private void drawLayout(final Activity activity, final Post post) {

        this.post = post;

        wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra(IntentTag.POST, post);
                activity.startActivity(i);
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        wrapperProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProfileActivity.class);
                i.putExtra(IntentTag.USER, String.valueOf(post.getUser().getId()));
                activity.startActivity(i);
            }
        });

        profileImage.setVisibility(View.VISIBLE);
        profileImage.setImageUrl(post.getUser().getProfileThumbUrl(), imageLoader);
        usernameText.setText(post.getUser().getUsername());

        imgUrls.clear();

        if (!(post.getImgUrl1().equals("null"))) {
            imgUrls.add(post.getImgUrl1());
        }
        if (!(post.getImgUrl2().equals("null"))) {
            imgUrls.add(post.getImgUrl2());
        }
        if (!(post.getImgUrl3().equals("null"))) {
            imgUrls.add(post.getImgUrl3());
        }
        if (!(post.getImgUrl4().equals("null"))) {
            imgUrls.add(post.getImgUrl4());
        }
        if (!(post.getImgUrl5().equals("null"))) {
            imgUrls.add(post.getImgUrl5());
        }

        GridLayoutManager layoutManager = new GridLayoutManager(activity, imgUrls.size());
        imageListView.setLayoutManager(layoutManager);
        adapter = new FeedImageAdapter(activity, imgUrls);
        imageListView.setAdapter(adapter);

        View.OnClickListener cartListener =(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra(IntentTag.POST, post);
                activity.startActivity(i);
            }
        });

        View.OnClickListener likeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLike();
            }
        };

        View.OnClickListener replyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ReplyActivity.class);
                i.putExtra(IntentTag.POST, post);
                activity.startActivity(i);
            }
        };

        View.OnClickListener shareListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };

        cartButton.setOnClickListener(cartListener);
        likeButton.setOnClickListener(likeListener);
        likeImageButton.setOnClickListener(likeListener);
        likeText.setOnClickListener(likeListener);
        replyButton.setOnClickListener(replyListener);
        replyImageButton.setOnClickListener(replyListener);
        replyText.setOnClickListener(replyListener);
        shareButton.setOnClickListener(shareListener);

        likeText.setText("좋아요 " + Utils.numberFormat(post.getLikes()) + "개");
        replyText.setText("댓글 " + Utils.numberFormat(post.getReplys()) + "개");

        int price = post.getPurchasePrice() + post.getFee();

        titleTextView.setText(post.getTitle());
        priceTextView.setText(Utils.numberFormat(price) + "원");

        if (post.isLiked()) {
            likeButton.setBackground(context.getResources().getDrawable(R.drawable.btn_feed_like_sel));
        } else {
            likeButton.setBackground(context.getResources().getDrawable(R.drawable.btn_feed_like_nor));
        }


    }

    public void onBind(final Activity activity, final Post post) {
        drawLayout(activity, post);
    }

    public void fetchLike() {
        if (post.isLiked()){

            post.setLikes(post.getLikes() - 1);
            post.setLiked(false);
            likeButton.setBackground(context.getResources().getDrawable(R.drawable.btn_feed_like_nor));

            final ShobitRequest req = new ShobitRequest(context, Request.Method.DELETE, "/like/" + post.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(context).addToRequestQueue(req);


        } else {
            post.setLikes(post.getLikes() + 1);
            post.setLiked(true);
            likeButton.setBackground(context.getResources().getDrawable(R.drawable.btn_feed_like_sel));

            final ShobitRequest req = new ShobitRequest(context, Request.Method.POST, "/like/" + post.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(context).addToRequestQueue(req);


        }
        likeText.setText("좋아요 " + Utils.numberFormat(post.getLikes()) + "개");
    }

}