package co.treasureisle.shobit.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.treasureisle.shobit.R;
import co.treasureisle.shobit.ViewHolder.DetailImageViewHolder;
import co.treasureisle.shobit.ViewHolder.PostThumbViewHolder;

/**
 * Created by pgseong on 2017. 3. 15..
 */

public class DetailImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = DetailImageAdapter.class.getSimpleName();

    private Activity mActivity;
    private ArrayList<String> imgUrls;

    public DetailImageAdapter(Activity activity, ArrayList<String> imgUrls) {
        this.mActivity = activity;
        this.imgUrls = imgUrls;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.detail_image, parent, false);

        return new DetailImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DetailImageViewHolder) {
            String url = getUrl(position);
            ((DetailImageViewHolder) holder).onBind(mActivity, url);
        }
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }


    private String getUrl(int position) {
        return imgUrls.get(position);
    }
}
