package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
    WHITE("White") {
        @Override
        public int getForwardDirection() { return -1; }
        @Override
        public int getEnemyForwardDirection() { return 1; }
        @Override
        public boolean isBlack() { return false; }
        @Override
        public boolean isWhite() { return true; }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK.get(position);
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK("Black") {
        @Override
        public int getForwardDirection() { return 1; }
        @Override
        public int getEnemyForwardDirection() { return -1; }
        @Override
        public boolean isBlack() { return true; }
        @Override
        public boolean isWhite() { return false; }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            // TODO check here if promotions don't work!
            return BoardUtils.FIRST_RANK.get(position);
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer,
                                   final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    private final String displayName;

    Alliance(final String displayName){
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    public abstract int getForwardDirection();
    public abstract int getEnemyForwardDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(final WhitePlayer whitePlayer,
                                        final BlackPlayer blackPlayer);
}
