package character;

import main.Game;

import java.io.Serializable;
import java.util.Random;

/**
 * represents a generic monster entity
 */
public class Monster extends Sprite implements Serializable {

    private static final int TYPE_OF_MONSTERS = 5;
    /**
     * constructs a purely abstract monster detached from UI, used for testing purposes only
     */
    public Monster() {
        super();
    }

    public Monster(int monsterType) {
        super("/res/monster" + monsterType + ".png");

        Random rnd = new Random();
        int spawnW = Game.getGame().getScreenWidth();
        int spawnH = Game.getGame().getScreenHeight();
        int spawnPadding = 50;

        getPosition().set(rnd.nextInt(spawnW - 2 * spawnPadding) + spawnPadding,
                rnd.nextInt(spawnH - 2 * spawnPadding) + spawnPadding);
        setHp(monsterType * 30);
        setSpeed(monsterType * 15 + 20);
        getVelocity().setMag(getSpeed());
    }

    public static Monster getRandomMonster() {
        return new Monster((new Random()).nextInt(5) + 1);
    }

    /**
     * Monsters pursue the player continuously without stopping
     * @param deltaTime time elapsed per frame
     */
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        getVelocity().setAngle(180 + Math.toDegrees(Math.atan2(
                getPosition().getY() - Game.getGame().getPlayer().getPosition().getY(),
                getPosition().getX() - Game.getGame().getPlayer().getPosition().getX()))
        );
    }
}
