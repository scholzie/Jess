package com.chess.tests.engine.util;

import com.chess.GameUtils.pgn.PGNUtilities;
import com.chess.GameUtils.pgn.ParsePGNException;
import com.chess.GameUtils.pgn.SQLiteGamePersistence;
import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;
import com.chess.engine.player.MoveTransition;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PGNParserTest {
    @Test
    public void test1() throws IOException {
        doTest("com/chess/tests/pgn/t1.pgn");
    }

    @Test
    public void test2() throws IOException {
        doTest("com/chess/tests/pgn/t2.pgn");
    }

    @Test
    public void test3() throws IOException {
        doTest("com/chess/tests/pgn/t3.pgn");
    }

    @Test
    public void test4() throws IOException {
        doTest("com/chess/tests/pgn/t4.pgn");
    }

    @Test
    public void test5() throws IOException {
        doTest("com/chess/tests/pgn/smallerTest.pgn");
    }

    @Test
    public void test6() throws IOException {
        doTest("com/chess/tests/pgn/t6.pgn");
    }

    @Test
    public void test8() throws IOException {
        doTest("com/chess/tests/pgn/t8.pgn");
    }

    @Test
    public void test9() throws IOException {
        doTest("com/chess/tests/pgn/t9.pgn");
    }

    @Test
    public void testPawnPromotion() throws IOException {
        doTest("com/chess/tests/pgn/queenPromotion.pgn");
    }

    @Test
    public void test10() throws IOException {
        doTest("com/chess/tests/pgn/t10.pgn");
    }

    @Test
    public void testMax() throws IOException {
        int maxId = SQLiteGamePersistence.get().getMaxGameRow();
        System.out.println("max id = " +maxId);
    }

    @Test
    public void testWithErol() throws IOException {
        final Board board = Board.createStandardBoard();
        final Move move = SQLiteGamePersistence.get().getNextBestMove(board, board.currentPlayer(), "");
        final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
        final Move move2 = SQLiteGamePersistence.get()
                .getNextBestMove(moveTransition.getTransitionBoard(),
                        moveTransition.getTransitionBoard().currentPlayer(), "e4");
        System.out.println("move 2 = " + move2);
        assertFalse(move2 instanceof Move.NullMove);
    }

    private static void doTest(final String testFilePath) throws IOException {
        final URL url = Resources.getResource(testFilePath);
        final File testPGNFile = new File(url.getFile());
        PGNUtilities.persistPGNFile(testPGNFile);
    }
}
