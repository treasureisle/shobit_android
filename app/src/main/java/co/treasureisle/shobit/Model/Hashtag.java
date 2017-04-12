package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 3. 30..
 */

public class Hashtag implements Parcelable{
    private int id;
    private String name;
    private int number;
    private int hidden;

    public static final Creator<Hashtag> CREATOR = new Creator<Hashtag>() {
        @Override
        public Hashtag createFromParcel(Parcel source) {
            return new Hashtag(source);
        }

        @Override
        public Hashtag[] newArray(int size) {
            return new Hashtag[size];
        }
    };

    public Hashtag(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setName(o.getString("name"));
            setNumber(o.getInt("number"));
            setHidden(o.getInt("hidden"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Hashtag(Parcel src) {
        setId(src.readInt());
        setName(src.readString());
        setNumber(src.readInt());
        setHidden(src.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeInt(getNumber());
        dest.writeInt(getHidden());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }
}
