package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {
    // TODO: add algebraic notation
    protected final int piecePosition;
    protected final Alliance pieceAlliance; // AKA piece 'color'

    protected boolean isFirstMove;

    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.isFirstMove = false;
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

    public enum PieceType {

        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        KING("K"),
        QUEEN("Q");

        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
