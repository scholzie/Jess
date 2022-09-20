package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;

import java.util.Collection;

public abstract class Piece {
    // TODO: add algebraic notation
    protected final int piecePosition;
    protected final Alliance pieceAlliance; // AKA piece 'color'
    protected final PieceType pieceType;
    private final int cachedHashCode;

    protected boolean isFirstMove;

    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        // Is this the same as Objects.hash(piecePosition, pieceAlliance, pieceType, isFirstMove); ?
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public boolean isBlack() {
        return this.pieceAlliance == Alliance.BLACK;
    }

    public boolean isWhite() {
        return this.pieceAlliance == Alliance.WHITE;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board); // Each piece type will override this method

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return piecePosition == piece.piecePosition && isFirstMove == piece.isFirstMove &&
                pieceAlliance == piece.pieceAlliance && pieceType == piece.pieceType;
    }

    public abstract Piece movePiece(Move move);

    public void setFirstMove(final boolean status) {
        this.isFirstMove = status;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType {

        PAWN(100, "P"),
        KNIGHT(300,"N"),
        BISHOP(330, "B"),
        ROOK(500,"R") {
            @Override
            public boolean isRook() { return true; }
        },
        KING(10000, "K") {
            @Override
            public boolean isKing() { return true; }
        },
        QUEEN(900, "Q");

        private final int value;
        private final String pieceName;

        PieceType(final int value,
                final String pieceName) {
            this.value = value;
            this.pieceName = pieceName;
        }

        public int getPieceValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        // TODO set default false and override once
        public boolean isKing() { return false; }
        public boolean isRook() { return false; }
    }
}
