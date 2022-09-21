package com.chess.tests.engine.player.ai;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.BoardBuilder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.move.MoveFactory;
import com.chess.engine.pieces.*;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import org.junit.jupiter.api.Test;

import static com.chess.engine.board.BoardUtils.getCoordinateAtPosition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MiniMaxTest {

    // TODO fix numBoardsEvaluated increment
    @Test
    public void testOpeningDepth1() {
        final Board board = Board.createStandardBoard();
        final MoveStrategy minMax = new MiniMax(1);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(20L, numBoardsEvaluated);
    }

    @Test
    public void testOpeningDepth2() {
        final Board board = Board.createStandardBoard();
        final MoveStrategy minMax = new MiniMax(2);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(400L, numBoardsEvaluated);
    }

    @Test
    public void testOpeningDepth3() {
        final Board board = Board.createStandardBoard();
        final MoveStrategy minMax = new MiniMax(3);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(8902L, numBoardsEvaluated);
    }

    @Test
    public void testOpeningDepth4() {
        final Board board = Board.createStandardBoard();
        final MoveStrategy minMax = new MiniMax(4);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(197281L, numBoardsEvaluated);
    }

    @Test
    public void testOpeningDepth5() {
        final Board board = Board.createStandardBoard();
        final MoveStrategy minMax = new MiniMax(5);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(4865609L, numBoardsEvaluated);
    }

    @Test
    public void testOpeningDepth6() {
        final Board board = Board.createStandardBoard();
        final MoveStrategy minMax = new MiniMax(6);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(119060324L, numBoardsEvaluated);
    }

    @Test
    /**
     * Given the known-fastest checkmate 1. f3 e5, 2. g4?? Qh4# (Fool's Mate)
     * Test that the MiniMax algorithm can determine 2...Qh4# is best.
     */
    public void testAIFoolsMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, getCoordinateAtPosition("f2"),
                        getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getTransitionBoard(), getCoordinateAtPosition("e7"),
                        getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getTransitionBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getTransitionBoard(), getCoordinateAtPosition("g2"),
                        getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveStrategy strategy = new MiniMax(4);
        final Move aiMove = strategy.execute(t3.getTransitionBoard());
        final Move bestMove = MoveFactory.createMove(t3.getTransitionBoard(), getCoordinateAtPosition("d8"),
                        getCoordinateAtPosition("h4"));


        assertEquals(aiMove, bestMove);

    }

    @Test
    public void testKiwiPeteDepth1() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Queen(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Bishop(14, Alliance.BLACK));
        builder.setPiece(new Bishop(16, Alliance.BLACK));
        builder.setPiece(new Knight(17, Alliance.BLACK));
        builder.setPiece(new Pawn(20, Alliance.BLACK));
        builder.setPiece(new Knight(21, Alliance.BLACK));
        builder.setPiece(new Pawn(22, Alliance.BLACK));
        builder.setPiece(new Pawn(33, Alliance.BLACK));
        builder.setPiece(new Pawn(47, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(27, Alliance.WHITE));
        builder.setPiece(new Knight(28, Alliance.WHITE));
        builder.setPiece(new Pawn(36, Alliance.WHITE));
        builder.setPiece(new Knight(42, Alliance.WHITE));
        builder.setPiece(new Queen(45, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Bishop(51, Alliance.WHITE));
        builder.setPiece(new Bishop(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MoveStrategy minMax = new MiniMax(1);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 46);
    }

//    @Test
//    public void testKiwiPeteDepth2() {
//        final BoardBuilder builder = new BoardBuilder();
//        // Black Layout
//        builder.setPiece(new Rook(0, Alliance.BLACK));
//        builder.setPiece(new King(4, Alliance.BLACK, false, false));
//        builder.setPiece(new Rook(7, Alliance.BLACK));
//        builder.setPiece(new Pawn(8, Alliance.BLACK));
//        builder.setPiece(new Pawn(10, Alliance.BLACK));
//        builder.setPiece(new Pawn(11, Alliance.BLACK));
//        builder.setPiece(new Queen(12, Alliance.BLACK));
//        builder.setPiece(new Pawn(13, Alliance.BLACK));
//        builder.setPiece(new Bishop(14, Alliance.BLACK));
//        builder.setPiece(new Bishop(16, Alliance.BLACK));
//        builder.setPiece(new Knight(17, Alliance.BLACK));
//        builder.setPiece(new Pawn(20, Alliance.BLACK));
//        builder.setPiece(new Knight(21, Alliance.BLACK));
//        builder.setPiece(new Pawn(22, Alliance.BLACK));
//        builder.setPiece(new Pawn(33, Alliance.BLACK));
//        builder.setPiece(new Pawn(47, Alliance.BLACK));
//        // White Layout
//        builder.setPiece(new Pawn(27, Alliance.WHITE));
//        builder.setPiece(new Knight(28, Alliance.WHITE));
//        builder.setPiece(new Pawn(36, Alliance.WHITE));
//        builder.setPiece(new Knight(42, Alliance.WHITE));
//        builder.setPiece(new Queen(45, Alliance.WHITE));
//        builder.setPiece(new Pawn(48, Alliance.WHITE));
//        builder.setPiece(new Pawn(49, Alliance.WHITE));
//        builder.setPiece(new Pawn(50, Alliance.WHITE));
//        builder.setPiece(new Bishop(51, Alliance.WHITE));
//        builder.setPiece(new Bishop(52, Alliance.WHITE));
//        builder.setPiece(new Pawn(53, Alliance.WHITE));
//        builder.setPiece(new Pawn(54, Alliance.WHITE));
//        builder.setPiece(new Pawn(55, Alliance.WHITE));
//        builder.setPiece(new Rook(56, Alliance.WHITE));
//        builder.setPiece(new King(60, Alliance.WHITE, false, false));
//        builder.setPiece(new Rook(63, Alliance.WHITE));
//        // Set the current player
//        builder.setMoveMaker(Alliance.WHITE);
//        final Board board = builder.build();
//        System.out.println(FenUtilities.createFENFromGame(board));
//        final MoveStrategy minMax = new MiniMax(2);
//        minMax.execute(board);
//        assertEquals(minMax.getNumBoardsEvaluated(), 1866L);
//    }
//
//    @Test
//    public void engineIntegrity1() {
//        final Board board = FenUtilities.createGameFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -\n");
//        final MoveStrategy minMax = new MiniMax(6);
//        minMax.execute(board);
//        assertEquals(minMax.getNumBoardsEvaluated(), 11030083);
//    }
//
//    @Test
//    public void testKiwiPeteDepth2Bug2() {
//        final Board board = FenUtilities.createGameFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
//        final MoveTransition t1 = board.currentPlayer()
//                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e5"),
//                        BoardUtils.getCoordinateAtPosition("d7")));
//        final MoveStrategy minMax = new MiniMax(1);
//        minMax.execute(t1.getTransitionBoard());
//        assertEquals(minMax.getNumBoardsEvaluated(), 45);
//    }
//
//    @Test
//    public void testChessDotComGame() {
//        final Board board = FenUtilities.createGameFromFEN("rnbk1bnr/1pN2ppp/p7/3P2q1/3Pp3/8/PPP1QPPP/RN2KB1R w KQ - 18 10");
//        final MoveStrategy minMax = new MiniMax(4);
//        minMax.execute(board);
//    }

    @Test
    public void testPosition3Depth1() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Rook(31, Alliance.BLACK));
        builder.setPiece(new Pawn(37, Alliance.BLACK));
        builder.setPiece(new King(39, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new King(24, Alliance.WHITE, false, false));
        builder.setPiece(new Pawn(25, Alliance.WHITE));
        builder.setPiece(new Rook(33, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MoveStrategy minMax = new MiniMax(1);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 14);
    }

    @Test
    public void testPosition3Depth2() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(19, Alliance.BLACK));
        builder.setPiece(new Rook(31, Alliance.BLACK));
        builder.setPiece(new Pawn(37, Alliance.BLACK));
        builder.setPiece(new King(39, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new King(24, Alliance.WHITE, false, false));
        builder.setPiece(new Pawn(25, Alliance.WHITE));
        builder.setPiece(new Rook(33, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MoveStrategy minMax = new MiniMax(2);
        minMax.execute(board);
        final long numBoardsEvaluated = minMax.getNumBoardsEvaluated();
        assertEquals(numBoardsEvaluated, 191);
    }
}
