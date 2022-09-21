package com.chess.tests.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.BoardBuilder;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.move.MoveFactory;
import com.chess.engine.pieces.*;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static com.chess.engine.board.BoardUtils.getCoordinateAtPosition;
import static org.junit.jupiter.api.Assertions.*;

public class PiecesTest {

    @Test
    public void testMiddleQueenOnEmptyBoard() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Queen(36, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(31, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testLegalMoveAllAvailable() {

        final BoardBuilder boardBuilder = new BoardBuilder();
        // Black Layout
        boardBuilder.setPiece(new King(4, Alliance.BLACK, false, false));
        boardBuilder.setPiece(new Knight(28, Alliance.BLACK));
        // White Layout
        boardBuilder.setPiece(new Knight(36, Alliance.WHITE));
        boardBuilder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        boardBuilder.setMoveMaker(Alliance.WHITE);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        assertEquals(13, whiteLegals.size());
        final Move wm1 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("d6"));
        final Move wm2 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("f6"));
        final Move wm3 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("c5"));
        final Move wm4 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("g5"));
        final Move wm5 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("c3"));
        final Move wm6 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("g3"));
        final Move wm7 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("d2"));
        final Move wm8 = MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("f2"));

        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        assertTrue(whiteLegals.contains(wm3));
        assertTrue(whiteLegals.contains(wm4));
        assertTrue(whiteLegals.contains(wm5));
        assertTrue(whiteLegals.contains(wm6));
        assertTrue(whiteLegals.contains(wm7));
        assertTrue(whiteLegals.contains(wm8));

        final BoardBuilder boardBuilder2 = new BoardBuilder();
        // Black Layout
        boardBuilder2.setPiece(new King(4, Alliance.BLACK, false, false));
        boardBuilder2.setPiece(new Knight(28, Alliance.BLACK));
        // White Layout
        boardBuilder2.setPiece(new Knight(36, Alliance.WHITE));
        boardBuilder2.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        boardBuilder2.setMoveMaker(Alliance.BLACK);
        final Board board2 = boardBuilder2.build();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();

        final Move bm1 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("d7"));
        final Move bm2 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("f7"));
        final Move bm3 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("c6"));
        final Move bm4 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("g6"));
        final Move bm5 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("c4"));
        final Move bm6 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("g4"));
        final Move bm7 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("d3"));
        final Move bm8 = MoveFactory
                .createMove(board2, getCoordinateAtPosition("e5"), getCoordinateAtPosition("f3"));

        assertEquals(13, blackLegals.size());

        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));
        assertTrue(blackLegals.contains(bm3));
        assertTrue(blackLegals.contains(bm4));
        assertTrue(blackLegals.contains(bm5));
        assertTrue(blackLegals.contains(bm6));
        assertTrue(blackLegals.contains(bm7));
        assertTrue(blackLegals.contains(bm8));
    }

    @Test
    public void testKnightInCorners() {
        final BoardBuilder boardBuilder = new BoardBuilder();
        boardBuilder.setPiece(new King(4, Alliance.BLACK, false, false));
        boardBuilder.setPiece(new Knight(0, Alliance.BLACK));
        boardBuilder.setPiece(new Knight(56, Alliance.WHITE));
        boardBuilder.setPiece(new King(60, Alliance.WHITE, false, false));
        boardBuilder.setMoveMaker(Alliance.WHITE);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(7, whiteLegals.size());
        assertEquals(7, blackLegals.size());
        final Move wm1 = MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("b3"));
        final Move wm2 = MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("c2"));
        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        final Move bm1 = MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("b6"));
        final Move bm2 = MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("c7"));
        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));

    }

    @Test
    public void testMiddleBishopOnEmptyBoard() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(35, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(18, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("d4"), getCoordinateAtPosition("a7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("d4"), getCoordinateAtPosition("b6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("d4"), getCoordinateAtPosition("c5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("d4"), getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("d4"), getCoordinateAtPosition("f2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("d4"), getCoordinateAtPosition("g1"))));
    }

    @Test
    public void testTopLeftBishopOnEmptyBoard() {
        BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(0, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(board.getPiece(0), board.getPiece(0));
        assertNotNull(board.getPiece(0));
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a8"), getCoordinateAtPosition("h1"))));
    }

    @Test
    public void testTopRightBishopOnEmptyBoard() {
        BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(7, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h8"), getCoordinateAtPosition("a1"))));
    }

    @Test
    public void testBottomLeftBishopOnEmptyBoard() {
        BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(56, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("a1"), getCoordinateAtPosition("h8"))));
    }

    @Test
    public void testBottomRightBishopOnEmptyBoard() {
        BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(63, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(12, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("h1"), getCoordinateAtPosition("a8"))));
    }

    @Test
    public void testMiddleRookOnEmptyBoard() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Rook(36, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(18, whiteLegals.size());
        assertEquals(5, blackLegals.size());
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(MoveFactory
                .createMove(board, getCoordinateAtPosition("e4"), getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testPawnPromotion() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Rook(3, Alliance.BLACK));
        builder.setPiece(new King(22, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Pawn(15, Alliance.WHITE));
        builder.setPiece(new King(52, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Move m1 = MoveFactory.createMove(board, getCoordinateAtPosition(
                "h7"), getCoordinateAtPosition("h8"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = MoveFactory.createMove(t1.getTransitionBoard(), getCoordinateAtPosition("d8"), getCoordinateAtPosition("h8"));
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = MoveFactory.createMove(t2.getTransitionBoard(), getCoordinateAtPosition("e2"), getCoordinateAtPosition("d2"));
        final MoveTransition t3 = board.currentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
    }

    @Test
    public void testSimpleWhiteEnPassant() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Move m1 = MoveFactory.createMove(board, getCoordinateAtPosition(
                "e2"), getCoordinateAtPosition("e4"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = MoveFactory.createMove(t1.getTransitionBoard(), getCoordinateAtPosition("e8"), getCoordinateAtPosition("d8"));
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = MoveFactory.createMove(t2.getTransitionBoard(), getCoordinateAtPosition("e4"), getCoordinateAtPosition("e5"));
        final MoveTransition t3 = t2.getTransitionBoard().currentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = MoveFactory.createMove(t3.getTransitionBoard(), getCoordinateAtPosition("d7"), getCoordinateAtPosition("d5"));
        final MoveTransition t4 = t3.getTransitionBoard().currentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = MoveFactory.createMove(t4.getTransitionBoard(), getCoordinateAtPosition("e5"), getCoordinateAtPosition("d6"));
        final MoveTransition t5 = t4.getTransitionBoard().currentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testSimpleBlackEnPassant() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Move m1 = MoveFactory.createMove(board, getCoordinateAtPosition(
                "e1"), getCoordinateAtPosition("d1"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = MoveFactory.createMove(t1.getTransitionBoard(), getCoordinateAtPosition("d7"), getCoordinateAtPosition("d5"));
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = MoveFactory.createMove(t2.getTransitionBoard(), getCoordinateAtPosition("d1"), getCoordinateAtPosition("c1"));
        final MoveTransition t3 = t2.getTransitionBoard().currentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = MoveFactory.createMove(t3.getTransitionBoard(), getCoordinateAtPosition("d5"), getCoordinateAtPosition("d4"));
        final MoveTransition t4 = t3.getTransitionBoard().currentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = MoveFactory.createMove(t4.getTransitionBoard(), getCoordinateAtPosition("e2"), getCoordinateAtPosition("e4"));
        final MoveTransition t5 = t4.getTransitionBoard().currentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
        final Move m6 = MoveFactory.createMove(t5.getTransitionBoard(), getCoordinateAtPosition("d4"), getCoordinateAtPosition("e3"));
        final MoveTransition t6 = t5.getTransitionBoard().currentPlayer().makeMove(m6);
        assertTrue(t6.getMoveStatus().isDone());
    }

    @Test
    public void testEnPassant2() {
        final Board board = Board.createStandardBoard();
        final Move m1 = MoveFactory.createMove(board, getCoordinateAtPosition(
                "e2"), getCoordinateAtPosition("e3"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = MoveFactory.createMove(t1.getTransitionBoard(), getCoordinateAtPosition("h7"), getCoordinateAtPosition("h5"));
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = MoveFactory.createMove(t2.getTransitionBoard(), getCoordinateAtPosition("e3"), getCoordinateAtPosition("e4"));
        final MoveTransition t3 = t2.getTransitionBoard().currentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = MoveFactory.createMove(t3.getTransitionBoard(), getCoordinateAtPosition("h5"), getCoordinateAtPosition("h4"));
        final MoveTransition t4 = t3.getTransitionBoard().currentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = MoveFactory.createMove(t4.getTransitionBoard(), getCoordinateAtPosition("g2"), getCoordinateAtPosition("g4"));
        final MoveTransition t5 = t4.getTransitionBoard().currentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testKingEquality() {
        final Board board = Board.createStandardBoard();
        final Board board2 = Board.createStandardBoard();
        assertEquals(board.getPiece(60), board2.getPiece(60));
        assertNotNull(board.getPiece(60));
    }

    @Test
    public void testHashCode() {
        final Board board = Board.createStandardBoard();
        final Set<Piece> pieceSet = Sets.newHashSet(board.getAllPieces());
        final Set<Piece> whitePieceSet = Sets.newHashSet(board.getWhitePieces());
        final Set<Piece> blackPieceSet = Sets.newHashSet(board.getBlackPieces());
        assertEquals(32, pieceSet.size());
        assertEquals(16, whitePieceSet.size());
        assertEquals(16, blackPieceSet.size());
    }
}
