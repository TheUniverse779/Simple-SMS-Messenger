package com.messenger.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.simplemobiletools.smsmessenger.R;


public class OverLay extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        overLay = this;
        setFullScreen();

        findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private static OverLay overLay;


    private void setFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void startScreen(Context context) {
        try {
            Intent intent = new Intent(context, OverLay.class);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }


    public static void finishActivity() {
        try {
            if (overLay != null && !overLay.isFinishing()){
                overLay.finish();
            }
        } catch (Exception e) {

        }
    }



}
