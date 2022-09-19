package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static Move NULL_MOVE = new NullMove();

    private Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinates) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinates;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        final Move move = (Move) o;
        return getDestinationCoordinate() == move.getDestinationCoordinate() &&
               getMovedPiece().equals(move.getMovedPiece());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    // TODO impl
    public boolean isAttack() {
        return false;
    }

    // TODO impl
    public boolean isCastlingMove() {
        return false;
    }

    // TODO impl
    public Piece getAttackedPiece() {
        return null;
    }

    // TODO impl
    public Board execute() {
        // Generate a new board to return
        final BoardBuilder builder = new BoardBuilder();
        for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
            // Copy all pieces except the one we're moving
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
        builder.setMoveMaker(this.board.nextPlayerAlliance());
        builder.setMoveTransition(this);

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

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof AttackMove)) return false;
            final AttackMove otherAttackMove = (AttackMove) o;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack() { return true; }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean isCastlingMove() { return false; }
    }

    public static class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinates){
            super(board, piece, destinationCoordinates);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnMove && super.equals(o);
        }

        // TODO impl
//        @Override
//        public String toString() {
//            return BoardUtils.INSTANCE.getPositionAtCoordinate(this.destinationCoordinate);
//        }
    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board,
                        final Piece piece,
                        final int destinationCoordinates){
            super(board, piece, destinationCoordinates);
        }

        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            // When a player executes a 2-space jump, that pawn is now eligible for en passant capture
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.nextPlayerAlliance());
            builder.setMoveTransition(this);
            return builder.build();
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

//    public static final class PawnEnPassentAttackMove extends PawnAttackMove {
//        final Piece attackedPiece;
//
//        public PawnEnPassentAttackMove(final Board board,
//                             final Piece piece,
//                             final int destinationCoordinates,
//                             final Piece attackedPiece){
//            super(board, piece, destinationCoordinates, attackedPiece);
//            this.attackedPiece = attackedPiece;
//        }
//    }

    static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int rookStart;
        protected final int rookDestination;

        public CastleMove(final Board board,
                         final Piece piece,
                         final int destinationCoordinates,
                          final Rook castleRook,
                          final int rookStart,
                          final int rookDestination) {
            super(board, piece, destinationCoordinates);

            if(!this.movedPiece.getPieceType().isKing()) {
                throw new RuntimeException("You cannot castle a piece other than the king");
            }

            this.castleRook = castleRook;
            this.rookStart = rookStart;
            this.rookDestination = rookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() { return true; }

        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();

            // Set uninvolved pieces
            for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece) && !piece.equals(this.castleRook)) {
                    builder.setPiece(piece);
                }
            }

            // Set all opponent's pieces
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            // Set king and rook
            builder.setPiece(this.movedPiece.movePiece(this)); // 'this' should be the king
            Rook newRook = new Rook(this.rookDestination, this.castleRook.getPieceAlliance());
            newRook.setFirstMove(false);
            builder.setPiece(newRook);
            builder.setMoveMaker(this.board.nextPlayerAlliance());
            builder.setMoveTransition(this);

            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece piece,
                                  final int destinationCoordinates,
                                  final Rook castleRook,
                                  final int rookStart,
                                  final int rookDestination) {
            super(board, piece, destinationCoordinates, castleRook, rookStart, rookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordinates,
                                   final Rook castleRook,
                                   final int rookStart,
                                   final int rookDestination) {
            super(board, piece, destinationCoordinates, castleRook, rookStart, rookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    public static final class NullMove extends Move {
        public NullMove(){
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Instance of NullMove not executable.");
        }
    }
}
