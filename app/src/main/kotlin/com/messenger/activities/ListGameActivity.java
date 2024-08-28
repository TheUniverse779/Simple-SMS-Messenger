package com.messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.simplemobiletools.smsmessenger.R;

public class ListGameActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_game);
    }

    public void click(View view) {
        HTML5Activity.linkGame = "https://chaping.github.io/game/flappy-bird/";
        startActivity(new Intent(this, HTML5Activity.class));
    }
}
