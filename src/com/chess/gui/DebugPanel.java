package com.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class DebugPanel extends JPanel implements Observer {
    private static final Dimension DEBUG_PANEL_DIMENSION = new Dimension(600, 150);
    private final JTextArea textArea;

    public DebugPanel() {
        super(new BorderLayout());
        this.textArea = new JTextArea("");
        add(this.textArea);
        setPreferredSize(DEBUG_PANEL_DIMENSION);
        validate();
        setVisible(true);
    }

    public void redo() {
        validate();
    }

    @Override
    public void update(final Observable o,
                       final Object arg) {
        this.textArea.setText(arg.toString().trim());
        redo();
    }
}
