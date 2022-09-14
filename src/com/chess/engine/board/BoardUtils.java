package com.chess.engine.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BoardUtils {

    INSTANCE;

    public static final int NUM_TILES = 64;
    private static final int NUM_TILES_PER_ROW = 8;
    private static final int START_TILE_INDEX = 0;

    // Tile addresses
    public final static int[] FIRST_COLUMN_CELLS = {0, 8, 16, 24, 32, 40, 48, 56};
    public final static int[] LAST_COLUMN_CELLS = {7, 15, 23, 31, 39, 47, 55, 63};
    public final static int[] FIRST_ROW_CELLS = {0, 1, 2, 3, 4, 5, 6, 7};
    public final static int[] LAST_ROW_CELLS = {56, 57, 58, 59, 60, 61, 62, 63};
    public final static int MIN_SQUARE = FIRST_COLUMN_CELLS[0];
    public final static int MAX_SQUARE = LAST_COLUMN_CELLS[LAST_COLUMN_CELLS.length - 1];

    public final List<Boolean> FIRST_COLUMN = initColumn(0);
    public final List<Boolean> SECOND_COLUMN = initColumn(1);
    public final List<Boolean> SEVENTH_COLUMN = initColumn(6);
    public final List<Boolean> EIGHTH_COLUMN = initColumn(7);


    private static List<Boolean> initColumn(int columnNumber) {

        final Boolean[] column = new Boolean[NUM_TILES];

        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < NUM_TILES);

        return List.of(column);
    }

    private static List<Boolean> initRow(int rowIndex) {

        final Boolean[] row = new Boolean[NUM_TILES];
        Arrays.fill(row, false);

        do {
            row[rowIndex] = true;
            rowIndex++;
        } while (rowIndex % NUM_TILES_PER_ROW != 0);

        return List.of(row);
    }

    private BoardUtils() {
        throw new RuntimeException("BoardUtils is not an instantiable object Class!");
    }

    public static boolean isValidTileCoordinate(final int tileCoordinate) {
        return tileCoordinate >= START_TILE_INDEX && tileCoordinate < NUM_TILES;
    }
}
