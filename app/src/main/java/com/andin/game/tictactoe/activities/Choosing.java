package com.andin.game.tictactoe.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.DisplayContext;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andin.game.tictactoe.R;
import com.andin.game.tictactoe.data.Constants;
import com.andin.game.tictactoe.services.BackgroundSoundService;

public class Choosing extends AppCompatActivity {

    private ImageButton easy, medium, impossible;
    private boolean screenChanged;
    private TextView coins;
    private int currentCoins, gameStatus;
    private SharedPreferences progress;
    private int MEDIUM_REQUIRED = 15;
    private Typeface typeface;
    private int IMPOSSIBLE_REQUIRED = 50;
    private int chatNr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);


        typeface = Typeface.createFromAsset(getAssets(), "Harrington.ttf");
        progress = this.getSharedPreferences(Constants.PROGRESS, Context.MODE_PRIVATE);

        easy = (ImageButton) findViewById(R.id.easy);
        medium = (ImageButton) findViewById(R.id.medium);
        impossible = (ImageButton) findViewById(R.id.impossible);

        if (progress.getBoolean(Constants.MEDIUM, false)) {
            medium.setBackgroundResource(R.mipmap.medium);
        } else {
            medium.setBackgroundResource(R.mipmap.mediumlocked);
        }

        if (progress.getBoolean(Constants.IMPOSSIBLE, false)) {
            impossible.setBackgroundResource(R.mipmap.impossible);
        } else {
            impossible.setBackgroundResource(R.mipmap.impossiblelocked);
        }

        coins = (TextView) findViewById(R.id.coinNr);

        Typeface coinsTypeface = Typeface.createFromAsset(getAssets(), "Harrington.ttf");

        currentCoins = progress.getInt(Constants.COINS, 0);
        coins.setText(String.valueOf(currentCoins));
        coins.setTypeface(coinsTypeface);

        screenChanged = false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        screenChanged = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!screenChanged)
            stopService(new Intent(this, BackgroundSoundService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        boolean music = prefs.getBoolean(Constants.MUSIC, true);
        if (music) {
            startService(new Intent(this, BackgroundSoundService.class));
        }
    }

    public void cheatClicked(View v){
        chatNr++;
        if (chatNr == 5){
            SharedPreferences.Editor editor = progress.edit();
            currentCoins = currentCoins + 10;
            editor.putInt(Constants.COINS, currentCoins);
            editor.commit();
            coins.setText(Integer.toString(currentCoins));
        }

    }

    public void easyClicked(View v) {
        Intent easyIntent = new Intent(this, Game.class);
        screenChanged = true;
        easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        easyIntent.putExtra(Constants.DIFFICULTY, Constants.EASY);
        startActivity(easyIntent);
    }

    public void mediumClicked(View v) {
        boolean unlocked = progress.getBoolean(Constants.MEDIUM, false);
        gameStatus = Constants.NORMAL;
        if (unlocked) {
            Intent easyIntent = new Intent(this, Game.class);
            screenChanged = true;
            easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            easyIntent.putExtra(Constants.DIFFICULTY, Constants.NORMAL);
            startActivity(easyIntent);
        } else {
            if (currentCoins < MEDIUM_REQUIRED) {
                Toast.makeText(this, "You need " + MEDIUM_REQUIRED + " coins to unlock Medium level.", Toast.LENGTH_LONG).show();
            } else {
                dialogMessage();
            }
        }
    }

    public void impossibleClicked(View v) {
        boolean unlocked = progress.getBoolean(Constants.IMPOSSIBLE, false);
        gameStatus = Constants.HARD;
        if (unlocked) {
            Intent easyIntent = new Intent(this, Game.class);
            screenChanged = true;
            easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            easyIntent.putExtra(Constants.DIFFICULTY, Constants.HARD);
            startActivity(easyIntent);
        } else {
            if (currentCoins < IMPOSSIBLE_REQUIRED) {
                Toast.makeText(this, "You need " + IMPOSSIBLE_REQUIRED + " coins to unlock Impossible level.", Toast.LENGTH_LONG).show();
            } else {
                dialogMessage();
            }
        }
    }

    public void dialogMessage() {
        // custom dialog
        final Dialog dialog = new Dialog(Choosing.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);

        // set the custom dialog components - text, image and button
        final TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setGravity(Gravity.CENTER);
        message.setTypeface(typeface);
        if (gameStatus == Constants.NORMAL) {
            message.setText("Unlock Medium level?");
        } else if (gameStatus == Constants.HARD) {
            message.setText("Unlock Impossible level?");
        }

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setText("Yes");
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameStatus == Constants.NORMAL) {
                    SharedPreferences.Editor editor = progress.edit();
                    editor.putBoolean(Constants.MEDIUM, true);
                    currentCoins = currentCoins - MEDIUM_REQUIRED;
                    editor.putInt(Constants.COINS, currentCoins);
                    editor.commit();
                    coins.setText(Integer.toString(currentCoins));
                    medium.setBackgroundResource(R.mipmap.medium);
                } else if (gameStatus == Constants.HARD) {
                    SharedPreferences.Editor editor = progress.edit();
                    editor.putBoolean(Constants.IMPOSSIBLE, true);
                    currentCoins = currentCoins - IMPOSSIBLE_REQUIRED;
                    editor.putInt(Constants.COINS, currentCoins);
                    editor.commit();
                    coins.setText(Integer.toString(currentCoins));
                    impossible.setBackgroundResource(R.mipmap.impossible);
                }
                dialog.dismiss();
            }
        });

        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
