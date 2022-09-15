package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
    }

    private King establishKing() {
        for(final Piece piece : getActivePieces()){
            if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Board not in valid state. No king found.");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    // TODO: implement
    public boolean isInCheck() {
        return false;
    }

    // TODO: implement
    public boolean isInCheckmate() {
        return false;
    }

    // TODO: implement
    public boolean isInStalemate() {
        return false;
    }

    // TODO: implement
    public boolean isCastled() {
        return false;
    }

    // TODO: implement
    public MoveTransition makeMove(final Move move) {
        return null;
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
