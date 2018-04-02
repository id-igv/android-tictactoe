package com.igv.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class WelcomeActivity extends AppCompatActivity {

    public static final String GS_MESSAGE_MODE = "com.igv.tictactoe.GS_MESSAGE_MODE";
    public static final String GS_MESSAGE_SIDE = "com.igv.tictactoe.GS_MESSAGE_SIDE";

    public static String chosenGameMode = GameSettings.GAME_MODE_PVE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ToggleButton tbtn = (ToggleButton) findViewById(R.id.toggleGameMode);
        tbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chosenGameMode = b ?
                        GameSettings.GAME_MODE_PVP : GameSettings.GAME_MODE_PVE;

                ((TextView)findViewById(R.id.infoText)).setText(chosenGameMode);
            }
        });

        ((TextView)findViewById(R.id.infoText)).setText(chosenGameMode);
    }

    @Override
    public void onResume() {
        super.onResume();

        chosenGameMode = GameSettings.GAME_MODE_PVE;
    }

    public void onCrossSelectButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GS_MESSAGE_MODE, chosenGameMode);
        intent.putExtra(GS_MESSAGE_SIDE, String.valueOf(GameSettings.PLAYER_SIDE_X));
        startActivity(intent);
    }

    public void onZeroSelectButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GS_MESSAGE_MODE, chosenGameMode);
        intent.putExtra(GS_MESSAGE_SIDE, String.valueOf(GameSettings.PLAYER_SIDE_O));
        startActivity(intent);
    }
}
