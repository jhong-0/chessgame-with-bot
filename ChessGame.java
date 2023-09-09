package org.cis1200.chess;
/* Handles overarching Chess rules and gameplay */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.awt.event.MouseListener;
import java.awt.geom.*;

public class ChessGame extends JPanel implements MouseListener {

    private boolean cpuOpponent;

    private Board board;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;

    private boolean castled;

    private boolean whiteTurn;

    private boolean gameOver;

    private Piece whiteSelected;
    private Piece blackSelected;

    private boolean moveMode;

    private List<Location> validMovesWhenWhiteInCheck;
    private List<Location> validMovesWhenBlackInCheck;

    private List<Piece> whitePiecesGivingCheck;
    private List<Piece> blackPiecesGivingCheck;

    private List<Piece> validWhitePiecesWhenInCheck;
    private List<Piece> validBlackPiecesWhenInCheck;

    private King whiteKing;
    private King blackKing;

    private Rook[] whiteRooks;
    private Rook[] blackRooks;

    public ChessGame() {
        board = new Board();
        whitePieces = new LinkedList<Piece>();

        validMovesWhenWhiteInCheck = new LinkedList<Location>();
        validMovesWhenBlackInCheck = new LinkedList<Location>();

        whitePiecesGivingCheck = new LinkedList<Piece>();
        blackPiecesGivingCheck = new LinkedList<Piece>();

        validWhitePiecesWhenInCheck = new LinkedList<Piece>();
        validBlackPiecesWhenInCheck = new LinkedList<Piece>();

        whiteRooks = new Rook[2];
        blackRooks = new Rook[2];

        whiteTurn = true;

        for (int i = 0; i < board.getBoard().length; i++) {
            whitePieces.add(new Pawn(i, 1, true, board));
        }
        whitePieces.add(whiteKing = new King(4, 0, true, board));
        whitePieces.add(new Queen(3, 0, true, board));
        whitePieces.add(new Bishop(2, 0, true, board));
        whitePieces.add(new Bishop(5, 0, true, board));
        whitePieces.add(new Knight(1, 0, true, board));
        whitePieces.add(new Knight(6, 0, true, board));
        Rook r1;
        Rook r2;
        whitePieces.add(r1 = new Rook(0, 0, true, board));
        whitePieces.add(r2 = new Rook(7, 0, true, board));

        whiteRooks[0] = r1;
        whiteRooks[1] = r2;

        generateAllWhiteMoves();

        blackPieces = new LinkedList<Piece>();

        for (int i = 0; i < board.getBoard().length; i++) {
            blackPieces.add(new Pawn(i, 6, false, board));
        }

        blackPieces.add(blackKing = new King(4, 7, false, board));
        blackPieces.add(new Queen(3, 7, false, board));

        // Testing getClass() method:
        BoardTile bQueenTile = board.getTileBasedOnLocation(new Location(3, 7));
        System.out.println(bQueenTile.getPiece().getClass().getName());

        blackPieces.add(new Bishop(2, 7, false, board));
        blackPieces.add(new Bishop(5, 7, false, board));
        blackPieces.add(new Knight(1, 7, false, board));
        blackPieces.add(new Knight(6, 7, false, board));
        Rook r3;
        Rook r4;
        blackPieces.add(r3 = new Rook(0, 7, false, board));
        blackPieces.add(r4 = new Rook(7, 7, false, board));

        blackRooks[0] = r3;
        blackRooks[1] = r4;

        generateAllBlackMoves();

        addMouseListener(this);

        JOptionPane.showMessageDialog(
                this, "Welcome to chess! \n"
                        + "Instructions: \n"
                        + "Click pieces to see valid moves for them. White goes first. Turns alternate after every move.\n"
                        +
                        "Checkmate occurs when the enemy king is under attack from an ally piece and the opponent has no "
                        +
                        "more valid moves to make.\nAt this point, you win! Stalemate (draw) occurs when there are no moves "
                        +
                        "that can be made but the enemy king is not in \"check.\"\nNeither player wins in this case. \n"
                        + "That should be all! Enjoy!"
        );

        String[] options = new String[]{"Player vs. Player", "Player vs. Computer"};
        int response = JOptionPane.showOptionDialog(
                this, "Please select your game mode: ", "GAME MODE SELECTION",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]
        );

