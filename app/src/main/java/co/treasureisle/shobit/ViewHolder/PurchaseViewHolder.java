package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Model.Order;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class PurchaseViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = CartViewHolder.class.getSimpleName();

    Order order;
    NetworkImageView imgPreview;
    Activity activity;

    public PurchaseViewHolder(View itemView) {
        super(itemView);
        imgPreview = (NetworkImageView)itemView.findViewById(R.id.img_preview);

    }

    private void drawLayout(final Activity activity, final Order order) {
        this.order = order;
        this.activity = activity;
        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, order.getColorSize().getName());
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageUrl(order.getPost().getImgUrl1(), imageLoader);



    }

    public void onBind(final Activity activity, final Order order) {
        drawLayout(activity, order);
    }
}
