package com.messenger.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.simplemobiletools.smsmessenger.R;

public class RateUtils {

    private static final String MARKET_DETAILS_ID = "market://details?id=";
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    public static void goToStore(Context context, String appId) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID + appId)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK + appId)));
        }
    }

    public static void ShareApp(Context context,String appid){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,PLAY_STORE_LINK+appid);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void Feedback(Context context,String mail){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        intent.setType("text/plain");
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "[Feedback] "+ R.string.app_name+"");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        Intent mailer = Intent.createChooser(intent, null);
        context.startActivity(mailer);

    }
}
