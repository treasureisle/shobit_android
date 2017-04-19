package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class Notification  implements Parcelable {
    private int id;
    private int code;
    private String message;
    private boolean isRead;
    private String createdAt;

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public Notification(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setCode(o.getInt("code"));
            setMessage(o.getString("message"));
            setRead(o.getBoolean("is_read"));
            setCreatedAt(o.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Notification(Parcel src) {
        setId(src.readInt());
        setCode(src.readInt());
        setMessage(src.readString());
        isRead = src.readByte() != 0;
        setCreatedAt(src.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getCode());
        dest.writeString(getMessage());
        dest.writeByte((byte) (isRead() ? 1 : 0));
        dest.writeString(getCreatedAt());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
