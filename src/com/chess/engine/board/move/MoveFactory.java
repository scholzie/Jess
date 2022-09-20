package com.chess.engine.board.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;

import static com.chess.engine.board.move.Move.NULL_MOVE;

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
