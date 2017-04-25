package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 2. 13..
 */

public class MessageList extends User {
    public static final String TAG = User.class.getSimpleName();

    private String lastMessage;
    private String lastMessageCreatedAt;
    private boolean isRead;

    public MessageList(JSONObject o) {
        super(o);
        try {
            setLastMessage(o.getString("last_message"));
            setLastMessageCreatedAt(o.getString("last_message_created_at"));
            setRead(o.getBoolean("is_read"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MessageList(Parcel src) {
        super(src);
        setLastMessage(src.readString());
        setLastMessageCreatedAt(src.readString());
        isRead = src.readByte() !=0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeString(getLastMessage());
        dest.writeString(getLastMessageCreatedAt());
        dest.writeByte((byte) (isRead() ? 1 : 0));
    }

    public static final Parcelable.Creator<MessageList> CREATOR = new Parcelable.Creator<MessageList>() {
        @Override
        public MessageList createFromParcel(Parcel source) {
            return new MessageList(source);
        }

        @Override
        public MessageList[] newArray(int size) {
            return new MessageList[size];
        }
    };

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageCreatedAt() {
        return lastMessageCreatedAt;
    }

    public void setLastMessageCreatedAt(String lastMessageCreatedAt) {
        this.lastMessageCreatedAt = lastMessageCreatedAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
