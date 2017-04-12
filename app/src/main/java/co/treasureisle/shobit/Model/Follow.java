package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 3. 23..
 */

public class Follow implements Parcelable {
    private int id;
    private int followerId;
    private int followingId;

    public static final Creator<Follow> CREATOR = new Creator<Follow>() {
        @Override
        public Follow createFromParcel(Parcel source) {
            return new Follow(source);
        }

        @Override
        public Follow[] newArray(int size) {
            return new Follow[size];
        }
    };

    public Follow(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setFollowerId(o.getInt("follower_id"));
            setFollowingId(o.getInt("following_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Follow(Parcel src) {
        setId(src.readInt());
        setFollowerId(src.readInt());
        setFollowingId(src.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getFollowerId());
        dest.writeInt(getFollowingId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public int getFollowingId() {
        return followingId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }
}
