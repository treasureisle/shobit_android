package co.treasureisle.shobit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import co.treasureisle.shobit.Activity.HomeActivity;
import co.treasureisle.shobit.Constant.PrefTag;
import co.treasureisle.shobit.Model.Session;

/**
 * Created by pgseong on 2017. 3. 17..
 */

public class SessionHelper {
    public static final String TAG = SessionHelper.class.getSimpleName();

    private SharedPreferences pref;

    public SessionHelper(Context context) {
        this.pref = Utils.getPrefs(context);
    }

    public void saveToken(Session session) {
        SharedPreferences.Editor editor = pref.edit();

        if (session == null) {
            throw new RuntimeException("sessionUserCard is required.");
        }

        if (session.getUuid() == null) {
            throw new RuntimeException("session uuid is required.");
        }

        if (session.getAccessToken() == null) {
            throw new RuntimeException("session accesstoken is required");
        }

        editor.putString(PrefTag.USER_ID, session.getUuid());
        editor.putString(PrefTag.ACCESS_TOKEN, session.getAccessToken());
        editor.apply();

        Log.d(TAG, "token saved");
    }

    public static Session getSession(Context context) {
        SharedPreferences pref = Utils.getPrefs(context);
        String userId = pref.getString(PrefTag.USER_ID, null);
        String accessToken = pref.getString(PrefTag.ACCESS_TOKEN, null);
        Session me = new Session(userId, accessToken);
        return me;
    }

    public void clearToken() {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(PrefTag.USER_ID);
        editor.remove(PrefTag.ACCESS_TOKEN);
        editor.apply();

        Log.d(TAG, "token cleared");
    }
}