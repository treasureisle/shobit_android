package co.treasureisle.shobit.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import com.devspark.robototextview.util.RobotoTextViewUtils;
import com.devspark.robototextview.util.RobotoTypefaceManager;

/**
 * Created by pgseong on 2017. 3. 27..
 */

public class ShobitTextView extends AppCompatTextView {
    public ShobitTextView(Context context) {
        super(context);

        setTypeFace(context);
        setIncludeFontPadding(false);
    }

    public ShobitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setTypeFace(context);
        setIncludeFontPadding(false);
    }

    public ShobitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setTypeFace(context);
        setIncludeFontPadding(false);
    }

    private void setTypeFace(Context context) {
        Typeface typeface = RobotoTypefaceManager.obtainTypeface(
                context,
                RobotoTypefaceManager.Typeface.ROBOTO_REGULAR);
        RobotoTextViewUtils.setTypeface(this, typeface);
    }
}


