package co.treasureisle.shobit.Model;

/**
 * Created by pgseong on 2017. 3. 30..
 */

public class Event {
    private int hashtagId;
    private String imgUrl;
    private String title;

    public Event(int hashtagId, String imgUrl, String title){
        this.hashtagId = hashtagId;
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public int getHashtagId() {
        return hashtagId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() { return title; }
}
