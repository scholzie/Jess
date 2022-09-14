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

import static com.chess.engine.board.BoardUtils.isValidTileCoordinate;

public class Rook extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -1, 1, -8, 8 };

    Rook(int piecePosition, Alliance pieceAlliance) { super(piecePosition, pieceAlliance); }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {

            int candidateDestinationCoordinate = this.piecePosition;

            while(isValidTileCoordinate(candidateDestinationCoordinate)) {
                if(isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if(isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    // Scenario 1: A piece was found.
                    // Scenario 2: No piece was found
                    //      Add a legal MajorMove
                    //      Keep looping

                    // TODO: pull this logic into a helper lib
                    if (candidateDestinationTile.isTileOccupied()) {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        // It's the opposite color
                        if (pieceAlliance != this.pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    } else {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.INSTANCE.FIRST_COLUMN.get(currentPosition) && (candidateOffset == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.INSTANCE.EIGHTH_COLUMN.get(currentPosition) && (candidateOffset == 1);
    }
}
