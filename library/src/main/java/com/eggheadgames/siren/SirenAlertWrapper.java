package com.eggheadgames.siren;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class SirenAlertWrapper {

    private final WeakReference<Activity> mActivityRef;
    private final ISirenListener mSirenListener;
    private final SirenAlertType mSirenAlertType;
    private final String mMinAppVersion;
    private final SirenSupportedLocales mLocale;
    private final SirenHelper mSirenHelper;
    private final String mDesc;
    private final Bitmap mImagen;
    private final Context ctx;

    public SirenAlertWrapper(Activity activity, ISirenListener sirenListener, SirenAlertType sirenAlertType,
                             String minAppVersion, SirenSupportedLocales locale, SirenHelper sirenHelper, String mDesc, Bitmap mImagen) {
        this.mSirenListener = sirenListener;
        this.mSirenAlertType = sirenAlertType;
        this.mMinAppVersion = minAppVersion;
        this.mLocale = locale;
        this.mSirenHelper = sirenHelper;
        this.mActivityRef = new WeakReference<>(activity);
        this.mDesc = mDesc;
        this.mImagen = mImagen;
        this.ctx = activity;
    }


    public void show() {
        Activity activity = mActivityRef.get();
        if (activity == null) {
            if (mSirenListener != null) {
                mSirenListener.onError(new NullPointerException("activity reference is null"));
            }
        } else if (Build.VERSION.SDK_INT >= 17 && !activity.isDestroyed() || Build.VERSION.SDK_INT < 17 && !activity.isFinishing()) {

            AlertDialog alertDialog = initDialog(activity);
            setupDialog(alertDialog);

            if (mSirenListener != null) {
                mSirenListener.onShowUpdateDialog();
            }
        }
    }

    @SuppressLint("InflateParams")
    private AlertDialog initDialog(Activity activity) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        //alertBuilder.setTitle(mSirenHelper.getLocalizedString(mActivityRef.get(), R.string.update_available, mLocale));
        alertBuilder.setCancelable(false);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.siren_dialog, null);
        alertBuilder.setView(dialogView);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        return alertDialog;
    }

    private void setupDialog(final AlertDialog dialog) {
        TextView message = (TextView) dialog.findViewById(R.id.tvSirenAlertMessage);
        TextView update = (TextView) dialog.findViewById(R.id.btnSirenUpdate);
        TextView nextTime = (TextView) dialog.findViewById(R.id.btnSirenNextTime);
        final TextView skip = (TextView) dialog.findViewById(R.id.btnSirenSkip);
        TextView desc = (TextView) dialog.findViewById(R.id.fingerprint_description);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.fingerprint_icon);

        //update.setText(mSirenHelper.getLocalizedString(mActivityRef.get(), R.string.update, mLocale));
        //nextTime.setText(mSirenHelper.getLocalizedString(mActivityRef.get(), R.string.next_time, mLocale));
        //skip.setText(mSirenHelper.getLocalizedString(mActivityRef.get(), R.string.skip_this_version, mLocale));

        if (desc != null && !desc.equals("")){
            desc.setText(ctx.getResources().getString(R.string.desc_version_app, mDesc));
        }

        if (mImagen != null ){
            imageView.setImageBitmap(mImagen);
        }

        message.setText(mSirenHelper.getAlertMessage(mActivityRef.get(), mMinAppVersion, mLocale));

        if (mSirenAlertType == SirenAlertType.FORCE
                || mSirenAlertType == SirenAlertType.OPTION
                || mSirenAlertType == SirenAlertType.SKIP) {
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSirenListener != null) {
                        mSirenListener.onLaunchGooglePlay();
                    }
                    dialog.dismiss();
                    mSirenHelper.openGooglePlay(mActivityRef.get());
                }
            });
        }

        if (mSirenAlertType == SirenAlertType.OPTION
                || mSirenAlertType == SirenAlertType.SKIP) {
            nextTime.setVisibility(View.VISIBLE);
            nextTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSirenListener != null) {
                        mSirenListener.onCancel();
                    }
                    dialog.dismiss();
                }
            });
        }
        if (mSirenAlertType == SirenAlertType.SKIP) {
            skip.setVisibility(View.VISIBLE);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSirenListener != null) {
                        mSirenListener.onSkipVersion();
                    }

                    mSirenHelper.setVersionSkippedByUser(mActivityRef.get(), mMinAppVersion);
                    dialog.dismiss();
                }
            });
        }
    }
}
