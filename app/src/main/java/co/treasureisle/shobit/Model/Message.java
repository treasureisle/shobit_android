package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 4. 19..
 */

public class Message implements Parcelable {
    private int id;
    private User sender;
    private User reciever;
    private String message;
    private String createdAt;
    private boolean isRead;

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public Message(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setSender(new User(o.getJSONObject("sender")));
            setReciever(new User(o.getJSONObject("reciever")));
            setMessage(o.getString("message"));
            setRead(o.getBoolean("is_read"));
            setCreatedAt(o.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Message(Parcel src) {
        setId(src.readInt());
        this.sender = src.readParcelable(User.class.getClassLoader());
        this.reciever = src.readParcelable(User.class.getClassLoader());
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
        dest.writeParcelable(getSender(), 0);
        dest.writeParcelable(getReciever(), 0);
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

