package org.cis1200.chess;

// import java.util.*;
public class BoardTile {

    private Piece piece;
    private Location location;

    private boolean containedInWhiteSight;
    private boolean containedInBlackSight;

    public BoardTile() {
        piece = null;
        location = null;
        containedInWhiteSight = false;
        containedInBlackSight = false;
    }

    public BoardTile(int x, int y) {
        piece = null;
        location = new Location(x, y);
        containedInWhiteSight = false;
        containedInBlackSight = false;
    }

    public BoardTile(Piece piece, int x, int y) {
        this.piece = piece;
        location = new Location(x, y);
        containedInWhiteSight = false;
        containedInBlackSight = false;
    }

    public int getX() {
        return location.x;
    }

    public int getY() {
        return location.y;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Location getLocation() {
        return location;
    }

    public void toggleContainedInWhiteSight() {
        if (containedInWhiteSight) {
            containedInWhiteSight = false;
        } else {
            containedInWhiteSight = true;
        }
    }

    public void setContainedInWhiteSight(boolean containedInWhiteSight) {
        this.containedInWhiteSight = containedInWhiteSight;
    }

    public void toggleContainedInBlackSight() {
        if (containedInBlackSight) {
            containedInBlackSight = false;
        } else {
            containedInBlackSight = true;
        }
    }

    public void setContainedInBlackSight(boolean containedInBlackSight) {
        this.containedInBlackSight = containedInBlackSight;
    }

    public boolean isContainedInWhiteSight() {
        return containedInWhiteSight;
    }

    public boolean isContainedInBlackSight() {
        return containedInBlackSight;
    }
}
