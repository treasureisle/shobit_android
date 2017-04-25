package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class Order implements Parcelable {
    Post post;
    ColorSize colorSize;
    int amount;

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public Order(Post post, ColorSize colorSize, int amount) {
        this.post = post;
        this.colorSize = colorSize;
        this.amount = amount;
    }

    public Order(JSONObject o) {
        try {
            setPost(new Post(o.getJSONObject("post")));
            setColorSize(new ColorSize(o.getJSONObject("color_size")));
            setAmount(o.getInt("amount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Order(Parcel src) {
        this.post = src.readParcelable(Post.class.getClassLoader());
        this.colorSize = src.readParcelable(ColorSize.class.getClassLoader());
        setAmount(src.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getPost(), 0);
        dest.writeParcelable(getColorSize(), 0);
        dest.writeInt(getAmount());
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public ColorSize getColorSize() {
        return colorSize;
    }

    public void setColorSize(ColorSize colorSize) {
        this.colorSize = colorSize;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
