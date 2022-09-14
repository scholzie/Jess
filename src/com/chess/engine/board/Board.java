package com.chess.engine.board;

public class Board {

    private final static int minSquare = 1;
    private final static int maxSquare = 64;
    public static boolean isValidTileCoordinate(int tileCoordinate) {
        return tileCoordinate >= minSquare && tileCoordinate <= maxSquare;
    }

    public Tile getTile(int tileCoordinate) {
        return null;
    }
}
