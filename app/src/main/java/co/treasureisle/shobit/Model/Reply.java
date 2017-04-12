package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 3. 27..
 */

public class Reply implements Parcelable {
    private int id;
    private User user;
    private Post post;
    private int parentId;
    private int depth;
    private String text;
    private int likes;
    private int replies;
    private String createdAt;
    private boolean isLiked;

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel source) {
            return new Reply(source);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

    public Reply(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setUser(new User(o.getJSONObject("user")));
            setPost(new Post(o.getJSONObject("post")));
            setParentId(o.getInt("parent_id"));
            setDepth(o.getInt("depth"));
            setText(o.getString("text"));
            setLikes(o.getInt("likes"));
            setReplies(o.getInt("replies"));
            setCreatedAt(o.getString("created_at"));
            setLiked(o.getBoolean("is_liked"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Reply(Parcel src) {
        setId(src.readInt());
        this.user = src.readParcelable(User.class.getClassLoader());
        this.post = src.readParcelable(Post.class.getClassLoader());
        setParentId(src.readInt());
        setDepth(src.readInt());
        setText(src.readString());
        setLikes(src.readInt());
        setReplies(src.readInt());
        setCreatedAt(src.readString());
        isLiked = src.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeParcelable(getUser(), 0);
        dest.writeParcelable(getPost(), 0);
        dest.writeInt(getParentId());
        dest.writeInt(getDepth());
        dest.writeString(getText());
        dest.writeInt(getLikes());
        dest.writeInt(getReplies());
        dest.writeString(getCreatedAt());
        dest.writeByte((byte) (isLiked() ? 1 : 0));
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
