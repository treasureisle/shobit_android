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

import co.treasureisle.shobit.Activity.DeliveryPopupActivity;
import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class SellListViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = SellListViewHolder.class.getSimpleName();

    Post post;
    Purchase purchase;
    NetworkImageView imgPreview;
    TextView textPurchaseId;
    TextView textTitle;
    TextView textPrice;
    TextView textColorSize;
    TextView textAmount;
    TextView textDeliveryCorp;
    Button btnDelivery;
    Button btnSeller;

    Activity activity;

    public SellListViewHolder(View itemView) {
        super(itemView);
        imgPreview = (NetworkImageView)itemView.findViewById(R.id.img_preview);
        textPurchaseId = (TextView)itemView.findViewById(R.id.text_purchase_id);
        textTitle = (TextView)itemView.findViewById(R.id.text_title);
        textPrice = (TextView)itemView.findViewById(R.id.text_price);
        textColorSize = (TextView)itemView.findViewById(R.id.text_colorsize);
        textAmount = (TextView)itemView.findViewById(R.id.text_amount);
        textDeliveryCorp = (TextView)itemView.findViewById(R.id.text_delivery_corp);
        btnDelivery = (Button)itemView.findViewById(R.id.btn_delivery);
        btnSeller = (Button) itemView.findViewById(R.id.btn_buyer);


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
        textPurchaseId.setText(String.valueOf(purchase.getId()));
        textTitle.setText(purchase.getPost().getText());
        textPrice.setText(Utils.numberFormat(price));
        textColorSize.setText(purchase.getColorSize().getName());
        textAmount.setText(purchase.getAmount()+ " 개");

        if (purchase.getIsPaid() == 0){
            textDeliveryCorp.setText("미결제");
        } else {
            textDeliveryCorp.setText("결제 완료");
        }

        if (purchase.getDeliveryCode() == 0) {
            textDeliveryCorp.setVisibility(View.INVISIBLE);
            btnDelivery.setVisibility(View.VISIBLE);
        } else {
            int deliveryCode = purchase.getDeliveryCode();
            String text = String.format("%s  %s", Constants.delivery_corp[deliveryCode], purchase.getDeliveryNumber());
            textDeliveryCorp.setVisibility(View.VISIBLE);
            btnDelivery.setVisibility(View.INVISIBLE);
            Log.e(TAG, text);
            textDeliveryCorp.setText(text);
        }

        btnSeller.setText(purchase.getBuyer().getUsername());
        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ProfileActivity.class);
                i.putExtra(IntentTag.USER, String.valueOf(purchase.getBuyer().getId()));
                activity.startActivity(i);
            }
        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DeliveryPopupActivity.class);
                i.putExtra(IntentTag.PURCHASE, purchase);
                activity.startActivity(i);
            }
        });

    }

    public void onBind(final Activity activity, final Purchase purchase) {
        drawLayout(activity, purchase);
    }
}
