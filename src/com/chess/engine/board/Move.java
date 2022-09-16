package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    private Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinates) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinates;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    // TODO impl
    public Board execute() {
        // Generate a new board to return
        final BoardBuilder builder = new BoardBuilder();
        for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
            // Copy all pieces except the one we're moving
            // TODO hashcode and equals for Piece
            if(!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            // Copy all opponent's pieces
            builder.setPiece(piece);
        }

        //TODO Impl
        builder.setPiece(this.movedPiece.movePiece((this)));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

        return builder.build();
    }

    public static final class MajorMove extends Move {
        public MajorMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinates,
                           final Piece attackedPiece) {
            super(board, piece, destinationCoordinates);
            this.attackedPiece = attackedPiece;
        }
    }

    public static class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinates){
            super(board, piece, destinationCoordinates);
        }
    }

    public static final class PawnJump extends PawnMove {
        public PawnJump(final Board board,
                        final Piece piece,
                        final int destinationCoordinates){
            super(board, piece, destinationCoordinates);
        }
    }

    public static final class PawnPromotionMove extends PawnMove {
        public PawnPromotionMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinates){
            super(board, piece, destinationCoordinates);
        }
    }

    public static class PawnAttackMove extends AttackMove {

        final Piece attackedPiece;

        public PawnAttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinates,
                          final Piece attackedPiece) {
            super(board, piece, destinationCoordinates, attackedPiece);
            this.attackedPiece = attackedPiece;
        }
    }

    public static final class PawnEnPassentAttackMove extends PawnAttackMove {
        final Piece attackedPiece;

        public PawnEnPassentAttackMove(final Board board,
                             final Piece piece,
                             final int destinationCoordinates,
                             final Piece attackedPiece){
            super(board, piece, destinationCoordinates, attackedPiece);
            this.attackedPiece = attackedPiece;
        }
    }

    public static abstract class CastleMove extends Move {
        public CastleMove(final Board board,
                         final Piece piece,
                         final int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static abstract class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece piece,
                                  final int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static abstract class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }
    }

    public static final class NullMove extends Move {
        public NullMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinates){
            super(board, piece, destinationCoordinates);
        }
    }
}
