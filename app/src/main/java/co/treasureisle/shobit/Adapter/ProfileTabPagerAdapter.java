package co.treasureisle.shobit.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.treasureisle.shobit.Fragment.RecyclerFragment;
import co.treasureisle.shobit.Fragment.StoreFragment;

/**
 * Created by pgseong on 2017. 3. 22..
 */

public class ProfileTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private String userId;

    public ProfileTabPagerAdapter(String userId, FragmentManager fm, int tabCount) {
        super(fm);
        this.userId = userId;
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = RecyclerFragment.newInstance("/liked_posts/" + userId + "?", RecyclerFragment.ADAPTER_TYPE_HOME);
                break;
            case 1:
                fragment = RecyclerFragment.newInstance("/user_posts/" + userId + "?", RecyclerFragment.ADAPTER_TYPE_HOME);
                break;
            default:
                fragment = RecyclerFragment.newInstance("/user_posts/" + userId + "?", RecyclerFragment.ADAPTER_TYPE_HOME);
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
