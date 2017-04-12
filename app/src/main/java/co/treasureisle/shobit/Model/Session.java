package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 2. 13..
 */

public class Session implements Parcelable {
    private String uuid;
    private String accessToken;

    public Session(JSONObject o) {
        try {
            setUuid(o.getString("id"));
            setAccessToken(o.getString("access_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Session(Parcel src) {
        setUuid(src.readString());
        setAccessToken(src.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getUuid());
        dest.writeString(getAccessToken());
    }

    public String getUuid() {
        return uuid;
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel source) {
            return new Session(source);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
