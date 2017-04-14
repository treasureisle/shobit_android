package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 3. 31..
 */

public class ColorSize implements Parcelable {
    private int id;
    private int postId;
    private String name;
    private int available;

    public static final Creator<ColorSize> CREATOR = new Creator<ColorSize>() {
        @Override
        public ColorSize createFromParcel(Parcel source) {
            return new ColorSize(source);
        }

        @Override
        public ColorSize[] newArray(int size) {
            return new ColorSize[size];
        }
    };

    public ColorSize(String name, int available) {
        this.name = name;
        this.available = available;
    }

    public ColorSize(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setPostId(o.getInt("post_id"));
            setName(o.getString("name"));
            setAvailable(o.getInt("available"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ColorSize(Parcel src) {
        setId(src.readInt());
        setPostId(src.readInt());
        setName(src.readString());
        setAvailable(src.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getPostId());
        dest.writeString(getName());
        dest.writeInt(getAvailable());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}