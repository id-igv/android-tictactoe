package com.igv.tictactoe;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Game {
    public static final int GAME_STATUS_START = -1;
    public static final int GAME_STATUS_FINISHED = 0;
    public static final int GAME_STATUS_PLAYING = 1;

    public static final int GAME_RESULT_UNKNOWN = -1;
    public static final int GAME_RESULT_LOSE = 0;
    public static final int GAME_RESULT_WIN = 1;
    public static final int GAME_RESULT_TIE = 2;

    public static final int EMPTY_CELL = -1;

    private static Game instance = null;

    private int[][] field;
    private int status;
    private int turn;
    private GameSettings settings;

    private List<Move> moves;

    private Player playerA;
    private Player playerB;

    private Direction winDirection;

    public static Game getInstance() {
        if (instance == null) { return new Game(); }

        return instance;
    }

    private Game() {
        this.status = GAME_STATUS_START; // get ready
        this.turn = GameSettings.PLAYER_SIDE_X; // first move on X player
        this.moves = new ArrayList<>();
        this.winDirection = null;

        // init game field with -1 - means unused cell
        //                       0 - means zero owner
        //                       1 - means x owner
        this.field = new int[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.field[row][col] = Game.EMPTY_CELL;
            }
        }
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    public GameSettings getSettings() {
        return this.settings;
    }

    public void setPlayers(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public Player getPlayer(int side) {
        return playerA.getSide() == side ? playerA : playerB;
    }

    public Player getPlayerA() { return this.playerA; }
    public Player getPlayerB() { return this.playerB; }

    public Player getCurrentPlayer() {
        return getPlayer(this.turn);
    }

    public int[][] getField() { return this.field; }

    public boolean cellIsEmpty(int row, int col) {
        return this.field[row][col] == -1;
    }

    /**
     * Complex. Processes whole cycle of game move
     *
     * @param row field row to set
     * @param col field col to set
     * @return true if move was saved
     * @return false if move impossible
     */
    public boolean move(int row, int col) {
        if (this.moves.size() == 0) { this.status = Game.GAME_STATUS_PLAYING; }
        // ! attention here !
        if (!setFieldCell(row, col)) { return false; }

        log(row, col);
        this.winDirection = checkMove();

        // continue playing
        if (this.winDirection == null) {
            if (this.moves.size() < 9) {
                switchTurn();

                return true;
            }
        }

        // end
        this.status = Game.GAME_STATUS_FINISHED;

        return false;
    }

    /**
     *
     * @param row field row to set
     * @param col field col to set
     * @return true if set OR false if did not
     */
    private boolean setFieldCell(int row, int col) {
        // if cell is empty
        if (!cellIsEmpty(row, col)) { return false; }

        this.field[row][col] = this.turn;

        return true;
    }

    private boolean isCorner(Move move) {
        int row = move.getRow();
        int col = move.getCol();
        return (row == 0 && col == 0) || (row == 0 && col == 2)
                || (row == 2 && col == 0) || (row == 2 && col == 2);

    }

    private boolean isCenter(Move move) {
        return move.getCol() == 1 && move.getRow() == 1;
    }

    /**
     * Checks field for end patterns on four directions:
     *      - vertical
     *      - horizontal
     *      - diagonal down
     *      - diagonal up
     *
     * @return true if game has ended (tie or win/lose)
     * @return false if game continues
     */
    @Nullable
    private Direction checkMove() {
        if (this.moves.size() < 4) { return null; }

        Move lastMove = getLastMove();

        if (lastMove == null) { return null; }

        Direction direction = Direction.getDirection(this.field, lastMove, Direction.HORIZONTAL);
        if (direction.check()) { return direction; }

        direction = Direction.getDirection(this.field, lastMove, Direction.VERTICAL);
        if (direction.check()) { return direction; }

        if (isCorner(lastMove)) {
            direction = Direction.getDirection(this.field, lastMove, Direction.DIAG);
            if (direction.check()) { return direction; }

        } else if (isCenter(lastMove)) {
            direction = Direction.getDirection(this.field, lastMove, Direction.DIAGDOWN);
            if (direction.check()) { return direction; }

            direction = Direction.getDirection(this.field, lastMove, Direction.DIAGUP);
            if (direction.check()) { return direction; }
        }

        return null;
    }

    @Nullable
    public Move getLastMove() {
        if (this.moves.size() < 1) { return null; }
        return this.moves.get(this.moves.size() - 1);
    }

    private void log(int row, int col) {
        this.moves.add(new Move(row, col, getPlayer(this.turn)));
    }

    private void switchTurn() {
        this.turn = (this.turn == GameSettings.PLAYER_SIDE_X) ?
                GameSettings.PLAYER_SIDE_O : GameSettings.PLAYER_SIDE_X;
    }

    public void setStatus(int status) { this.status = status; }

    public boolean isPlaying() {
        return this.status == Game.GAME_STATUS_PLAYING;
    }

    public Direction getWinDirection() {
        return this.winDirection;
    }

    public int gameResult() {
        if (this.status != Game.GAME_STATUS_FINISHED) { return Game.GAME_RESULT_UNKNOWN; }

        if (this.winDirection == null) { return Game.GAME_RESULT_TIE; }

        if (Objects.equals(this.settings.getGameMode(), GameSettings.GAME_MODE_PVE)) {
            return this.winDirection.getSide() == this.playerA.getSide() ?
                    Game.GAME_RESULT_WIN : Game.GAME_RESULT_LOSE;
        }

        // for pvp no matter what to return... btw tie already returned
        return Game.GAME_RESULT_LOSE;
    }

    public static void reset() {
        instance = null;
    }
}
