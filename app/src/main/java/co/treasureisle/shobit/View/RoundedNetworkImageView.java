package co.treasureisle.shobit.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by pgseong on 2017. 3. 13..
 */

public class RoundedNetworkImageView extends NetworkImageView {
    public static final String TAG = RoundedNetworkImageView.class.getSimpleName();

    public RoundedNetworkImageView(Context context) {
        super(context);
    }

    public RoundedNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null || getWidth() == 0 || getHeight() == 0)
            return;

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (b != null) {
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

            int radius = (getWidth() < getHeight()) ? getWidth() / 2 : getHeight() / 2;

            Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }
    }

    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, radius * 2, radius * 2, false);
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        // Draws the image subtracting the border width
        BitmapShader s = new BitmapShader(scaledBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(s);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - 0.5f, paint);

        return output;
    }
}