        if (response == 0) {
            cpuOpponent = false;
        } else {
            cpuOpponent = true;
        }

        System.out.println("Playing against computer is: " + cpuOpponent);
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteCheckmate() {
        return false;
    }

    public boolean isBlackCheckmate() {
        return false;
    }

    public void paint(Graphics g) {
        BoardTile[][] tiles = board.getBoard();
        boolean white;
        for (int i = 0; i < tiles.length; i++) {
            if (i % 2 == 0) {
                white = true;
            } else {
                white = false;
            }
            for (int j = 0; j < tiles[i].length; j++) {
                if (white) {
                    if (tiles[i][j].getPiece() != null
                            && tiles[i][j].getPiece().toString().charAt(0) == 'K'
                            && ((King) tiles[i][j].getPiece()).inCheck()) {
                        g.setColor(new Color(255, 100, 100));
                    } else {
                        g.setColor(new Color(255, 255, 200));
                    }
                    white = false;
                } else {
                    if (tiles[i][j].getPiece() != null
                            && tiles[i][j].getPiece().toString().charAt(0) == 'K'
                            && ((King) tiles[i][j].getPiece()).inCheck()) {
                        g.setColor(new Color(255, 100, 100));
                    } else {
                        g.setColor(new Color(115, 175, 115));
                    }
                    white = true;
                }
                g.fillRect(64 * j, 64 * i, 64, 64);
            }
        }

        /*
         * Pawn p = new Pawn(0, 1, true, board);
         * BufferedImage img = null;
         * try {
         * //File f = new File("white_pawn.png");
         * //System.out.println("file retrieval successful!");
         * img = ImageIO.read(getClass().getResource("White_pawn.png"));
         * } catch(IOException e) {
         * e.printStackTrace();
         * }
         * Graphics2D g2 = (Graphics2D) g;
         * g2.drawImage(img, (p.location.x * 64), 512 - (64 * (p.location.y + 1)),
         * this);
         */

        BufferedImage img = null;
        for (Piece p : whitePieces) {
            if (!p.isTaken) {
                try {
                    switch (p.toString().charAt(0)) {
                        case 'K':
                            img = ImageIO.read(new File("files/White_king.png"));
                            break;
                        case 'Q':
                            img = ImageIO.read(new File("files/White_queen.png"));
                            break;
                        case 'N':
                            img = ImageIO.read(new File("files/White_knight.png"));
                            break;
                        case 'B':
                            img = ImageIO.read(new File("files/White_bishop.png"));
                            break;
                        case 'R':
                            img = ImageIO.read(new File("files/white_rook.png"));
                            break;
                        case 'P':
                            img = ImageIO.read(new File("files/White_pawn.png"));
                            break;
                        default:
                            img = null;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.drawImage(img, 64 * p.location.x, 512 - (64 * (p.location.y + 1)), this);
            }
        }

        for (Piece p : blackPieces) {
            if (!p.isTaken) {
                try {
                    switch (p.toString().charAt(0)) {
                        case 'K':
                            img = ImageIO.read(new File("files/Black_king.png"));
                            break;
                        case 'Q':
                            img = ImageIO.read(new File("files/Black_queen.png"));
                            break;
                        case 'N':
                            img = ImageIO.read(new File("files/Black_knight.png"));
                            break;
                        case 'B':
                            img = ImageIO.read(new File("files/Black_bishop.png"));
                            break;
                        case 'R':
                            img = ImageIO.read(new File("files/Black_rook.png"));
                            break;
                        case 'P':
                            img = ImageIO.read(new File("files/Black_pawn.png"));
                            break;
                        default:
                            img = null;
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g.drawImage(img, 64 * p.location.x, 512 - (64 * (p.location.y + 1)), this);
            }
        }

        if (moveMode) {
            if (whiteSelected != null) {
                for (Location loc : whiteSelected.validMoves) {
                    if (whiteSelected.toString().charAt(0) == 'K' ||
                            (!whiteKing.inCheck() || validMovesWhenWhiteInCheck.contains(loc))) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(new Color(40, 40, 40, 40));

                        int r = 10;
                        int x = (loc.x * 64) + (32 - r);
                        int y = ((7 - loc.y) * 64) + (32 - r);
                        g2.fill(new Ellipse2D.Double(x, y, 2 * r, 2 * r));
                    }
                }
            }
            if (blackSelected != null) {
                for (Location loc : blackSelected.validMoves) {
                    if (blackSelected.toString().charAt(0) == 'K' ||
                            (!blackKing.inCheck() || validMovesWhenBlackInCheck.contains(loc))) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(new Color(40, 40, 40, 40));

                        int r = 10;
                        int x = (loc.x * 64) + (32 - r);
                        int y = ((7 - loc.y) * 64) + (32 - r);
                        g2.fill(new Ellipse2D.Double(x, y, 2 * r, 2 * r));
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*if (moveMode && cpuOpponent) {
            repaint();
        }*/
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        BoardTile selectedTile = board.getBoard()[mouseY / 64][mouseX / 64];
        Piece selectedPiece = selectedTile.getPiece();
        boolean moved = false;
        if (moveMode) {
            if (whiteTurn) {
                Location loc = selectedTile.getLocation();
                if (whiteKing.checkCanCastle(whiteRooks[0]) /* && selectedPiece != null && selectedPiece == whiteKing*/
                        && loc.equals(
                        new Location(whiteKing.location.x - 2, whiteKing.location.y)
                )) {
                    // repaint();
                    Location oneRight = new Location(
                            whiteKing.location.x - 1, whiteKing.location.y
                    );
                    whiteKing.move(new Location(whiteKing.location.x - 2, whiteKing.location.y));
                    whiteRooks[0].move(oneRight);
                    whiteTurn = false;
                    castled = true;
                    moved = true;
                }
                if (whiteKing.checkCanCastle(whiteRooks[1]) /*&& selectedPiece != null && selectedPiece == whiteKing*/
                        && loc.equals(
                        new Location(whiteKing.location.x + 2, whiteKing.location.y)
                )) {
                    // repaint();
                    Location oneLeft = new Location(whiteKing.location.x + 1, whiteKing.location.y);
                    whiteKing.move(new Location(whiteKing.location.x + 2, whiteKing.location.y));
                    whiteRooks[1].move(oneLeft);
                    whiteTurn = false;
                    castled = true;
                    moved = true;
                }
                for (Location l : whiteSelected.validMoves) {
                    if (l.equals(loc) &&
                            (!whiteKing.inCheck() ||
                                    (whiteSelected.toString().charAt(0) == 'K'
                                            || validMovesWhenWhiteInCheck.contains(loc)))) {
                        // System.out.println(whiteSelected + " moved!");
                        if (!castled) {
                            whiteSelected.move(loc);
                            // castled = false;
                            moved = true;
                        }

                        Piece converted = null;
                        Piece toConvert = null;

                        for (Piece p : whitePieces) {
                            for (int i = 0; i < board.getBoard()[0].length; i++) {
                                if (!p.isTaken && p.toString().charAt(0) == 'P'
                                        && board.getBoard()[0][i].getPiece() != null
                                        && board.getBoard()[0][i].getPiece().equals(p)) {
                                    toConvert = p;
                                    Object[] options = {
                                            "QUEEN",
                                            "ROOK",
                                            "BISHOP",
                                            "KNIGHT"
                                    };
                                    int choice = JOptionPane.showOptionDialog(
                                            this,
                                            "Choose piece to promote to:",
                                            "Pawn Promotion!",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            options,
                                            null
                                    );
                                    switch (choice) {
                                        case 0:
                                            converted = new Queen(
                                                    p.location.x, p.location.y, true, board
                                            );
                                            // p.isTaken = true;
                                            break;
                                        case 1:
                                            converted = new Rook(
                                                    p.location.x, p.location.y, true, board
                                            );
                                            break;
                                        case 2:
                                            converted = new Bishop(
                                                    p.location.x, p.location.y, true, board
                                            );
                                            break;
                                        case 3:
                                            converted = new Knight(
                                                    p.location.x, p.location.y, true, board
                                            );
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                }
                            }
                        }

                        if (converted != null && toConvert != null) {
                            whitePieces.remove(toConvert);
                            board.getTileBasedOnLocation(toConvert.location).setPiece(converted);
                            whitePieces.add(converted);
                        }

                        for (Piece p : blackPieces) {
                            if (p.toString().charAt(0) == 'P' && !p.isTaken) {
                                ((Pawn) p).movedTwo = false;
                            }
                        }

                        validMovesWhenBlackInCheck = new LinkedList<Location>();
                        whitePiecesGivingCheck = new LinkedList<Piece>();

                        if (whiteKing.inCheck()) {
                            whiteKing.setInCheck(false);
                        }

                        whiteSelected = null;
                        whiteTurn = false;

                        for (Piece p : blackPieces) {
                            p.isPinnedFrom = new boolean[8];
                        }

                        board.resetSightLines();
                        generateAllWhiteMoves();

                        for (Piece p : whitePieces) {
                            if (!p.isTaken && p.givingCheck) {
                                // System.out.println(p + " is giving check!");
                                whitePiecesGivingCheck.add(p);
                            }
                        }

                        generateAllBlackMoves();

                        if (whiteMatesBlack()) {
                            // System.out.println("White checkmates black!");
                            repaint();
                            JOptionPane.showMessageDialog(this, "White wins by checkmate!");
                        }
                        if (isStalemate()) {
                            repaint();
                            JOptionPane.showMessageDialog(this, "Draw - Stalemate");
                        }
                    }
                }
                moveMode = false;
                castled = false;

                // CPU makes its move in response
                if (cpuOpponent && moved) {
                    /*try {
                        repaint();
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }*/

                    // Compute optimal move (using minimax and alpha-beta pruning)
                    BoardState currState = new BoardState(board);

                    currState.blackKingInCheck = isBlackKingInCheck();
                    currState.whiteKingInCheck = isWhiteKingInCheck();
                    currState.blackPiecesGivingCheck = blackPiecesGivingCheck;
                    currState.whitePiecesGivingCheck = whitePiecesGivingCheck;

                    minimax(currState, 3, true);
                    Move optimalMove = currState.getOptimalMove();


                    // Retrieve optimal piece and optimal move

                    // Piece piece = optimalMove.getPiece();

                    Piece piece;
                    int startX = optimalMove.getStartX();
                    int startY = optimalMove.getStartY();
                    piece = board.getTileBasedOnLocation(new Location(startX, startY)).getPiece();

                    // System.out.println(piece.getClass() + ", " + piece.location.x + ", " + piece.location.y);
                    Location optimalDestination = new Location(optimalMove.getEndX(), optimalMove.getEndY());

                    // Perform move
                    piece.move(optimalDestination);

                    validMovesWhenBlackInCheck = new LinkedList<Location>();
                    whitePiecesGivingCheck = new LinkedList<Piece>();

                    if (blackKing.inCheck()) {
                        blackKing.setInCheck(false);
                    }

                    for (Piece p : whitePieces) {
                        p.isPinnedFrom = new boolean[8];
                    }

                    board.resetSightLines();
                    generateAllBlackMoves();

                    for (Piece p : blackPieces) {
                        if (!p.isTaken && p.givingCheck) {
                            // System.out.println(p + " is giving check!");
                            blackPiecesGivingCheck.add(p);
                        }
                    }

                    // Check if computer wins
                    if (blackMatesWhite()) {
                        repaint();
                        JOptionPane.showMessageDialog(this, "Black wins by checkmate!");
                    }
                    if (isStalemate()) {
                        repaint();
                        JOptionPane.showMessageDialog(this, "Draw - Stalemate");
                    }

                    // Keep it white turn
                    whiteTurn = true;
                    generateAllWhiteMoves();
                }
                moved = false;
            } else {
                // generateAllWhiteMoves();
                // generateAllBlackMoves();
                // System.out.println("Clicked during move mode!");
                Location loc = selectedTile.getLocation();
                if (blackKing.checkCanCastle(blackRooks[0]) /*&& selectedPiece != null && selectedPiece == blackKing*/
                        && loc.equals(
                        new Location(blackKing.location.x - 2, blackKing.location.y)
                )) {
                    // repaint();
                    Location oneRight = new Location(
                            blackKing.location.x - 1, blackKing.location.y
                    );
                    blackKing.move(new Location(blackKing.location.x - 2, blackKing.location.y));
                    blackRooks[0].move(oneRight);
                    whiteTurn = true;
                    castled = true;
                }
                if (blackKing.checkCanCastle(blackRooks[1]) /*&& selectedPiece != null && selectedPiece == blackKing*/
                        && loc.equals(
                        new Location(blackKing.location.x + 2, blackKing.location.y)
                )) {
                    // repaint();
                    Location oneLeft = new Location(blackKing.location.x + 1, blackKing.location.y);
                    blackKing.move(new Location(blackKing.location.x + 2, blackKing.location.y));
                    blackRooks[1].move(oneLeft);
                    System.out.println("here!");
                    whiteTurn = true;
                    castled = true;
                }
                for (Location l : blackSelected.validMoves) {
                    if (l.equals(loc) &&
                            (!blackKing.inCheck() ||
                                    (blackSelected.toString().charAt(0) == 'K'
                                            || validMovesWhenBlackInCheck.contains(loc)))) {
                        // System.out.println(blackSelected + " moved!"){
                        if (!castled) {
                            blackSelected.move(loc);
                            // castled = false;
                        }

                        Piece converted = null;
                        Piece toConvert = null;

                        for (Piece p : blackPieces) {
                            for (int i = 0; i < board.getBoard()[7].length; i++) {
                                if (!p.isTaken && p.toString().charAt(0) == 'P'
                                        && board.getBoard()[7][i].getPiece() != null
                                        && board.getBoard()[7][i].getPiece().equals(p)) {
                                    toConvert = p;
                                    Object[] options = {
                                            "QUEEN",
                                            "ROOK",
                                            "BISHOP",
                                            "KNIGHT"
                                    };
                                    int choice = JOptionPane.showOptionDialog(
                                            this,
                                            "Choose piece to promote to:",
                                            "Pawn Promotion!",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            options,
                                            null
                                    );
                                    switch (choice) {
                                        case 0:
                                            converted = new Queen(
                                                    p.location.x, p.location.y, false, board
                                            );
                                            break;
                                        case 1:
                                            converted = new Rook(
                                                    p.location.x, p.location.y, false, board
                                            );
                                            break;
                                        case 2:
                                            converted = new Bishop(
                                                    p.location.x, p.location.y, false, board
                                            );
                                            break;
                                        case 3:
                                            converted = new Knight(
                                                    p.location.x, p.location.y, false, board
                                            );
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                }
                            }
                        }

                        if (converted != null && toConvert != null) {
                            blackPieces.remove(toConvert);
                            board.getTileBasedOnLocation(toConvert.location).setPiece(converted);
                            blackPieces.add(converted);
                        }

                        for (Piece p : whitePieces) {
                            if (p.toString().charAt(0) == 'P' && !p.isTaken) {
                                ((Pawn) p).movedTwo = false;
                            }
                        }

                        validMovesWhenBlackInCheck = new LinkedList<Location>();
                        whitePiecesGivingCheck = new LinkedList<Piece>();

                        if (blackKing.inCheck()) {
                            blackKing.setInCheck(false);
                        }

                        blackSelected = null;
                        whiteTurn = true;
                        board.resetSightLines();

                        for (Piece p : whitePieces) {
                            p.isPinnedFrom = new boolean[8];
                        }

                        generateAllBlackMoves();

                        for (Piece p : blackPieces) {
                            if (!p.isTaken && p.givingCheck) {
                                // System.out.println(p + " is giving check!");
                                blackPiecesGivingCheck.add(p);
                            }
                        }

                        generateAllWhiteMoves();

                        if (blackMatesWhite()) {
                            // System.out.println("Black checkmates white!");
                            repaint();
                            JOptionPane.showMessageDialog(this, "Black wins by checkmate!");
                        }
                        if (isStalemate()) {
                            repaint();
                            JOptionPane.showMessageDialog(this, "Draw - Stalemate");
                        }
                    }
                }
                moveMode = false;
                castled = false;
            }
            repaint();
        } else {
            if (whiteTurn) {
                if (selectedPiece != null && selectedPiece.white) {
                    // System.out.println(selectedPiece);
                    whiteSelected = selectedPiece;
                    if (whiteSelected == whiteKing) {
                        whiteKing.checkCanCastle(whiteRooks[0]);
                        whiteKing.checkCanCastle(whiteRooks[1]);
                    }
                    moveMode = true;
                }
            } else {
                if (!cpuOpponent) {
                    if (selectedPiece != null && !selectedPiece.white) {
                        blackSelected = selectedPiece;
                        if (blackSelected == blackKing) {
                            blackKing.checkCanCastle(blackRooks[0]);
                            blackKing.checkCanCastle(blackRooks[1]);
                        }
                        moveMode = true;
                    }
                }
            }
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void generateAllWhiteMoves() {
        if (!isWhiteKingInCheck()) {
            for (Piece p : whitePieces) {
                p.generateValidMoves();
            }
        } else {
            if (blackPiecesGivingCheck.size() > 1) {
                // validMovesWhenWhiteInCheck.addAll(whiteKing.validMoves);
            } else {
                Piece attacker = blackPiecesGivingCheck.get(0);
                List<Location> validLocs = new LinkedList<Location>();
                switch (attacker.toString().charAt(0)) {
                    case 'B':
                        validLocs.addAll(((Bishop) attacker).movesToBlockCheck);
                        validLocs.add(attacker.location);
                        break;
                    case 'Q':
                        validLocs.addAll(((Queen) attacker).movesToBlockCheck);
                        validLocs.add(attacker.location);
                        break;
                    case 'R':
                        validLocs.addAll(((Rook) attacker).movesToBlockCheck);
                        validLocs.add(attacker.location);
                        break;
                    default:
                        validLocs.add(attacker.location);
                        break;
                }
                for (Piece p : whitePieces) {
                    if (!p.isTaken) {
                        p.generateValidMoves();
                        if (p.toString().charAt(0) != 'K') {
                            for (Location loc : p.validMoves) {
                                // System.out.println(validLocs.contains(new Location(2, 5)));
                                if (!validMovesWhenWhiteInCheck.contains(loc)
                                        && validLocs.contains(loc)) {
                                    validMovesWhenWhiteInCheck.add(loc);
                                }
                            }
                        }
                    }
                }
                // validMovesWhenWhiteInCheck.addAll(whiteKing.validMoves);
            }
        }
    }

    protected /*private*/ void generateAllBlackMoves() {
        if (!isBlackKingInCheck()) {
            for (Piece p : blackPieces) {
                p.generateValidMoves();
            }
        } else {
            if (whitePiecesGivingCheck.size() > 1) {
                // validMovesWhenBlackInCheck.addAll(blackKing.validMoves);
            } else {
                Piece attacker = whitePiecesGivingCheck.get(0);
                List<Location> validLocs = new LinkedList<Location>();
                switch (attacker.toString().charAt(0)) {
                    case 'B':
                        validLocs.addAll(((Bishop) attacker).movesToBlockCheck);
                        validLocs.add(attacker.location);
                        break;
                    case 'Q':
                        validLocs.addAll(((Queen) attacker).movesToBlockCheck);
                        validLocs.add(attacker.location);
                        /*
                         * for (Location l : validLocs) {
                         * System.out.print(l);
                         * }
                         * System.out.print("\n");
                         */
                        break;
                    case 'R':
                        validLocs.addAll(((Rook) attacker).movesToBlockCheck);
                        validLocs.add(attacker.location);
                        break;
                    default:
                        validLocs.add(attacker.location);
                        break;
                }
                for (Piece p : blackPieces) {
                    if (!p.isTaken) {
                        p.generateValidMoves();
                        if (p.toString().charAt(0) != 'K') {
                            for (Location loc : p.validMoves) {
                                // System.out.println(validLocs.contains(new Location(2, 5)));
                                if (!validMovesWhenBlackInCheck.contains(loc)
                                        && validLocs.contains(loc)) {
                                    validMovesWhenBlackInCheck.add(loc);
                                }
                            }
                        }
                    }
                }
                // validMovesWhenBlackInCheck.addAll(blackKing.validMoves);
            }
        }
    }

    private boolean isWhiteKingInCheck() {
        return whiteKing.inCheck();
    }

    private boolean isBlackKingInCheck() {
        return blackKing.inCheck();
    }

    private boolean whiteMatesBlack() {
        return (blackKing.inCheck() && validMovesWhenBlackInCheck.size() == 0
                && blackKing.validMoves.size() == 0);
    }

    private boolean blackMatesWhite() {
        return (whiteKing.inCheck() && validMovesWhenWhiteInCheck.size() == 0
                && whiteKing.validMoves.size() == 0);
    }

    private boolean isStalemate() {
        // One of the players has no valid moves but their King is not in check
        List<Location> whiteValidMoves = new LinkedList<Location>();
        for (Piece p : whitePieces) {
            if (!p.isTaken) {
                whiteValidMoves.addAll(p.validMoves);
            }
        }
        List<Location> blackValidMoves = new LinkedList<Location>();
        for (Piece p : blackPieces) {
            if (!p.isTaken) {
                blackValidMoves.addAll(p.validMoves);
            }
        }

        if ((!whiteKing.inCheck() && whiteValidMoves.size() == 0)
                || (!blackKing.inCheck() && blackValidMoves.size() == 0)) {
            return true;
        }

        // Check that only Kings are left
        int numWhitePiecesLeft = 0;
        for (Piece p : whitePieces) {
            if (!p.isTaken) {
                numWhitePiecesLeft++;
            }
        }
        int numBlackPiecesLeft = 0;
        for (Piece p : blackPieces) {
            if (!p.isTaken) {
                numBlackPiecesLeft++;
            }
        }

        return numWhitePiecesLeft == 0 && numBlackPiecesLeft == 0;

        // Check for 3 moves in a row from both sides
    }

    private double minimax(BoardState state, int depth, boolean minimizingPlayer) {
        /*if (state.checkGameOver()) {
            if (minimizingPlayer) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }*/
        if (depth == 0) {
            return state.evaluate();
        }
        if (minimizingPlayer) { // black (the computer)'s turn to move
            double value = Integer.MAX_VALUE;

            if (!state.blackKingInCheck) {
                for (Piece piece : state.getBlackPieces()) {
                    if (piece != null && !piece.isTaken) {
                        Location origin = piece.location;
                        for (Location destination : piece.validMoves) {
                            state.simulateMove(origin, destination);
                        }
                    }
                }
            } else {
                for (Piece piece : state.getBlackPieces()) {
                    Location origin = piece.location;
                    if (state.whitePiecesGivingCheck.size() == 1) {
                        Piece attacker = state.whitePiecesGivingCheck.get(0);
                        for (Location destination : piece.validMoves) {
                            switch (attacker.toString().charAt(0)) {
                                case 'B':
                                    if (((Bishop) attacker).movesToBlockCheck.contains(destination)
                                            || destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                                case 'Q':
                                    if (((Queen) attacker).movesToBlockCheck.contains(destination)
                                            || destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                                case 'R':
                                    if (((Rook) attacker).movesToBlockCheck.contains(destination)
                                            || destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                                default:
                                    if (destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                            }
                        }
                    } else {
                        if (piece.toString().charAt(0) == 'K') {
                            if (piece.validMoves.size() == 0) {
                                return Integer.MAX_VALUE;
                            } else {
                                for (Location destination : piece.validMoves) {
                                    state.simulateMove(origin, destination);
                                }
                            }
                        }
                    }
                }
                if (state.getNextStates().size() == 0) {
                    return Integer.MAX_VALUE;
                }
            }

            for (int i = 0; i < state.getNextStates().size(); i++) {
                BoardState boardState = state.getNextStates().get(i);
                double minimaxResult = minimax(boardState, depth - 1, false);
                if (value > minimaxResult) {
                    value = minimaxResult;
                    // re-assign optimalMove object
                    state.setOptimalMove(state.getAssociatedMoves().get(i));
                }
            }
            return value;
        } else { // white (the player)'s turn to move
            double value = Integer.MIN_VALUE;


            if (!state.whiteKingInCheck) {
                for (Piece piece : state.getWhitePieces()) {
                    if (piece != null && !piece.isTaken) {
                        Location origin = piece.location;
                        for (Location destination : piece.validMoves) {
                            state.simulateMove(origin, destination);
                        }
                    }
                }
            } else {
                for (Piece piece : state.getWhitePieces()) {
                    Location origin = piece.location;
                    if (state.blackPiecesGivingCheck.size() == 1) {
                        Piece attacker = state.blackPiecesGivingCheck.get(0);
                        for (Location destination : piece.validMoves) {
                            switch (attacker.toString().charAt(0)) {
                                case 'B':
                                    if (((Bishop) attacker).movesToBlockCheck.contains(destination)
                                            || destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                                case 'Q':
                                    if (((Queen) attacker).movesToBlockCheck.contains(destination)
                                            || destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                                case 'R':
                                    if (((Rook) attacker).movesToBlockCheck.contains(destination)
                                            || destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                                default:
                                    if (destination.equals(attacker.location)) {
                                        state.simulateMove(origin, destination);
                                    }
                                    break;
                            }
                        }
                    } else {
                        if (piece.toString().charAt(0) == 'K') {
                            if (piece.validMoves.size() == 0) {
                                return Integer.MIN_VALUE;
                            } else {
                                for (Location destination : piece.validMoves) {
                                    state.simulateMove(origin, destination);
                                }
                            }
                        }
                    }
                }
                if (state.getNextStates().size() == 0) {
                    return Integer.MIN_VALUE;
                }
            }

            for (int i = 0; i < state.getNextStates().size(); i++) {
                BoardState boardState = state.getNextStates().get(i);
                double minimaxResult = minimax(boardState, depth - 1, true);
                if (value < minimaxResult) {
                    value = minimaxResult;
                    // re-assign optimalMove object
                    state.setOptimalMove(state.getAssociatedMoves().get(i));
                }
            }
            return value;
        }
    }
}
