package com.learningmachine.android.app.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.learningmachine.android.app.R;
import com.learningmachine.android.app.dialog.AlertDialogFragment;
import com.learningmachine.android.app.dialog.ProgressDialogFragment;

import java.net.UnknownHostException;

import retrofit2.HttpException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class DialogUtils {

    public static final String TAG_DIALOG_PROGRESS = "DialogUtils.Dialog.Progress";
    private static final String TAG_DIALOG_ALERT = "DialogUtils.Dialog.Alert";

    public static void showProgressDialog(FragmentManager fragmentManager, String message) {
        ProgressDialogFragment dialog = ProgressDialogFragment.newInstance(message);
        dialog.setCancelable(false);
        dialog.show(fragmentManager, TAG_DIALOG_PROGRESS);

    }

    public static void hideProgressDialog(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_DIALOG_PROGRESS);
        if (fragment instanceof ProgressDialogFragment) {
            ((ProgressDialogFragment) fragment).dismissAllowingStateLoss();
        }
    }

    public static void showAlertDialog(Context context, FragmentManager fragmentManager, @StringRes int messageResId) {
        AlertDialogFragment dialog = AlertDialogFragment.newInstance(context, messageResId);
        dialog.show(fragmentManager, TAG_DIALOG_ALERT);
    }

    public static void showErrorAlertDialog(Context context, FragmentManager fragmentManager, @StringRes int titleResId, Throwable throwable) {
        String titleString = context.getString(titleResId);
        String errorString = context.getString(getErrorMessageResourceId(throwable));
        showErrorAlertDialog(context, fragmentManager, titleString, errorString, throwable);
    }

    private static void showErrorAlertDialog(Context context, FragmentManager fragmentManager, String title, String errorMessage, Throwable throwable) {
        AlertDialogFragment dialog = AlertDialogFragment.newInstance(title, errorMessage);
        fragmentManager.beginTransaction()
                .add(dialog, TAG_DIALOG_ALERT)
                .commitAllowingStateLoss();
    }

    private static int getErrorMessageResourceId(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            return R.string.connection_error_message;
        } else if (throwable instanceof HttpException) {
            switch (((HttpException) throwable).code()) {
                case HTTP_NOT_FOUND:
                    return R.string.fragment_add_issuer_bad_url_error;
                default:
                case HTTP_BAD_REQUEST:
                    return R.string.fragment_add_issuer_invalid_issuer_error;
            }
        } else {
            return R.string.unknown_error_message;
        }
    }

}