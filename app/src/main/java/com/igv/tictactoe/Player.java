package com.igv.tictactoe;

public class Player {

    public static final int PLAYER_HUMAN = 0;
    public static final int PLAYER_MACHINE = 1;

    protected String name;
    protected int side;
    protected int type;

    protected Player() {}

    public Player(int side, int type) {
        this.name = "Player_" + (side+1);
        this.side = side;
        this.type = type;
    }

    public Player(String name, int side, int type) {
        this.name = name;
        this.side = side;
        this.type = type;
    }

    public int getSide() { return this.side; }
    public int getType() { return this.type; }
    public String getName() { return this.name; }
}
