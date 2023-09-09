package org.cis1200.chess;

import java.awt.*;
import java.util.LinkedList;

public class Pawn extends Piece {

    protected boolean movedTwo;
    protected Location prevLoc;
    // protected boolean enPassant;

    public Pawn() {
        super();
        movedTwo = false;
        // enPassant = false;
        prevLoc = location;
    }

    public Pawn(int x, int y) {
        super(x, y);
        movedTwo = false;
        // enPassant = false;
        prevLoc = location;
    }

    public Pawn(int x, int y, boolean white) {
        super(x, y, white);
        movedTwo = false;
        // enPassant = false;
        prevLoc = location;
    }

    public Pawn(int x, int y, Board board) {
        super(x, y, board);
        movedTwo = false;
        // enPassant = false;
        prevLoc = location;
    }

    public Pawn(int x, int y, boolean white, Board board) {
        super(x, y, white, board);
        movedTwo = false;
        // enPassant = false;
        prevLoc = location;
    }

    public void generateValidMoves() {
        // TODO: Complete this method
        validMoves = new LinkedList<Location>();
        if (givingCheck) {
            givingCheck = false;
        }

        Location upTwo;
        Location downTwo;
        // standard forward move
        if (white) {
            Location upOne = new Location(location.x, location.y + 1);
            if (upOne.isOnBoard(8, 8) && board.getTileBasedOnLocation(upOne).getPiece() == null) {
                validMoves.add(upOne);
            }
            if (numMoves == 0) {
                upTwo = new Location(location.x, location.y + 2);
                if (board.getTileBasedOnLocation(upTwo).getPiece() == null
                        && board.getTileBasedOnLocation(upOne).getPiece() == null) {
                    validMoves.add(upTwo);
                }
            }
        } else {
            Location downOne = new Location(location.x, location.y - 1);
            if (downOne.isOnBoard(8, 8)
                    && board.getTileBasedOnLocation(downOne).getPiece() == null) {
                validMoves.add(downOne);
            }
            if (numMoves == 0) {
                downTwo = new Location(location.x, location.y - 2);
                if (board.getTileBasedOnLocation(downTwo).getPiece() == null
                        && board.getTileBasedOnLocation(downOne).getPiece() == null) {
                    validMoves.add(downTwo);
                }
            }
        }

        // diagonal capture
        LinkedList<Location> diagonalMoves = new LinkedList<Location>();
        Location upOneLeftOne = null, upOneRightOne = null, downOneLeftOne = null,
                downOneRightOne = null;
        if (white) {
            upOneLeftOne = new Location(location.x - 1, location.y + 1);
            upOneRightOne = new Location(location.x + 1, location.y + 1);
            if (upOneLeftOne.isOnBoard(8, 8)) {
                BoardTile bt = board.getTileBasedOnLocation(upOneLeftOne);
                bt.setContainedInWhiteSight(true);
                if (bt.getPiece() != null && !bt.getPiece().white) {
                    if (bt.getPiece().toString().charAt(0) == 'K') {
                        ((King) bt.getPiece()).setInCheck(true);
                        givingCheck = true;
                    } else {
                        diagonalMoves.add(upOneLeftOne);
                    }
                }
            }
            if (upOneRightOne.isOnBoard(8, 8)) {
                BoardTile bt2 = board.getTileBasedOnLocation(upOneRightOne);
                bt2.setContainedInWhiteSight(true);
                if (bt2.getPiece() != null && !bt2.getPiece().white) {
                    if (bt2.getPiece().toString().charAt(0) == 'K') {
                        ((King) bt2.getPiece()).setInCheck(true);
                        givingCheck = true;
                    } else {
                        diagonalMoves.add(upOneRightOne);
                    }
                }
            }
        } else {
            downOneLeftOne = new Location(location.x - 1, location.y - 1);
            downOneRightOne = new Location(location.x + 1, location.y - 1);
            if (downOneLeftOne.isOnBoard(8, 8)) {
                BoardTile bt = board.getTileBasedOnLocation(downOneLeftOne);
                bt.setContainedInBlackSight(true);
                if (bt.getPiece() != null && bt.getPiece().white) {
                    if (bt.getPiece().toString().charAt(0) == 'K') {
                        ((King) bt.getPiece()).setInCheck(true);
                        givingCheck = true;
                    } else {
                        diagonalMoves.add(downOneLeftOne);
                    }
                }
            }
            if (downOneRightOne.isOnBoard(8, 8)) {
                BoardTile bt2 = board.getTileBasedOnLocation(downOneRightOne);
                bt2.setContainedInBlackSight(true);
                if (bt2.getPiece() != null && bt2.getPiece().white) {
                    if (bt2.getPiece().toString().charAt(0) == 'K') {
                        ((King) bt2.getPiece()).setInCheck(true);
                        givingCheck = true;
                    } else {
                        diagonalMoves.add(downOneRightOne);
                    }
                }
            }
        }

        // en passant
        Location toTheLeft = new Location(location.x - 1, location.y);
        Location toTheRight = new Location(location.x + 1, location.y);
        if (white) {
            BoardTile left;
            if (toTheLeft.isOnBoard(8, 8)) {
                left = board.getTileBasedOnLocation(toTheLeft);
                Piece piece = left.getPiece();
                if (piece != null && !piece.white) {
                    if (piece.toString().charAt(0) == 'P' && ((Pawn) piece).movedTwo) {
                        diagonalMoves.add(upOneLeftOne);
                    }
                }
            }
            BoardTile right;
            if (toTheRight.isOnBoard(8, 8)) {
                right = board.getTileBasedOnLocation(toTheRight);
                Piece piece = right.getPiece();
                if (piece != null && !piece.white) {
                    if (piece.toString().charAt(0) == 'P' && ((Pawn) piece).movedTwo) {
                        diagonalMoves.add(upOneRightOne);
                    }
                }
            }
        } else {
            BoardTile left;
            if (toTheLeft.isOnBoard(8, 8)) {
                left = board.getTileBasedOnLocation(toTheLeft);
                Piece piece = left.getPiece();
                if (piece != null && piece.white) {
                    if (piece.toString().charAt(0) == 'P' && ((Pawn) piece).movedTwo) {
                        diagonalMoves.add(downOneLeftOne);
                    }
                }
            }
            BoardTile right;
            if (toTheRight.isOnBoard(8, 8)) {
                right = board.getTileBasedOnLocation(toTheRight);
                Piece piece = right.getPiece();
                if (piece != null && piece.white) {
                    if (piece.toString().charAt(0) == 'P' && ((Pawn) piece).movedTwo) {
                        diagonalMoves.add(downOneRightOne);
                    }
                }
            }
        }

        // check if pinned
        if (white) {
            if (isPinnedFrom[2] || isPinnedFrom[6]) {
                return;
            }
            if (isPinnedFrom[0] || isPinnedFrom[4]) {
                validMoves = new LinkedList<Location>();
                return;
            }
            if (isPinnedFrom[1]) {
                validMoves = new LinkedList<Location>();
                if (diagonalMoves.contains(upOneRightOne)) {
                    validMoves.add(upOneRightOne);
                }
                return;
            }
            if (isPinnedFrom[3]) {
                validMoves = new LinkedList<Location>();
                if (diagonalMoves.contains(upOneLeftOne)) {
                    validMoves.add(upOneLeftOne);
                }
                return;
            }
            validMoves.addAll(diagonalMoves);
        } else {
            if (isPinnedFrom[2] || isPinnedFrom[6]) {
                return;
            }
            if (isPinnedFrom[0] || isPinnedFrom[4]) {
                validMoves = new LinkedList<Location>();
                return;
            }
            if (isPinnedFrom[5]) {
                validMoves = new LinkedList<Location>();
                if (diagonalMoves.contains(downOneLeftOne)) {
                    validMoves.add(downOneLeftOne);
                }
                return;
            }
            if (isPinnedFrom[7]) {
                validMoves = new LinkedList<Location>();
                if (diagonalMoves.contains(downOneRightOne)) {
                    validMoves.add(downOneRightOne);
                }
                return;
            }
            validMoves.addAll(diagonalMoves);
        }
    }

