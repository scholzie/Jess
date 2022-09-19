package com.chess.engine.board;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum BoardUtils {

    INSTANCE;

    public static final int NUM_BOARD_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    private static final int START_TILE_INDEX = 0;

    // Tile addresses
    public final static int[] FIRST_COLUMN_CELLS = {0, 8, 16, 24, 32, 40, 48, 56};
    public final static int[] LAST_COLUMN_CELLS = {7, 15, 23, 31, 39, 47, 55, 63};
    public final static int[] FIRST_ROW_CELLS = {0, 1, 2, 3, 4, 5, 6, 7};
    public final static int[] LAST_ROW_CELLS = {56, 57, 58, 59, 60, 61, 62, 63};
    public final static int MIN_SQUARE = FIRST_COLUMN_CELLS[0];
    public final static int MAX_SQUARE = LAST_COLUMN_CELLS[LAST_COLUMN_CELLS.length - 1];

    public final static List<Boolean> A_FILE = initColumn(0);
    public final static List<Boolean> B_FILE = initColumn(1);
    public final static List<Boolean> G_FILE = initColumn(6);
    public final static List<Boolean> H_FILE = initColumn(7);

    // Define Rows
    // TODO Can I use a 2d ROWS array?
    public final static List<Boolean> EIGHTH_RANK = initRow(0);
    public final static List<Boolean> SEVENTH_RANK = initRow(8);
    public final static List<Boolean> SIXTH_RANK = initRow(16);
    public final static List<Boolean> FIFTH_RANK = initRow(24);
    public final static List<Boolean> FOURTH_RANK = initRow(32);
    public final static List<Boolean> THIRD_RANK = initRow(40);
    public final static List<Boolean> SECOND_RANK = initRow(48);
    public final static List<Boolean> FIRST_RANK = initRow(56);

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();

    public static final Map<String, Integer> BOARD_COORDINATES = initializeBoardCoordinateMap();


    private static List<Boolean> initColumn(int columnNumber) {

        final Boolean[] column = new Boolean[NUM_BOARD_TILES];

        Arrays.fill(column, false);
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while(columnNumber < NUM_BOARD_TILES);

        return List.of(column);
    }

    private static List<Boolean> initRow(int rowIndex) {

        final Boolean[] row = new Boolean[NUM_BOARD_TILES];
        Arrays.fill(row, false);

        do {
            row[rowIndex] = true;
            rowIndex++;
        } while (rowIndex % NUM_TILES_PER_ROW != 0);

        return List.of(row);
    }

    private static String[] initializeAlgebraicNotation() {
        return null;
    }

    private static Map<String, Integer> initializeBoardCoordinateMap() {
        return null;
    }

//    private BoardUtils() {
//        throw new RuntimeException("BoardUtils is not an instantiable object Class!");
//    }

    public static boolean isValidTileCoordinate(final int tileCoordinate) {
        return tileCoordinate >= START_TILE_INDEX && tileCoordinate < NUM_BOARD_TILES;
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }

    public static int getCoordinateAtPosition(final String position) {
        return BOARD_COORDINATES.get(position);
    }
}
