package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateCastlingMoves(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {

        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            // King Side
            if (this.board.getTile(61).isTileEmpty() && this.board.getTile(62).isTileEmpty()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()) {
                        // TODO impl move
                        kingCastles.add(null);
                    }
                }
            }

            // Queen Side
            if (this.board.getTile(57).isTileEmpty() && this.board.getTile(58).isTileEmpty() && this.board.getTile(59).isTileEmpty()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()) {
                        // TODO impl move
                        kingCastles.add(null);
                    }
                }
            }

            /*
                - Create list of rook positions
                - For each rook:
                    - Is it the first move?
                    - Are the spaces between king and rook clear?
                    - Are the tiles the king moves through under attack
             */
//            final int[] rookPositions = { 56, 63 };
//            final int kingPosition = this.playerKing.getPiecePosition();
//            for(final int rookPosition : rookPositions) {
//                final Piece rook = this.board.getTile(rookPosition).getPiece();
//                if(!(rook instanceof Rook)){
//                    throw new RuntimeException("Piece should be Rook but isn't");
//                }
//                if(!rook.isFirstMove()) {
//                    continue;
//                }
//
//                // Check for empty tiles
////                List<Tile> emptyTiles = new ArrayList<>();
//                int spaces = rookPosition - kingPosition;
//                int direction = spaces < 0 ? -1 : 1;
//
//                // Check for empty tiles
//                for(int i = 1; i < Math.abs(spaces); i++){
//                    int checkPos = kingPosition + i * direction;
//                    Tile checkTile = this.board.getTile(checkPos);
//                    if(!checkTile.isTileEmpty()) {
//                        break;
//                    }
//                    // Check for checks
//                    if(i <= 2 && !Player.calculateAttacksOnTile(checkPos, opponentsLegals).isEmpty()) {
//                        break;
//                    }
//
//                }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
