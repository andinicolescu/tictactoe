package com.andin.game.tictactoe.logic;

import com.andin.game.tictactoe.data.Constants;

import java.util.Arrays;

public class GameManagement {

    private int nrOfMove = 0;
    private boolean pvp = false;
    //private AccountRepository accounts = AccountRepository.getInstance();
    private int board[][] = new int[3][3];
    private static final int HUMAN_MOVE = 1;
    private static final int PC_MOVE = 2;
    private static final int HUMAN = 1;
    private static final int PC = 2;
    private static final int SCORE_IF_WIN = 20;
    private static final int SCORE_IF_TIE = 5;
    private static final int SCORE_IF_LOST = -5;

    private int difficulty;

    private AI computerPlayer = null;

    public GameManagement(int difficulty) {
        this.difficulty = difficulty;
        switch (difficulty) {
            case Constants.HARD:
                computerPlayer = new HardAI();
                break;
            case Constants.NORMAL:
                computerPlayer = new NormalAI();
                break;
            case Constants.EASY:
                computerPlayer = new EasyAI();
                break;
        }
    }

    public int makeMove(int playerLastMove) {

        nrOfMove++;
        if (((pvp) && (nrOfMove % 2 == 0)) || (!pvp)) {
            switch (playerLastMove) {
                case 0:
                    board[0][0] = HUMAN_MOVE;
                    break;
                case 1:
                    board[0][1] = HUMAN_MOVE;
                    break;
                case 2:
                    board[0][2] = HUMAN_MOVE;
                    break;
                case 3:
                    board[1][0] = HUMAN_MOVE;
                    break;
                case 4:
                    board[1][1] = HUMAN_MOVE;
                    break;
                case 5:
                    board[1][2] = HUMAN_MOVE;
                    break;
                case 6:
                    board[2][0] = HUMAN_MOVE;
                    break;
                case 7:
                    board[2][1] = HUMAN_MOVE;
                    break;
                case 8:
                    board[2][2] = HUMAN_MOVE;
                    break;
                case 10:
                    break;
            }
        }

        int checkWin = checkWin();
        if (checkWin != -1) {
            return -1;
        }

        // ------------------------------HARD---------------------------

        if (difficulty != Constants.PVP) {

            int pcMove = computerPlayer.executeMove(playerLastMove);
            int i = pcMove / 3;
            int j = pcMove % 3;
            board[i][j] = PC_MOVE;
            return pcMove;

        } else {
            pvp = true;
            if (nrOfMove % 2 == 1)
                switch (playerLastMove) {
                    case 0:
                        board[0][0] = PC_MOVE;
                        break;
                    case 1:
                        board[0][1] = PC_MOVE;
                        break;
                    case 2:
                        board[0][2] = PC_MOVE;
                        break;
                    case 3:
                        board[1][0] = PC_MOVE;
                        break;
                    case 4:
                        board[1][1] = PC_MOVE;
                        break;
                    case 5:
                        board[1][2] = PC_MOVE;
                        break;
                    case 6:
                        board[2][0] = PC_MOVE;
                        break;
                    case 7:
                        board[2][1] = PC_MOVE;
                        break;
                    case 8:
                        board[2][2] = PC_MOVE;
                        break;
                }

            return -1;
        }
    }

    public String getMatrix(){
        return matrixToString(board);
    }

    public static String matrixToString(int[][] M) {
        String separator = ", ";
        StringBuffer result = new StringBuffer();

        // iterate over the first dimension
        for (int i = 0; i < M.length; i++) {
            // iterate over the second dimension
            for(int j = 0; j < M[i].length; j++){
                result.append(M[i][j]);
                result.append(separator);
            }
            // remove the last separator
            result.setLength(result.length() - separator.length());
            // add a line break.
            result.append("\n");
        }
        return result.toString();
    }

    public int winner() {

        // if human wins
        if ((board[0][0] == HUMAN_MOVE) && (board[0][1] == HUMAN_MOVE) && (board[0][2] == HUMAN_MOVE))
            return 1;
        if ((board[0][0] == HUMAN_MOVE) && (board[1][0] == HUMAN_MOVE) && (board[2][0] == HUMAN_MOVE))
            return 1;
        if ((board[2][0] == HUMAN_MOVE) && (board[2][1] == HUMAN_MOVE) && (board[2][2] == HUMAN_MOVE))
            return 1;
        if ((board[0][2] == HUMAN_MOVE) && (board[1][2] == HUMAN_MOVE) && (board[2][2] == HUMAN_MOVE))
            return 1;
        if ((board[0][0] == HUMAN_MOVE) && (board[1][1] == HUMAN_MOVE) && (board[2][2] == HUMAN_MOVE))
            return 1;
        if ((board[0][2] == HUMAN_MOVE) && (board[1][1] == HUMAN_MOVE) && (board[2][0] == HUMAN_MOVE))
            return 1;
        if ((board[0][1] == HUMAN_MOVE) && (board[1][1] == HUMAN_MOVE) && (board[2][1] == HUMAN_MOVE))
            return 1;
        if ((board[1][0] == HUMAN_MOVE) && (board[1][1] == HUMAN_MOVE) && (board[1][2] == HUMAN_MOVE))
            return 1;
        // if pc wins
        if ((board[0][0] == PC_MOVE) && (board[0][1] == PC_MOVE) && (board[0][2] == PC_MOVE))
            return 2;
        if ((board[0][0] == PC_MOVE) && (board[1][0] == PC_MOVE) && (board[2][0] == PC_MOVE))
            return 2;
        if ((board[2][0] == PC_MOVE) && (board[2][1] == PC_MOVE) && (board[2][2] == PC_MOVE))
            return 2;
        if ((board[0][2] == PC_MOVE) && (board[1][2] == PC_MOVE) && (board[2][2] == PC_MOVE))
            return 2;
        if ((board[0][0] == PC_MOVE) && (board[1][1] == PC_MOVE) && (board[2][2] == PC_MOVE))
            return 2;
        if ((board[0][2] == PC_MOVE) && (board[1][1] == PC_MOVE) && (board[2][0] == PC_MOVE))
            return 2;
        if ((board[0][1] == PC_MOVE) && (board[1][1] == PC_MOVE) && (board[2][1] == PC_MOVE))
            return 2;
        if ((board[1][0] == PC_MOVE) && (board[1][1] == PC_MOVE) && (board[1][2] == PC_MOVE))
            return 2;

        return 0;
    }

    public boolean tableFull(int boardToCheck[][]) {
        boolean full = true;
        for (int u = 0; u < 3; u++)
            for (int y = 0; y < 3; y++) {
                if (boardToCheck[u][y] == 0)
                    full = false;
            }
        return full;
    }

    public int checkWin() {
        if (winner() == HUMAN) {
            if (pvp) {
                return Constants.PVP_WIN1;
            } else {
                return Constants.PVC_WIN;
            }
        }

        if (winner() == PC) {
            if (pvp) {
                return Constants.PVP_WIN2;
            } else {
                return Constants.PVC_LOSE;
            }
        }

        if (tableFull(board)) {

            if (winner() == 0) { //tie
                return Constants.TIE;
            }
        }

        return -1;
    }
}