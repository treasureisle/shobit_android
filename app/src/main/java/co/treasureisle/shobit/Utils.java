package co.treasureisle.shobit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.Weeks;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.treasureisle.shobit.Constant.PrefTag;

/**
 * Created by pgseong on 2017. 1. 25..
 */

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PrefTag.NAME, Context.MODE_PRIVATE);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLostConnectionToast(Context context) {
        Utils.showToast(context, context.getString(R.string.lost_connection));
    }

//    public static void sendGaScreenView(Activity activity) {
//        Tracker t = ((ShobitApplication) activity.getApplication()).getTracker();
//        t.setScreenName(activity.getClass().getSimpleName());
//        t.send(new HitBuilders.AppViewBuilder().build());
//    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String getStrDateFromNow(Context context, DateTime now, String mysqlDate) {
        SimpleDateFormat dateFormat = Utils.getMysqlDateFormat();

        try {
            Date mMysqlDate = dateFormat.parse(mysqlDate);
            LocalDateTime before = new DateTime(mMysqlDate).toLocalDateTime();

            LocalDateTime today = now.toLocalDateTime();

            int mins = Minutes.minutesBetween(today, before).getMinutes() * -1;

            if (mins < 2) {
                return context.getString(R.string.just_now);
            } else {
                if (mins < 60) {
                    return mins + "m";
                } else {
                    int hours = Hours.hoursBetween(today, before).getHours() * -1;

                    if (hours < 24) {
                        return hours + "h";
                    } else {
                        LocalDate beforeDate = before.toLocalDate();
                        LocalDate todayDate = today.toLocalDate();

                        int days = Days.daysBetween(todayDate, beforeDate).getDays() * -1;

                        if (days < 7) {
                            return days + "d";
                        } else {
                            int weeks = Weeks.weeksBetween(todayDate, beforeDate).getWeeks() * -1;

                            return weeks + "w";
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SimpleDateFormat getMysqlDateFormat() {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    }

    public static boolean isNullString(String str) {
        return str == null || "null".equals(str);
    }

    public static int getDisplayWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDisplayHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("getPackageInfo exception");
        }
    }

    public static int getAppVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static String getAppVersion(Context context) {
        return getPackageInfo(context).versionName;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * 파라메터를 포함하여 method get 에 쓰일 url 을 만든다.
     *
     * @param url    url
     * @param params query parameters
     * @return 완성된 url
     */
    public static String makeGetUrl(String url, HashMap<String, String> params) {
        Uri.Builder builder = new Uri.Builder().path(url);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            builder.appendQueryParameter(key, value);
        }

        return builder.build().toString();
    }

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }


    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    public static String numberFormat(int num){
        return NumberFormat.getIntegerInstance().format(num);
    }

}
