package com.igv.tictactoe;

public class Move {
    private Player p;
    private int row;
    private int col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Move(int row, int col, Player p) {
        this.row = row;
        this.col = col;
        this.p = p;
    }

    public int getRow() { return this.row; }
    public int getCol() { return this.col; }
    public int getSide() { return this.p.getSide(); }
    public Player getPlayer() { return this.p; }
}
