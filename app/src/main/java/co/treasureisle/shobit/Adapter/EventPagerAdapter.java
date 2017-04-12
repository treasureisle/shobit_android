package co.treasureisle.shobit.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import co.treasureisle.shobit.Fragment.EventFragmnet;
import co.treasureisle.shobit.Model.Event;

/**
 * Created by pgseong on 2017. 3. 30..
 */

public class EventPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Event> events;

    public EventPagerAdapter(FragmentManager fm, ArrayList<Event> events) {
        super(fm);
        this.events = events;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        fragment = EventFragmnet.newInstance(events.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return events.size();
    }
}
