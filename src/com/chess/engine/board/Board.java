package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whitePieces;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
    }

    private Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile : gameBoard){
            final Piece pieceOnTile = tile.getPiece();
            if(pieceOnTile.getPieceAlliance() == alliance){
                activePieces.add(pieceOnTile);
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate) {
        return null;
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_BOARD_TILES];
        for(int i = 0; i < BoardUtils.NUM_BOARD_TILES; i++){
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();

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

    public static class Builder {

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        public Builder() {}

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
