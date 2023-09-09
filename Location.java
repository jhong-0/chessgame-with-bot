package org.cis1200.chess;

public class Location {

    public int x;
    public int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location toTheRight(int numTiles) {
        return new Location(x + numTiles, y);
    }

    public Location toTheLeft(int numTiles) {
        return new Location(x - numTiles, y);
    }

    public Location above(int numTiles) {
        return new Location(x, y + numTiles);
    }

    public Location below(int numTiles) {
        return new Location(x, y - numTiles);
    }

    public Location diagonalNE(int numTiles) {
        return new Location(x + numTiles, y + numTiles);
    }

    public Location diagonalSE(int numTiles) {
        return new Location(x + numTiles, y - numTiles);
    }

    public Location diagonalSW(int numTiles) {
        return new Location(x - numTiles, y - numTiles);
    }

    public Location diagonalNW(int numTiles) {
        return new Location(x - numTiles, y + numTiles);
    }

    public boolean isOnBoard(int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // @Overwrite
    /*
     * public boolean equals(Location loc) {
     * return this.x == loc.x && this.y == loc.y;
     * }
     */

    public boolean equals(Object o) {
        return this.x == ((Location) o).x && this.y == ((Location) o).y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
