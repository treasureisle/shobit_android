package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 2. 13..
 */

public class User implements Parcelable {
    public static final String TAG = User.class.getSimpleName();

    private int id;
    private String username;
    private String profileThumbUrl;
    private String introduce;
    private boolean isMe;

    public User(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setUsername(o.getString("username"));
            setProfileThumbUrl(o.getString("profile_thumb_url"));
            setIntroduce(o.getString("introduce"));
            setMe(o.getBoolean("is_me"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(Parcel src) {
        setId(src.readInt());
        setUsername(src.readString());
        setProfileThumbUrl(src.readString());
        setIntroduce(src.readString());
        isMe = src.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getUsername());
        dest.writeString(getProfileThumbUrl());
        dest.writeString(getIntroduce());
        dest.writeByte((byte) (isMe() ? 1 : 0));
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileThumbUrl() {
        return profileThumbUrl;
    }

    public void setProfileThumbUrl(String profileThumbUrl) {
        this.profileThumbUrl = profileThumbUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
