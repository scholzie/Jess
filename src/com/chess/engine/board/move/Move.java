package com.chess.engine.board.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    public static Move NULL_MOVE = new NullMove();

    private Move(final Board board,
         final Piece movedPiece,
         final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.isFirstMove = movedPiece.isFirstMove();
        this.destinationCoordinate = destinationCoordinate;
    }

    private Move(final Board board,
                 final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public String toString() {
        return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Move move)) return false;
        return getDestinationCoordinate() == move.getDestinationCoordinate() &&
               getMovedPiece().equals(move.getMovedPiece()) &&
                getCurrentCoordinate() == move.getCurrentCoordinate();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    public Board getBoard() {
        return this.board;
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

        builder.setPiece(this.movedPiece.movePiece((this)));
        builder.setMoveMaker(this.board.nextPlayerAlliance());
        builder.setMoveTransition(this);

        return builder.build();
    }

    public Board undo() {
        final BoardBuilder b = new BoardBuilder();
        this.board.getAllPieces().forEach(b::setPiece);
        b.setMoveMaker(this.board.currentPlayer().getAlliance());
        return b.build();
    }


    public static final class MajorMove extends Move {
        public MajorMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof MajorMove && super.equals(o);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class AttackMove extends Move {

        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                           final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof final AttackMove otherAttackMove)) return false;
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

    public static class MajorAttackMove extends AttackMove {
        public MajorAttackMove(final Board board,
                               final Piece movedPiece,
                               final int destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, movedPiece, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof final MajorAttackMove otherMajorAttackMove)) return false;
            return super.equals(otherMajorAttackMove) && getAttackedPiece().equals(otherMajorAttackMove.getAttackedPiece());
        }

        @Override
        public String toString() {
            return movedPiece.toString() + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

    }

    public static class PawnMove extends Move {
        public PawnMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinate){
            super(board, piece, destinationCoordinate);
        }

        @Override
        public Pawn getMovedPiece() {
            return (Pawn) this.movedPiece;
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnMove && super.equals(o);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnJump extends PawnMove {
        public PawnJump(final Board board,
                        final Piece piece,
                        final int destinationCoordinate){
            super(board, piece, destinationCoordinate);
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
            builder.setPiece(movedPawn);
            builder.setMoveMaker(this.board.nextPlayerAlliance());
            builder.setMoveTransition(this);

            return builder.build();
        }

    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(final Board board,
                                       final Piece capturingPawn,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece) {
            super(board, capturingPawn, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnEnPassantAttackMove && super.equals(o);
        }

        @Override
        public Board execute(){
            final BoardBuilder builder = new BoardBuilder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if(!this.attackedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.nextPlayerAlliance());
            return builder.build();
        }
    }

    public static final class PawnPromotionMove extends PawnMove {
        final Move decoratedMove;
        final Pawn promotedPawn;
        final Piece promotionPiece;

        public PawnPromotionMove(final Move decoratedMove, final Piece promotionPiece){
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.promotionPiece = promotionPiece;
        }

        @Override
        public String toString() {
            return this.decoratedMove.toString() + "=" + this.promotionPiece.toString();
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnPromotionMove && this.decoratedMove.equals(o);
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public Board execute() {
            // Delegate the move execution to the decorated move
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final BoardBuilder builder = new BoardBuilder();
            builder.setAllPiecesExcept(pawnMovedBoard.getAllPieces(), this.promotedPawn);
            builder.setPiece(this.promotionPiece.movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance()); // NOT next player

            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(final Board board,
                          final Piece pieceMoved,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, pieceMoved, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof PawnAttackMove && super.equals(o);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).charAt(0) + "x" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board,
                         final Piece piece,
                         final int destinationCoordinate,
                          final Rook castleRook,
                          final int castleRookStart,
                          final int castleRookDestination) {
            super(board, piece, destinationCoordinate);

            if(!this.movedPiece.getPieceType().isKing()) {
                throw new RuntimeException("You cannot castle a piece other than the king");
            }

            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() { return true; }

        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            // TODO fix setAllPiecesExcept method
//            builder.setAllPiecesExcept(this.board.getAllPieces(), (new ArrayList<>(this.movedPiece, this.castleRook)));
            for(final Piece piece : this.board.getAllPieces()) {
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this)); // 'this' should be the king
            Rook newRook = new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance(), false);
            builder.setPiece(newRook);
            builder.setMoveMaker(this.board.nextPlayerAlliance());
//            builder.setMoveTransition(this);

            return builder.build();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object o) {
            if(this == o) return true;
            if(!(o instanceof final CastleMove oMove)) return false;
            return super.equals(oMove) && this.castleRook.equals(oMove.getCastleRook()) &&
                    this.castleRookDestination == oMove.castleRookDestination;
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board,
                                  final Piece piece,
                                  final int destinationCoordinate,
                                  final Rook castleRook,
                                  final int rookStart,
                                  final int rookDestination) {
            super(board, piece, destinationCoordinate, castleRook, rookStart, rookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof KingSideCastleMove && super.equals(o);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordinate,
                                   final Rook castleRook,
                                   final int rookStart,
                                   final int rookDestination) {
            super(board, piece, destinationCoordinate, castleRook, rookStart, rookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

        @Override
        public boolean equals(final Object o) {
            return this == o || o instanceof QueenSideCastleMove && super.equals(o);
        }
    }

    public static final class NullMove extends Move {
        public NullMove(){
            super(null, 65);
        }

        @Override
        public int getCurrentCoordinate() {
            // Tracking currentCoordinate is delegated to the Piece, but a NullMove has no Piece.
            // TODO Create NullPiece to handle this
            return -1;
        }

        @Override
        public Board execute() {
            throw new RuntimeException("cannot execute null move!");
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }
}
