package com.andin.game.tictactoe.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andin.game.tictactoe.R;
import com.andin.game.tictactoe.data.Constants;
import com.andin.game.tictactoe.logic.GameManagement;

import java.util.Random;

public class Game extends AppCompatActivity {

    private static final int NR_BUTTONS = 9;
    private int difficulty;
    private GameManagement gameManagement;
    private Button buttons[];
    private TextView report;
    private Typeface typeface;
    private int moveNr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageGrid();

        buttons = new Button[9];

        buttons[0] = (Button) findViewById(R.id.b1);
        buttons[1] = (Button) findViewById(R.id.b2);
        buttons[2] = (Button) findViewById(R.id.b3);
        buttons[3] = (Button) findViewById(R.id.b4);
        buttons[4] = (Button) findViewById(R.id.b5);
        buttons[5] = (Button) findViewById(R.id.b6);
        buttons[6] = (Button) findViewById(R.id.b7);
        buttons[7] = (Button) findViewById(R.id.b8);
        buttons[8] = (Button) findViewById(R.id.b9);

        report = (TextView) findViewById(R.id.textView);

        typeface = Typeface.createFromAsset(getAssets(), "Apple Butter.ttf");

        Typeface typefaceXO = Typeface.createFromAsset(getAssets(), "Ariendesse.ttf");

        for (int i = 0; i < NR_BUTTONS; i++) {
            buttons[i].setText("");
            buttons[i].setTypeface(typefaceXO);
        }

        difficulty = getIntent().getIntExtra(Constants.DIFFICULTY, 0);
        gameManagement = new GameManagement(difficulty);

        if (difficulty != Constants.PVP) {
            Random rand = new Random();
            if (rand.nextBoolean()) {
                pcMove(10);
            }
        }

    }

    private void manageGrid() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_game_old);
            } else {
            setContentView(R.layout.activity_game);
        }
    }

    public void buttonsClicked(View v) {
        switch (v.getId()) {
            case R.id.b1:
                manageMove(0);
                break;
            case R.id.b2:
                manageMove(1);
                break;
            case R.id.b3:
                manageMove(2);
                break;
            case R.id.b4:
                manageMove(3);
                break;
            case R.id.b5:
                manageMove(4);
                break;
            case R.id.b6:
                manageMove(5);
                break;
            case R.id.b7:
                manageMove(6);
                break;
            case R.id.b8:
                manageMove(7);
                break;
            case R.id.b9:
                manageMove(8);
                break;
        }
        moveNr++;
    }

    public void manageMove(int index){
        if (buttons[index].getText().toString().isEmpty()) {
            if (difficulty == Constants.PVP){
                if (moveNr % 2 == 0){
                    buttons[index].setText("X");
                } else {
                    buttons[index].setText("0");
                }
            } else {
                buttons[index].setText("X");
            }
            pcMove(index);
        }
    }

    public void pcMove(final int playerMove) {

        int resultedMove = gameManagement.makeMove(playerMove);

        int gameStatus = gameManagement.checkWin();

        report.setText(gameManagement.getMatrix());

        if (gameStatus != Constants.PVC_WIN && resultedMove != -1) {
            buttons[resultedMove].setText("0");
        }
        if (gameStatus != -1) {
            report.setText("Castiga");
            dialogMessage(gameStatus);
        }


    }

    public void dialogMessage(int gameStatus) {
        // custom dialog
        final Dialog dialog = new Dialog(Game.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.customdialog);
        dialog.setTitle("Game Over");

        // set the custom dialog components - text, image and button
        TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setTypeface(typeface);
        switch (gameStatus) {
            case Constants.PVC_WIN:
                message.setText("Congratulations! You won!");
                break;
            case Constants.PVC_LOSE:
                message.setText("Too bad! You lost!");
                break;
            case Constants.TIE:
                message.setText("It's a tie!");
                break;
            case Constants.PVP_WIN1:
                message.setText("Player 0 wins!");
                break;
            case Constants.PVP_WIN2:
                message.setText("Player X wins!");
                break;

        }

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getBack();
            }
        });

        dialog.show();
    }

    public void getBack() {
        Intent intent = new Intent(this, Choosing.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


}
