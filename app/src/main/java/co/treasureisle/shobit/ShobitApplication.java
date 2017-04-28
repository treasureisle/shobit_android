package co.treasureisle.shobit;

/**
 * Created by pgseong on 2017. 1. 25..
 */

import android.app.Application;
import android.content.SharedPreferences;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import net.danlew.android.joda.JodaTimeAndroid;

import co.treasureisle.shobit.Constant.PrefTag;

@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        formUri = BuildConfig.API_URL + "/android_crashes",
        httpMethod = HttpSender.Method.POST,
        reportType = HttpSender.Type.JSON
)

public class ShobitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

        ACRA.init(this);

        SharedPreferences gluviPrefs = Utils.getPrefs(this);
        String UUID = gluviPrefs.getString(PrefTag.USER_ID, "null");

        SharedPreferences pref = ACRA.getACRASharedPreferences();
        pref.edit().putString(ACRA.PREF_USER_EMAIL_ADDRESS, UUID).apply();
    }

//    public synchronized Tracker getTracker() {
//        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//
//        return analytics.newTracker(R.xml.global_tracker);
//    }
}
