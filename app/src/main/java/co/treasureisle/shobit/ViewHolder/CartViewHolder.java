package co.treasureisle.shobit.ViewHolder;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Adapter.PostThumbAdapter;
import co.treasureisle.shobit.Model.Basket;
import co.treasureisle.shobit.Model.ColorSize;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 31..
 */

public class CartViewHolder extends RecyclerView.ViewHolder {
    public static String TAG = CartViewHolder.class.getSimpleName();

    private Basket basket;

    private CheckBox chkItem;
    private NetworkImageView imgPreview;
    private TextView textTitle;
    private Spinner spnrColorSize;
    private Spinner spnrAmount;
    private Activity activity;

    private ArrayList<ColorSize> colorSizes = new ArrayList<>();
    private ArrayList<String> colorSizeNames = new ArrayList<>();

    public ColorSize selectedColorSize;
    public int selectedAmount;

    public CartViewHolder(View itemView) {
        super(itemView);

        chkItem = (CheckBox) itemView.findViewById(R.id.chk_item);
        imgPreview = (NetworkImageView) itemView.findViewById(R.id.img_preview);
        textTitle = (TextView) itemView.findViewById(R.id.text_title);
        spnrColorSize = (Spinner) itemView.findViewById(R.id.spnr_colorsize);
        spnrAmount = (Spinner) itemView.findViewById(R.id.spnr_amount);

    }

    private void drawLayout(final Activity activity, final Basket basket) {
        this.activity = activity;
        this.basket = basket;
        imgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, basket.getColorSize().getName());
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(activity).getImageLoader();

        imgPreview.setVisibility(View.VISIBLE);
        imgPreview.setImageUrl(basket.getPost().getImgUrl1(), imageLoader);

        textTitle.setText(basket.getPost().getTitle());

        spnrColorSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ColorSize selColorSize = colorSizes.get(position);
                ArrayList<String> amountArray = new ArrayList<>();

                for(int i=0; i<selColorSize.getAvailable(); i++){
                    amountArray.add(String.valueOf(i+1));
                }

                final ArrayAdapter<String> spnrAmountAdapter = new ArrayAdapter<>(
                        activity, R.layout.spinner_item, amountArray);

                spnrAmountAdapter.setDropDownViewResource(R.layout.spinner_item);
                spnrAmount.setAdapter(spnrAmountAdapter);

                selectedColorSize = selColorSize;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnrAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAmount = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fetchColorSize();

    }

    public void onBind(final Activity activity, final Basket basket) {
        drawLayout(activity, basket);
    }

    public void fetchColorSize() {
        final ShobitRequest req = new ShobitRequest(activity, Request.Method.GET, "/color_sizes/" + basket.getPost().getId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray postArray = response.getJSONArray("color_sizes");

                    for (int i = 0; i < postArray.length(); i++) {
                        ColorSize colorSize = new ColorSize(postArray.getJSONObject(i));
                        colorSizes.add(colorSize);
                        colorSizeNames.add(colorSize.getName());
                    }

                    final ArrayAdapter<String> spnrColorSizeAdapter = new ArrayAdapter<>(
                            activity, R.layout.spinner_item, colorSizeNames);

                    spnrColorSizeAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spnrColorSize.setAdapter(spnrColorSizeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        VolleySingleTon.getInstance(activity).addToRequestQueue(req);
    }
}
