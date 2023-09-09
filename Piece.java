package org.cis1200.chess;

import java.util.*;
import java.awt.Graphics;

public abstract class Piece {

    protected boolean white;
    protected Location location;
    protected List<Location> validMoves;
    protected Board board;
    protected boolean isProtected;
    protected int numMoves;
    protected boolean isPinned;
    protected boolean[] isPinnedFrom;
    protected boolean isTaken;
    protected boolean givingCheck;

    public Piece() {
        white = true;
        location = null;
        validMoves = new LinkedList<Location>();
        board = null;
        isProtected = false;
        numMoves = 0;
        isPinned = false;
        isPinnedFrom = new boolean[8];
        isTaken = false;
        givingCheck = false;
    }

    public Piece(int x, int y) {
        white = true;
        location = new Location(x, y);
        validMoves = new LinkedList<Location>();
        board = null;
        isProtected = false;
        numMoves = 0;
        isPinned = false;
        isPinnedFrom = new boolean[8];
        isTaken = false;
        givingCheck = false;
    }

    public Piece(int x, int y, Board board) {
        white = true;
        location = new Location(x, y);
        validMoves = new LinkedList<Location>();
        this.board = board;

        BoardTile boardLoc = this.board.getTileBasedOnLocation(location);
        boardLoc.setPiece(this);

        isProtected = false;
        numMoves = 0;
        isPinned = false;
        isPinnedFrom = new boolean[8];
        isTaken = false;
        givingCheck = false;
    }

    public Piece(int x, int y, boolean white) {
        this.white = white;
        location = new Location(x, y);
        validMoves = new LinkedList<Location>();
        board = null;
        isProtected = false;
        numMoves = 0;
        isPinned = false;
        isPinnedFrom = new boolean[8];
        isTaken = false;
        givingCheck = false;
    }

    public Piece(int x, int y, boolean white, Board board) {
        this.white = white;
        location = new Location(x, y);
        validMoves = new LinkedList<Location>();
        this.board = board;

        BoardTile boardLoc = this.board.getTileBasedOnLocation(location);
        boardLoc.setPiece(this);

        isProtected = false;
        numMoves = 0;
        isPinned = false;
        isPinnedFrom = new boolean[8];
        isTaken = false;
        givingCheck = false;
    }

    public boolean isWhite() {
        return white;
    }

    public Location getLocation() {
        return this.location;
    }

    public List<Location> getValidMoves() {
        return this.validMoves;
    }

    public void resetIsPinnedFrom() {
        /*
         * for (int i = 0; i < isPinnedFrom.length; i++) {
         * isPinnedFrom[i] = false;
         * }
         */
        isPinnedFrom = new boolean[8];
    }

    public abstract void generateValidMoves();

    public abstract boolean canMove();

    public abstract void move(Location location);

    public abstract void draw(Graphics g);

    public abstract String toString();
}
