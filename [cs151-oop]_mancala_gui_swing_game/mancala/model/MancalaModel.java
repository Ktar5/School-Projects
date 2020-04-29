package mancala.model;

import mancala.MancalaViewAndController;

import java.util.UUID;

/**
 * Represents the model of the Mancala game board. Stores all data and provides all turn logic
 */
public class MancalaModel {
    /**
     * Holds the number of stones in each pit.
     * The pit represented by each index is the Pit.ordinal() method value
     */
    private int[] pits = new int[14];

    /**
     * The side that is currently able to make a turn
     */
    private Side currentSide;

    /**
     * Whether or not the user is able to undo
     * Informs the View about whether the undo button should be enabled or disabled
     */
    private boolean canUndo = false;
    //The number of undos left for this turn
    private int numberUndosLeft = 3;

    //The current turn id
    //This is used to determine distinct turns
    private UUID currentTurnId;

    //The captured data from the last turn.
    //Restore this data on undo
    private int[] previousTurn = new int[14];
    private Side previousSide;
    private UUID previousTurnId;

    /**
     * Create a mancala game state with the specified starting side and
     * the starting amount of stones in each non-mancala pit
     */
    public MancalaModel(Side startingSide, int startingAmount) {
        assert startingAmount <= 4 && startingAmount >= 1;

        this.currentSide = startingSide;

        for (int i = 0; i < pits.length; i++) {
            if (Pit.B_MANCALA.ordinal() == i || Pit.A_MANCALA.ordinal() == i) {
                pits[i] = 0;
            } else {
                pits[i] = startingAmount;
            }
        }
    }

    //================================================================================
    //                              GAME-LOGIC
    //================================================================================

    /**
     * Takes a mancala turn with the specified Pit in accordance to the following rules:
     * 1. Pick up all in pit
     * 2. Place one in each pit until you run out counter clockwise (A1 -> A6, B1->B6)
     * 3. Place one stone in the mancala, it counts as a pit, if you have more left,
     * start placing into opponents side
     * 4. Skip opponent's mancala tho
     * 5. If last stone drops into your mancala, you get another free turn
     * 6. If the last stone you drop is in an empty pit on your side, you get to take that
     * stone and all of your opponent's stones in the opposite pit
     *
     * @param pit The pit to take out of
     */
    public void takeTurn(Pit pit, MancalaViewAndController mancalaView) {
        //Make sure that they use their own side
        if (pit.side != currentSide) {
            //Inform view to display the "Not your side" error message.
            mancalaView.update(TurnResult.NOT_YOUR_SIDE, this);
            return;
        }

        //Make sure they don't try to use their mancala
        if (pit.mancala) {
            //Inform view to display the "Can't use mancalas" error message.
            mancalaView.update(TurnResult.CANT_USE_MANCALA, this);
            return;
        }

        //Make sure they don't try to use an empty pit
        if (getAmountInPit(pit) == 0) {
            //Inform view to display the "Can't use empty pit" error message.
            mancalaView.update(TurnResult.EMPTY_PIT, this);
            return;
        }

        //The turn proceeds as normal, first make a copy of current board state in case they want to undo
        captureBoardState();

        //execute inside of if statement when two turns happen back to back with no undoing in the middle
        if (previousTurnId == null || !previousTurnId.equals(currentTurnId)) {
            resetUndoCounter();
        }

        //Clear the pit and fetch the amount that should be in their hand
        int amountInHand = clearPit(pit);

        //Add one to each pit in a counter-clockwise direction
        while (amountInHand > 0) {
            pit = pit.next();
            //If the next pit would be the opponent's mancala, skip
            if (pit.mancala && !pit.side.equals(currentSide)) {
                pit = pit.next();
            }
            addOneToPit(pit);
            amountInHand -= 1;
        }

        //A turn has completed, update turn id ; AKA a new turn will have started
        //If we undo, we set the current turn id to the previous turn id
        //and that's what we use to know if we're inside of an undo turn
        previousTurnId = currentTurnId;
        currentTurnId = UUID.randomUUID();

        //Check if the last stone was put into an empty pit
        //Would equal 1 because we placed the last one there
        if (pit.side.equals(currentSide) && getAmountInPit(pit) == 1 && !pit.mancala) {
            addToMancala(currentSide, clearPit(pit) + clearPit(pit.opposite()));
        }

        //Check win conditions
        Side winningSide = checkWinConditions();
        if (winningSide == null) {
            //If there was no winning side and if you end in your mancala
            if (pit.mancala && pit.side.equals(currentSide)) {
                //Keep current side turn
                //Inform view to display the "Player gets another turn" message.
                mancalaView.update(TurnResult.GETS_ANOTHER_TURN, this);
                return;
            }

            //A normal non-game-ending move occurred
            //Flip the sides
            currentSide = currentSide.opposite();
            //Inform view to continue as normal.
            mancalaView.update(TurnResult.SUCCESS, this);
            return;
        } else if (winningSide.equals(Side.A)) {
            //Inform view to display the "Player A wins" message.
            mancalaView.update(TurnResult.WINNER_A, this);
            return;
        } else {
            //Inform view to display the "Player B wins" message.
            mancalaView.update(TurnResult.WINNER_B, this);
            return;
        }
    }

