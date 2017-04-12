package co.treasureisle.shobit.View;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import co.treasureisle.shobit.R;

/**
 * Created by pgseong on 2017. 2. 20..
 */

public class ShobitSwipeLayout extends SwipeRefreshLayout{
    public ShobitSwipeLayout(Context context) {
        super(context);

        this.setColorSchemeResources(R.color.primary_color);
    }

    public ShobitSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setColorSchemeResources(R.color.primary_color);
    }
}
