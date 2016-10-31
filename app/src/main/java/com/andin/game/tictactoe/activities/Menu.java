package com.andin.game.tictactoe.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.andin.game.tictactoe.R;
import com.andin.game.tictactoe.activities.Choosing;
import com.andin.game.tictactoe.data.Constants;
import com.andin.game.tictactoe.services.BackgroundSoundService;

public class Menu extends AppCompatActivity {

    private ImageButton sax;
    private Intent musicService;
    private boolean music, screenChanged;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sax = (ImageButton) findViewById(R.id.saxButton);

        prefs = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        music = prefs.getBoolean(Constants.MUSIC,true);

        musicService = new Intent(this, BackgroundSoundService.class);

        if (music){
            sax.setBackgroundResource(R.mipmap.sax);
            startService(musicService);
        } else {
            sax.setBackgroundResource(R.mipmap.nosax);
        }

        screenChanged = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(musicService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!screenChanged) {
            stopService(musicService);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (music) {
            startService(musicService);
        }
    }

    public void playClicked(View view){
        Intent i = new Intent(this,Choosing.class);
        screenChanged = true;
        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);

    }

    public void pvpClicked(View v){
        Intent easyIntent = new Intent(this, Game.class);
        screenChanged = true;
        easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        easyIntent.putExtra(Constants.DIFFICULTY, Constants.PVP);
        startActivity(easyIntent);
    }

    public void musicClicked(View v){
        if (music){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.MUSIC,false);
            editor.commit();
            sax.setBackgroundResource(R.mipmap.nosax);
            stopService(musicService);
            music = false;
        } else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(Constants.MUSIC,true);
            editor.commit();
            sax.setBackgroundResource(R.mipmap.sax);
            startService(musicService);
            music = true;
        }
    }
}