    /**
     * Undoes the board state
     *
     * @return false if you cannot undo
     */
    public boolean undo() {
        if (numberUndosLeft > 0) {
            numberUndosLeft -= 1;
            //Copy previous move to pits, doing an undo
            System.arraycopy(previousTurn, 0, pits, 0, pits.length);
            currentSide = previousSide;
            currentTurnId = previousTurnId;
            //Now currentTurnId = previousTurnId, we know we're in a undo session
            //undo success
            if (numberUndosLeft == 0) {
                canUndo = false;
            }
            return true;
        }
        //Too many undos!
        canUndo = false;
        return false;
    }

    /**
     * Check if there is a win condition, and if there is, determine winner
     *
     * @return null if no win condition, otherwise return the side that won
     */
    private Side checkWinConditions() {
        int amountOnBSide = 0;
        for (int i = Pit.B6.ordinal(); i <= Pit.B1.ordinal(); i++) {
            amountOnBSide += pits[i];
        }

        int amountOnASide = 0;
        for (int i = Pit.A6.ordinal(); i <= Pit.A1.ordinal(); i++) {
            amountOnASide += pits[i];
        }

        //Win condition:
        if (amountOnASide == 0 || amountOnBSide == 0) {
            addToMancala(Side.A, amountOnASide);
            addToMancala(Side.B, amountOnBSide);

            for (int i = 0; i < pits.length; i++) {
                //If it is not a mancala, wipe the board
                if(!Pit.values()[i].mancala){
                    pits[i] = 0;
                }
            }

            if (getAmountInPit(Pit.A_MANCALA) > getAmountInPit(Pit.B_MANCALA)) {
                return Side.A;
            } else {
                return Side.B;
            }
        }

        //Otherwise nobody won, continue as normal:
        return null;
    }

    //================================================================================
    //                           DATA MODIFIERS
    //================================================================================

    private int getAmountInPit(Pit pit) {
        return pits[pit.ordinal()];
    }

    private void addOneToPit(Pit pit) {
        pits[pit.ordinal()] += 1;
    }

    private int clearPit(Pit pit) {
        int amount = pits[pit.ordinal()];
        pits[pit.ordinal()] = 0;
        return amount;
    }

    private void addToMancala(Side side, int amount) {
        if (side.equals(Side.A)) {
            pits[Pit.A_MANCALA.ordinal()] += amount;
        } else {
            pits[Pit.B_MANCALA.ordinal()] += amount;
        }
    }

    //Create a snapshot of the board state
    private void captureBoardState() {
        System.arraycopy(pits, 0, previousTurn, 0, pits.length);
        previousSide = currentSide;

        //The above code is simply a faster way of doing the below
        //        for (int i = 0; i < pits.length; i++) {
        //            previousTurn[i] = pits[i];
        //        }
    }

    private void resetUndoCounter() {
        canUndo = true;
        numberUndosLeft = 3;
    }

    //================================================================================
    //                           DATA ACCESSORS
    //================================================================================

    public int[] getPits() {
        return pits;
    }

    public Side getCurrentSide() {
        return currentSide;
    }

    public boolean isCanUndo() {
        return canUndo;
    }

    public int getNumberUndosLeft() {
        return numberUndosLeft;
    }


    //================================================================================
    //                           VIEW INFORMATION
    //================================================================================

    /**
     * Defines a set of callback information that determines what the View displays
     */
    public static enum TurnResult {
        NOT_YOUR_SIDE,
        SUCCESS,
        GETS_ANOTHER_TURN,
        EMPTY_PIT,
        WINNER_A,
        WINNER_B,
        CANT_USE_MANCALA
    }

}
