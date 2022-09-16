package com.chess.engine.board;

import static com.chess.engine.board.Move.NULL_MOVE;

public class MoveFactory {

    private MoveFactory() {
        throw new RuntimeException("MoveFactory is non-instantiable!");
    }

    public static Move createMove(final Board board,
                                  final int currentCoordinate,
                                  final int destinationCoordinate) {
        for(final Move move : board.getAllLegalMoves()) {
            if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                return move;
            }
        }

        return NULL_MOVE;
    }
}
