package co.treasureisle.shobit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Activity.ProfileActivity;
import co.treasureisle.shobit.Activity.ReplyActivity;
import co.treasureisle.shobit.Constant.IntentTag;
import co.treasureisle.shobit.Model.Reply;
import co.treasureisle.shobit.Model.User;
import co.treasureisle.shobit.R;
import co.treasureisle.shobit.Request.DefaultRequest;
import co.treasureisle.shobit.Request.ShobitRequest;
import co.treasureisle.shobit.Utils;
import co.treasureisle.shobit.View.HeaderRecyclerViewAdapter;
import co.treasureisle.shobit.View.RoundedNetworkImageView;
import co.treasureisle.shobit.View.ShobitTextView;
import co.treasureisle.shobit.VolleySingleTon;

import static co.treasureisle.shobit.Activity.BaseActivity.context;

/**
 * Created by pgseong on 2017. 3. 27..
 */

public class ReplyAdapter extends HeaderRecyclerViewAdapter {
    public static final String TAG = ReplyAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Reply> replies;
    private OnCreateHeaderView onCreateHeaderView;
//    private OnClickMore onClickMore;

    public ReplyAdapter(Context context, ArrayList<Reply> objs, OnCreateHeaderView onCreateHeaderView, OnClickMore onClickMore) {
        this.context = context;
        this.replies = objs;
        setOnCreateHeaderView(onCreateHeaderView);
        setOnClickMore(onClickMore);
    }

    @Override
    public boolean useHeader() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_header_reply, parent, false);

        return new HeaderViewHolder(v);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            Log.e(TAG, "view holder is null!!");
            return;
        }
        RelativeLayout allWrapper = ((HeaderViewHolder) holder).allWrapper;
        allWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onClickMore.onClick();
            }
        });
    }

    @Override
    public boolean useFooter() {
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reply, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        final Reply reply = getItem(position);

        final User user = reply.getUser();

        int marginLeft = reply.getDepth() * 100;

        ViewGroup.LayoutParams layoutParams = viewHolder.spaceIndent.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.width += marginLeft;
        viewHolder.spaceIndent.setLayoutParams(layoutParams);

        viewHolder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ReplyActivity.class);
                i.putExtra(IntentTag.POST, reply.getPost());
                i.putExtra(IntentTag.PARENT_ID, reply.getId());
                context.startActivity(i);
            }
        });
        viewHolder.numReplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ReplyActivity.class);
                i.putExtra(IntentTag.POST, reply.getPost());
                i.putExtra(IntentTag.PARENT_ID, reply.getId());
                context.startActivity(i);
            }
        });

        viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        viewHolder.profileThumb.setImageUrl(user.getProfileThumbUrl(), VolleySingleTon.getInstance(context).getImageLoader());
        viewHolder.profileThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "profile touched");
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra(IntentTag.USER, String.valueOf(reply.getUser().getId()));
                context.startActivity(i);
            }
        });
        viewHolder.profileName.setText(user.getUsername());

        JodaTimeAndroid.init(context);
        viewHolder.reply.setText(reply.getText());
        viewHolder.createdAt.setText(Utils.getStrDateFromNow(context, new DateTime(), reply.getCreatedAt()));

        viewHolder.numLikes.setText("좋아요 " + reply.getLikes() + "개");
        viewHolder.numReplies.setText("댓글 " + reply.getReplies() + "개");

        if (reply.isLiked()) {
            viewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_preview_like_sel));
        } else {
            viewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_preview_like_nor));
        }

        viewHolder.numLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLikes(viewHolder, reply);
            }
        });

        viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLikes(viewHolder, reply);
            }
        });


    }


    @Override
    public int getBasicItemCount() {
        return replies.size();
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    public Reply getItem(int position) {
        return replies.get(position);
    }

    public void setOnCreateHeaderView(OnCreateHeaderView onCreateHeaderView) {
        this.onCreateHeaderView = onCreateHeaderView;
    }

    public void setOnClickMore(OnClickMore onClickMore) {
//        this.onClickMore = onClickMore;
    }

    public interface OnCreateHeaderView {
        void onCreated(View headerView);
    }

    public interface OnClickMore {
        void onClick();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout allWrapper;
        RoundedNetworkImageView profileThumb;
        TextView profileName;
        TextView reply;
        TextView createdAt;
        TextView numLikes;
        TextView numReplies;
        Button btnLike;
        Button btnReply;
        Space spaceIndent;

        public ViewHolder(View view) {
            super(view);

            allWrapper = (RelativeLayout) view.findViewById(R.id.allWrapper);
            spaceIndent = (Space) view.findViewById(R.id.space_indent);
            profileThumb = (RoundedNetworkImageView) view.findViewById(R.id.profileThumb);
            profileName = (TextView) view.findViewById(R.id.profileName);
            reply = (TextView) view.findViewById(R.id.reply_text);
            createdAt = (TextView) view.findViewById(R.id.createdAt);
            numLikes = (TextView) view.findViewById(R.id.num_like_text);
            numReplies = (TextView) view.findViewById(R.id.num_reply_text);
            btnLike = (Button) view.findViewById(R.id.like_image_button);
            btnReply = (Button) view.findViewById(R.id.reply_image_button);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout allWrapper;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            allWrapper = (RelativeLayout) itemView.findViewById(R.id.commentHeaderAllWrapper);
            onCreateHeaderView.onCreated(allWrapper);
        }
    }

    private void fetchLikes(ViewHolder viewHolder, Reply reply) {
        if (reply.isLiked()) {

            reply.setLikes(reply.getLikes() - 1);
            reply.setLiked(false);
            viewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_preview_like_nor));

            final ShobitRequest req = new ShobitRequest(context, Request.Method.DELETE, "/reply_like/" + reply.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(context).addToRequestQueue(req);


        } else

        {
            reply.setLikes(reply.getLikes() + 1);
            reply.setLiked(true);
            viewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.btn_detail_preview_like_sel));

            final ShobitRequest req = new ShobitRequest(context, Request.Method.POST, "/reply_like/" + reply.getId(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null) {
                        Log.e(TAG, error.getMessage());
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                            }
                        }
                    }
                }
            });

            VolleySingleTon.getInstance(context).addToRequestQueue(req);


        }

        viewHolder.numLikes.setText("좋아요 " + Utils.numberFormat(reply.getLikes()) + "개");
    }

}