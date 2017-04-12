package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Activity.DetailActivity;
import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Layout.RoundedCornerLayout;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

import static co.treasureisle.shobit.Activity.BaseActivity.context;

/**
 * Created by pgseong on 2017. 3. 13..
 */

public class PostThumbViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = PostThumbViewHolder.class.getSimpleName();

    public RoundedCornerLayout wrapper;
    public NetworkImageView thumbImageView;
    public TextView titleTextView;
    public TextView ratioTextView;
    public TextView purchasePriceTextView;
    public ImageView imgFlag;

    public PostThumbViewHolder(View itemView) {
        super(itemView);

        wrapper = (RoundedCornerLayout) itemView.findViewById(R.id.wrapper);
        titleTextView = (TextView) itemView.findViewById(R.id.post_title);
        ratioTextView = (TextView) itemView.findViewById(R.id.ratio);
        purchasePriceTextView = (TextView) itemView.findViewById(R.id.post_purchase_price);
        thumbImageView = (NetworkImageView) itemView.findViewById(R.id.post_thumb);
        imgFlag = (ImageView) itemView.findViewById(R.id.img_flag);
    }

    private void drawLayout(final Activity activity, final Post post) {

        wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra(IntentTag.POST, post);
                activity.startActivity(i);
            }
        });

        int price = post.getPurchasePrice() + post.getFee();
        int flagNum = Constants.flag_img[Integer.parseInt(post.getRegion())];

        titleTextView.setText(post.getTitle());
        int ratio = 100 - (post.getPurchasePrice() * 100 / post.getOriginPrice());
        ratioTextView.setText(ratio + "%");
        purchasePriceTextView.setText(Utils.numberFormat(price) + "원");
        imgFlag.setBackground(context.getResources().getDrawable(flagNum));

//        comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(activity, DetailActivity.class);
//                i.putExtra(IntentTag.SHOBIT, post);
//                activity.startActivity(i);
//            }
//        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        thumbImageView.setVisibility(View.VISIBLE);
        thumbImageView.setImageUrl(post.getImgUrl1(), imageLoader);
    }

    public void onBind(final Activity activity, final Post post) {
        drawLayout(activity, post);
    }

}