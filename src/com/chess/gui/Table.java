package com.chess.gui;

import com.chess.engine.board.*;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.*;

public class Table {
    private Board chessBoard;

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private final String pieceTheme;
    private static String defaultPieceArtPath = "art/pieces/";
    private static String DEFAULT_PIECE_THEME = "simple";
    private Color darkTileColor = new Color(60, 95, 135); // Blue
    private Color lightTileColor = new Color(229, 229, 200); // Beige

    private Tile moveSourceTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;

    // TODO Need to make panels:
    //      Taken Pieces
    //      Moves

    public Table() {
        this.chessBoard = Board.createStandardBoard();
        this.pieceTheme = DEFAULT_PIECE_THEME;

        this.gameFrame = new JFrame("Jess");
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setJMenuBar(createTableMenuBar());
        this.gameFrame.setLayout(new BorderLayout());
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);

        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Selected load PGN");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem openFEN = new JMenuItem("Load position from FEN");
        openFEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Selected load FEN");
            }
        });
        fileMenu.add(openFEN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardItem = new JMenuItem("Flip Board");
        flipBoardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardItem);

        final JCheckBoxMenuItem legalMoveHighlightCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", true);
        legalMoveHighlightCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlightCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlightCheckbox);
        return preferencesMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUM_BOARD_TILES; i++) {
                final TilePanel tile = new TilePanel(this, i);
                this.boardTiles.add(tile);
                add(tile);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for(final TilePanel boardTile : boardDirection.traverse(boardTiles)) {
                boardTile.drawTile(board);
                add(boardTile);
            }
            validate();
            repaint();
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    // Right click cancels any selection made
                    if(isRightMouseButton(e)) {
                        moveSourceTile = null;
                        humanMovedPiece = null;
                    } else if(isLeftMouseButton(e)) {
                        if(moveSourceTile == null){ // No source tile picked already (first click)
                            moveSourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = moveSourceTile.getPiece();
                            if(humanMovedPiece == null){
                                moveSourceTile = null;
                            }
                        } else { //second click
                            // Return the move if legal, otherwise return null move
                            final Move move = MoveFactory.createMove(chessBoard, moveSourceTile.getTileCoordinate(), tileId);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getToBoard();
                                // TODO add move to move log
                            }
                            moveSourceTile = null;
                            humanMovedPiece = null;
                        }
                        invokeLater(() -> boardPanel.drawBoard(chessBoard));
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });

            validate();
        }

        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).getPiece() != null) {
                // Render piece
                try {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceArtPath + "/" +
                            pieceTheme + "/" +
                            board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1) + "" +
                            board.getTile(this.tileId).getPiece().toString() +
                            ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegalMoves(final Board board){
            if(highlightLegalMoves){ // TODO add preference to toggle
                for(final Move move : selectedPieceLegalMoves(board)){
                    if(move.getDestinationCoordinate() == this.tileId) {
                        try {
                            // TODO generalize
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> selectedPieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor() {
            // First row (Tiles 0-7, or the 8th Rank) starts with white
            // Every other row also starts with white
            // TODO generalize number of rows
            if(BoardUtils.EIGHTH_RANK.get(this.tileId) ||
                    BoardUtils.SIXTH_RANK.get(this.tileId) ||
                    BoardUtils.FOURTH_RANK.get(this.tileId) ||
                    BoardUtils.SECOND_RANK.get(this.tileId)) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils.SEVENTH_RANK.get(this.tileId) ||
                    BoardUtils.FIFTH_RANK.get(this.tileId) ||
                    BoardUtils.THIRD_RANK.get(this.tileId) ||
                    BoardUtils.FIRST_RANK.get(this.tileId)) {
                setBackground(this.tileId % 2 == 0 ? darkTileColor : lightTileColor);
            }
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        }, // White at bottom
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

}
