package org.cis1200.chess;

import java.util.*;
import java.awt.Graphics;

public class King extends Piece {

    private boolean isInCheck;

    public King() {
        super();
    }

    public King(int x, int y, Board board) {
        super(x, y, board);
    }

    public King(int x, int y, boolean white, Board board) {
        super(x, y, white, board);
    }

    /* setters and getters */
    public void setInCheck(boolean isInCheck) {
        this.isInCheck = isInCheck;
    }

    /* piece-specific methods */
    public boolean checkGameOver() {
        if (!canMove() && isInCheck) {
            return true;
        }

        return false;
    }

    public /* void */ boolean inCheck() {
        /*
         * if (white) {
         * isInCheck =
         * board.getTileBasedOnLocation(this.location).isContainedInBlackSight();
         * } else {
         * isInCheck =
         * board.getTileBasedOnLocation(this.location).isContainedInWhiteSight();
         * }
         */
        return isInCheck;
    }

    /* abstract methods */
    public void generateValidMoves() {
        validMoves = new LinkedList<Location>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Location moveTile = new Location(location.x + i, location.y + j);
                BoardTile bt = board.getTileBasedOnLocation(moveTile);
                if (!moveTile.equals(location) && moveTile.isOnBoard(8, 8)) {
                    if (white) {
                        bt.setContainedInWhiteSight(true);
                    } else {
                        bt.setContainedInBlackSight(true);
                    }
                    if (white && (bt.isContainedInBlackSight()
                            || (bt.getPiece() != null && bt.getPiece().white))) {
                        continue;
                    } else if (!white && (bt.isContainedInWhiteSight()
                            || (bt.getPiece() != null && !bt.getPiece().white))) {
                        continue;
                    }
                    validMoves.add(moveTile);
                }

            }
        }
    }

    public boolean checkCanCastle(Rook rook) {
        if ((white && rook.white) || (!white && !rook.white)) {
            if (numMoves == 0 && rook.numMoves == 0) {
                if (rook.location.x > location.x) {
                    BoardTile twoRight = board
                            .getTileBasedOnLocation(new Location(location.x + 2, location.y));
                    BoardTile oneRight = board
                            .getTileBasedOnLocation(new Location(location.x + 1, location.y));
                    if (twoRight.getPiece() == null && oneRight.getPiece() == null
                            && ((white && !twoRight.isContainedInBlackSight())
                                    || (!white && !twoRight.isContainedInWhiteSight()))
                            && ((white && !oneRight.isContainedInBlackSight())
                                    || (!white && !oneRight.isContainedInWhiteSight()))
                            && !isInCheck && !rook.isTaken) {
                        this.validMoves.add(twoRight.getLocation());
                        return true;
                    }
                } else {
                    BoardTile threeLeft = board
                            .getTileBasedOnLocation(new Location(location.x - 3, location.y));
                    BoardTile twoLeft = board
                            .getTileBasedOnLocation(new Location(location.x - 2, location.y));
                    BoardTile oneLeft = board
                            .getTileBasedOnLocation(new Location(location.x - 1, location.y));
                    if (threeLeft.getPiece() == null && twoLeft.getPiece() == null
                            && oneLeft.getPiece() == null
                            && ((white && !twoLeft.isContainedInBlackSight())
                                    || (!white && !twoLeft.isContainedInWhiteSight()))
                            && ((white && !oneLeft.isContainedInBlackSight())
                                    || (!white && !oneLeft.isContainedInWhiteSight()))
                            && !isInCheck && !rook.isTaken) {
                        this.validMoves.add(twoLeft.getLocation());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canMove() {
        return validMoves.size() == 0;
    }

    public void move(Location location) {
        for (Location loc : validMoves) {
            if (loc.equals(location)) {
                board.getTileBasedOnLocation(this.location).setPiece(null);
                this.location = location;
                validMoves = new LinkedList<Location>();

                Piece atLocation = board.getTileBasedOnLocation(this.location).getPiece();
                if (atLocation != null) {
                    atLocation.isTaken = true;
                }

                board.getTileBasedOnLocation(this.location).setPiece(this);
                numMoves++;

                return;
            }
        }
    }

    public void draw(Graphics g) {

    }

    public String toString() {
        String whiteOrBlack = "";
        if (white) {
            whiteOrBlack = "w";
        } else {
            whiteOrBlack = "b";
        }
        return "K" + whiteOrBlack + location.x + location.y;
    }
}
