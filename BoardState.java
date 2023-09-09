package org.cis1200.chess;
import java.util.ArrayList;
import java.util.List;

public class BoardState {

    private Board localBoard;
    private ArrayList<BoardState> nextStates;
    private ArrayList<Move> associatedMoves;
    private Move optimalMove;

    protected boolean whiteKingInCheck;
    protected boolean blackKingInCheck;
    protected List<Piece> whitePiecesGivingCheck;
    protected List<Piece> blackPiecesGivingCheck;

    private King whiteKing;
    private King blackKing;

    private List<Piece> whitePieces;
    private List<Piece> blackPieces;

    public BoardState(Board board) {
        localBoard = board;

        whitePieces = new ArrayList<Piece>();
        blackPieces = new ArrayList<Piece>();

        for (BoardTile[] row : localBoard.getBoard()) {
            for (BoardTile tile : row) {
                Piece currPiece = tile.getPiece();
                if (currPiece != null) {
                    if (currPiece.white) {
                        whitePieces.add(currPiece);
                    } else {
                        blackPieces.add(currPiece);
                    }
                }
                if (currPiece != null && currPiece.toString().charAt(0) == 'K') {
                    if (currPiece.white) {
                        whiteKing = (King) currPiece;
                    } else {
                        blackKing = (King) currPiece;
                    }
                }
            }
        }

        nextStates = new ArrayList<BoardState>();
        associatedMoves = new ArrayList<Move>();

        whitePiecesGivingCheck = new ArrayList<Piece>();
        blackPiecesGivingCheck = new ArrayList<Piece>();
    }

    private Board getBoardObj() {
        return localBoard;
    }

    public ArrayList<BoardState> getNextStates() {
        return nextStates;
    }

    public ArrayList<Move> getAssociatedMoves() {
        return associatedMoves;
    }

    public void setOptimalMove(Move move) {
        optimalMove = move;
    }

    public Move getOptimalMove() {
        return optimalMove;
    }

    public void setWhiteKing(King king) {
        whiteKing = king;
    }

    public void setBlackKing(King king) {
        blackKing = king;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public boolean checkGameOver() {
        /*if (blackKingInCheck) {
            for (BoardTile[] row : localBoard.getBoard()) {
                for (BoardTile tile : row) {

                }
            }

        } else if (whiteKingInCheck) {

        }*/
        return false;
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

        BoardTile[][] boardTiles = localBoard.getBoard();

        for (int i = 0; i < boardTiles.length; i++) {
            for (int j = 0; j < boardTiles[i].length; j++) {
                if (boardTiles[i][j].getPiece() != null) {
                    Piece p = boardTiles[i][j].getPiece();
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
                            value = 15.0;
                            break;
                        case 'K':
                            value = 5.0;
                            break;
                        default:
                            value = 0.0;
                            break;
                    }
                    /*if (p.white && boardTiles[i][j].isContainedInWhiteSight()
                    || (!p.white && boardTiles[i][j].isContainedInBlackSight())) {
                        value *= 2;
                    }*/
                    if (!p.white) {
                        value *= -1;
                    }
                    /*if (value > 0 && whiteKingInCheck
                    || (value < 0 && blackKingInCheck)) {
                        value /= 2;
                    }*/
                    netEval += (value * evals[i][j]);
                }
            }
        }

        return netEval;
    }

