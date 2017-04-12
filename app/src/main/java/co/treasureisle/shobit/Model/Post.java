package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.treasureisle.shobit.Utils;


/**
 * Created by pgseong on 2017. 2. 13..
 */

public class Post implements Parcelable {

    private int id;
    private int postType;
    private User user;
    private String imgUrl1;
    private String imgUrl2;
    private String imgUrl3;
    private String imgUrl4;
    private String imgUrl5;
    private String title;
    private String brand;
    private String productName;
    private int originPrice;
    private int purchasePrice;
    private int fee;
    private String region;
    private String hashtag;
    private String text;
    private int replys;
    private int likes;
    private boolean isLiked;

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Post(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setPostType(o.getInt("post_type"));
            setUser(new User(o.getJSONObject("user")));
            setImgUrl1(o.getString("img_url1"));
            setImgUrl2(o.getString("img_url2"));
            setImgUrl3(o.getString("img_url3"));
            setImgUrl4(o.getString("img_url4"));
            setImgUrl5(o.getString("img_url5"));
            setTitle(o.getString("title"));
            setBrand(o.getString("brand"));
            setProductName(o.getString("product_name"));
            setOriginPrice(o.getInt("origin_price"));
            setPurchasePrice(o.getInt("purchase_price"));
            setFee(o.getInt("fee"));
            setRegion(o.getString("region"));
            setHashtag(o.getString("hashtag"));
            setText(o.getString("text"));
            setReplys(o.getInt("replies"));
            setLikes(o.getInt("likes"));
            setLiked(o.getBoolean("is_liked"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Post(Parcel src) {
        setId(src.readInt());
        setPostType(src.readInt());
        this.user = src.readParcelable(User.class.getClassLoader());
        setImgUrl1(src.readString());
        setImgUrl2(src.readString());
        setImgUrl3(src.readString());
        setImgUrl4(src.readString());
        setImgUrl5(src.readString());
        setTitle(src.readString());
        setBrand(src.readString());
        setProductName(src.readString());
        setOriginPrice(src.readInt());
        setPurchasePrice(src.readInt());
        setFee(src.readInt());
        setRegion(src.readString());
        setHashtag(src.readString());
        setText(src.readString());
        setReplys(src.readInt());
        setLikes(src.readInt());
        isLiked = src.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getPostType());
        dest.writeParcelable(getUser(), 0);
        dest.writeString(getImgUrl1());
        dest.writeString(getImgUrl2());
        dest.writeString(getImgUrl3());
        dest.writeString(getImgUrl4());
        dest.writeString(getImgUrl5());
        dest.writeString(getTitle());
        dest.writeString(getBrand());
        dest.writeString(getProductName());
        dest.writeInt(getOriginPrice());
        dest.writeInt(getPurchasePrice());
        dest.writeInt(getFee());
        dest.writeString(getRegion());
        dest.writeString(getHashtag());
        dest.writeString(getText());
        dest.writeInt(getLikes());
        dest.writeInt(getReplys());
        dest.writeByte((byte) (isLiked() ? 1 : 0));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getImgUrl4() {
        return imgUrl4;
    }

    public void setImgUrl4(String imgUrl4) {
        this.imgUrl4 = imgUrl4;
    }

    public String getImgUrl5() {
        return imgUrl5;
    }

    public void setImgUrl5(String imgUrl5) {
        this.imgUrl5 = imgUrl5;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(int originPrice) {
        this.originPrice = originPrice;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReplys() {
        return replys;
    }

    public void setReplys(int replys) {
        this.replys = replys;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
