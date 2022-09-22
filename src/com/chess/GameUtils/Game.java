package com.chess.GameUtils;

import com.chess.GameUtils.pgn.PGNGameTags;
import com.chess.engine.Alliance;

import java.util.List;

public abstract class Game {

    protected final List<String> moves;
    protected final String winner;
    protected final PGNGameTags tags;

    Game(final PGNGameTags tags,
        final List<String> moves,
         final String gameOutcome) {
        this.tags = tags;
        this.moves = moves;
        this.winner = calculateWinner(gameOutcome);
    }

    @Override
    public String toString() {
        return this.tags.toString();
    }

    public List<String> getMoves() {
        return this.moves;
    }

    public String getWinner() {
        return this.winner;
    }

    private static String calculateWinner(final String gameOutcome) {
        if(gameOutcome.equals("1-0")) {
            return Alliance.WHITE.toString();
        }
        if(gameOutcome.equals("0-1")) {
            return Alliance.BLACK.toString();
        }
        if(gameOutcome.equals("1/2-1/2")) {
            return "Draw";
        }
        return "None"; // TODO "Game could continue?"
    }

    public abstract boolean isValid();
}
