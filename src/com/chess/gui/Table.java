package com.chess.gui;

import com.chess.GameUtils.pgn.SQLiteGamePersistence;
import com.chess.engine.board.*;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.move.MoveFactory;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.chess.engine.player.ai.StandardBoardEvaluator;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.Observable;

import static com.chess.GameUtils.pgn.PGNUtilities.writeGameToPGNFile;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import static javax.swing.SwingUtilities.*;

// TODO research alternative for Observable
public final class Table extends Observable {
    private Board chessBoard;

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final DebugPanel debugPanel;
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
    private final GameSetup gameSetup;
    private Move computerMove;
    private boolean useBook;

    private Table() {
        this.gameFrame = new JFrame("Jess");
        gameFrame.setJMenuBar(createTableMenuBar());
        gameFrame.setLayout(new BorderLayout());

        this.chessBoard = Board.createStandardBoard();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.pieceTheme = "simple";

        this.useBook = false;
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = true;

        this.takenPiecesPanel = new TakenPiecesPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.debugPanel = new DebugPanel();

        gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        gameFrame.add(this.debugPanel, BorderLayout.SOUTH);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setSize(OUTER_FRAME_DIMENSION);
        center(this.gameFrame);
        gameFrame.setVisible(true);
    }

    private static void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
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

        final JMenuItem saveToPgn = new JMenuItem("Export to PGN");
        saveToPgn.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(final File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith("pgn");
                }

