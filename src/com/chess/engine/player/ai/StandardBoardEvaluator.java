package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50; // Half point for forcing move
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    protected static final StandardBoardEvaluator INSTANCE = new StandardBoardEvaluator();
    private static final int ATTACK_MULTIPLIER = 1;
    private static final int BISHOP_PAIR_BONUS = 25;

    public static StandardBoardEvaluator get() {
        return INSTANCE;
    }

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
                castle(player);
    }

    private static int castle(final Player player) {
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

    public String evaluationDetails(final Board board, final int depth) {
        return
                ("White Mobility : " + mobility(board.whitePlayer()) + "\n") +
                        "White kingThreats : " + kingThreats(board.whitePlayer(), depth) + "\n" +
                        "White attacks : " + attacks(board.whitePlayer()) + "\n" +
                        "White castle : " + castle(board.whitePlayer()) + "\n" +
                        "White pieceEval : " + pieceEvaluations(board.whitePlayer()) + "\n" +
//                        "White pawnStructure : " + pawnStructure(board.whitePlayer()) + "\n" +
                        "---------------------\n" +
                        "Black Mobility : " + mobility(board.blackPlayer()) + "\n" +
                        "Black kingThreats : " + kingThreats(board.blackPlayer(), depth) + "\n" +
                        "Black attacks : " + attacks(board.blackPlayer()) + "\n" +
                        "Black castle : " + castle(board.blackPlayer()) + "\n" +
                        "Black pieceEval : " + pieceEvaluations(board.blackPlayer()) + "\n" +
//                        "Black pawnStructure : " + pawnStructure(board.blackPlayer()) + "\n\n" +
                        "Final Score = " + evaluate(board, depth);
    }

    /**
     * Determine the relative strength of pieces based on their effectiveness
     * @param player
     * @return
     */
    private static int pieceEvaluations(final Player player) {
        int pieceValueTotal = 0;
        int numBishops = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueTotal += piece.getPieceValue() + piece.getLocationBonus();
            if(piece.getPieceType().isBishop()) {
                numBishops++;
            }
        }
        return pieceValueTotal + (numBishops == 2 ? BISHOP_PAIR_BONUS : 0);
    }

    private static int attacks(final Player player) {
        int attackScore = 0;
        for(final Move move : player.getLegalMoves()){
            if(move.isAttack()) {
                final Piece movedPiece = move.getMovedPiece();
                final Piece attackedPiece = move.getAttackedPiece();
                if(movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }
}
