package org.cis1200.chess;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args) {

        ChessGame game = new ChessGame();
        Board gameBoard = game.getBoard();

        JFrame frame = new JFrame("CHESS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(512, 540));
        frame.add(game);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);

        // makeShiftTestCase1();
        // makeShiftTestCase2();
        // testRookByItself();
        // testRookWithAnotherPiece();
        // testRookPinsRook();
        // testRookDoesNotPinAlly();
        // testRookPinsRookVertically();
        // testRookMoves();
        // testKnightByItself();
        // testKnightWithAllyPiece();
        // testKnightWithEnemyPiece();
        // testKnightNearEdgeOfBoard();
        // testBishopByItself();
        // testBishopPinsRook();
        // testRookPinsBishop();
        // testBishopPinsBishop();
        // testQueenByItself();
        // testQueenWithOtherPieces();
        // testPieceTakesPiece();
        // testPawnByItself();
        // testPawnEnPassant();
    }

    /** FOR TESTING PURPOSES BELOW */
    public static void makeShiftTestCase1() {
        Board board = new Board();
        King king = new King(7, 7, board);

        // Check that king does not move out of the board
        king.generateValidMoves();
        king.move(new Location(8, 8));
        System.out.println(king); // Expected: Kw77

        // Check that king does not move into an invalid space
        king.generateValidMoves();
        king.move(new Location(6, 5));
        System.out.println(king); // Expected: Kw77

        // Check that the king moves into a valid space
        king.generateValidMoves();
        king.move(new Location(6, 6));
        System.out.println(king); // Expected: Kw66

        // Check that board state is updated
        System.out.println(board.getTileBasedOnLocation(new Location(6, 6)).getPiece()); // Expected:
                                                                                         // Kw66

        // Check that original position's piece is null
        System.out.println(board.getTileBasedOnLocation(new Location(7, 7)).getPiece() == null); // Expected:
                                                                                                 // true
    }

    public static void makeShiftTestCase2() {
        Board board = new Board();
        King king = new King(7, 7, true, board);

        King king2 = new King(6, 7, false, board);

        king.generateValidMoves();
        /*
         * for(Location l : king.validMoves) { debugging purposes
         * System.out.println(l + " ");
         * }
         * System.out.println("\n");
         */

        king.move(new Location(6, 7));
        System.out.println(king); // Expected: Kw67

        // Check that board state is updated
        System.out.println(board.getTileBasedOnLocation(new Location(6, 7)).getPiece()); // Expected:
                                                                                         // Kw67

        // Check that original position's piece is null
        System.out.println(board.getTileBasedOnLocation(new Location(7, 7)).getPiece() == null); // Expected:
                                                                                                 // true
    }

    public static void testRookByItself() {
        Board board = new Board();
        Rook rook = new Rook(0, 0, true, board);

        rook.generateValidMoves();
        for (Location loc : rook.validMoves) {
            System.out.print(loc + " ");
        }
    }

    public static void testRookWithAnotherPiece() {
        Board board = new Board();
        Rook rook = new Rook(0, 0, true, board);
        Rook rook2 = new Rook(5, 0, true, board);

        Rook rook3 = new Rook(5, 5, false, board);

        rook.generateValidMoves();
        for (Location loc : rook.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.println("\n");

        rook2.generateValidMoves();

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.println("\n");
        }

        board.resetSightLines();
    }

    public static void testRookPinsRook() {
        Board board = new Board();
        Rook rook = new Rook(0, 0, true, board);
        Rook rook2 = new Rook(5, 0, false, board);

        King bKing = new King(7, 0, false, board);

        rook.generateValidMoves();

        if (rook2.isPinnedFrom[4]) {
            System.out.println("Success!");
        }

        rook2.generateValidMoves();
        for (Location loc : rook2.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.println("\n");

        bKing.generateValidMoves();
        bKing.move(new Location(7, 1));

        rook2.resetIsPinnedFrom();

        rook.generateValidMoves();
        if (!rook2.isPinnedFrom[4]) {
            System.out.println("Success!");
        }

        rook2.generateValidMoves();
        for (Location loc : rook2.validMoves) {
            System.out.print(loc + " ");
        }
    }

    public static void testRookDoesNotPinAlly() {
        Board board = new Board();
        Rook rook = new Rook(0, 0, true, board);
        Rook rook2 = new Rook(5, 0, true, board);

        King bKing = new King(7, 0, false, board);

        rook.generateValidMoves();

        if (!rook2.isPinnedFrom[4]) {
            System.out.println("Success!");
        }

        Board board2 = new Board();
        rook = new Rook(0, 0, true, board2);
        rook2 = new Rook(5, 0, false, board2);

        bKing = new King(7, 0, true, board2);

        rook.generateValidMoves();

        if (!rook2.isPinnedFrom[4]) {
            System.out.println("Success!");
        }

        Board board3 = new Board();
        rook = new Rook(0, 0, false, board3);
        rook2 = new Rook(5, 0, false, board3);

        bKing = new King(7, 0, true, board3);

        rook.generateValidMoves();

        if (!rook2.isPinnedFrom[4]) {
            System.out.println("Success!");
        }
    }

    public static void testRookPinsRookVertically() {
        Board board = new Board();
        Rook rook = new Rook(0, 0, true, board);
        Rook rook2 = new Rook(0, 5, false, board);

        King bKing = new King(0, 7, false, board);

        rook.generateValidMoves();
        if (!rook2.isPinnedFrom[4]) {
            System.out.println("Success!");
        }
        if (rook2.isPinnedFrom[6]) {
            System.out.println("Success!");
        }

        rook2.generateValidMoves();
        for (Location loc : rook2.validMoves) {
            System.out.print(loc + " ");
        }
    }

    public static void testRookMoves() {
        Board board = new Board();
        Rook rook = new Rook(0, 0, true, board);

        rook.generateValidMoves();
        rook.move(new Location(3, 3)); // shouldn't do anything
        System.out.println(rook); // Expected: Rw00

        // rook.generateValidMoves(); // should not matter whether this is commented out
        // or not
        rook.move(new Location(5, 0));
        System.out.println(rook); // Expected: Rw50
    }

    public static void testKnightByItself() {
        Board board = new Board();
        Knight knight = new Knight(4, 4, true, board);

        knight.generateValidMoves();
        for (Location loc : knight.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testKnightWithAllyPiece() {
        Board board = new Board();
        Knight knight = new Knight(4, 4, true, board);
        Knight knight2 = new Knight(2, 5, true, board);

        knight.generateValidMoves();
        for (Location loc : knight.validMoves) { // should be same as above except (2, 5) is no
                                                 // longer valid
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) { // should be the same result as the
                                                            // above test case
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testKnightWithEnemyPiece() {
        Board board = new Board();
        Knight knight = new Knight(4, 4, true, board);
        Knight knight2 = new Knight(2, 5, false, board);

        knight.generateValidMoves();
        for (Location loc : knight.validMoves) { // should be same as above except (2, 5) is now
                                                 // valid
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) { // should be the same result as the
                                                            // above test case
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testKnightNearEdgeOfBoard() {
        Board board = new Board();
        Knight knight = new Knight(0, 0, true, board);

        knight.generateValidMoves();
        for (Location loc : knight.validMoves) { // should only contain two elements
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) { // should only show two 'o's
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testBishopByItself() {
        Board board = new Board();
        Bishop bishop = new Bishop(4, 4, true, board);

        bishop.generateValidMoves();
        for (Location loc : bishop.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testBishopPinsRook() {
        Board board = new Board();
        Bishop bishop = new Bishop(0, 0, true, board);

        Rook rook = new Rook(4, 4, false, board);

        King king = new King(7, 7, false, board);

        bishop.generateValidMoves();
        rook.generateValidMoves();
        if (!rook.canMove()) {
            System.out.println("Success!");
        }
    }

    public static void testRookPinsBishop() {
        Board board = new Board();
        Bishop bishop = new Bishop(5, 0, false, board);

        Rook rook = new Rook(0, 0, true, board);

        King king = new King(7, 0, false, board);

        rook.generateValidMoves();
        bishop.generateValidMoves();
        if (!bishop.canMove()) {
            System.out.println("Success!");
        }

        System.out.println(bishop.isPinnedFrom[4]); // Expected: true

        for (Location loc : bishop.validMoves) { // should print nothing
            System.out.print(loc + " ");
        }
        System.out.print("\n");
    }

    public static void testBishopPinsBishop() {
        Board board = new Board();
        Bishop bishop = new Bishop(5, 0, true, board);

        Bishop bishop2 = new Bishop(3, 2, false, board);

        King king = new King(0, 5, false, board);

        bishop.generateValidMoves();
        System.out.println(bishop2.isPinnedFrom[7]); // Expected: true

        bishop2.generateValidMoves();
        for (Location loc : bishop2.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");
    }

    public static void testQueenByItself() {
        Board board = new Board();
        Queen queen = new Queen(4, 4, true, board);

        queen.generateValidMoves();
        for (Location loc : queen.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testQueenWithOtherPieces() {
        Board board = new Board();
        Queen queen = new Queen(4, 4, true, board);

        Knight knight = new Knight(2, 2, true, board);

        Bishop bishop = new Bishop(4, 1, true, board);

        Rook rook = new Rook(1, 4, true, board);

        queen.generateValidMoves();
        for (Location loc : queen.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static void testPieceTakesPiece() {
        Board board = new Board();
        Knight knight = new Knight(0, 0, true, board);
        Bishop enemyBishop = new Bishop(2, 1, false, board);

        knight.generateValidMoves();
        for (Location loc : knight.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        knight.move(new Location(2, 1));
        knight.generateValidMoves();
        for (Location loc : knight.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.print("\n");

        System.out.println("Bishop was taken!: " + enemyBishop.isTaken); // Expected: true

        System.out.println(board.getTileBasedOnLocation(new Location(2, 1)).getPiece()); // Expected:
                                                                                         // Nw21

        System.out.println(board.getTileBasedOnLocation(new Location(0, 0)).getPiece()); // Expected:
                                                                                         // null
    }

    public static void testPawnByItself() {
        Board board = new Board();
        Pawn pawn = new Pawn(1, 1, true, board);

        pawn.generateValidMoves();
        for (Location loc : pawn.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.println("\n");

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }

        board.resetSightLines();

        pawn.move(new Location(1, 3));
        pawn.generateValidMoves();
        for (Location loc : pawn.validMoves) {
            System.out.print(loc + " ");
        }
        System.out.println("\n");

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (board.getBoard()[i][j].isContainedInWhiteSight()) {
                    System.out.print("o");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }

        System.out.println("Pawn moved two!: " + pawn.movedTwo); // Expected: true
    }

    public static void testPawnEnPassant() {
        Board board = new Board();
        Pawn pawn = new Pawn(0, 4, true, board);

        Pawn enemyPawn = new Pawn(1, 6, false, board);

        pawn.generateValidMoves();
        enemyPawn.generateValidMoves();

        enemyPawn.move(new Location(1, 4));
        System.out.println("Pawn moved two!: " + enemyPawn.movedTwo); // Expected: true

        enemyPawn.generateValidMoves();
        pawn.generateValidMoves();

        pawn.move(new Location(1, 5));

        System.out.println(pawn); // Expected: Pw15

        System.out.println(board.getTileBasedOnLocation(new Location(1, 4)).getPiece() == null); // Expected:
                                                                                                 // true

        System.out.println("Enemy pawn was taken!: " + enemyPawn.isTaken); // Expected: true
    }
}
