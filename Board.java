package org.cis1200.chess;

public class Board {

    private BoardTile[][] board;

    public Board() {
        board = new BoardTile[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new BoardTile(j, board.length - i - 1);
            }
        }
    }

    public BoardTile[][] getBoard() {
        return board;
    }

    public void resetSightLines() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                BoardTile curr = board[i][j];
                curr.setContainedInWhiteSight(false);
                curr.setContainedInBlackSight(false);
            }
        }
    }

    public BoardTile getTileBasedOnLocation(Location location) {
        if (isValidLocation(location)) {
            return board[board.length - location.y - 1][location.x]; // converts coordinates
        }
        return null;
    }

    private boolean isValidLocation(Location location) {
        return location.x >= 0 && location.x < board[0].length
                && location.y >= 0 && location.y < board.length;
    }

    public double evaluate() {
        double netEval = 0.0;

        double[][] evals = {
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 1.0},
                {1.0, 2.0, 3.0, 3.0, 3.0, 3.0, 2.0, 1.0},
                {1.0, 2.0, 3.0, 4.0, 4.0, 3.0, 2.0, 1.0},
                {1.0, 2.0, 3.0, 4.0, 4.0, 3.0, 2.0, 1.0},
                {1.0, 2.0, 3.0, 3.0, 3.0, 3.0, 2.0, 1.0},
                {1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}
        };

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getPiece() != null) {
                    Piece p = board[i][j].getPiece();
                    double value;
                    switch (p.toString().charAt(0)) {
                        case 'P':
                            value = 10.0;
                            break;
                        case 'N':
                            value = 30.0;
                            break;
                        case 'B':
                            value = 35.0;
                            break;
                        case 'R':
                            value = 50.0;
                            break;
                        case 'Q':
                            value = 90.0;
                            break;
                        case 'K':
                            value = 5.0;
                            break;
                        default:
                            value = 0.0;
                            break;
                    }
                    if (!p.white) {
                        value *= -1;
                        /*if (board[i][j].isContainedInWhiteSight()) {
                            value /= 2.0;
                        }
                        if (board[i][j].isContainedInBlackSight()) {
                            value *= 2.0;
                        }*/
                    } else {
                        /*if (board[i][j].isContainedInBlackSight()) {
                            value /= 2.0;
                        }
                        if (board[i][j].isContainedInWhiteSight()) {
                            value *= 2.0;
                        }*/
                    }

                }

            }
        }

        return netEval;
    }
}
