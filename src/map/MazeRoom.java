package map;

import character.*;
import main.Game;
import shape.Vector;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Random;

public class MazeRoom implements Serializable {
    private final String type;
    private final ArrayList<Monster> monsters;
    private final ArrayList<Collectable> items;
    private boolean visited;

    /**
     * SAMPLE constructor for MazeRoomContent
     * please DELETE before release!
     * @param type the type of the room
     * @param visited whether the room is visited
     */
    public MazeRoom(String type, boolean visited) {
        this.type = type;
        this.visited = visited;

        monsters = new ArrayList<>();
        items = new ArrayList<>();

        //in non-ui test environment, monsters will be added manually
        if (Game.getGame() != null) {
            if (!type.equals("CHALLENGE")) {
                populate();
            }
        }

    }

    private static final int NORMAL_ROOM_MONSTERS_NUM = 2;


    /**
     * populate the room with monsters
     * this should only be called when the room NEEDS to have monsters actively appearing inside it
     * the method is calibrated for each type of room. No need to set other variables for number of
     * monsters and their type
     */
    public void populate() {

        int numMonsters = 0;
        switch (type) {
        case "TEST":
            //test rooms don't have enemies at time of initialization
            break;
        case "START":
            //no enemy at start room
            break;
        case "EXIT":
            monsters.add(new Boss());
            return;
        case "CHALLENGE":
            //challenge rooms spawn more monsters (both min and max are higher!)
            numMonsters = 2 * NORMAL_ROOM_MONSTERS_NUM
                    + (new Random()).nextInt(NORMAL_ROOM_MONSTERS_NUM) + 1;
            break;
        default:
            //random number of enemy at random room
            numMonsters = NORMAL_ROOM_MONSTERS_NUM;
            break;
        }

        //for ordinary and challenge rooms
        if (Game.getGame() != null) {
            for (int i = 0; i < numMonsters; i++) {
                monsters.add(Monster.getRandomMonster());
            }
        }
    }

    /**
     * generates a random maze room. the room cannot be start or exit
     * @return a random maze room
     */
    public static MazeRoom getRandomRoom() {
        return new MazeRoom("SAMPLE", false);
    }

    public void addMonster(Monster m) {
        monsters.add(m);
    }

    /**
     * remove a dying monster from the room
     * @param m the monster that needs to be removed
     * @return whether the removal is successful
     */
    public boolean removeMonster(Monster m) {
        if (monsters.contains(m)) {
            monsters.remove(m);
            dropRandomItem(m.getPosition());
            return true;
        }
        return false;
    }

    /**
     * drop either a potion or a weapon at the given position
     * @param position the position for the colletable to spawn
     */
    protected void dropRandomItem(Vector position) {
        Collectable drop;
        if ((new Random()).nextBoolean()) {
            drop = Potion.getRandomPotion();
        } else {
            drop = Weapon.getRandomWeapon();
        }
        drop.setPosition(position);
        items.add(drop);
    }

    /**
     * check if there are any monsters in the room
     * @return true if no monsters, false if some monsters
     */
    public boolean isSafe() {
        return monsters.isEmpty();
    }

    /**
     * simulate room visit
     */
    public void visit() {
        visited = true;
    }

    /**
     * accessor for visited attribute
     * @return the visited attribute
     */
    public boolean getVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return type;
    }

    /**
     * a getter for monster arraylist
     * @return the monster arraylist
     */
    public ArrayList<Monster> getMonsterList() {
        return monsters;
    }

    public ArrayList<Collectable> getItemsList() {
        return items;
    }
}
