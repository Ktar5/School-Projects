package mancala;

import mancala.model.MancalaModel;
import mancala.model.Side;
import mancala.strategy.*;

import javax.swing.*;

/**
 * The main class of the application
 */
public class MancalaTest {

    public static void main(String[] args) {

        //Define a list of all the mancala styles and create an option dialogue to choose a style for the game
        //This fulfills the strategy pattern
        MancalaStyle[] mancalaStyles = {new BeachStyle(), new DarkStyle(), new LightBlueStyle(), new ModernStyle(),
                new PurpleStyle(), new RetroStyle(), new WarmStyle(), new SupermanStyle(), new SJSUStyle()};
        MancalaStyle style = (MancalaStyle) JOptionPane.showInputDialog(null, "Choose a style: ",
                "Choose a Style", JOptionPane.QUESTION_MESSAGE, null,
                mancalaStyles, mancalaStyles[0]);

        //Define a list of all the options for starting amounts in each of the pits and display a dialogue for it
        Integer[] startingAmounts = {3, 4};
        Object startingAmountObject = JOptionPane.showInputDialog(null, "Choose starting amount",
                "Choose starting amount", JOptionPane.QUESTION_MESSAGE, null,
                startingAmounts, startingAmounts[0]);
        int startingAmount;
        if(startingAmountObject == null){
            startingAmount = 3;
        }else{
            startingAmount = (int) startingAmountObject;
        }

        //Create the model
        MancalaModel mancalaModel = new MancalaModel(Side.A, startingAmount);

        if(style == null){
            style = new SJSUStyle();
        }


        //Apply the model to a view & controller
        MancalaViewAndController mancalaViewAndController = new MancalaViewAndController(style, startingAmount, mancalaModel);
    }

}
