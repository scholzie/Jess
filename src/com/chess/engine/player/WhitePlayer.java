package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.move.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.move.Move.*;

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateCastlingMoves(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            // King Side
            if (this.board.getTile(61).isTileEmpty() &&
                    this.board.getTile(62).isTileEmpty()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        // TODO impl move
                        kingCastles.add(new KingSideCastleMove(
                                this.board,
                                this.playerKing,
                                62,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                61));
                    }
                }
            }

            // Queen Side
            if (this.board.getTile(57).isTileEmpty() &&
                    this.board.getTile(58).isTileEmpty() &&
                    this.board.getTile(59).isTileEmpty()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {
                        // TODO impl move
                        kingCastles.add(new QueenSideCastleMove(
                                this.board,
                                this.playerKing,
                                58,
                                (Rook)rookTile.getPiece(),
                                rookTile.getTileCoordinate(),
                                59));
                    }
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
