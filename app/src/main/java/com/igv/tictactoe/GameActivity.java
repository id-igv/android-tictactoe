package com.igv.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    private static Game session;
    private static GameSettings gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();

        gameSettings = new GameSettings(
                intent.getStringExtra(WelcomeActivity.GS_MESSAGE_MODE),
                Integer.parseInt(intent.getStringExtra(WelcomeActivity.GS_MESSAGE_SIDE))
        );

        GameActivity.session = Game.getInstance();

        session.setSettings(gameSettings);

        Player playerA = new Player(
                session.getSettings().getPlayerASide(),
                Player.PLAYER_HUMAN
        );

        Player playerB;

        if (Objects.equals(session.getSettings().getGameMode(), GameSettings.GAME_MODE_PVE)) {
            playerB = (Player) new Machine(
                    session.getSettings().getPlayerASide() == GameSettings.PLAYER_SIDE_O ?
                            GameSettings.PLAYER_SIDE_X : GameSettings.PLAYER_SIDE_O
            );
        } else {
            playerB = new Player(
                    session.getSettings().getPlayerASide() == GameSettings.PLAYER_SIDE_O ?
                            GameSettings.PLAYER_SIDE_X : GameSettings.PLAYER_SIDE_O,
                    Player.PLAYER_HUMAN
            );
        }

        session.setPlayers(playerA, playerB);

        ((ImageButton) findViewById(R.id.cell00Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(0,0, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell01Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(0,1, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell02Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(0,2, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell10Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(1,0, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell11Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(1,1, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell12Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(1,2, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell20Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(2,0, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell21Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(2,1, v);
                    }
                }
        );
        ((ImageButton) findViewById(R.id.cell22Btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cellClick(2,2, v);
                    }
                }
        );

        // if PVE and player chose side 'O' machine makes 1st move
        if ((session.getPlayerB().getType() == Player.PLAYER_MACHINE)
                && session.getPlayerB().getSide() == GameSettings.PLAYER_SIDE_X) {

            int[] move = ((Machine) session.getPlayerB()).move(session.getField());
            cellClick(move[0], move[1], findViewById(
                    getResources().getIdentifier("cell" + move[0] + move[1] + "Btn", "id", getPackageName())
            ));
        }
    }

    public void showMessage(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Game Result")
                .setMessage(text)
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean proceedMove(int row, int col, View view) {
        // game continues
        boolean canContinue = session.move(row, col);
        boolean imageIsSet = setCellImage(GameActivity.session.getLastMove(), view);

        if(canContinue) {
            if (imageIsSet) { return true; }

            return false;
        }

        // game ended
        int gameResult = GameActivity.session.gameResult();

        if (gameResult == Game.GAME_RESULT_UNKNOWN) {
            resetSession();

            return false; // something gone wrong (game ended but no result)
        }

        if (gameResult == Game.GAME_RESULT_TIE) {
            // simple tie message
            showMessage("TIE");
            resetSession();

            return true;
        }

        Direction direction = GameActivity.session.getWinDirection();
        if (direction == null) {
            resetSession();
            return false;
        }

        if (Objects.equals(GameActivity.session.getSettings().getGameMode(), GameSettings.GAME_MODE_PVE)) {
            if (gameResult == Game.GAME_RESULT_WIN) {
                showMessage("WIN");
            }
            if (gameResult == Game.GAME_RESULT_LOSE) {
                showMessage("LOSE");
            }
            resetSession();
            return true;
        }

        if (Objects.equals(GameActivity.session.getSettings().getGameMode(), GameSettings.GAME_MODE_PVP)) {
            Player winner = direction.getMove().getPlayer();
            // show who won in message
            showMessage(winner.getName() +  " won");
            resetSession();
            return true;
        }

        return false;
    }

    public boolean setCellImage(Move move, View view) {
        switch (move.getSide()) {

            case GameSettings.PLAYER_SIDE_O : {
                ((ImageButton) view).setImageResource(R.drawable.ic_if_circle);
                return true;
            }

            case GameSettings.PLAYER_SIDE_X : {
                ((ImageButton) view).setImageResource(R.drawable.ic_if_cross);
                return true;
            }

            default: { return false; }
        }
    }

    public void resetSession() {
        Game.reset();
        session = Game.getInstance();
        session.setSettings(gameSettings);
        Player playerA = new Player(
                session.getSettings().getPlayerASide(),
                Player.PLAYER_HUMAN
        );

        Player playerB;

        if (Objects.equals(session.getSettings().getGameMode(), GameSettings.GAME_MODE_PVE)) {
            playerB = (Player) new Machine(
                    session.getSettings().getPlayerASide() == GameSettings.PLAYER_SIDE_O ?
                            GameSettings.PLAYER_SIDE_X : GameSettings.PLAYER_SIDE_O
            );
        } else {
            playerB = new Player(
                    session.getSettings().getPlayerASide() == GameSettings.PLAYER_SIDE_O ?
                            GameSettings.PLAYER_SIDE_X : GameSettings.PLAYER_SIDE_O,
                    Player.PLAYER_HUMAN
            );
        }

        session.setPlayers(playerA, playerB);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String imageBtnID = "cell" + i + j + "Btn";
                int resID = getResources().getIdentifier(imageBtnID, "id", getPackageName());

                ImageButton cellBtn = (ImageButton) findViewById(resID);
                cellBtn.setImageResource(R.drawable.ic_empty);
            }
        }
    }

    public void cellClick(int row, int col, View view) {
        if (!proceedMove(row, col, view)) {
            showMessage("Oops... An error occurred!");
            resetSession();
        }

        if (Objects.equals(session.getSettings().getGameMode(), GameSettings.GAME_MODE_PVE)
                && session.getCurrentPlayer().getType() == Player.PLAYER_MACHINE) {
            int[] move = ((Machine) session.getPlayerB()).move(session.getField());
            cellClick(move[0], move[1], findViewById(
                    getResources().getIdentifier("cell" + move[0] + move[1] + "Btn", "id", getPackageName())
            ));
        }
    }
}
