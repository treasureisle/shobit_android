package co.treasureisle.shobit.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import co.treasureisle.shobit.Activity.SubStoreActivity;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Event;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.VolleySingleTon;

/**
 * Created by pgseong on 2017. 3. 30..
 */

public class EventFragmnet extends Fragment {
    public static final String TAG = EventFragmnet.class.getSimpleName();
    private Event event;
    private Context context;
    private Activity activity;

    public static EventFragmnet newInstance(Event event) {
        Bundle args = new Bundle();

        EventFragmnet fragment = new EventFragmnet();
        fragment.setArguments(args);
        fragment.event = event;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        NetworkImageView imgEvent = (NetworkImageView)view.findViewById(R.id.img_event);

        imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SubStoreActivity.class);
                i.putExtra(IntentTag.URL, "/store_detail/");
                i.putExtra(IntentTag.ID, event.getHashtagId());
                i.putExtra(IntentTag.TITLE, event.getTitle());
                activity.startActivity(i);
            }
        });

        ImageLoader imageLoader = VolleySingleTon.getInstance(context).getImageLoader();

        imgEvent.setVisibility(View.VISIBLE);
        imgEvent.setImageUrl(event.getImgUrl(), imageLoader);

        return view;
    }
}
