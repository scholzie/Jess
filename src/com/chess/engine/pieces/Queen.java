package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.move.Move.MajorAttackMove;
import com.chess.engine.board.move.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.BoardUtils.isValidTileCoordinate;

public class Queen extends Piece {

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

    public Queen(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
    }

    public Queen(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
    }

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
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    } else {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public int getLocationBonus() {
        return this.pieceAlliance.getBonus(PieceType.QUEEN, this.piecePosition);
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    @Override
    public Queen movePiece(final Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.A_FILE.get(currentPosition) && (candidateOffset == -9 ||
                candidateOffset == 7 || candidateOffset == -1);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.H_FILE.get(currentPosition) && (candidateOffset == -7 ||
                candidateOffset == 9 || candidateOffset == 1);
    }
}
