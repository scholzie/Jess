package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50; // Half point for forcing move
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    @Override
    public int evaluate(final Board board,
                        final int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) -
                scorePlayer(board, board.blackPlayer(), depth);
    }

    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth) {
        // TODO optional scaling factors?
        return pieceValue(player) +
                mobility(player) +
                check(player) +
                checkmate(player, depth) +
                castled(player);
    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    /**
     * checkmate bonus is dependent on depth
     * @param player
     * @param depth
     * @return
     */
    private static int checkmate(final Player player, final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int mobility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int pieceValue(final Player player) {
        return player.getActivePieces().stream()
                .mapToInt(Piece::getPieceValue)
                .sum();
    }
}
