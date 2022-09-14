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

public class Bishop extends Piece {

    // +/- 7 and 9
//    private final static int[] CANDIDATE_MOVE_COORDINATES = { -9, -18, -27, -28, -35, -36, -42, -45, -49, -54, -63,
//            7, 9, 14, 18, 21, 27, 28, 35, 36, 42, 45, 49, 54, 63};

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -7, 7, 9 };

    Bishop(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        /* Loop through each vector coordinate
         * Continue applying vector offset to current position until the tile is no longer valid
         * Skip to next Coordinate and repeat
         */

        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                // Scenario 1: A piece was found.
                // Scenario 2: No piece was found
                //      Add a legal MajorMove
                //      Keep looping
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    // It's the opposite color
                    if (pieceAlliance != this.pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                    break;
                } else {
                    legalMoves.add(new Move.MajorMove(board, this,
                            candidateDestinationCoordinate));
                }

                candidateDestinationCoordinate += currentCandidateOffset;
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}
