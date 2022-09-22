package com.chess.GameUtils.pgn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.regex.Pattern;

public class PGNUtilities {

    private PGNUtilities() {
        throw new RuntimeException("Not Instantiable!");


    }

    public static List<String> processMoveText(final String gameText) throws ParsePGNException {
        return gameText.isEmpty() ?
               Collections.emptyList() :
               createMovesFromPGN(gameText);
    }

    private static List<String> createMovesFromPGN(final String gameText) {
        if(!gameText.startsWith("1.")) {
            return Collections.emptyList();
        }

        final List<String> sanitizedMoves = new LinkedList<>(Arrays.asList(
                removeParentheses(gameText)
                        .replaceAll(Pattern.quote("$") + "[0-9+", "")
                        .replaceAll("[0-9]+\\s*\\.\\.\\.", "")
                        .split("\\s*[0-9]+" + Pattern.quote("."))));
        final List<String> processedData = removeEmptyText(sanitizedMoves);
        final String[] moveRows = processedData.toArray(new String[processedData.size()]);
        final ImmutableList.Builder<String> moves = new Builder<>();
        for(final String row : moveRows) {
            final String[] moveContent = removeExtraWhiteSpace(row).split(" ");
            if(moveContent.length == 1) {
                moves.add(moveContent[0]);
            } else if (moveContent.length == 2) {
                moves.add(moveContent[0]);
                moves.add(moveContent[1]);
            } else {
                System.out.println("Problem reading: " + gameText + ". Skipping.");
                return Collections.emptyList();
            }
        }
        return moves.build();
    }

    private static String removeExtraWhiteSpace(final String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    private static List<String> removeEmptyText(final List<String> moves) {
        final List<String> result = new ArrayList<>();
        for(final String moveText : moves){
            if(!moveText.isEmpty()){
                result.add(moveText);
            }
        }
        return result;
    }

    private static String removeParentheses(final String text) {
        return null;
    }
}
