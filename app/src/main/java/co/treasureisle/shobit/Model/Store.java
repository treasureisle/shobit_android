package co.treasureisle.shobit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pgseong on 2017. 3. 30..
 */

public class Store implements Parcelable {
    private int id;
    private int numEvents;
    private int event1HashtagId;
    private int event2HashtagId;
    private int event3HashtagId;
    private int event4HashtagId;
    private int event5HashtagId;
    private String event1ImgUrl;
    private String event2ImgUrl;
    private String event3ImgUrl;
    private String event4ImgUrl;
    private String event5ImgUrl;
    private String event1Title;
    private String event2Title;
    private String event3Title;
    private String event4Title;
    private String event5Title;
    private int sellerId;
    private int editorsPickHashtagId;
    private String editorsPickTitle;
    private String todaySellerTitle;

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public Store(JSONObject o) {
        try {
            setId(o.getInt("id"));
            setNumEvents(o.getInt("num_events"));
            setEvent1HashtagId(o.getInt("event1_hashtag_id"));
            setEvent2HashtagId(o.getInt("event2_hashtag_id"));
            setEvent3HashtagId(o.getInt("event3_hashtag_id"));
            setEvent4HashtagId(o.getInt("event4_hashtag_id"));
            setEvent5HashtagId(o.getInt("event5_hashtag_id"));
            setEvent1ImgUrl(o.getString("event1_img_url"));
            setEvent2ImgUrl(o.getString("event2_img_url"));
            setEvent3ImgUrl(o.getString("event3_img_url"));
            setEvent4ImgUrl(o.getString("event4_img_url"));
            setEvent5ImgUrl(o.getString("event5_img_url"));
            setEvent1Title(o.getString("event1_title"));
            setEvent2Title(o.getString("event2_title"));
            setEvent3Title(o.getString("event3_title"));
            setEvent4Title(o.getString("event4_title"));
            setEvent5Title(o.getString("event5_title"));
            setSellerId(o.getInt("seller_id"));
            setTodaySellerTitle(o.getString("today_seller_title"));
            setEditorsPickHashtagId(o.getInt("editors_pick_hashtag_id"));
            setEditorsPickTitle(o.getString("editors_pick_title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Store(Parcel src) {
        setId(src.readInt());
        setNumEvents(src.readInt());
        setEvent1HashtagId(src.readInt());
        setEvent2HashtagId(src.readInt());
        setEvent3HashtagId(src.readInt());
        setEvent4HashtagId(src.readInt());
        setEvent5HashtagId(src.readInt());
        setEvent1ImgUrl(src.readString());
        setEvent2ImgUrl(src.readString());
        setEvent3ImgUrl(src.readString());
        setEvent4ImgUrl(src.readString());
        setEvent5ImgUrl(src.readString());
        setEvent1Title(src.readString());
        setEvent2Title(src.readString());
        setEvent3Title(src.readString());
        setEvent4Title(src.readString());
        setEvent5Title(src.readString());
        setSellerId(src.readInt());
        setTodaySellerTitle(src.readString());
        setEditorsPickHashtagId(src.readInt());
        setEditorsPickTitle(src.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getNumEvents());
        dest.writeInt(getEvent1HashtagId());
        dest.writeInt(getEvent2HashtagId());
        dest.writeInt(getEvent3HashtagId());
        dest.writeInt(getEvent4HashtagId());
        dest.writeInt(getEvent5HashtagId());
        dest.writeString(getEvent1ImgUrl());
        dest.writeString(getEvent2ImgUrl());
        dest.writeString(getEvent3ImgUrl());
        dest.writeString(getEvent4ImgUrl());
        dest.writeString(getEvent5ImgUrl());
        dest.writeString(getEvent1Title());
        dest.writeString(getEvent2Title());
        dest.writeString(getEvent3Title());
        dest.writeString(getEvent4Title());
        dest.writeString(getEvent5Title());
        dest.writeInt(getSellerId());
        dest.writeString(getEditorsPickTitle());
        dest.writeInt(getEditorsPickHashtagId());
        dest.writeString(getTodaySellerTitle());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumEvents() {
        return numEvents;
    }

    public void setNumEvents(int numEvents) {
        this.numEvents = numEvents;
    }

    public int getEvent1HashtagId() {
        return event1HashtagId;
    }

    public void setEvent1HashtagId(int event1HashtagId) {
        this.event1HashtagId = event1HashtagId;
    }

    public int getEvent2HashtagId() {
        return event2HashtagId;
    }

    public void setEvent2HashtagId(int event2HashtagId) {
        this.event2HashtagId = event2HashtagId;
    }

    public int getEvent3HashtagId() {
        return event3HashtagId;
    }

    public void setEvent3HashtagId(int event3HashtagId) {
        this.event3HashtagId = event3HashtagId;
    }

    public int getEvent4HashtagId() {
        return event4HashtagId;
    }

    public void setEvent4HashtagId(int event4HashtagId) {
        this.event4HashtagId = event4HashtagId;
    }

    public int getEvent5HashtagId() {
        return event5HashtagId;
    }

    public void setEvent5HashtagId(int event5HashtagId) {
        this.event5HashtagId = event5HashtagId;
    }

    public String getEvent1ImgUrl() {
        return event1ImgUrl;
    }

    public void setEvent1ImgUrl(String event1ImgUrl) {
        this.event1ImgUrl = event1ImgUrl;
    }

    public String getEvent2ImgUrl() {
        return event2ImgUrl;
    }

    public void setEvent2ImgUrl(String event2ImgUrl) {
        this.event2ImgUrl = event2ImgUrl;
    }

    public String getEvent3ImgUrl() {
        return event3ImgUrl;
    }

    public void setEvent3ImgUrl(String event3ImgUrl) {
        this.event3ImgUrl = event3ImgUrl;
    }

    public String getEvent4ImgUrl() {
        return event4ImgUrl;
    }

    public void setEvent4ImgUrl(String event4ImgUrl) {
        this.event4ImgUrl = event4ImgUrl;
    }

    public String getEvent5ImgUrl() {
        return event5ImgUrl;
    }

    public void setEvent5ImgUrl(String event5ImgUrl) {
        this.event5ImgUrl = event5ImgUrl;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getEditorsPickHashtagId() {
        return editorsPickHashtagId;
    }

    public void setEditorsPickHashtagId(int editorsPickHashtagId) {
        this.editorsPickHashtagId = editorsPickHashtagId;
    }

    public String getEditorsPickTitle() {
        return editorsPickTitle;
    }

    public void setEditorsPickTitle(String editorsPickTitle) {
        this.editorsPickTitle = editorsPickTitle;
    }

    public String getTodaySellerTitle() {
        return todaySellerTitle;
    }

    public void setTodaySellerTitle(String todaySellerTitle) {
        this.todaySellerTitle = todaySellerTitle;
    }

    public String getEvent1Title() {
        return event1Title;
    }

    public void setEvent1Title(String event1Title) {
        this.event1Title = event1Title;
    }

    public String getEvent2Title() {
        return event2Title;
    }

    public void setEvent2Title(String event2Title) {
        this.event2Title = event2Title;
    }

    public String getEvent3Title() {
        return event3Title;
    }

    public void setEvent3Title(String event3Title) {
        this.event3Title = event3Title;
    }

    public String getEvent4Title() {
        return event4Title;
    }

    public void setEvent4Title(String event4Title) {
        this.event4Title = event4Title;
    }

    public String getEvent5Title() {
        return event5Title;
    }

    public void setEvent5Title(String event5Title) {
        this.event5Title = event5Title;
    }
}
