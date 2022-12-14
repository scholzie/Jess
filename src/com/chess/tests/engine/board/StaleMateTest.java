package com.chess.tests.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.BoardBuilder;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.player.MoveTransition;
import org.junit.jupiter.api.Test;

import static com.chess.engine.board.BoardUtils.getCoordinateAtPosition;
import static com.chess.engine.board.move.MoveFactory.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StaleMateTest {
    @Test
    public void testAnandKramnikStaleMate() {

        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(21, Alliance.BLACK));
        builder.setPiece(new King(36, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(29, Alliance.WHITE));
        builder.setPiece(new King(31, Alliance.WHITE, false, false));
        builder.setPiece(new Pawn(39, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.BLACK);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(createMove(board, getCoordinateAtPosition("e4"),
                        getCoordinateAtPosition("f5")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getTransitionBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheck());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(2, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(10, Alliance.WHITE));
        builder.setPiece(new King(26, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(createMove(board, getCoordinateAtPosition("c5"),
                        getCoordinateAtPosition("c6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getTransitionBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheck());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate2() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(0, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(16, Alliance.WHITE));
        builder.setPiece(new King(17, Alliance.WHITE, false, false));
        builder.setPiece(new Bishop(19, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        assertFalse(board.currentPlayer().isInStaleMate());
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(createMove(board, getCoordinateAtPosition("a6"),
                        getCoordinateAtPosition("a7")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getTransitionBoard().currentPlayer().isInStaleMate());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheck());
        assertFalse(t1.getTransitionBoard().currentPlayer().isInCheckMate());
    }
}
