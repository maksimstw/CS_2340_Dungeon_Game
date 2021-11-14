package character;

import java.util.Random;

/**
 * Weapon is a (Collectable extends Sprite) that has an icon image and name
 * The animation that when the player holding the weapon and fire
 * is rendered in GameScreen.java like spawnLaser()
 */
public class Weapon extends Collectable {

    private static final String DEFAULT_ICON = "/res/weapon1.png";
    private final int type; // choice is in [1,2,3] to indicate weapon chosen
    private int weaponDamage;
    private int weaponSpeed;
    private double weaponRange;

    public Weapon() {
        super(DEFAULT_ICON, "weapon");
        type = 1;
        weaponDamage = -10;
        weaponSpeed = 600;
        weaponRange = 0.8;
    }

    /**
     * For testing purpose
     * @param name weapon name
     */
    public Weapon(String name) {
        super(DEFAULT_ICON, name);
        type = 1;
        weaponDamage = -10;
        weaponSpeed = 600;
        weaponRange = 0.8;
    }

    private static final String[] WEAPON_NAMES = {"Black Night", "Blue Ocean", "Red Sun"};

    public Weapon(int type) {
        super("/res/weapon" + type + ".png", WEAPON_NAMES[type - 1]);
        this.type = type;
        switch (type) {
        case 1:
            weaponDamage = -10;
            weaponSpeed = 600;
            weaponRange = 0.7;
            break;
        case 2:
            weaponDamage = -30;
            weaponSpeed = 200;
            weaponRange = 0.3;
            break;
        case 3:
            weaponDamage = -5;
            weaponSpeed = 1000;
            weaponRange = 2;
            break;
        default:
            break;
        }
    }

    public static Weapon getRandomWeapon() {
        return new Weapon((new Random()).nextInt(WEAPON_NAMES.length) + 1);
    }

    public int getType() {
        return type;
    }

    /**
     * get weapon damage
     * @return weapon damage
     */
    public int getWeaponDamage() {
        return weaponDamage;
    }

    /**
     * get the speed of the laser
     * @return laser speed
     */
    public int getWeaponSpeed() {
        return weaponSpeed;
    }

    /**
     * set weapon damage
     * @param weaponDamage the new damage
     */
    public void setWeaponDamage(int weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    /**
     * set weapon speed
     * @param weaponSpeed the new speed
     */
    public void setWeaponSpeed(int weaponSpeed) {
        this.weaponSpeed = weaponSpeed;
    }

    /**
     * get weapon range
     * @return weapon range
     */
    public double getWeaponRange() {
        return weaponRange;
    }

    /**
     * set weapon range
     * @param weaponRange the new weapon range
     */
    public void setWeaponRange(double weaponRange) {
        this.weaponRange = weaponRange;
    }
}
