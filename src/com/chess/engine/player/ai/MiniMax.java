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
 *
 *  To try:
 *      - favor early castling,
 *      - favor control of center squares
 *      - minimize number of pawn islands
 *      - maximize number of passed pawns
 *      - minimize number of backwards or doubled pawns
 */
public class MiniMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;
    private int searchDepth;
    private int numBoardsEvaluated;

    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
        this.numBoardsEvaluated = 0;
    }

    @Override
    /**
     * Returns the best move
     */
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.print(board.currentPlayer() + " thinking with depth = " + this.searchDepth);

        int numMoves = board.currentPlayer().getLegalMoves().size();

        for(final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                // After evaluating this move, the next player's min/max function needs to run
                currentValue = board.currentPlayer().isWhite() ?
                               min(moveTransition.getTransitionBoard(), this.searchDepth - 1) :
                               max(moveTransition.getTransitionBoard(), this.searchDepth - 1);

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

        final long executionTime = System.currentTimeMillis() - startTime;
        System.out.println(" " + this.numBoardsEvaluated + " positions evaluated.");
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
            this.numBoardsEvaluated++;
            return this.boardEvaluator.evaluate(board, depth);
        }
        if(isEndGameScenario(board)) {
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
            this.numBoardsEvaluated++;
            return this.boardEvaluator.evaluate(board, depth);
        }
        if(isEndGameScenario(board)){
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

    private boolean isEndGameScenario(final Board board) {
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
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
