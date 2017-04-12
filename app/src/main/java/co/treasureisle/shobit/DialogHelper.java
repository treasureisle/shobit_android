package co.treasureisle.shobit;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by pgseong on 2017. 3. 27..
 */

public class DialogHelper {
    private Context context;

    public DialogHelper(Context context) {
        this.context = context;
    }

    public void showSimpleDialog(String title, String message) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(context.getString(R.string.ok))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showMessageDialog(String message) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(context.getString(R.string.ok))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showSomethingWentWrongDialog() {
        showSimpleDialog(context.getString(R.string.sorry_something_went_wrong), context.getString(R.string.we_are_working));
    }
}
