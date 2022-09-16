package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whitePieces;
//    private final Collection<Piece> blackCapturedPieces;
//    private final Collection<Piece> whiteCapturedPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private Board(BoardBuilder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
//        this.blackCapturedPieces = new ArrayList<Piece>();
//        this.whiteCapturedPieces = new ArrayList<Piece>();

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
        // TODO: impl
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_BOARD_TILES; i++) {
            final Tile tile = this.gameBoard.get(i);
            final String tileText = tile.toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) builder.append("\n");
        }

        return builder.toString();
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()) {
                final Piece pieceOnTile = tile.getPiece();
                if (pieceOnTile.getPieceAlliance() == alliance) {
                    activePieces.add(pieceOnTile);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Player getBlackPlayer() {
        return this.blackPlayer;
    }

    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Tile getTile(final int tileCoordinate) {
        return this.gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard(final BoardBuilder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_BOARD_TILES];
        for(int i = 0; i < BoardUtils.NUM_BOARD_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        final BoardBuilder builder = new BoardBuilder();

        // Set the black pieces
        builder.setPiece(new Rook(0, Alliance.BLACK))
                .setPiece(new Knight(1, Alliance.BLACK))
                .setPiece(new Bishop(2, Alliance.BLACK))
                .setPiece(new Queen(3, Alliance.BLACK))
                .setPiece(new King(4, Alliance.BLACK))
                .setPiece(new Bishop(5, Alliance.BLACK))
                .setPiece(new Knight(6, Alliance.BLACK))
                .setPiece(new Rook(7, Alliance.BLACK));
        for(int i = 8; i < 16; i++) {
            builder.setPiece(new Pawn(i, Alliance.BLACK));
        }

        // Set the white pieces
        builder.setPiece(new Rook(56, Alliance.WHITE))
                .setPiece(new Knight(57, Alliance.WHITE))
                .setPiece(new Bishop(58, Alliance.WHITE))
                .setPiece(new Queen(59, Alliance.WHITE))
                .setPiece(new King(60, Alliance.WHITE))
                .setPiece(new Bishop(61, Alliance.WHITE))
                .setPiece(new Knight(62, Alliance.WHITE))
                .setPiece(new Rook(63, Alliance.WHITE));
        for(int i = 48; i < 56; i++) {
            builder.setPiece(new Pawn(i, Alliance.WHITE));
        }

        // White moves first
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Collection<Move> getAllLegalMoves() {
        final List<Move> legalMoves = new ArrayList<>();
        legalMoves.addAll(this.whitePlayer.getLegalMoves());
        legalMoves.addAll(this.blackPlayer.getLegalMoves());
        return ImmutableList.copyOf(legalMoves);
    }


    public static class BoardBuilder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        public BoardBuilder() {
            this.boardConfig = new HashMap<>();
        }

        public BoardBuilder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public BoardBuilder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
