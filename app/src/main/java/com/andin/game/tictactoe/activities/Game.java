package com.andin.game.tictactoe.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.andin.game.tictactoe.R;
import com.andin.game.tictactoe.data.Constants;
import com.andin.game.tictactoe.logic.GameManagement;
import com.andin.game.tictactoe.services.BackgroundSoundService;

import java.util.Random;

public class Game extends AppCompatActivity {

    private static final int NR_BUTTONS = 9;
    private static final int DIALOG_WAIT = 1000;
    private static final int MOVE_WAIT = 500;
    private int difficulty;
    private GameManagement gameManagement;
    private SharedPreferences progress;
    private Button buttons[];
    private Typeface typeface;
    private int moveNr;
    private int finalCoins;
    private boolean screenChanged, moveMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageGrid();


        progress = this.getSharedPreferences(Constants.PROGRESS, Context.MODE_PRIVATE);

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

        typeface = Typeface.createFromAsset(getAssets(), "Harrington.ttf");

        Typeface typefaceXO = Typeface.createFromAsset(getAssets(), "Harrington.ttf");

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
        screenChanged = false;
        moveMade = true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        screenChanged = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!screenChanged) {
            stopService(new Intent(this, BackgroundSoundService.class));
        }

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

    private void manageGrid() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_game_old);
        } else {
            setContentView(R.layout.activity_game);
        }
    }

    public void buttonsClicked(View v) {
        if (moveMade || difficulty == Constants.PVP) {
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
        }
    }

    public void manageMove(int index) {
        if (buttons[index].getText().toString().isEmpty()) {
            moveMade = false;
            if (difficulty == Constants.PVP) {
                if (moveNr % 2 == 0) {
                    buttons[index].setText("X");
                } else {
                    buttons[index].setText("0");
                }
            } else {
                buttons[index].setText("X");
            }
            moveNr++;
            pcMove(index);
        }
    }

    public void pcMove(final int playerMove) {

        final int resultedMove = gameManagement.makeMove(playerMove);

        int gameStatus = gameManagement.checkWin();


        if (gameStatus != Constants.PVC_WIN && resultedMove != -1) {
            if (moveNr != 0 && gameStatus == -1 && difficulty != Constants.PVP) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttons[resultedMove].setText("0");
                        moveMade = true;
                    }
                }, MOVE_WAIT);
            } else {
                buttons[resultedMove].setText("0");
            }
        }
        if (gameStatus != -1) {
            dialogMessage(gameStatus);

        }
    }

    public int getCoins(int gameStatus) {
        int coins = 0;
        switch (gameStatus) {
            case Constants.PVC_WIN:
                switch (difficulty) {
                    case Constants.EASY:
                        coins = 2;
                        break;
                    case Constants.NORMAL:
                        coins = 5;
                        break;
                    case Constants.HARD:
                        coins = 20;
                        break;
                }
                break;
            case Constants.PVC_LOSE:
                switch (difficulty) {
                    case Constants.EASY:
                        coins = -1;
                        break;
                    case Constants.NORMAL:
                        coins = -2;
                        break;
                    case Constants.HARD:
                        coins = -5;
                        break;
                }
                break;
            case Constants.TIE:
                switch (difficulty) {
                    case Constants.NORMAL:
                        coins = 1;
                        break;
                    case Constants.HARD:
                        coins = 10;
                        break;
                }
                break;
        }
        SharedPreferences.Editor editor = progress.edit();
        finalCoins = progress.getInt(Constants.COINS, 0) + coins;
        if (finalCoins < 0) {
            finalCoins = 0;
        }
        editor.putInt(Constants.COINS, finalCoins);
        editor.commit();
        return coins;
    }

    public void dialogMessage(int gameStatus) {
        // custom dialog
        final Dialog dialog = new Dialog(Game.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog);

        // set the custom dialog components - text, image and button
        TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setGravity(Gravity.CENTER);
        message.setTypeface(typeface);
        int coins = getCoins(gameStatus);
        switch (gameStatus) {
            case Constants.PVC_WIN:
                if (Math.abs(coins) == 1) {
                    message.setText("Congratulations!\n You won " + coins + " coin!");
                } else {
                    message.setText("Congratulations!\n You won " + coins + " coins!");
                }
                break;
            case Constants.PVC_LOSE:
                if (finalCoins != 0) {
                    if (Math.abs(coins) == 1) {
                        message.setText("Too bad!\n You lost " + Math.abs(coins) + " coin.");
                    } else {
                        message.setText("Too bad!\n You lost " + Math.abs(coins) + " coins.");
                    }
                } else {
                        message.setText("Too bad!\n You now have 0 coins.");
                }
                break;
            case Constants.TIE:
                if (coins == 0) {
                    message.setText("It's a tie!\n");
                } else {
                    if (Math.abs(coins) == 1) {
                        message.setText("It's a tie!\n" + coins + " coin won!");
                    } else {
                        message.setText("It's a tie!\n" + coins + " coins won!");
                    }
                }
                break;
            case Constants.PVP_WIN1:
                message.setText("Player 0 wins!");
                break;
            case Constants.PVP_WIN2:
                message.setText("Player X wins!");
                break;

        }

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
        dialogButtonNo.setVisibility(View.INVISIBLE);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getBack();
            }
        });

        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (difficulty != Constants.PVP) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }
            }, DIALOG_WAIT);
        } else {
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

    }

    public void getBack() {
        Intent intent = new Intent(this, Menu.class);
        if (difficulty != Constants.PVP) {
            intent = new Intent(this, Choosing.class);
        }
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        screenChanged = true;
        startActivity(intent);
    }


}
