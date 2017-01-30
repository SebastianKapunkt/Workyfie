package workyfie.github.de.workyfie.presentation.common;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class FragmentUtils {
    public static final String TAG = FragmentUtils.class.getSimpleName();

    public static FragmentTransaction defaultAnimation(FragmentTransaction transaction) {
        return transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
    }
}
