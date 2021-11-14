package character;

import main.Game;

public class Boss extends Monster {
    public Boss() {
        super(1);
        setScale(2.5);
        getPosition().set(Game.getGame().getScreenWidth() / 2.0,
                Game.getGame().getScreenHeight() / 2.0);
        setHp(300);
        getVelocity().setMag(0);
    }

    private static final double STEP_FREQUENCY = 1.5; //steps per second

    /**
     * The boss takes "steps" when moving, pausing in between
     * @param deltaTime time elapsed since last frame
     */
    @Override
    public void update(double deltaTime) {

        super.update(deltaTime);

        double s = Math.floor(getElapsedTime() / STEP_FREQUENCY) % 2 == 1 ? 50 : 0;
        setSpeed(s);
        getVelocity().setMag(s);
    }
}