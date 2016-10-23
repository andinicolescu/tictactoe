package com.andin.game.tictactoe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.andin.game.tictactoe.R;
import com.andin.game.tictactoe.data.Constants;

public class Choosing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);
    }

    public void easyClicked(View v){
        Intent easyIntent = new Intent(this, Game.class);
        easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        easyIntent.putExtra(Constants.DIFFICULTY, Constants.EASY);
        startActivity(easyIntent);
    }

    public void mediumClicked(View v){
        Intent easyIntent = new Intent(this, Game.class);
        easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        easyIntent.putExtra(Constants.DIFFICULTY, Constants.NORMAL);
        startActivity(easyIntent);

    }

    public void impossibleClicked(View v){
        Intent easyIntent = new Intent(this, Game.class);
        easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        easyIntent.putExtra(Constants.DIFFICULTY, Constants.HARD);
        startActivity(easyIntent);

    }

    public void pvpClicked(View v){
        Intent easyIntent = new Intent(this, Game.class);
        easyIntent.setFlags(easyIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        easyIntent.putExtra(Constants.DIFFICULTY, Constants.PVP);
        startActivity(easyIntent);
    }
}
