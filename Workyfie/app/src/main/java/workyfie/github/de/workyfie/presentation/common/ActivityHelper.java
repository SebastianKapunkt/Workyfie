package workyfie.github.de.workyfie.presentation.common;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class ActivityHelper {
    public static final String TAG = ActivityHelper.class.getSimpleName();

    public static FragmentTransaction defaultAnimation(FragmentTransaction transaction) {
        return transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
    }

    public static AlertDialog showError(String TAG, Throwable e, Context context, String msg) {
        Log.e(TAG, "source msg: " + msg + " Error: " + e.getMessage(), e);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle("Error: ")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> dialog.cancel())
                .setIcon(android.R.drawable.ic_dialog_alert);
        return alertDialog.show();
    }
}
