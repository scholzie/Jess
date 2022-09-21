package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;

public class MoveTransition {

    private final Board fromBoard;
    private final Board transitionBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board fromBoard,
                          final Board transitionBoard,
                          final Move transitionMove,
                          final MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.transitionBoard = transitionBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getFromBoard() {
        return this.fromBoard;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }

}
