package org.cis1200.chess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Graphics;
import java.util.List;

public class Bishop extends Piece {

    protected List<Location> movesToBlockCheck;

    public Bishop() {
        super();
        movesToBlockCheck = new LinkedList<Location>();
    }

    public Bishop(int x, int y, Board board) {
        super(x, y, board);
        movesToBlockCheck = new LinkedList<Location>();
    }

    public Bishop(int x, int y, boolean white, Board board) {
        super(x, y, white, board);
        movesToBlockCheck = new LinkedList<Location>();
    }

    @Override
    public void generateValidMoves() {
        if (givingCheck) {
            givingCheck = false;
        }
        if (isTaken) {
            return;
        }
        if (board == null) {
            return;
        }

        validMoves = new LinkedList<Location>();
        movesToBlockCheck = new LinkedList<Location>();

        int[] dx = new int[] { 1, -1, -1, 1 };
        int[] dy = new int[] { 1, 1, -1, -1 };

        int[] directionsToBePinned = new int[] { 5, 7, 1, 3 };

        boolean[] reachedPiece = new boolean[4];
        ArrayList<Location>[] directionalValidMoves = new ArrayList[4];

        Location moveTile;
        Piece firstPieceReached;

        boolean multiplePiecesBtwnKing = false;

        for (int i = 0; i < dx.length; i++) {
            directionalValidMoves[i] = new ArrayList<Location>();

            firstPieceReached = null; // save first piece to determine if it is pinned
            for (int j = 1; j < board.getBoard().length; j++) {
                moveTile = new Location(location.x + (dx[i] * j), location.y + (dy[i] * j));
                if (!reachedPiece[i]) {
                    if (moveTile.isOnBoard(8, 8)) {
                        BoardTile currTile = board.getTileBasedOnLocation(moveTile);

                        Piece p;
                        if ((p = currTile.getPiece()) != null && !p.isTaken) {
                            reachedPiece[i] = true;
                            firstPieceReached = p;
                            if (white) {
                                if (!p.white) { // piece is for the taking
                                    if (p.toString().charAt(0) == 'K') {
                                        if (!this.isTaken) {
                                            ((King) p).setInCheck(true);
                                            givingCheck = true;
                                        }
                                        movesToBlockCheck.addAll(directionalValidMoves[i]);
                                    } else {
                                        directionalValidMoves[i].add(moveTile);
                                    }
                                }
                                currTile.setContainedInWhiteSight(true);
                            } else {
                                if (p.white) { // piece is for the taking
                                    if (p.toString().charAt(0) == 'K') {
                                        if (!this.isTaken) {
                                            ((King) p).setInCheck(true);
                                            givingCheck = true;
                                        }
                                        movesToBlockCheck.addAll(directionalValidMoves[i]);
                                    } else {
                                        directionalValidMoves[i].add(moveTile);
                                    }
                                }
                                currTile.setContainedInBlackSight(true);
                            }
                        } else {
                            if (white) {
                                currTile.setContainedInWhiteSight(true);
                            } else {
                                currTile.setContainedInBlackSight(true);
                            }
                            directionalValidMoves[i].add(moveTile);
                        }
                    } else {
                        break;
                    }
                } else {
                    if (moveTile.isOnBoard(8, 8)) {
                        BoardTile currTile2 = board.getTileBasedOnLocation(moveTile);
                        Piece p2;
                        if ((p2 = currTile2.getPiece()) != null) {
                            if (white && !p2.white && !firstPieceReached.white) {
                                if (p2.toString().charAt(0) == 'K' && !multiplePiecesBtwnKing) {
                                    firstPieceReached.isPinnedFrom[directionsToBePinned[i]] = true;
                                } else {
                                    multiplePiecesBtwnKing = true;
                                }
                            } else if (!white && p2.white && firstPieceReached.white) {
                                if (p2.toString().charAt(0) == 'K' && !multiplePiecesBtwnKing) {
                                    firstPieceReached.isPinnedFrom[directionsToBePinned[i]] = true;
                                } else {
                                    multiplePiecesBtwnKing = true;
                                }
                            }
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        if (isPinnedFrom[0] || isPinnedFrom[2] || isPinnedFrom[4] || isPinnedFrom[6]) {
            validMoves = new LinkedList<Location>();
            return;
        }
        if (!isPinnedFrom[3] && !isPinnedFrom[7]) {
            validMoves.addAll(directionalValidMoves[0]); // can move NE
            validMoves.addAll(directionalValidMoves[2]); // can move SW
        }
        if (!isPinnedFrom[1] && !isPinnedFrom[5]) {
            validMoves.addAll(directionalValidMoves[1]); // can move NW
            validMoves.addAll(directionalValidMoves[3]); // can move SE
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
        return "B" + whiteOrBlack + location.x + location.y;
    }
}
