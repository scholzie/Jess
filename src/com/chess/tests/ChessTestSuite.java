package com.chess.tests;

import com.chess.tests.engine.board.BoardTest;
import com.chess.tests.engine.board.CastlingTest;
import com.chess.tests.engine.board.CheckMateTest;
import com.chess.tests.engine.board.StaleMateTest;
import com.chess.tests.engine.pieces.PiecesTest;
import com.chess.tests.engine.player.PlayerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


// TODO move to JUnit 5
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BoardTest.class,
        CastlingTest.class,
        CheckMateTest.class,
        PiecesTest.class,
        PlayerTest.class,
        StaleMateTest.class,
})
public class ChessTestSuite {
}
