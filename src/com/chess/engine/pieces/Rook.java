package com.chess.engine.pieces;

public class Rook {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {
            -56, -48, -40, -32, -24, -16, -8, // Vertical neg
            8, 16, 24, 32, 40, 48, 56, // Vertical pos
            -7, -6, -5, -4, -3, -2, -1, // Horizontal neg
            1, 2, 3, 4, 5, 6, 7  // Horizontal pos
    };

}
