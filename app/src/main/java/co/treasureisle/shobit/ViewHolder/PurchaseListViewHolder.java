package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Model.Purchase;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseListViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = PurchaseListViewHolder.class.getSimpleName();

    Purchase purchase;
    NetworkImageView imgPreview;
    Activity activity;

    public PurchaseListViewHolder(View itemView) {
        super(itemView);
        imgPreview = (NetworkImageView)itemView.findViewById(R.id.img_preview);

    }

    private void drawLayout(final Activity activity, final Purchase purchase) {
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

    }

    public void onBind(final Activity activity, final Purchase purchase) {
        drawLayout(activity, purchase);
    }
}
