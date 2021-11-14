package map;

import main.Game;
import shape.Vector;

import java.util.Random;

public class ChallengeRoom extends MazeRoom {

    //whether the enter-challenge portal is displaying
    private boolean portal;
    private boolean challengeCompleted;

    private static final int EXTRA_ITEM_DROP = 5;

    public ChallengeRoom(boolean visited) {
        super("CHALLENGE", visited);
        portal = true;
        challengeCompleted = false;
    }

    public boolean getPortalVisibility() {
        return portal;
    }

    /**
     * this is called when player enters the challenge portal
     */
    public void enterChallenge() {
        portal = false;
        challengeCompleted = false;
        super.populate();
    }

    /**
     * this is called by game screen when challenge room is safe
     */
    public void exitChallenge() {
        // if challenge hasn't even started yet,
        // or, if challenge completed is already called,
        // quit directly
        if (portal || challengeCompleted) {
            return;
        }
        //otherwise, drop lots of items
        for (int i = 0; i < EXTRA_ITEM_DROP; i++) {
            double w = ((new Random()).nextDouble() - 0.5) * 0.3 + 0.5;
            double h = ((new Random()).nextDouble() - 0.5) * 0.3 + 0.5;
            w *= Game.getGame().getScreenWidth();
            h *= Game.getGame().getScreenHeight();
            dropRandomItem(new Vector(w, h));
        }

        challengeCompleted = true;
    }
}
