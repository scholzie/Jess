package com.chess.gui;

import com.chess.engine.board.*;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.move.MoveFactory;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import static javax.swing.SwingUtilities.*;

// TODO research alternative for Observable
public final class Table extends Observable {
    private Board chessBoard;

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final MoveLog moveLog;
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
//    private final static Dimension TAKEN_PIECES_PANEL_DIMENSION = new Dimension(60,350);
//    private final static Dimension GAME_HISTORY_PANEL_DIMENSION = new Dimension(100,350);

    private final String pieceTheme;
    private final Color darkTileColor = new Color(60, 95, 135); // Blue
    private final Color lightTileColor = new Color(229, 229, 200); // Beige

    private Tile moveSourceTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;

    private static final Table INSTANCE = new Table();
    private GameSetup gameSetup;

    public Table() {
        this.gameFrame = new JFrame("Jess");
        gameFrame.setSize(OUTER_FRAME_DIMENSION);
        gameFrame.setJMenuBar(createTableMenuBar());
        gameFrame.setLayout(new BorderLayout());

        this.chessBoard = Board.createStandardBoard();
        this.moveLog = new MoveLog();
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.pieceTheme = "simple";

        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = true;

        this.takenPiecesPanel = new TakenPiecesPanel();
//        this.takenPiecesPanel.setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
        this.gameHistoryPanel = new GameHistoryPanel();
//        this.gameHistoryPanel.setPreferredSize(GAME_HISTORY_PANEL_DIMENSION);

        gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);

        gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(e -> System.out.println("Selected load PGN"));
        fileMenu.add(openPGN);

        final JMenuItem openFEN = new JMenuItem("Load position from FEN");
        openFEN.addActionListener(e -> System.out.println("Selected load FEN"));
        fileMenu.add(openFEN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardItem = new JMenuItem("Flip Board");
        flipBoardItem.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferencesMenu.add(flipBoardItem);

        final JCheckBoxMenuItem legalMoveHighlightCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", true);
        legalMoveHighlightCheckbox.addActionListener(e -> highlightLegalMoves = legalMoveHighlightCheckbox.isSelected());
        preferencesMenu.add(legalMoveHighlightCheckbox);
        return preferencesMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem playerSettingsMenuItem = new JMenuItem("Player Setup");
        playerSettingsMenuItem.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
            System.out.println("Selected Player Setup");
        });
        optionsMenu.add(playerSettingsMenuItem);

        return optionsMenu;
    }

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private static Table get() {
        return INSTANCE;
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

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }

        public Move removeMove(final int index){
            return this.moves.remove(index);
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
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            moveSourceTile = null;
                            humanMovedPiece = null;
                        }
                        invokeLater(() -> {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            takenPiecesPanel.redo(moveLog);
                            boardPanel.drawBoard(chessBoard);
                        });
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
                    String defaultPieceArtPath = "art/pieces/";
                    final BufferedImage image = ImageIO.read(new File(defaultPieceArtPath + "/" +
                            pieceTheme + "/" +
                            board.getTile(this.tileId).getPiece().getPieceAlliance().toString().charAt(0) + "" +
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

    public enum PlayerType {
        HUMAN {
            @Override
            public String getPlayerTypeLabel() {
                return "Human";
            }
        },
        COMPUTER {
            @Override
            public String getPlayerTypeLabel() {
                return "Computer";
            }
        };

        public abstract String getPlayerTypeLabel();
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
