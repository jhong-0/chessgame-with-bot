package org.cis1200.chess;

import java.util.LinkedList;
import java.awt.Graphics;

public class Knight extends Piece {

    public Knight() {
        super();
    }

    public Knight(int x, int y, Board board) {
        super(x, y, board);
    }

    public Knight(int x, int y, boolean white, Board board) {
        super(x, y, white, board);
    }

    @Override
    public void generateValidMoves() {
        if (givingCheck) {
            givingCheck = false;
        }
        validMoves = new LinkedList<Location>();

        for (boolean pinned : isPinnedFrom) {
            if (pinned) {
                return;
            }
        }

        int[] dx = { -2, -2, -1, -1, 1, 1, 2, 2 };
        int[] dy = { 1, -1, 2, -2, 2, -2, 1, -1 };

        Location moveTile;
        for (int i = 0; i < dx.length; i++) {
            moveTile = new Location(this.location.x + dx[i], this.location.y + dy[i]);
            if (moveTile.isOnBoard(8, 8)) {
                BoardTile currTile = board.getTileBasedOnLocation(moveTile);
                if (white) {
                    currTile.setContainedInWhiteSight(true);
                } else {
                    currTile.setContainedInBlackSight(true);
                }
                Piece p;
                if ((p = currTile.getPiece()) != null) {
                    if ((white && p.white) || (!white && !p.white)) {
                        continue;
                    }
                    if (p.toString().charAt(0) == 'K') {
                        ((King) p).setInCheck(true);
                        givingCheck = true;
                        continue;
                    }
                }
                validMoves.add(moveTile);
            }
        }
    }

    @Override
    public boolean canMove() {
        return validMoves.size() > 0;
    }

    @Override
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

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public String toString() {
        String whiteOrBlack = "";
        if (white) {
            whiteOrBlack = "w";
        } else {
            whiteOrBlack = "b";
        }
        return "N" + whiteOrBlack + location.x + location.y;
    }
}
