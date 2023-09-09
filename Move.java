package org.cis1200.chess;

public class Move {

    private Piece piece;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(Piece piece, int startX, int startY, int endX, int endY) {
        this.piece = piece;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
