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

    public abstract Board execute();

    public static final class MajorMove extends Move {
        public MajorMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinates) {
            super(board, piece, destinationCoordinates);
        }

        // TODO impl
        @Override
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

        // TODO impl
        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            // copy all pieces except moved piece
            for(final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)) builder.setPiece(piece);
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if(!this.attackedPiece.equals(piece)) builder.setPiece(piece);
            }

            //TODO finish

            // copy all opponent's piece except attacked piece
            return null;
        }
    }

//    public static final class CaptureMove extends Move {
//        final Piece capturedPiece;
//
//        public CaptureMove(final Board board,
//                            final Piece piece,
//                            final int destinationCoordinates,
//                            final Piece capturedPiece){
//            super(board, piece, destinationCoordinates);
//            this.capturedPiece = capturedPiece;
//        }
//
//        // TODO: implement
//        @Override
//        public Board execute() {
//            // create a new board, then change active board to that new board
//            return null;
//        }
//    }


    public static final class EnPassentCaptureMove extends Move {
        final Piece capturedPiece;

        public EnPassentCaptureMove(final Board board,
                             final Piece piece,
                             final int destinationCoordinates,
                             final Piece capturedPiece){
            super(board, piece, destinationCoordinates);
            this.capturedPiece = capturedPiece;
        }

        // TODO: implement
        @Override
        public Board execute() {
            // Switch active player
            return null;
        }
    }
}
