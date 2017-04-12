package co.treasureisle.shobit.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.treasureisle.shobit.Fragment.RecyclerFragment;
import co.treasureisle.shobit.Fragment.StoreFragment;

/**
 * Created by pgseong on 2017. 3. 14..
 */

public class HomeTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;

    public HomeTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = RecyclerFragment.newInstance("/posts?post_type=sell", RecyclerFragment.ADAPTER_TYPE_HOME);
                break;
            case 1:
                fragment = RecyclerFragment.newInstance("/posts?post_type=buy", RecyclerFragment.ADAPTER_TYPE_HOME);
                break;
            case 2:
                fragment = RecyclerFragment.newInstance("/posts?post_type=review", RecyclerFragment.ADAPTER_TYPE_HOME);
                break;
            case 3:
            default:
                fragment = StoreFragment.newInstance();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
