package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 4. 21..
 */

public class MessageTimestamp implements Parcelable {
    private int id;
    private int userId;
    private String timestamp;

    public static final Creator<MessageTimestamp> CREATOR = new Creator<MessageTimestamp>() {
        @Override
        public MessageTimestamp createFromParcel(Parcel source) {
            return new MessageTimestamp(source);
        }

        @Override
        public MessageTimestamp[] newArray(int size) {
            return new MessageTimestamp[size];
        }
    };

    public MessageTimestamp(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setUserId(o.getInt("user_id"));
            setTimestamp(o.getString("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MessageTimestamp(Parcel src) {
        setId(src.readInt());
        setUserId(src.readInt());
        setTimestamp(src.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getUserId());
        dest.writeString(getTimestamp());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
