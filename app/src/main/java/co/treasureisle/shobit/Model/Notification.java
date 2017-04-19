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
    private User user;
    private User sender;
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
            setUser(new User(o.getJSONObject("user")));
            setSender(new User(o.getJSONObject("sender")));
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
        this.user = src.readParcelable(User.class.getClassLoader());
        this.sender = src.readParcelable(User.class.getClassLoader());
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
        dest.writeParcelable(getUser(), 0);
        dest.writeParcelable(getSender(), 0);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
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
