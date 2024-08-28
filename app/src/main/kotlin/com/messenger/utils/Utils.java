package com.messenger.utils;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.messenger.App;

public class Utils {
    public static int heightForFixedFacebookNavbar(Context context) {
        final int navbar = getNavigationBarHeight(context, context.getResources().getConfiguration().orientation);
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) ((context.getResources().getDisplayMetrics().heightPixels - navbar - 44) / density);
    }
    private static int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ?
                "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void vibrator(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(50); // for 500 ms
        }
    }

    public static Boolean checkOverlay(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return Settings.canDrawOverlays(context);
        }else {
            return true;
        }
    }
    public static void startOverLay(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Boolean checkUsagePermission(Context context){
        try {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if(appOps==null)return false;
            return appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS,
                    context.getPackageManager().getApplicationInfo(context.getPackageName(),0).uid,
                    context.getPackageName()) == MODE_ALLOWED;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void startUsage(Context context){
        context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    public static Drawable getIconFromPkn(String appId){
        try {
            return App.get().getPackageManager().getApplicationIcon(appId);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean openApp(Context context,String appId){
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appId);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
            return true;
        }else {
            Toast.makeText(context, "Current app not install!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void goHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
