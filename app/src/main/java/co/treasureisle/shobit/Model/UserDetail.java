package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 4. 12..
 */

public class UserDetail implements Parcelable {
    public static final String TAG = UserDetail.class.getSimpleName();

    private int id;
    private String username;
    private String email;
    private String name;
    private int zipcode;
    private String address1;
    private String address2;
    private String recentName;
    private int recentZipcode;
    private String recentAdd1;
    private String recentAdd2;
    private String recentPhone;
    private String phone;
    private int level;
    private int point;
    private String region;
    private int sellerLevel;
    private String bankAccount;
    private String bizNum;
    private String bizName;
    private int recommenderId;
    private String profileThumbUrl;
    private String introduce;
    private boolean isMe;

    public UserDetail(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setUsername(o.getString("username"));
            setEmail(o.getString("email"));
            setName(o.getString("name"));
            setZipcode(o.getInt("zipcode"));
            setAddress1(o.getString("address1"));
            setAddress2(o.getString("address2"));
            setRecentName(o.getString("recent_name"));
            setRecentZipcode(o.getInt("recent_zipcode"));
            setRecentAdd1(o.getString("recent_add1"));
            setRecentAdd2(o.getString("recent_add2"));
            setRecentPhone(o.getString("recent_phone"));
            setPhone(o.getString("phone"));
            setLevel(o.getInt("level"));
            setPoint(o.getInt("point"));
            setSellerLevel(o.getInt("seller_level"));
            setBankAccount(o.getString("bank_account"));
            setRegion(o.getString("region"));
            setBizNum(o.getString("biz_num"));
            setBizName(o.getString("biz_name"));
            setRecommenderId(o.getInt("recommender_id"));
            setProfileThumbUrl(o.getString("profile_thumb_url"));
            setIntroduce(o.getString("introduce"));
            setMe(o.getBoolean("is_me"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UserDetail(Parcel src) {
        setId(src.readInt());
        setUsername(src.readString());
        setName(src.readString());
        setZipcode(src.readInt());
        setAddress1(src.readString());
        setAddress2(src.readString());
        setPhone(src.readString());
        setRecentZipcode(src.readInt());
        setRecentAdd1(src.readString());
        setRecentAdd2(src.readString());
        setRecentPhone(src.readString());
        setLevel(src.readInt());
        setPoint(src.readInt());
        setSellerLevel(src.readInt());
        setBankAccount(src.readString());
        setRegion(src.readString());
        setBizNum(src.readString());
        setBizName(src.readString());
        setRecommenderId(src.readInt());
        setProfileThumbUrl(src.readString());
        setIntroduce(src.readString());
        isMe = src.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getUsername());
        dest.writeString(getName());
        dest.writeInt(getZipcode());
        dest.writeString(getAddress1());
        dest.writeString(getAddress2());
        dest.writeString(getRecentName());
        dest.writeString(getPhone());
        dest.writeInt(getRecentZipcode());
        dest.writeString(getRecentAdd1());
        dest.writeString(getRecentAdd2());
        dest.writeString(getRecentPhone());
        dest.writeInt(getLevel());
        dest.writeInt(getPoint());
        dest.writeInt(getSellerLevel());
        dest.writeString(getBankAccount());
        dest.writeString(getRegion());
        dest.writeString(getBizNum());
        dest.writeString(getBizName());
        dest.writeInt(getRecommenderId());
        dest.writeString(getProfileThumbUrl());
        dest.writeString(getIntroduce());
        dest.writeByte((byte) (isMe() ? 1 : 0));
    }

    public static final Parcelable.Creator<UserDetail> CREATOR = new Parcelable.Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel source) {
            return new UserDetail(source);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileThumbUrl() {
        return profileThumbUrl;
    }

    public void setProfileThumbUrl(String profileThumbUrl) {
        this.profileThumbUrl = profileThumbUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getRecentName() {
        return recentName;
    }

    public void setRecentName(String recentName) {
        this.recentName = recentName;
    }

    public int getRecentZipcode() {
        return recentZipcode;
    }

    public void setRecentZipcode(int recentZipcode) {
        this.recentZipcode = recentZipcode;
    }

    public String getRecentAdd1() {
        return recentAdd1;
    }

    public void setRecentAdd1(String recentAdd1) {
        this.recentAdd1 = recentAdd1;
    }

    public String getRecentAdd2() {
        return recentAdd2;
    }

    public void setRecentAdd2(String recentAdd2) {
        this.recentAdd2 = recentAdd2;
    }

    public String getRecentPhone() {
        return recentPhone;
    }

    public void setRecentPhone(String recentPhone) {
        this.recentPhone = recentPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getSellerLevel() {
        return sellerLevel;
    }

    public void setSellerLevel(int sellerLevel) {
        this.sellerLevel = sellerLevel;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBizNum() {
        return bizNum;
    }

    public void setBizNum(String bizNum) {
        this.bizNum = bizNum;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public int getRecommenderId() {
        return recommenderId;
    }

    public void setRecommenderId(int recommenderId) {
        this.recommenderId = recommenderId;
    }
}