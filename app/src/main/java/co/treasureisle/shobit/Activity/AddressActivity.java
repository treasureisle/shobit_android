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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.Model.UserDetail;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.MultipartRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.View.RoundedNetworkImageView;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 4. 12..
 */

public class AddressActivity extends BaseActivity{
    public static final String TAG = SettingActivity.class.getSimpleName();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private File imgData = null;

    private User user;

    private RoundedNetworkImageView imgProfileThumb;
    private EditText textName;
    private EditText textPostalCode;
    private EditText textAddress;
    private EditText textAddressDetail;
    private EditText textPhone;
    private Button btnChangePassword;
    private Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        user = getIntent().getParcelableExtra(IntentTag.USER);

        imgProfileThumb = (RoundedNetworkImageView)findViewById(R.id.img_profile_thumbnail);
        textName = (EditText)findViewById(R.id.text_name);
        textPostalCode = (EditText)findViewById(R.id.text_postalcode);
        textAddress = (EditText)findViewById(R.id.text_address);
        textAddressDetail = (EditText)findViewById(R.id.text_address_detail);
        textPhone = (EditText)findViewById(R.id.text_phone);
        btnChangePassword = (Button)findViewById(R.id.btn_change_password);
        btnSave = (Button)findViewById(R.id.btn_save);

        ImageLoader imageLoader = VolleySingleTon.getInstance(this).getImageLoader();
        imgProfileThumb.setVisibility(View.VISIBLE);
        imgProfileThumb.setImageUrl(user.getProfileThumbUrl(), imageLoader);

        imgProfileThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicturePopup();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddressActivity.this, PasswordPopupActivity.class);
                i.putExtra(IntentTag.USER, user);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });

        final ShobitRequest req = new ShobitRequest(this, Request.Method.GET, "/user_detail/" + user.getId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    UserDetail userDetail = new UserDetail(response.getJSONObject("user_detail"));

                    if (!(Utils.isNullString(userDetail.getName()))) { textName.setText(userDetail.getName()); }
                    if (userDetail.getZipcode() != 0) { textPostalCode.setText(String.valueOf(userDetail.getZipcode())); }
                    if (!(Utils.isNullString(userDetail.getAddress1()))) { textAddress.setText(userDetail.getAddress1()); }
                    if (!(Utils.isNullString(userDetail.getAddress2()))) { textAddressDetail.setText(userDetail.getAddress2()); }
                    if (!(Utils.isNullString(userDetail.getPhone()))) { textPhone.setText(userDetail.getPhone()); }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        VolleySingleTon.getInstance(this).addToRequestQueue(req);

    }

    public void showPicturePopup(){
        final PopupWindow popup = new PopupWindow(imgProfileThumb);
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

                    ActivityCompat.requestPermissions(AddressActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PICK_FROM_CAMERA);
                } else {
                    // permission has been granted, continue as usual

                    Log.e(TAG, "granted");
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
        popup.showAsDropDown(imgProfileThumb);
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

                    imgData = imgFile;
                    imgProfileThumb.setImageBitmap(cropBitmap);

                }
            }
        }
    }

    public void saveAddress() {
        HashMap<String,String> params = new HashMap<>();
        HashMap<String,File> fileParams = new HashMap<>();

        if (imgData != null){
            fileParams.put("profile_thumb", imgData);
        } else {
            Log.e("TAG", "imgData null!");
        }

        if(isEmpty(textName) || isEmpty(textPostalCode) || isEmpty(textAddress) || isEmpty(textAddressDetail) || isEmpty(textPhone)) {
            Utils.showToast(this, "완료되지 않은 항목이 존재합니다");
            return;
        }

        params.put("name", textName.getText().toString());
        params.put("zipcode", textPostalCode.getText().toString());
        params.put("address1", textAddress.getText().toString());
        params.put("address2", textAddressDetail.getText().toString());
        params.put("phone", textPhone.getText().toString());

        MultipartRequest req = new MultipartRequest(this, com.android.volley.Request.Method.POST, "/user_detail/" + user.getId(), params, fileParams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.showToast(AddressActivity.this, "저장되었습니다.");
                        finish();
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null) {
                        Log.e(TAG, error.networkResponse.toString());
                    }
                }
            }
        });


        VolleySingleTon.getInstance(this).addToRequestQueue(req, new DefaultRetryPolicy(
                5 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private boolean isEmpty(EditText et) {
        return et.getText().toString().trim().length() == 0;
    }
}
