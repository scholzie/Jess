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

    public static final class CaptureMove extends Move {
        final Piece capturedPiece;

        public CaptureMove(final Board board,
                            final Piece piece,
                            final int destinationCoordinates,
                            final Piece capturedPiece){
            super(board, piece, destinationCoordinates);
            this.capturedPiece = capturedPiece;
        }
    }


    public static final class EnPassentCaptureMove extends Move {
        final Piece capturedPiece;

        public EnPassentCaptureMove(final Board board,
                             final Piece piece,
                             final int destinationCoordinates,
                             final Piece capturedPiece){
            super(board, piece, destinationCoordinates);
            this.capturedPiece = capturedPiece;
        }
    }
}
