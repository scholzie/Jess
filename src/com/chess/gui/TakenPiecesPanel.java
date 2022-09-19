package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class TakenPiecesPanel extends JPanel {
    private final JPanel northPanel, southPanel;
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);

    public TakenPiecesPanel() {
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        add(this.northPanel, BorderLayout.NORTH);
        add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move m : moveLog.getMoves()) {
            if(m.isAttack()) {
                final Piece takenPiece = m.getAttackedPiece();
                if(takenPiece.isWhite()){
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here. Can't determine if taken pieces are black or white.");
                }
            }
        }

        whiteTakenPieces.sort(new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for(final Piece takenPiece : whiteTakenPieces) {
            try {
                // TODO generalize for theme
                final BufferedImage image = ImageIO.read(new File("art/pieces/simple/" +
                        takenPiece.getPieceAlliance().toString().charAt(0) + "" + takenPiece + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.northPanel.add(imageLabel);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }

        for(final Piece takenPiece : blackTakenPieces) {
            try {
                // TODO generalize for theme
                final BufferedImage image = ImageIO.read(new File("art/pieces/simple/" +
                        takenPiece.getPieceAlliance().toString().charAt(0) + "" + takenPiece + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            } catch(final IOException e) {
                e.printStackTrace();
            }
        }
    }
}
