package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseListViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = PurchaseListViewHolder.class.getSimpleName();

    Post post;
    Purchase purchase;
    NetworkImageView imgPreview;
    TextView textTitle;
    TextView textPrice;
    TextView textColorSize;
    TextView textAmount;
    TextView textDeliveryCorp;
    Button btnDelivery;
    Button btnSeller;

    Activity activity;

    public PurchaseListViewHolder(View itemView) {
        super(itemView);
        imgPreview = (NetworkImageView)itemView.findViewById(R.id.img_preview);
        imgPreview = (NetworkImageView)itemView.findViewById(R.id.img_preview);
        textTitle = (TextView)itemView.findViewById(R.id.text_title);
        textPrice = (TextView)itemView.findViewById(R.id.text_price);
        textColorSize = (TextView)itemView.findViewById(R.id.text_colorsize);
        textAmount = (TextView)itemView.findViewById(R.id.text_amount);
        textDeliveryCorp = (TextView)itemView.findViewById(R.id.text_delivery_corp);
        btnDelivery = (Button)itemView.findViewById(R.id.btn_delivery);
        btnSeller = (Button) itemView.findViewById(R.id.btn_seller);

    }

    private void drawLayout(final Activity activity, final Purchase purchase) {
        post = purchase.getPost();

        this.purchase = purchase;
        this.activity = activity;
        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, purchase.getColorSize().getName());
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageUrl(purchase.getPost().getImgUrl1(), imageLoader);

        int price = (post.getPurchasePrice() + post.getFee()) * purchase.getAmount();
        textTitle.setText(purchase.getPost().getText());
        textPrice.setText(Utils.numberFormat(price));
        textColorSize.setText(purchase.getColorSize().getName());
        textAmount.setText(purchase.getAmount()+ " 개");

        if (purchase.getIsPaid() == 0){
            textDeliveryCorp.setText("결제여부를 확인중입니다.\n(ㅇㅇ은행 000-00-000000)");
            btnDelivery.setVisibility(View.INVISIBLE);
        } else {
            if (purchase.getDeliveryCode() == 0) {
                textDeliveryCorp.setText("배송준비중입니다.");
                btnDelivery.setVisibility(View.INVISIBLE);
            } else {

            }
        }

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

    public void onBind(final Activity activity, final Purchase purchase) {
        drawLayout(activity, purchase);
    }
}
