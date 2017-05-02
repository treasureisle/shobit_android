package co.treasureisle.shobit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.treasureisle.shobit.Adapter.DetailImageAdapter;
import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Constant.RequestCode;
import co.treasureisle.shobit.Model.ColorSize;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.SessionHelper;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.View.RoundedNetworkImageView;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 15..
 */

public class DetailActivity extends BaseActivity {
    public static final String TAG = DetailActivity.class.getSimpleName();

    private Post post;
    ArrayList<String> imgUrls = new ArrayList<>();

    private ArrayList<ColorSize> colorSizes = new ArrayList<>();
    private ArrayList<String> colorSizeNames = new ArrayList<>();

    public ColorSize selectedColorSize;
    public int selectedAmount;


    private RecyclerView imageListView;
    private DetailImageAdapter adapter;
    private Button likeImageButton;
    private TextView likeText;
    private Button replyImageButton;
    private TextView replyText;
    private View wrapperProfile;
    private RoundedNetworkImageView profileThumbnail;
    private TextView usernameText;
    private TextView userCommentText;
    private TextView brandText;
    private TextView productNameText;
    private TextView ratioText;
    private TextView purchasePriceText;
    private TextView originPriceText;
    private TextView feeText;
    private TextView colorSizeText;
    private TextView regionText;
    private TextView hashtagText;
    private TextView textText;
    private LinearLayout detailButtons;
    private FrameLayout layoutPopupLocation;

    private Button btnCart;
    private Button btnLike;
    private Button btnReply;
    private Button btnShare;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        activity = this;

        post = (Post)(getIntent().getParcelableExtra(IntentTag.POST));

        imageListView = (RecyclerView) findViewById(R.id.image_list);
        likeImageButton = (Button) findViewById(R.id.like_image_button);
        likeText = (TextView) findViewById(R.id.like_text);
        replyImageButton = (Button) findViewById(R.id.reply_image_button);
        replyText = (TextView) findViewById(R.id.reply_text);
        wrapperProfile = findViewById(R.id.wrapper_profile);
        profileThumbnail = (RoundedNetworkImageView) findViewById(R.id.profile_thumbnail);
        usernameText = (TextView) findViewById(R.id.username_text);
        userCommentText = (TextView) findViewById(R.id.usercomment_text);
        brandText = (TextView) findViewById(R.id.brand_text);
        productNameText = (TextView) findViewById(R.id.product_name_text);
        ratioText = (TextView) findViewById(R.id.ratio_text);
        purchasePriceText = (TextView) findViewById(R.id.purchase_price_text);
        originPriceText = (TextView) findViewById(R.id.origin_price_text);
        feeText = (TextView) findViewById(R.id.fee_text);
        colorSizeText = (TextView) findViewById(R.id.color_size_text);
        hashtagText = (TextView) findViewById(R.id.hashtag_text);
        regionText = (TextView) findViewById(R.id.region_text);
        textText = (TextView) findViewById(R.id.text_text);
        detailButtons = (LinearLayout) findViewById(R.id.detail_buttons);

        layoutPopupLocation = (FrameLayout) findViewById(R.id.layout_popup_location);

