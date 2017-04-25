package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 4. 25..
 */

public class Purchase implements Parcelable {
    public static final String TAG = Purchase.class.getSimpleName();

    int id;
    Post post;
    User seller;
    User buyer;
    ColorSize colorSize;
    int amount;
    int price;
    int payment;
    int zipcode;
    String address1;
    String address2;
    String phone;
    String comment;
    int deliveryCode;
    String deliveryNumber;
    String createdAt;

    public static final Creator<Purchase> CREATOR = new Creator<Purchase>() {
        @Override
        public Purchase createFromParcel(Parcel source) {
            return new Purchase(source);
        }

        @Override
        public Purchase[] newArray(int size) {
            return new Purchase[size];
        }
    };

    public Purchase(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setPost(new Post(o.getJSONObject("post")));
            setSeller(new User(o.getJSONObject("seller")));
            setBuyer(new User(o.getJSONObject("buyer")));
            setColorSize(new ColorSize(o.getJSONObject("color_size")));
            setPrice(o.getInt("price"));
            setPayment(o.getInt("payment"));
            setAmount(o.getInt("amount"));
            setZipcode(o.getInt("zipcode"));
            setAddress1(o.getString("address1"));
            setAddress2(o.getString("address2"));
            setPhone(o.getString("phone"));
            setComment(o.getString("comment"));
            setDeliveryCode(o.getInt("delivery_code"));
            setDeliveryNumber(o.getString("delivery_number"));
            setCreatedAt(o.getString("created_at"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Purchase(Parcel src) {
        setId(src.readInt());
        this.post = src.readParcelable(Post.class.getClassLoader());
        this.seller = src.readParcelable(User.class.getClassLoader());
        this.buyer = src.readParcelable(User.class.getClassLoader());
        this.colorSize = src.readParcelable(ColorSize.class.getClassLoader());
        setPrice(src.readInt());
        setPayment(src.readInt());
        setAmount(src.readInt());
        setZipcode(src.readInt());
        setAddress1(src.readString());
        setAddress2(src.readString());
        setPhone(src.readString());
        setComment(src.readString());
        setDeliveryCode(src.readInt());
        setDeliveryNumber(src.readString());
        setCreatedAt(src.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeParcelable(getPost(), 0);
        dest.writeParcelable(getSeller(), 0);
        dest.writeParcelable(getBuyer(), 0);
        dest.writeParcelable(getColorSize(), 0);
        dest.writeInt(getPrice());
        dest.writeInt(getPayment());
        dest.writeInt(getAmount());
        dest.writeInt(getZipcode());
        dest.writeString(getAddress1());
        dest.writeString(getAddress2());
        dest.writeString(getPhone());
        dest.writeString(getComment());
        dest.writeInt(getDeliveryCode());
        dest.writeString(getDeliveryNumber());
        dest.writeString(getCreatedAt());
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

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(int deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
