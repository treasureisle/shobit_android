package co.treasureisle.shobit.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import co.treasureisle.shobit.Constant.Constants;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.ColorSize;
import co.treasureisle.shobit.Model.Post;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 17..
 */

public class UploadActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = UploadActivity.class.getSimpleName();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static final int BTN_IMG1 = 1;
    private static final int BTN_IMG2 = 2;
    private static final int BTN_IMG3 = 3;
    private static final int BTN_IMG4 = 4;
    private static final int BTN_IMG5 = 5;
    public static final int POST_TYPE_SELL = 0;
    public static final int POST_TYPE_BUY = 1;
    public static final int POST_TYPE_REVIEW = 2;

    private ArrayList<String> regions = new ArrayList<>();
    private ArrayList<String> hashtags = new ArrayList<>();
    private ArrayList<ColorSize> colorSizes = new ArrayList<>();


    private ImageButton btnImg1;
    private ImageButton btnImg2;
    private ImageButton btnImg3;
    private ImageButton btnImg4;
    private ImageButton btnImg5;
    private LinearLayout wrapperColorSize;
    private LinearLayout wrapperHashtag;
    private EditText titleText;
    private EditText brandText;
    private EditText productNameText;
    private EditText originPriceText;
    private EditText purchasePriceText;
    private EditText feeText;
    private EditText deliveryPriceText;
    private EditText colorSizeText;
    private EditText amountText;
    private Spinner spnrRegion;
    private EditText hashtagText;
    private EditText textText;
    private TextView textColorSizes;
    private TextView textHashtags;
    private Button btnUpload;
    private Button btnAddColorSize;
    private Button btnAddHashtag;

    private int selectedImageButton = 0;

    private Uri mImageCaptureUri;

    private int selectedRegion = 0;
    private int postType = 0;
    private File imgData1 = null;
    private File imgData2 = null;
    private File imgData3 = null;
    private File imgData4 = null;
    private File imgData5 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        getIntent().getIntExtra(IntentTag.POST_TYPE, 0);

        btnImg1 = (ImageButton) findViewById(R.id.btn_img1);
        btnImg2 = (ImageButton) findViewById(R.id.btn_img2);
        btnImg3 = (ImageButton) findViewById(R.id.btn_img3);
        btnImg4 = (ImageButton) findViewById(R.id.btn_img4);
        btnImg5 = (ImageButton) findViewById(R.id.btn_img5);
        titleText = (EditText) findViewById(R.id.title_text);
        brandText = (EditText) findViewById(R.id.brand_text);
        productNameText = (EditText) findViewById(R.id.product_name_text);
        originPriceText = (EditText) findViewById(R.id.origin_price_text);
        purchasePriceText = (EditText) findViewById(R.id.purchase_price_text);
        feeText = (EditText) findViewById(R.id.fee_text);
        deliveryPriceText = (EditText) findViewById(R.id.delivery_price);
        colorSizeText = (EditText) findViewById(R.id.color_size_text);
        amountText = (EditText) findViewById(R.id.amount_text);
        btnAddColorSize = (Button) findViewById(R.id.btn_color_size_add);
        wrapperColorSize = (LinearLayout) findViewById(R.id.wrapper_colorsize);
        hashtagText = (EditText) findViewById(R.id.hashtag_text);
        btnAddHashtag = (Button) findViewById(R.id.btn_hashtag_add);
        wrapperHashtag = (LinearLayout) findViewById(R.id.wrapper_hashtag);
        spnrRegion = (Spinner) findViewById(R.id.spnr_region);
        textText = (EditText) findViewById(R.id.text_text);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        textColorSizes = (TextView) findViewById(R.id.text_colorsizes);
        textHashtags = (TextView) findViewById(R.id.text_hashtags);

        ArrayAdapter<String> spnrRegionAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, Constants.region);
        spnrRegionAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnrRegion.setAdapter(spnrRegionAdapter);

        spnrRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRegion = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnImg1.setOnClickListener(this);
        btnImg2.setOnClickListener(this);
        btnImg3.setOnClickListener(this);
        btnImg4.setOnClickListener(this);
        btnImg5.setOnClickListener(this);

        btnAddColorSize.setOnClickListener(this);
        btnAddHashtag.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnUpload) {
            upload();
            return;
        }

        if (v == btnAddColorSize) {
            addColorSize();
            return;
        }

        if (v == btnAddHashtag) {
            addHashtag();
            return;
        }

        if (v == btnImg1) {
            selectedImageButton = BTN_IMG1;
        } else if (v == btnImg2) {
            selectedImageButton = BTN_IMG2;
        } else if (v == btnImg3) {
            selectedImageButton = BTN_IMG3;
        } else if (v == btnImg4) {
            selectedImageButton = BTN_IMG4;
        } else if (v == btnImg5) {
            selectedImageButton = BTN_IMG5;
        }

        final PopupWindow popup = new PopupWindow(v);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_picture, null);

        Button btnCamera = (Button)view.findViewById(R.id.btn_camera);
        Button btnAlbum = (Button)view.findViewById(R.id.btn_album);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                    Log.e(TAG, "permissioin not granted");

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 임시로 사용할 파일의 경로를 생성
                    String filename = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), filename));


                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    ActivityCompat.requestPermissions(UploadActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            UploadActivity.PICK_FROM_CAMERA);
                } else {
                    // permission has been granted, continue as usual

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // 임시로 사용할 파일의 경로를 생성
                    String filename = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), filename));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                }



            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });

        popup.setContentView(view);
        popup.setWindowLayoutMode(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }
            case CROP_FROM_IMAGE: {
                if (resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap cropBitmap = extras.getParcelable("data");
                    File imgFile = new File(SaveBitmapToFileCache(cropBitmap));

                    switch (selectedImageButton) {
                        case BTN_IMG1:
                            imgData1 = imgFile;
                            btnImg1.setImageBitmap(cropBitmap);
                            break;
                        case BTN_IMG2:
                            imgData2 = imgFile;
                            btnImg2.setImageBitmap(cropBitmap);
                            break;
                        case BTN_IMG3:
                            imgData3 = imgFile;
                            btnImg3.setImageBitmap(cropBitmap);
                            break;
                        case BTN_IMG4:
                            imgData4 = imgFile;
                            btnImg4.setImageBitmap(cropBitmap);
                            break;
                        case BTN_IMG5:
                            imgData5 = imgFile;
                            btnImg5.setImageBitmap(cropBitmap);
                            break;
                    }

                    break;

                }
            }
        }
    }

    public void upload() {
        HashMap<String,String> params = new HashMap<>();
        HashMap<String,File> fileParams = new HashMap<>();
        if (imgData1 != null) fileParams.put("img1", imgData1);
        if (imgData2 != null) fileParams.put("img2", imgData2);
        if (imgData3 != null) fileParams.put("img3", imgData3);
        if (imgData4 != null) fileParams.put("img4", imgData4);
        if (imgData5 != null) fileParams.put("img5", imgData5);

        params.put("post_type", String.valueOf(postType));

        if(imgData1 == null || isEmpty(titleText) || isEmpty(brandText) || isEmpty(productNameText) || isEmpty(originPriceText) || isEmpty(purchasePriceText) ||
               isEmpty(feeText) || isEmpty(hashtagText) || isEmpty(textText) ) {
            Utils.showToast(this, "완료되지 않은 항목이 존재합니다");
            return;
        }

        params.put("title", titleText.getText().toString());
        params.put("brand", brandText.getText().toString());
        params.put("product_name", productNameText.getText().toString());
        params.put("origin_price", originPriceText.getText().toString());
        params.put("purchase_price", purchasePriceText.getText().toString());
        params.put("fee", feeText.getText().toString());
        params.put("region", String.valueOf(selectedRegion));
        params.put("hashtag", hashtagText.getText().toString());
        params.put("text", textText.getText().toString());

        MultipartRequest req = new MultipartRequest(this, com.android.volley.Request.Method.POST, "/posts", params, fileParams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Post post = new Post(response.getJSONObject("posts"));
                            uploadColorSizes(post);
                            uploadHashtags(post);
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
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

    private String SaveBitmapToFileCache(Bitmap bitmap) {

        String strFilePath = getApplicationContext().getFilesDir().getPath().toString() + "tmp_crop_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        Log.e(TAG, strFilePath);

        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if (out != null) out.close();
                return strFilePath;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    private boolean isEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }

    private void addColorSize(){

        if (isEmpty(colorSizeText)) {
            Utils.showToast(this, "옵션을 입력 해 주세요");
            return;
        }

        if (isEmpty(amountText)) {
            Utils.showToast(this, "수량을 입력 해 주세요");
            return;
        }

        String text = textColorSizes.getText().toString();
        String strColorSize = colorSizeText.getText().toString();
        int amount = Integer.parseInt(amountText.getText().toString());
        colorSizeText.setText("");
        amountText.setText("");
        colorSizes.add(new ColorSize(strColorSize, amount));

        if(text.isEmpty()){
            text = strColorSize + ": " + amount + "개";
        } else {
            text = text + "\n" + strColorSize + ": " + amount + "개";
        }
        textColorSizes.setText(text);
    }

    private void addHashtag(){

        if (isEmpty(hashtagText)) {
            Utils.showToast(this, "해시태그를 입력 해 주세요");
            return;
        }

        String text= textHashtags.getText().toString();
        String strHashtag = hashtagText.getText().toString();
        textHashtags.setText("");
        hashtags.add(strHashtag);

        if(text.isEmpty()){
            text = "#" + strHashtag;
        } else {
            text = text + "  #" + strHashtag;
        }
        textHashtags.setText(text);
    }

    private void uploadColorSizes(Post post) {
        HashMap<String,String> params = new HashMap<>();

        params.put("count", String.valueOf(colorSizes.size()));
        if(colorSizes.size() > 0) {
            params.put("name1", colorSizes.get(0).getName());
            params.put("available1", String.valueOf(colorSizes.get(0).getAvailable()));
        }
        if(colorSizes.size() > 1) {
            params.put("name2", colorSizes.get(1).getName());
            params.put("available2", String.valueOf(colorSizes.get(1).getAvailable()));
        }
        if(colorSizes.size() > 2) {
            params.put("name3", colorSizes.get(2).getName());
            params.put("available3", String.valueOf(colorSizes.get(2).getAvailable()));
        }
        if(colorSizes.size() > 3) {
            params.put("name4", colorSizes.get(3).getName());
            params.put("available4", String.valueOf(colorSizes.get(3).getAvailable()));
        }
        if(colorSizes.size() > 4) {
            params.put("name5", colorSizes.get(4).getName());
            params.put("available5", String.valueOf(colorSizes.get(4).getAvailable()));
        }

        MultipartRequest req = new MultipartRequest(context, com.android.volley.Request.Method.POST, "/color_sizes/" + post.getId(), params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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


        VolleySingleTon.getInstance(context).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void uploadHashtags(Post post) {
        HashMap<String,String> params = new HashMap<>();

        params.put("count", String.valueOf(hashtags.size()));
        if(hashtags.size() > 0) {
            params.put("name1", hashtags.get(0));
        }
        if(hashtags.size() > 1) {
            params.put("name2", hashtags.get(1));
        }
        if(hashtags.size() > 2) {
            params.put("name3", hashtags.get(2));
        }
        if(hashtags.size() > 3) {
            params.put("name4", hashtags.get(3));
        }
        if(hashtags.size() > 4) {
            params.put("name5", hashtags.get(4));
        }
        MultipartRequest req = new MultipartRequest(context, com.android.volley.Request.Method.POST, "/hashtags/" + post.getId(), params, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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


        VolleySingleTon.getInstance(context).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}

