package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Activity.DetailActivity;
import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = CartViewHolder.class.getSimpleName();

    Order order;
    Post post;
    NetworkImageView imgPreview;
    TextView textTitle;
    TextView textPrice;
    TextView textColorSize;
    TextView textAmount;
    Button btnSeller;

    Activity activity;

    public PurchaseViewHolder(View itemView) {
        super(itemView);
        imgPreview = (NetworkImageView)itemView.findViewById(R.id.img_preview);
        textTitle = (TextView)itemView.findViewById(R.id.text_title);
        textPrice = (TextView)itemView.findViewById(R.id.text_price);
        textColorSize = (TextView)itemView.findViewById(R.id.text_colorsize);
        textAmount = (TextView)itemView.findViewById(R.id.text_amount);
        btnSeller = (Button) itemView.findViewById(R.id.btn_seller);

    }

    private void drawLayout(final Activity activity, final Order order) {
        post = order.getPost();

        this.order = order;
        this.activity = activity;
        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailActivity.class);
                i.putExtra(IntentTag.POST, order.getPost());
                activity.startActivity(i);
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();
        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageUrl(order.getPost().getImgUrl1(), imageLoader);

        int price = (post.getPurchasePrice() + post.getFee()) * order.getAmount();
        textTitle.setText(order.getPost().getText());
        textPrice.setText(Utils.numberFormat(price));
        textColorSize.setText(order.getColorSize().getName());
        textAmount.setText(order.getAmount()+ " 개");

        btnSeller.setText(post.getUser().getUsername());
        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProfileActivity.class);
                i.putExtra(IntentTag.USER, String.valueOf(post.getUser().getId()));
                activity.startActivity(i);
            }
        });
    }

    public void onBind(final Activity activity, final Order order) {
        drawLayout(activity, order);
    }

}
