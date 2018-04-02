package com.igv.tictactoe;

public class GameSettings {
    public static final String GAME_MODE_PVP = "PVP";
    public static final String GAME_MODE_PVE = "PVE";
    public static final int PLAYER_SIDE_X = 1;
    public static final int PLAYER_SIDE_O = 0;

    private String gameMode;
    private int playerASide;

    public GameSettings(String mode, int playerASide) {
        this.gameMode = mode;
        this.playerASide = playerASide;
    }

    public String getGameMode() { return this.gameMode; }

    public int getPlayerASide() { return this.playerASide; }
}
