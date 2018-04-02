package com.igv.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Machine extends Player {
    private int[][] field;
    private int opSide;

    public Machine(int side) {
        setSide(side);
        this.type = Player.PLAYER_MACHINE;
    }

    public void setSide(int side) {
        this.side = side;
        this.opSide = (side == GameSettings.PLAYER_SIDE_X) ?
                GameSettings.PLAYER_SIDE_O : GameSettings.PLAYER_SIDE_X;
    }

    public int[] move(int[][] field) {
        this.field = field.clone();
        int[] result = minimax(2, this.side);
        this.field = null;
        return new int[] {result[1], result[2]};
    }

    private int[] minimax(int depth, int turn) {
        List<int[]> nextMoves = generateMoves();

        int bestScore = (turn == this.side) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                this.field[move[0]][move[1]] = turn;

                if (turn == this.side) {
                    currentScore = minimax(depth - 1, this.opSide)[0];

                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {
                    currentScore = minimax(depth - 1, this.side)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }

                this.field[move[0]][move[1]] = Game.EMPTY_CELL;
            }
        }
        return new int[] {bestScore, bestRow, bestCol};
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>();

        if (hasWon(this.side) || hasWon(this.opSide)) {
            return nextMoves;
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (this.field[row][col] == Game.EMPTY_CELL) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }

        return nextMoves;
    }

    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);
        score += evaluateLine(1, 0, 1, 1, 1, 2);
        score += evaluateLine(2, 0, 2, 1, 2, 2);
        score += evaluateLine(0, 0, 1, 0, 2, 0);
        score += evaluateLine(0, 1, 1, 1, 2, 1);
        score += evaluateLine(0, 2, 1, 2, 2, 2);
        score += evaluateLine(0, 0, 1, 1, 2, 2);
        score += evaluateLine(0, 2, 1, 1, 2, 0);
        return score;
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        if (this.field[row1][col1] == this.side) {
            score = 1;
        } else if (this.field[row1][col1] == this.opSide) {
            score = -1;
        }

        // Second cell
        if (this.field[row2][col2] == this.side) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (this.field[row2][col2] == this.opSide) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (this.field[row3][col3] == this.side) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (this.field[row3][col3] == this.opSide) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    private int[] winPatterns = {
            0b111000000, 0b000111000, 0b000000111,
            0b100100100, 0b010010010, 0b001001001,
            0b100010001, 0b001010100
    };

    private boolean hasWon(int turn) {
        int pattern = 0b000000000; // field in bits

        // fills pattern with bits
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (this.field[row][col] == turn) {
                    pattern |= (1 << (row * 3 + col));
                }
            }
        }

        // looks for win pattern
        for (int winPattern : this.winPatterns) {
            // uses simple logic AND
            if ((pattern & winPattern) == winPattern) { return true; }
        }

        return false;
    }
}
