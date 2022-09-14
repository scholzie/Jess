package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece piece;
    final int destinationCoordinate;

    Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinates) {
        this.board = board;
        this.piece = movedPiece;
        this.destinationCoordinate = destinationCoordinates;
    }

    public static final class MajorMove extends Move {
        public MajorMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static final class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinates,
                           final Piece attackedPiece) {
            super(board, piece, destinationCoordinates);
            this.attackedPiece = attackedPiece;
        }
    }
}
