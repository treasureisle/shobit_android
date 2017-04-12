package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 3. 31..
 */

public class Basket implements Parcelable {
    private int id;
    private Post post;
    private User user;
    private ColorSize colorSize;
    private int amount;

    public static final Creator<Basket> CREATOR = new Creator<Basket>() {
        @Override
        public Basket createFromParcel(Parcel source) {
            return new Basket(source);
        }

        @Override
        public Basket[] newArray(int size) {
            return new Basket[size];
        }
    };

    public Basket(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setPost(new Post(o.getJSONObject("post")));
            setUser(new User(o.getJSONObject("user")));
            setColorSize(new ColorSize(o.getJSONObject("color_size")));
            setAmount(o.getInt("amount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Basket(Parcel src) {
        setId(src.readInt());
        this.post = src.readParcelable(Post.class.getClassLoader());
        this.user = src.readParcelable(User.class.getClassLoader());
        this.colorSize = src.readParcelable(ColorSize.class.getClassLoader());
        setAmount(src.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeParcelable(getPost(), 0);
        dest.writeParcelable(getUser(), 0);
        dest.writeParcelable(getColorSize(), 0);
        dest.writeInt(getAmount());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
