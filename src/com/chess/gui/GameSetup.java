package com.chess.gui;

import com.chess.gui.Table.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSetup extends JDialog {

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;
    private JSpinner searchDepthSpinner;


    GameSetup(final JFrame frame,
              final boolean modal) {
        super(frame, modal);

        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton whiteHumanButton = new JRadioButton(PlayerType.HUMAN.getPlayerTypeLabel());
        final JRadioButton whiteComputerButton = new JRadioButton(PlayerType.COMPUTER.getPlayerTypeLabel());
        final JRadioButton blackHumanButton = new JRadioButton(PlayerType.HUMAN.getPlayerTypeLabel());
        final JRadioButton blackComputerButton = new JRadioButton(PlayerType.COMPUTER.getPlayerTypeLabel());

        whiteHumanButton.setActionCommand(PlayerType.HUMAN.getPlayerTypeLabel());
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);

        myPanel.add(new JLabel("Search"));
        this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(6, 0, Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(e -> {
            whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            GameSetup.this.setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            System.out.println("Cancel");
            GameSetup.this.setVisible(false);
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    private static JSpinner addLabeledSpinner(final Container container,
                                              final String label,
                                              final SpinnerModel model) {
        final JLabel l = new JLabel(label);
        container.add(l);
        final JSpinner s = new JSpinner(model);
        l.setLabelFor(s);
        container.add(s);
        return s;
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }
}
