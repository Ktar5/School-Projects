package mancala;

import mancala.model.MancalaModel;
import mancala.model.Pit;
import mancala.model.Side;
import mancala.shapes.MancalaPitShape;
import mancala.shapes.PitShape;
import mancala.strategy.MancalaStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Display and mutate the MancalaModel
 * <p>
 * This class serves as both the View and Controller in the MVC Pattern because
 * the buttons directly mutate the MancalaModel
 * <p>
 * This class also accepts a MancalaStyle, following the Strategy design pattern
 */
public class MancalaViewAndController {
    private MancalaStyle style;
    private PitShape[] pitShapes = new PitShape[14];
    private JLabel[] pitLabels = new JLabel[14];
    private JFrame frame;
    private JButton undoJButton;

    public MancalaViewAndController(MancalaStyle style, int startingAmount, MancalaModel model) {
        this.style = style;
        frame = new JFrame();

        //Add B's pits
        for (int i = 0; i < 6; i++) {
            pitShapes[i] = new PitShape(style, startingAmount, Side.B, Integer.valueOf(Pit.values()[i].name().substring(1)));
            pitLabels[i] = new JLabel(pitShapes[i]);
        }

        //Add A's mancala
        pitShapes[6] = new MancalaPitShape(style, Side.A);
        pitLabels[6] = new JLabel(pitShapes[6]);

        //Add A's pits
        for (int i = 7; i < 13; i++) {
            pitShapes[i] = new PitShape(style, startingAmount, Side.A, Integer.valueOf(Pit.values()[i].name().substring(1)));
            pitLabels[i] = new JLabel(pitShapes[i]);
        }

        //Add B's mancala
        pitShapes[13] = new MancalaPitShape(style, Side.B);
        pitLabels[13] = new JLabel(pitShapes[13]);

        for (int i = 0; i < pitLabels.length; i++) {
            int finalI = i;
            pitLabels[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    model.takeTurn(Pit.values()[finalI], MancalaViewAndController.this);
                }
                @Override public void mousePressed(MouseEvent e) { }
                @Override public void mouseReleased(MouseEvent e) { }
                @Override public void mouseEntered(MouseEvent e) { }
                @Override public void mouseExited(MouseEvent e) { }
            });
        }

        //Layout pits
        GridLayout gridLayout = new GridLayout(2, 6);
        gridLayout.setVgap(50);
        JPanel pitsPanel = new JPanel(gridLayout);

        for (int i = 0; i < pitShapes.length; i++) {
            if (i != 6 && i != 13) {
                if (i > 6) {
                    //I know this is confusing but it assists in making the pits in the correct location for the second row
                    pitsPanel.add(pitLabels[19 - i]);
                } else {
                    pitsPanel.add(pitLabels[i]);
                }
            }
        }


        //Create the undo button
        undoJButton = new JButton("Undo");
        undoJButton.setEnabled(false);
        undoJButton.addActionListener(e -> {
            model.undo(); //take the undo action
            MancalaViewAndController.this.updateDisplay(model); //update the display to reflect the undo
            undoJButton.setEnabled(false); //disable the undo button, can't undo multiple turns back
        });

        //Layout entire board with the two mancala pits

        frame.setLayout(new FlowLayout());

        //Add everything to the frame
        frame.add(pitLabels[13]);
        frame.add(pitsPanel);
        frame.add(pitLabels[6]);
        frame.add(undoJButton);

        frame.setSize(500, 500);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //Set the initial title of the game
        frame.setTitle("Currently player: " + model.getCurrentSide().name() + "'s turn!");
    }

    /**
     * This method should only be used internally. It updates the display with a resulting
     * TurnResult received from the MancalaModel
     */
    public void update(MancalaModel.TurnResult result, MancalaModel model) {
        switch (result) {
            case NOT_YOUR_SIDE:
                //Don't update
                JOptionPane.showMessageDialog(frame, "You cannot select the opposite side's pieces.");
                return;
            case EMPTY_PIT:
                //Dont update
                JOptionPane.showMessageDialog(frame, "You cannot select an empty pit.");
                return;
            case WINNER_A:
                updateDisplay(model);
                JOptionPane.showMessageDialog(frame, "Player A Wins!!!!!");
                return;
            case WINNER_B:
                updateDisplay(model);
                JOptionPane.showMessageDialog(frame, "Player B Wins!!!!!");
                return;
            case CANT_USE_MANCALA:
                JOptionPane.showMessageDialog(frame, "You cannot select your mancala.");
                //Don't update
                return;
            case SUCCESS:
                //continue
                updateDisplay(model);
                break;
            case GETS_ANOTHER_TURN:
                updateDisplay(model);
                JOptionPane.showMessageDialog(frame, "Player gets another turn!.");
                break;
        }
    }

    //This piece of code simply updates the pits, title, and undo button
    private void updateDisplay(MancalaModel model) {
        for (int i = 0; i < pitShapes.length; i++) {
            pitShapes[i].setStoneCount(model.getPits()[i]);
            pitLabels[i].repaint();
        }

        frame.setTitle("Currently player: " + model.getCurrentSide().name() + "'s turn!");
        undoJButton.setEnabled(model.isCanUndo());
    }

}
