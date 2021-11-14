package character;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import main.Game;
import map.GameMap;
import shape.Rect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * The player class. It is also an entity, so it extends Sprite.
 *
 */
public class Player extends Sprite implements Serializable {
    private Weapon weapon;
    private int difficulty;
    private int money;

    //enterable is false when player just entered a door
    //it is set to true when player exits the door transition region
    private boolean enterable;

    private final ObservableList<Collectable> weaponInventory;
    private final ObservableList<Collectable> potionInventory;

    private final ArrayList<Potion> usedPotionList;

    private static final int INITIAL_HP = 300;
    private static final int INITIAL_SPEED = 350;

    public Player(String imagePath) {
        super(imagePath);
        this.weapon = new Weapon();
        setHp(INITIAL_HP);
        setSpeed(INITIAL_SPEED);
        weaponInventory = FXCollections.observableArrayList();
        potionInventory = FXCollections.observableArrayList();
        usedPotionList = new ArrayList<>();
    }

    /**
     * for testing
     */
    public Player() {
        enterable = true;
        setHp(INITIAL_HP);
        setSpeed(INITIAL_SPEED);
        weaponInventory = FXCollections.observableArrayList();
        potionInventory = FXCollections.observableArrayList();
        usedPotionList = new ArrayList<>();
    }

    /**
     * Sets the name of the player
     *
     * @param name the name of the player
     */
    public void setName(String name) {
        super.setName(name);
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Sets the weapon of the player
     * @param weapon the name of the weapon
     */
    public void swapWeapon(Weapon weapon) {
        //System.out.println("from " + this.weapon.getName() + " to " + weapon.getName());
        //swap weapon in inventory
        if (!weaponInventory.remove(weapon)) {
            throw new NoSuchElementException(weapon.getName() + "is not in the inventory");
        }
        // if it is not first time to equip a weapon
        if (this.weapon != null) {
            weaponInventory.add(this.weapon);
        }
        //equip new weapon
        this.weapon = weapon;
    }

    public void usePotion(Potion potion) {
        potionInventory.remove(potion);
        switch (potion.getType()) {
        case "healthPotion":
            setHp(Math.min(INITIAL_HP, getHp() + 50));
            break;
        case "attackPotion":
            getWeapon().setWeaponDamage(getWeapon().getWeaponDamage() - 5);
            potion.setElapsedTime(Game.getGame().getPlayer().getElapsedTime());
            usedPotionList.add(potion);
            break;
        case "otherPotion":
            setSpeed(getSpeed() - 100);
            potion.setElapsedTime(Game.getGame().getPlayer().getElapsedTime());
            usedPotionList.add(potion);
            break;
        default:
            break;
        }
    }

    /**
     * Sets the difficulty of the current game
     *
     * @param difficulty the difficulty of the game from 1 to 3
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * getter for difficulty
     *
     * @return difficulty of the game from 1 to 3
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * getter for character name
     *
     * @return name
     */
    public String getName() {
        return super.getName();
    }

    /**
     * getter for weapon name
     *
     * @return weapon
     */
    public Weapon getWeapon() {
        return weapon;
    }

    public ObservableList<Collectable> getWeaponInventory() {
        return weaponInventory;
    }

    public ObservableList<Collectable> getPotionInventory() {
        return potionInventory;
    }

    /**
     * add a collected item to the player's inventory depending on its type
     * @param item an item that could be a weapon or a potion
     */
    public void collectItem(Collectable item) {
        if (item instanceof Weapon) {
            weaponInventory.add(item);
        } else if (item instanceof Potion) {
            potionInventory.add(item);
        } else {
            throw new IllegalArgumentException(item + " miscellaneous item!");
        }
    }

    /**
     * getter for money
     *
     * @return return the amount of money you have
     */
    public int getMoney() {
        return money;
    }

    /**
     * setter for money
     *
     * @param money set the amount of money
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * after locked, the door collision will no longer be active
     * until the player leaves the current door region
     */
    public void lockDoorCollision() {
        enterable = false;
    }

    @Override
    public void update(double deltaTime) {
        setElapsedTime(getElapsedTime() + deltaTime);
        double dx = this.getVelocity().getX()
                * deltaTime;
        double dy = this.getVelocity().getY() * deltaTime;
        this.getPosition().add(dx, dy);
        this.weapon.getPosition().add(dx, dy);
        GameMap map = Game.getGame().getGameMap();
        int doorCode = map.testDoorBounds(
                Rect.getRectByCornerSize(
                        getBoundary().getX(),
                        getBoundary().getY(),
                        getBoundary().getWidth(),
                        getBoundary().getHeight()
                )
        );
        if (doorCode == GameMap.NOT_IN_ANY_DOOR) {
            enterable = true;
        } else {
            if (enterable) {
                double[] newPosition = map.enterDoor(doorCode);
                if (newPosition[0] >= 0) {
                    this.getPosition().set(newPosition[0], newPosition[1]);
                    this.weapon.getPosition().set(newPosition[0], newPosition[1] + 5);
                    enterable = false;
                }
            }
        }

        wrap(this);
        wrap(this.weapon);
    }

    @Override
    public void setFlip(boolean flip) {
        super.setFlip(flip);
        try {
            this.weapon.setFlip(flip);
        } catch (Exception e) {
            System.out.println("weapon has not been instantiated.");
        }

    }

    @Override
    public void render(GraphicsContext context) {
        super.render(context);
        this.weapon.render(context);
        context.restore();
    }

    /**
     * clear inventory
     */
    public void clearInventory() {
        potionInventory.clear();
        weaponInventory.clear();
    }

    /**
     * get used potion
     * @return a list of used potion
     */
    public ArrayList<Potion> getUsedPotionList() {
        return usedPotionList;
    }
}