                @Override
                public String getDescription() {
                    return ".pgn";
                }
            });
            final int option = chooser.showSaveDialog(Table.get().getGameFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                savePGNFile(chooser.getSelectedFile());
            }
        });
        fileMenu.add(saveToPgn);

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

    private static void savePGNFile(final File pgnFile) {
        try {
            writeGameToPGNFile(pgnFile, Table.get().getMoveLog());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private JFrame getGameFrame() {
        return this.gameFrame;
    }

    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");

        // Flip board direction
        final JMenuItem flipBoardItem = new JMenuItem("Flip Board");
        flipBoardItem.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        preferencesMenu.add(flipBoardItem);

        // Move highlighting
        final JCheckBoxMenuItem legalMoveHighlightCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", true);
        legalMoveHighlightCheckbox.addActionListener(e -> highlightLegalMoves = legalMoveHighlightCheckbox.isSelected());
        preferencesMenu.add(legalMoveHighlightCheckbox);
        return preferencesMenu;

        // Highlight color
        // Tile colors
        // Piece theme

    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Game Options");

        // Undo last move
        final JMenuItem undoLastMoveItem = new JMenuItem("Undo Last Move");
        undoLastMoveItem.addActionListener(e -> {
            if(Table.get().getMoveLog().size() > 0) {
                undoLastMove();
            } else {
                System.out.println("Nothing to undo!");
            }
        });
        optionsMenu.add(undoLastMoveItem);

        // TODO Resign Item
        // New Game
        final JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> undoAllMoves());
        optionsMenu.add(newGameItem);


        // Evaluate Board
        final JMenuItem evaluateBoardItem = new JMenuItem("Evaluate Board");
        evaluateBoardItem.addActionListener(e -> {
            System.out.println(
                    StandardBoardEvaluator.get().evaluationDetails(chessBoard, gameSetup.getSearchDepth())
            );
        });
        optionsMenu.add(evaluateBoardItem);

        // Escape Analysis

        // Current Board State
        final JMenuItem currentStateItem = new JMenuItem("Current Game State");
        currentStateItem.addActionListener(e -> {
            System.out.println(chessBoard.getWhitePieces());
            System.out.println(chessBoard.getBlackPieces());
            System.out.println(playerInfo(chessBoard.currentPlayer()));
            System.out.println(playerInfo(chessBoard.currentPlayer().getOpponent()));
        });
        optionsMenu.add(currentStateItem);

        //

        // Set Human/AI and search depth
        final JMenuItem playerSettingsMenuItem = new JMenuItem("Player Setup");
        playerSettingsMenuItem.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
            System.out.println("Selected Player Setup");
        });
        optionsMenu.add(playerSettingsMenuItem);

        // Use book moves
        final JCheckBoxMenuItem useBookMoves = new JCheckBoxMenuItem("AI Uses Book Moves", false);
        useBookMoves.addActionListener(e -> useBook = useBookMoves.isSelected());


        return optionsMenu;
    }

    private static String playerInfo(final Player player) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Player is ").append(player.getAlliance()).append("\n")
                .append("Legal Moves (").append(player.getLegalMoves().size()).append(") = ")
                .append(player.getLegalMoves()).append("\n")
                .append("inCheck: ").append(player.isInCheck()).append("\n")
                .append("inCheckMate: ").append(player.isInCheckMate()).append("\n")
                .append("inStaleMate: ").append(player.isInStaleMate()).append("\n")
                .append("isCastled: ").append(player.isCastled()).append("\n");
        return builder.toString();
    }

    private void undoLastMove() {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getTransitionBoard();
        this.computerMove = null;
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
        Table.get().getDebugPanel().redo();
    }

    private DebugPanel getDebugPanel() {
        return this.debugPanel;
    }

    private void undoAllMoves() {
        for (int i = Table.get().getMoveLog().size() - 1; i >= 0; i--){
            final Move lastMove = moveLog.removeMove(Table.get().getMoveLog().size() - 1);
            this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getTransitionBoard();
        }
        this.computerMove = null;
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
        Table.get().getDebugPanel().redo();
    }

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher
        implements Observer {

        @Override
        public void update(final Observable o, final Object arg){
            if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
                !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
                !Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
                // Create an AI thread, execute AI work
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) {
//                JOptionPane.showMessageDialog(Table.get().getBoardPanel(), );
                System.out.println("Game Over, " + Table.get().getGameBoard().currentPlayer() + " is in Checkmate!");
            }

            if (Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
//                JOptionPane.showMessageDialog(Table.get().getBoardPanel(), );
                System.out.println("Game Over, " + Table.get().getGameBoard().currentPlayer() + " is in Stalemate!");
            }
        }
    }

    private static class AIThinkTank extends SwingWorker<Move, String> {

        private AIThinkTank() {

        }

        @Override
        protected Move doInBackground() {
            // TODO use preferences
//            final MoveStrategy miniMax = new MiniMax(4);
//            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            final Move bestMove;
            final Move bookMove = Table.get().getUseBook()
                                  ? SQLiteGamePersistence.get()
                                          .getNextBestMove(Table.get().getGameBoard(),
                                                  Table.get().getGameBoard().currentPlayer(),
                                                  Table.get().getMoveLog()
                                                          .getMoves().toString()
                                                          .replaceAll("\\[", "")
                                                          .replaceAll("]", ""))
                                  : MoveFactory.getNullMove();
            if(Table.get().getUseBook() && bookMove != MoveFactory.getNullMove()) {
                bestMove = bookMove;
            } else {
                final MiniMax miniMax = new MiniMax(Table.get().getGameSetup().getSearchDepth());
                miniMax.addObserver(Table.get().getDebugPanel());
                bestMove = miniMax.execute(Table.get().getGameBoard());
            }
            return bestMove;
        }

        @Override
        public void done() {
            try {
                final Move bestMove = get();

                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private boolean getUseBook() {
        return this.useBook;
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    public void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    public static Table get() {
        return INSTANCE;
    }

    public void show(){
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());

        // TODO debug panel
//        Table.get().getDebugPanel().redo();
    }

//    private DebugPanel getDebugPanel() {
//        return this.debugPanel;
//    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
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

                            if(gameSetup.isAIPlayer(chessBoard.currentPlayer())) {
                                Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            }

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
