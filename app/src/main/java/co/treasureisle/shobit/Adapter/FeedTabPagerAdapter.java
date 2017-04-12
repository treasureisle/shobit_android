package co.treasureisle.shobit.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.treasureisle.shobit.Fragment.RecyclerFragment;

/**
 * Created by pgseong on 2017. 3. 15..
 */

public class FeedTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;

    public FeedTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = RecyclerFragment.newInstance("/feeds?post_type=sell", RecyclerFragment.ADAPTER_TYPE_FEED);
                break;
            default:
                fragment = RecyclerFragment.newInstance("/feeds?post_type=common", RecyclerFragment.ADAPTER_TYPE_FEED);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
