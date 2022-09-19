package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    // To Consider:
    // Pawns can move 1 or 2 spaces on first move only
    // Pawns can only move forward w.r.t. their side (i.e. the black and white pawns move in opposite directions)
    // Pawns capture diagonally
    // Pawns are capable of 'en passant' capture
    // Pawns are capable of promotion

    private final static int[] CANDIDATE_MOVE_COORDINATES = { 7, 8, 9, 16 };

//    private boolean hasMoved;

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);

//        this.hasMoved = false;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {

            int candidateDestinationCoordinate = this.piecePosition +
                    this.pieceAlliance.getForwardDirection() * currentCandidateOffset;

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            if(currentCandidateOffset == 8 && candidateDestinationTile.isTileEmpty()) {
                // TODO need to deal with promotions
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if(currentCandidateOffset == 16 &&
                    !this.isFirstMove() && (
                        (BoardUtils.SEVENTH_RANK.get(this.piecePosition) && this.pieceAlliance.isBlack()) ||
                        (BoardUtils.SECOND_RANK.get(this.piecePosition) && this.pieceAlliance.isWhite()))) {
                final int behindCandidateDestinationCoordinate = this.piecePosition +
                        (this.pieceAlliance.getForwardDirection() * 8);
                if(board.getTile(behindCandidateDestinationCoordinate).isTileEmpty() &&
                    board.getTile(candidateDestinationCoordinate).isTileEmpty()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
            } else if(currentCandidateOffset == 7 &&
                    !(this.pieceAlliance.isWhite() && BoardUtils.EIGHTH_COLUMN.get(this.piecePosition)) &&
                    !(this.pieceAlliance.isBlack() && BoardUtils.FIRST_COLUMN.get(this.piecePosition))) {
                // White can't capture in last column
                // Black can't capture in first column
                // Target tile must be occupied by a piece of the opposite color
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidateTile = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidateTile.pieceAlliance != this.pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidateTile));
                    }
                }

            } else if(currentCandidateOffset == 9 &&
                    !(this.pieceAlliance.isBlack() && BoardUtils.EIGHTH_COLUMN.get(this.piecePosition)) &&
                    !(this.pieceAlliance.isWhite() && BoardUtils.FIRST_COLUMN.get(this.piecePosition))) {
                // white can't capture in first column
                // black can't capture in last column
                // Target tile must be occupied by a piece of the opposite color
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidateTile = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidateTile.pieceAlliance != this.pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidateTile));
                    }
                }
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
