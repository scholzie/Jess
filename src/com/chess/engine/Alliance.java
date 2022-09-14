package com.chess.engine;

public enum Alliance {
    WHITE {
        @Override
        public int getForwardDirection() {
            return -1;
        }
    },
    BLACK {
        @Override
        public int getForwardDirection() {
            return 1;
        }
    };

    public abstract int getForwardDirection();
}
