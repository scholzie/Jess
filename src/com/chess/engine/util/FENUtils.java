package com.chess.engine.util;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Board.BoardBuilder;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.*;

import java.util.ArrayList;

public class FENUtils {

    private FENUtils() {
        throw new RuntimeException("Not Instantiable!");
    }

    public static Board createBoardFromFEN(final String fenString){
        return parseFEN(fenString);
    }

    public static String createFENFromBoard(final Board board) {
        return String.format("%s %s %s %s %s %s",
         calculateBoardText(board),
                calculateCurrentPlayerText(board),
                calculateCastleText(board),
                calculateEnPassentSquare(board),
                "0", "1");
    }

    private static Board parseFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
        final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
        final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
        final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        final BoardBuilder builder = new BoardBuilder();

        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r' -> {
                    builder.setPiece(new Rook(i, Alliance.BLACK));
                    i++;
                }
                case 'n' -> {
                    builder.setPiece(new Knight(i, Alliance.BLACK));
                    i++;
                }
                case 'b' -> {
                    builder.setPiece(new Bishop(i, Alliance.BLACK));
                    i++;
                }
                case 'q' -> {
                    builder.setPiece(new Queen(i, Alliance.BLACK));
                    i++;
                }
                case 'k' -> {
                    builder.setPiece(new King(i, Alliance.BLACK, blackKingSideCastle, blackQueenSideCastle));
                    i++;
                }
                case 'p' -> {
                    builder.setPiece(new Pawn(i, Alliance.BLACK));
                    i++;
                }
                case 'R' -> {
                    builder.setPiece(new Rook(i, Alliance.WHITE));
                    i++;
                }
                case 'N' -> {
                    builder.setPiece(new Knight(i, Alliance.WHITE));
                    i++;
                }
                case 'B' -> {
                    builder.setPiece(new Bishop(i, Alliance.WHITE));
                    i++;
                }
                case 'Q' -> {
                    builder.setPiece(new Queen(i, Alliance.WHITE));
                    i++;
                }
                case 'K' -> {
                    builder.setPiece(new King(i, Alliance.WHITE, whiteKingSideCastle, whiteQueenSideCastle));
                    i++;
                }
                case 'P' -> {
                    builder.setPiece(new Pawn(i, Alliance.WHITE));
                    i++;
                }
                default -> throw new RuntimeException("Invalid game configuration FEN string: " + gameConfiguration);
            }
        }

        builder.setMoveMaker(moveMaker(fenPartitions[1]));
        return builder.build();
    }

    private static Alliance moveMaker(final String moveMakerString) {
        if(moveMakerString.equals("w")) return Alliance.WHITE;
        if(moveMakerString.equals("b")) return Alliance.BLACK;
        throw new RuntimeException("Invalid FEN move maker string: " + moveMakerString);
    }

    private static boolean whiteKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("K");
    }

    private static boolean whiteQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("Q");
    }

    private static boolean blackKingSideCastle(final String fenCastleString) {
        return fenCastleString.contains("k");
    }

    private static boolean blackQueenSideCastle(final String fenCastleString) {
        return fenCastleString.contains("q");
    }

    private static String calculateEnPassentSquare(final Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if(enPassantPawn != null) {
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                    (8) * enPassantPawn.getPieceAlliance().getEnemyForwardDirection());
        }
        return "-";
    }

    private static String calculateCastleText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        if(board.whitePlayer().isKingSideCastleCapable()) builder.append("K");
        if(board.whitePlayer().isQueenSideCastleCapable()) builder.append("Q");
        if(board.blackPlayer().isKingSideCastleCapable()) builder.append("k");
        if(board.blackPlayer().isQueenSideCastleCapable()) builder.append("q");
        final String result = builder.toString();
        return result.isEmpty() ? "-" : result;
    }

    private static String calculateCurrentPlayerText(final Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }

    private static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_BOARD_TILES; i++) {
            final Piece piece = board.getPiece(i);
            final String tileText = piece == null ? "-" :
                                    piece.isWhite() ? piece.toString().toUpperCase() :
                                    piece.toString().toLowerCase();
            builder.append(tileText);
        }
        for(final int pos : (new int[]{8, 17, 26, 35, 44, 53, 62})) {
            builder.insert(pos, "/");
        }

        return builder.toString()
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");
    }
}