    public void simulateMove(/*Piece piece,*/ Location origin, Location destination) {
        BoardState nextState = makeDeepCopy();

        BoardTile[][] originalBoard = localBoard.getBoard();
        BoardTile[][] copiedBoard = nextState.localBoard.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (originalBoard[i][j].getPiece() == null && copiedBoard[i][j].getPiece() == null) {
                    // System.out.println("tile successfully copied at i: " + i + ", j: " + j);
                } else if (originalBoard[i][j].getPiece() != null) {
                    if (copiedBoard[i][j].getPiece().getClass().getName().equals(originalBoard[i][j].getPiece().getClass().getName())) {
                        // System.out.println("Piece types match and in same location at i: " + i + ", j: " + j);
                    } else {
                        // System.out.println("Piece differs at i: " + i + ", " + j);
                    }
                } else {
                    // System.out.println("tile differs at i: " + i + ", j: " + j);
                }
            }
        }

        /*if (nextState == null) {
            System.out.println("nextState is null!");
        } else if (nextState.getBoardObj() == null) {
            System.out.println("nextState Board is null!");
        } else if (nextState.getBoardObj().getTileBasedOnLocation(origin) == null) {
            System.out.println("nextState Board Tile is null!");
        }*/

        Piece original = localBoard.getTileBasedOnLocation(origin).getPiece();
        if (original == null) {
            // System.out.println("Piece never existed here to begin with!");
        }

        Piece piece = nextState.getBoardObj().getTileBasedOnLocation(origin).getPiece();

        // if (piece.validMoves.contains(destination)) {
        piece.move(destination);
        Move move = new Move(piece, origin.x, origin.y, destination.x, destination.y);
        associatedMoves.add(move);

        nextState.blackKingInCheck = false;
        nextState.whiteKingInCheck = false;

        if (piece.white) {
            for (Piece p : nextState.whitePieces) {
                p.generateValidMoves();
                if (p.givingCheck) {
                    nextState.whitePiecesGivingCheck.add(p);
                    nextState.blackKingInCheck = true;
                }
            }
            for (Piece p : nextState.blackPieces) {
                p.generateValidMoves();
            }
        } else {
            for (Piece p : nextState.blackPieces) {
                p.generateValidMoves();
                if (p.givingCheck) {
                    nextState.blackPiecesGivingCheck.add(p);
                    nextState.whiteKingInCheck = true;
                }
            }
            for (Piece p : nextState.whitePieces) {
                p.generateValidMoves();
            }
        }

        /*if (piece.white) {
            // nextState.whiteKingInCheck = false;
            // nextState.blackPiecesGivingCheck = new ArrayList<Piece>();
            for (BoardTile[] row : nextState.getBoardObj().getBoard()) {
                for (BoardTile tile : row) {
                    Piece currPiece = tile.getPiece();
                    if (currPiece != null) {
                        if (currPiece.toString().charAt(0) == 'K') {
                            if (((King) currPiece).inCheck()) {
                                nextState.blackKingInCheck = true;
                            }
                        } else {
                            if (currPiece.givingCheck) {
                                nextState.whitePiecesGivingCheck.add(currPiece);
                            }
                        }
                    }
                }
            }
        } else {
            for (BoardTile[] row : nextState.getBoardObj().getBoard()) {
                for (BoardTile tile : row) {
                    Piece currPiece = tile.getPiece();
                    if (currPiece != null) {
                        if (currPiece.toString().charAt(0) == 'K') {
                            if (((King) currPiece).inCheck()) {
                                nextState.whiteKingInCheck = true;
                            }
                        } else {
                            if (currPiece.givingCheck) {
                                nextState.blackPiecesGivingCheck.add(currPiece);
                            }
                        }
                    }
                }
            }
        }*/
        // }


        /*Location end = new Location(move.getEndX(), move.getEndY());
        Location start = new Location(move.getStartX(), move.getStartY());

        BoardTile destination = nextState.getBoardObj().getTileBasedOnLocation(end);
        BoardTile origin = nextState.getBoardObj().getTileBasedOnLocation(start);*/

        // INVESTIGATE HOW TO DO THIS WITHOUT MUTATING ACTUAL GAME STATE
        /*Piece taken = destination.getPiece();
        if (taken != null) {
            taken.isTaken = true;
        }*/

        /*destination.setPiece(origin.getPiece());

        origin.setPiece(null);*/

        nextStates.add(nextState);
    }

    private BoardState makeDeepCopy() {
        Board boardCopy = new Board();
        BoardTile[][] boardTiles = localBoard.getBoard();
        for (int i = 0; i < boardTiles.length; i++) {
            for (int j = 0; j < boardTiles[i].length; j++) {
                // Deep copy the pieces on the board
                Piece copyOfPiece;
                Piece originalPiece = boardTiles[i][j].getPiece();
                if (originalPiece != null) {
                    String typeOfPiece = originalPiece.getClass().getName();
                    if (typeOfPiece.contains("Pawn")) {
                        copyOfPiece = new Pawn();
                        Pawn origPiece = (Pawn) originalPiece;
                        ((Pawn) copyOfPiece).movedTwo = origPiece.movedTwo;
                        ((Pawn) copyOfPiece).prevLoc = new Location(origPiece.prevLoc.x, origPiece.prevLoc.y);
                    } else if (typeOfPiece.contains("Knight")) {
                        copyOfPiece = new Knight();
                    } else if (typeOfPiece.contains("Bishop")) {
                        copyOfPiece = new Bishop();
                        Bishop origPiece = (Bishop) originalPiece;
                        for (Location loc : origPiece.movesToBlockCheck) {
                            ((Bishop) copyOfPiece).movesToBlockCheck.add(loc);
                        }
                    } else if (typeOfPiece.contains("Rook")) {
                        copyOfPiece = new Rook();
                        Rook origPiece = (Rook) originalPiece;
                        for (Location loc : origPiece.movesToBlockCheck) {
                            ((Rook) copyOfPiece).movesToBlockCheck.add(loc);
                        }
                    } else if (typeOfPiece.contains("Queen")) {
                        copyOfPiece = new Queen();
                        Queen origPiece = (Queen) originalPiece;
                        for (Location loc : origPiece.movesToBlockCheck) {
                            ((Queen) copyOfPiece).movesToBlockCheck.add(loc);
                        }
                    } else if (typeOfPiece.contains("King")) {
                        copyOfPiece = new King();
                        King origPiece = (King) originalPiece;
                        ((King) copyOfPiece).setInCheck(origPiece.inCheck());
                    /*if (origPiece.white) {
                        whiteKing = origPiece;
                    } else {
                        blackKing = origPiece;
                    }*/
                    } else {
                        copyOfPiece = null;
                    }
                    copyOfPiece.white = originalPiece.white;
                    copyOfPiece.location = new Location(originalPiece.location.x, originalPiece.location.y);
                    for (Location loc : originalPiece.validMoves) {
                        copyOfPiece.validMoves.add(loc);
                    }
                    copyOfPiece.board = boardCopy; // We are setting the Piece's Board field to point to the copied Board
                    copyOfPiece.isProtected = originalPiece.isProtected;
                    copyOfPiece.numMoves = originalPiece.numMoves;
                    copyOfPiece.isPinned = originalPiece.isPinned;
                    copyOfPiece.isPinnedFrom = new boolean[8];
                    for (int k = 0; k < 8; k++) {
                        copyOfPiece.isPinnedFrom[k] = originalPiece.isPinnedFrom[k];
                    }
                    copyOfPiece.isTaken = originalPiece.isTaken;
                    copyOfPiece.givingCheck = originalPiece.givingCheck;
                    boardCopy.getBoard()[i][j] =
                            new BoardTile(copyOfPiece, j, boardTiles.length - i - 1);
                }
            }
        }

        BoardState copy = new BoardState(boardCopy);
        copy.whiteKingInCheck = this.whiteKingInCheck;
        copy.blackKingInCheck = this.blackKingInCheck;

        return copy;
    }
}
