package com.chess.GameUtils;

import com.chess.GameUtils.pgn.PGNGameTags;

import java.util.Collections;
import java.util.List;

public class InvalidGame extends Game {

    final String badGameText;

    public InvalidGame(final PGNGameTags tags,
                       final String badGameText,
                       final String outcome) {
        super(tags, Collections.emptyList(), outcome);
        this.badGameText = badGameText;
    }

    @Override
    public String toString() {
        return "Invalid Game " + this.tags;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
