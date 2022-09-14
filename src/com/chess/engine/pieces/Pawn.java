package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

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

    private boolean hasMoved;

    Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {

            int candidateDestinationCoordinate = this.piecePosition +
                    this.getPieceAlliance().getForwardDirection() * currentCandidateOffset;

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            if(currentCandidateOffset == 8 && !candidateDestinationTile.isTileOccupied()) {
                // TODO more work to wrap up here
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            }
        }

        return null;
    }
}
