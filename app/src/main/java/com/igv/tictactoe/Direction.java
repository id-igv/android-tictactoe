package com.igv.tictactoe;

public class Direction {
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    public static final int DIAGUP = 2;
    public static final int DIAGDOWN = 3;
    public static final int DIAG = 4;

    private int index;
    private int type;
    private Move move;

    private int[] values;

    public static Direction getDirection(int[][] field, Move move, int type) {
        return new Direction(field, move, type);
    }

    public Direction(int[][] field, Move move, int type) {
        this.move = move;
        this.type = type;
        this.values = new int[] {-1, -1, -1};
        switch (type) {

            case Direction.VERTICAL: {
                for (int i = 0; i < 3; i++) {
                    this.values[i] = field[i][move.getCol()];
                }
                break;
            }

            case Direction.HORIZONTAL: {
                for (int i = 0; i < 3; i++) {
                    this.values[i] = field[move.getRow()][i];
                }
                break;
            }

            case Direction.DIAG: {
                if ((move.getRow() == 0 && move.getCol() == 0)
                        || (move.getRow() == 2 && move.getCol() == 2)) {

                    this.values[0] = field[0][0];
                    this.values[1] = field[1][1];
                    this.values[2] = field[2][2];
                    this.type = Direction.DIAGDOWN;
                } else {
                    this.values[0] = field[2][0];
                    this.values[1] = field[1][1];
                    this.values[2] = field[0][2];
                    this.type = Direction.DIAGUP;
                }
                break;
            }

            case Direction.DIAGUP: {
                this.values[0] = field[2][0];
                this.values[1] = field[1][1];
                this.values[2] = field[0][2];
                break;
            }

            case Direction.DIAGDOWN: {
                this.values[0] = field[0][0];
                this.values[1] = field[1][1];
                this.values[2] = field[2][2];
                break;
            }
        }
    }

    public boolean check() {
        for (int i = 0; i < 3; i++) {
            if (this.move.getSide() != this.values[i]) {
                return false;
            }
        }

        return true;
    }

    public int getType() { return this.type; }

    public Move getMove() { return this.move; }

    public int getSide() { return this.move.getSide(); }
}
