package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 5. 2..
 */

public class Address implements Parcelable {
    private String roadAddr;
    private String roadAddrPart1;
    private String roadAddrPart2;
    private String jibunAddr;
    private String engAddr;
    private int zipNo;

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address(JSONObject o) {
        try {
            setRoadAddr(o.getString("roadAddr"));
            setRoadAddrPart1(o.getString("roadAddrPart1"));
            setRoadAddrPart2(o.getString("roadAddrPart2"));
            setJibunAddr(o.getString("jibunAddr"));
            setEngAddr(o.getString("engAddr"));
            setZipNo(o.getInt("zipNo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Address(Parcel src) {
        setRoadAddr(src.readString());
        setJibunAddr(src.readString());
        setRoadAddrPart1(src.readString());
        setRoadAddrPart2(src.readString());
        setEngAddr(src.readString());
        setZipNo(src.readInt());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getRoadAddr());
        dest.writeString(getRoadAddrPart1());
        dest.writeString(getRoadAddrPart2());
        dest.writeString(getJibunAddr());
        dest.writeString(getEngAddr());
        dest.writeInt(getZipNo());
    }

    public String getRoadAddr() {
        return roadAddr;
    }

    public void setRoadAddr(String roadAddr) {
        this.roadAddr = roadAddr;
    }

    public String getRoadAddrPart1() {
        return roadAddrPart1;
    }

    public void setRoadAddrPart1(String roadAddrPart1) {
        this.roadAddrPart1 = roadAddrPart1;
    }

    public String getRoadAddrPart2() {
        return roadAddrPart2;
    }

    public void setRoadAddrPart2(String roadAddrPart2) {
        this.roadAddrPart2 = roadAddrPart2;
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public String getEngAddr() {
        return engAddr;
    }

    public void setEngAddr(String engAddr) {
        this.engAddr = engAddr;
    }

    public int getZipNo() {
        return zipNo;
    }

    public void setZipNo(int zipNo) {
        this.zipNo = zipNo;
    }

    @Override
    public String toString(){
        String string = "";
        string = string + getZipNo() + "\n";
        string = string + getRoadAddr() + "\n";
        return string;
    }
}
