package com.chess.GameUtils.pgn;

import com.chess.GameUtils.Game;
import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;
import com.chess.engine.player.Player;

public interface PGNPersistence {

    void persistGame(Game game);
    Move getNextBestMove(Board board, Player player,String gameText);
}
