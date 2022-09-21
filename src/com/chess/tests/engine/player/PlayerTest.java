package com.chess.tests.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.BoardBuilder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.move.MoveFactory;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.MoveTransition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testSimpleEvaluation() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        // TODO reenable
        //assertEquals(StandardBoardEvaluator.get().evaluate(t2.getToBoard(), 0), 0);
    }

    @Test
    public void testBug() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c2"),
                        BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("a6")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getToBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("a4")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getToBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertFalse(t4.getMoveStatus().isDone());
    }

    @Test
    public void testDiscoveredCheck() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        builder.setPiece(new Rook(24, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Bishop(44, Alliance.WHITE));
        builder.setPiece(new Rook(52, Alliance.WHITE));
        builder.setPiece(new King(58, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e3"),
                        BoardUtils.getCoordinateAtPosition("b6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().currentPlayer().isInCheck());
        final MoveTransition t2 = t1.getToBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("a5"),
                        BoardUtils.getCoordinateAtPosition("b5")));
        assertFalse(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t1.getToBoard()
                .currentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("a5"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t3.getMoveStatus().isDone());
    }

    // TODO Reenable test
//    @Test
//    public void testUnmakeMove() {
//        final Board board = Board.createStandardBoard();
//        final Move m1 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
//                BoardUtils.getCoordinateAtPosition("e4"));
//        final MoveTransition t1 = board.currentPlayer()
//                .makeMove(m1);
//        assertTrue(t1.getMoveStatus().isDone());
//        t1.getToBoard().currentPlayer().getOpponent().unMakeMove(m1);
//    }

    @Test
    public void testIllegalMove() {
        final Board board = Board.createStandardBoard();
        final Move m1 = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                BoardUtils.getCoordinateAtPosition("e6"));
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(m1);
        assertFalse(t1.getMoveStatus().isDone());
    }
}
