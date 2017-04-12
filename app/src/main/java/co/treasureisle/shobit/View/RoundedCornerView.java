package co.treasureisle.shobit.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Chcheung on 2016-01-12.
 */

public class RoundedCornerView extends View {
    private Path path;
    public RoundedCornerView(Context context) {
        super(context);
    }

    public RoundedCornerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCornerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (path == null) {
            path = new Path();
            path.addRoundRect(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), 10, 10, Path.Direction.CW);
        }
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }
}