    public boolean canMove() {
        return true;
    }

    public void move(Location location) {
        for (Location loc : validMoves) {
            if (loc.equals(location)) {
                board.getTileBasedOnLocation(this.location).setPiece(null);
                this.location = location;

                if (Math.abs(location.y - prevLoc.y) == 2) {
                    movedTwo = true;
                    prevLoc = location;
                }

                validMoves = new LinkedList<Location>();

                Piece atLocation = board.getTileBasedOnLocation(this.location).getPiece();
                Piece behindLocation;
                BoardTile behind = null;
                if (white) {
                    behind = board.getTileBasedOnLocation(new Location(location.x, location.y - 1));
                } else {
                    behind = board.getTileBasedOnLocation(new Location(location.x, location.y + 1));
                }
                behindLocation = behind.getPiece();
                if (atLocation != null) {
                    atLocation.isTaken = true;
                } else if (behindLocation != null) {
                    behindLocation.isTaken = true;
                    behind.setPiece(null);
                }

                board.getTileBasedOnLocation(this.location).setPiece(this);
                numMoves++;
                if (numMoves > 1) {
                    movedTwo = false;
                }

                return;
            }
        }
    }

    // Implement GUI elements here
    public void draw(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image image = t.getImage("white_pawn.png");
        g.drawImage(image, 100 * location.x + 25, 800 - (100 * location.y), null);
    }

    public Image draw() {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image image = t.getImage("white_pawn.png");
        return image;
    }

    public String toString() {
        String whiteOrBlack = "";
        if (white) {
            whiteOrBlack = "w";
        } else {
            whiteOrBlack = "b";
        }
        return "P" + whiteOrBlack + location.x + location.y;
    }
}
