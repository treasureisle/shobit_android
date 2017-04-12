package co.treasureisle.shobit.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by pgseong on 2017. 3. 27..
 */

public class RelativeLayoutDetectSoftKeyBoard extends RelativeLayout {
    public RelativeLayoutDetectSoftKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface Listener {
        void onSoftKeyBoardShown(boolean isShowing);
    }

    private Listener listener;

    @Override
    protected void onMeasure(int widthMesureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Activity activity = (Activity) getContext();

        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int screenHeight = size.y;

        int diff = (screenHeight - statusBarHeight) - height;
        if (listener != null) {
            listener.onSoftKeyBoardShown(diff > 128);
        }
        super.onMeasure(widthMesureSpec, heightMeasureSpec);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}