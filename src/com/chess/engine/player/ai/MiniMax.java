package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;
import com.chess.engine.player.MoveTransition;

/**
 * Use the Minimax algorithm to calculate the best move for each player.
 *
 * By convention, evaluation is a sliding scale from -infinity (black advantage) to +infinity (white advantage)
 * Possible areas of evaluation:
 *  - Piece count
 *  - Number of moves available
 *  - Pawn structure
 *  - Checks/Mates
 *  - Castling
 *  - etc.
 */
public class MiniMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;
    private int numBoardsEvaluated;

    public MiniMax() {
        // TODO
        this.boardEvaluator = null;
        this.numBoardsEvaluated = 0;
    }

    @Override
    /**
     * Returns the best move
     */
    public Move execute(final Board board, final int depth) {
        Move bestMove = null;
        final long startTime = System.currentTimeMillis();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + " thinking with depth = " + depth);

        int numMoves = board.currentPlayer().getLegalMoves().size();
        for(final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                // After evaluating this move, the next player's min/max function needs to run
                currentValue = board.currentPlayer().isWhite() ?
                               min(moveTransition.getTransitionBoard(), depth - 1) :
                               max(moveTransition.getTransitionBoard(), depth - 1);

                if(board.currentPlayer().isWhite() &&
                        currentValue >= highestSeenValue) {
                    // This move is now the best we've seen
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().isBlack() &&
                            currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        this.numBoardsEvaluated++;
        return bestMove;
    }

    /**
     * min() makes each possible move and keeps track of lowest seen value. min() is co-recursive with max()
     * @param board
     * @param depth
     * @return
     */
    public int min(final Board board,
                   final int depth) {
        if(depth == 0){
            return this.boardEvaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.getAllLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if(currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }

        return lowestSeenValue;
    }

    /**
     * max() makes each possible move and keeps track of highest seen value. max() is co-recursive with min()
     * @param board
     * @param depth
     * @return
     */
    public int max(final Board board,
                   final int depth) {
        if(depth == 0){
            return this.boardEvaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.getAllLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if(currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }

        return highestSeenValue;
    }

    // Getters & Setters
    @Override
    public int getNumBoardsEvaluated() {
        return this.numBoardsEvaluated;
    }

    // Override standard object methods
    @Override
    public String toString() {
        return "MiniMax";
    }
}
