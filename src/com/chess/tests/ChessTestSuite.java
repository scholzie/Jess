package com.chess.tests;

import com.chess.tests.engine.board.BoardTest;
import com.chess.tests.engine.board.CastlingTest;
import com.chess.tests.engine.pieces.PiecesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


// TODO move to JUnit 5
@RunWith(Suite.class)
@Suite.SuiteClasses({BoardTest.class,
                     PiecesTest.class,
CastlingTest.class})
public class ChessTestSuite {
}