        btnCart = (Button) findViewById(R.id.btn_cart);
        btnLike = (Button) findViewById(R.id.btn_like);
        btnReply = (Button) findViewById(R.id.btn_reply);
        btnShare = (Button) findViewById(R.id.btn_share);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCartPopup();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLike();
            }
        });

        if(post.isLiked()){
            btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_like_sel));
        } else {
            btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_like_nor));
        }

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, ReplyActivity.class);
                i.putExtra(IntentTag.POST, post);
                startActivity(i);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(DetailActivity.this, "아직 지원하지 않는 기능입니다");
            }
        });


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

        GridLayoutManager layoutManager = new GridLayoutManager(this, imgUrls.size());
        imageListView.setLayoutManager(layoutManager);
        adapter = new DetailImageAdapter(this, imgUrls);
        imageListView.setAdapter(adapter);

        View.OnClickListener likeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLike();
            }
        };

        View.OnClickListener replyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, ReplyActivity.class);
                i.putExtra(IntentTag.POST, post);
                startActivity(i);
            }
        };

        View.OnClickListener profileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = String.valueOf(post.getUser().getId());
                Intent i = new Intent(DetailActivity.this, ProfileActivity.class);
                i.putExtra(IntentTag.USER, userId);
                startActivity(i);
            }
        };

        likeImageButton.setOnClickListener(likeListener);

        likeText.setText("좋아요 " + Utils.numberFormat(post.getLikes()) + "개");
        likeText.setOnClickListener(likeListener);

        replyImageButton.setOnClickListener(replyListener);

        replyText.setText("댓글 " + Utils.numberFormat(post.getReplys()) + "개");
        replyText.setOnClickListener(replyListener);

        wrapperProfile.setOnClickListener(profileListener);

        ImageLoader imageLoader = VolleySingleTon.getInstance(this).getImageLoader();
        profileThumbnail.setVisibility(View.VISIBLE);
        profileThumbnail.setImageUrl(post.getUser().getProfileThumbUrl(), imageLoader);

        usernameText.setText(post.getUser().getUsername());
        userCommentText.setText(post.getUser().getIntroduce());
        brandText.setText(post.getBrand());
        productNameText.setText(post.getProductName());

        int ratio = 100 - (post.getPurchasePrice() * 100 / post.getOriginPrice());
        ratioText.setText(ratio + "%");
        purchasePriceText.setText(Utils.numberFormat(post.getPurchasePrice()) + "  ");
        originPriceText.setText(Utils.numberFormat(post.getOriginPrice()));
        originPriceText.setPaintFlags(originPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        feeText.setText(Utils.numberFormat(post.getFee()));

        String colorSizeString = "";
        colorSizeText.setText(colorSizeString);

        hashtagText.setText(post.getHashtag());
        regionText.setText(Constants.region[Integer.parseInt(post.getRegion())]);
        textText.setText(post.getText());

    }

    private void showCartPopup(){
        final PopupWindow popup = new PopupWindow(layoutPopupLocation);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_purchase, null);

        TextView textTitle = (TextView) view.findViewById(R.id.text_title);
        final Spinner spnrColorSize = (Spinner) view.findViewById(R.id.spnr_colorsize);
        final Spinner spnrAmount = (Spinner) view.findViewById(R.id.spnr_amount);
        Button btnPut = (Button) view.findViewById(R.id.btn_put);
        Button btnClose = (Button) view.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick!!");
                popup.dismiss();
            }
        });

        btnPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = Utils.getPrefs(DetailActivity.this);
                String userId = pref.getString(PrefTag.USER_ID, null);

                if(userId != null) {
                    putCart();
                } else {
                    startActivityForResult(new Intent(DetailActivity.this, LoginPopupActivity.class), RequestCode.REQ_LOGIN);
                }
            }
        });

        textTitle.setText(post.getTitle());

        spnrColorSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ColorSize selColorSize = colorSizes.get(position);
                ArrayList<String> amountArray = new ArrayList<>();

                for(int i=0; i<selColorSize.getAvailable(); i++){
                    amountArray.add(String.valueOf(i+1));
                }

                final ArrayAdapter<String> spnrAmountAdapter = new ArrayAdapter<>(
                        context, R.layout.spinner_item, amountArray);

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

        popup.setContentView(view);
        popup.setWindowLayoutMode(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        layoutPopupLocation.getLocationOnScreen(location);

        popup.showAtLocation(detailButtons, Gravity.NO_GRAVITY, location[0], location[1]);

        fetchColorSize(spnrColorSize);
    }

    public void fetchColorSize(final Spinner spnrColorSize) {
        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/color_sizes/" + post.getId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray postArray = response.getJSONArray("color_sizes");
                    colorSizes.clear();
                    colorSizeNames.clear();

                    for (int i = 0; i < postArray.length(); i++) {
                        ColorSize colorSize = new ColorSize(postArray.getJSONObject(i));
                        colorSizes.add(colorSize);
                        colorSizeNames.add(colorSize.getName());
                    }

                    final ArrayAdapter<String> spnrColorSizeAdapter = new ArrayAdapter<>(
                            context, R.layout.spinner_item, colorSizeNames);

                    spnrColorSizeAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spnrColorSize.setAdapter(spnrColorSizeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        VolleySingleTon.getInstance(this).addToRequestQueue(req);
    }

    public void fetchLike() {
        if (post.isLiked()){

            post.setLikes(post.getLikes() - 1);
            post.setLiked(false);
            btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_like_nor));

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
            btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_like_sel));

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

    public void tryLogin() {
        JSONObject obj = new JSONObject();
        try {
            SharedPreferences pref = Utils.getPrefs(DetailActivity.this);
            String userId = pref.getString(PrefTag.USER_ID, null);
            String accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);

            if (userId != null) {
                obj.put("id", userId);
                obj.put("access_token", accessToken);

                final ShobitRequest req = new ShobitRequest(this, Request.Method.POST, "/session", obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "login succeed");
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            if (error.networkResponse != null) {
                                if (error.networkResponse.statusCode == 403) {
                                    SessionHelper sessionHelper = new SessionHelper(DetailActivity.this);
                                    sessionHelper.clearToken();
                                    Utils.showToast(DetailActivity.this, "다른 기기에서 로그인하여 로그아웃됩니다.");
                                    finish();
                                    startActivity(getIntent());

                                } else if (error.networkResponse.statusCode == 404) {
                                    SessionHelper sessionHelper = new SessionHelper(DetailActivity.this);
                                    sessionHelper.clearToken();
                                    Utils.showToast(DetailActivity.this, "알수없는 오류가 발생하였습니다. 다시 로그인해주세요.");
                                }
                            }
                        }
                    }
                });

                VolleySingleTon.getInstance(this).addToRequestQueue(req);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putCart() {

        HashMap<String,String> params = new HashMap<>();

        params.put("color_size_id", String.valueOf(selectedColorSize.getId()));
        params.put("amount", String.valueOf(selectedAmount));

        params.put("text", replyText.getText().toString());

        MultipartRequest req = new MultipartRequest(this, com.android.volley.Request.Method.POST, "/basket/" + post.getId(), params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "good");
                        Intent i = new Intent(DetailActivity.this, ShobitAlertActivity.class);
                        i.putExtra(IntentTag.ALERT_TITLE, "Shobit");
                        i.putExtra(IntentTag.ALERT_TEXT, "제품을 장바구니에 담았습니다. 장바구니로 이동하시겠습니까?");
                        i.putExtra(IntentTag.ALERT_OK_TITLE, "이동하기");
                        i.putExtra(IntentTag.ALERT_CANCLE_TITLE, "머무르기");

                        startActivityForResult(i, RequestCode.REQ_ALERT);
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500) {
                            Log.d(TAG, error.networkResponse.toString());
                        } else {
                            Log.e(TAG, error.networkResponse.toString());
                        }
                    }
                }
            }
        });

        VolleySingleTon.getInstance(this).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQ_LOGIN:
                tryLogin();
                break;
            case RequestCode.REQ_ALERT:
                if(requestCode == 0) { //OK
                    startActivity(new Intent(this, CartActivity.class));
                } else { //Cancle
                    return;
                }
                break;
        }

    }
}
