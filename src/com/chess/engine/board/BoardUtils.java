package com.chess.engine.board;

public class BoardUtils {

    // Tile addresses
    public final static int[] FIRST_COLUMN_CELLS = {0, 8, 16, 24, 32, 40, 48, 56};
    public final static int[] LAST_COLUMN_CELLS = {7, 15, 23, 31, 39, 47, 55, 63};
    public final static int[] FIRST_ROW_CELLS = {0, 1, 2, 3, 4, 5, 6, 7};
    public final static int[] LAST_ROW_CELLS = {56, 57, 58, 59, 60, 61, 62, 63};
    public final static int MIN_SQUARE = FIRST_COLUMN_CELLS[0];
    public final static int MAX_SQUARE = LAST_COLUMN_CELLS[LAST_COLUMN_CELLS.length - 1];

    // Helper boolean arrays
    // TODO: Is there a way to dynamically create this array, similar to a list comprehension in python?
    public static final boolean[] FIRST_COLUMN = null;
    public static final boolean[] SECOND_COLUMN = null;
    public static final boolean[] SEVENTH_COLUMN = null;
    public static final boolean[] EIGHTH_COLUMN = null;

    private BoardUtils() {
        throw new RuntimeException("BoardUtils is not an instantiable object Class!");
    }

    public static boolean isValidTileCoordinate(int tileCoordinate) {
        return tileCoordinate >= MIN_SQUARE && tileCoordinate <= MAX_SQUARE;
    }
}
