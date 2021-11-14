import character.SampleMonster;
import javafx.stage.Stage;
import map.GameMap;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class RoomNavigationTest extends ApplicationTest {
    //private Game game;
    private GameMap map;

    public void start(Stage primaryStage) {
        map = new GameMap();
        map.generateMap();
        primaryStage.show();
    }

    @Test
    public void testSafeRoomNavigation() {
        map.unlockBossRoom();

        //make sure adj rooms are not visited before this test
        assertFalse(map.getMap()[0][1].getVisited());
        assertFalse(map.getMap()[2][1].getVisited());
        assertFalse(map.getMap()[1][0].getVisited());
        assertFalse(map.getMap()[1][2].getVisited());

        //go up from start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.TOP_DOOR);
        assertTrue(map.getCurrentRoom().getVisited());
        assertArrayEquals(new int[]{1, 0}, map.getLocation());
        //go back down to start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.BOTTOM_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());

        //go left from start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.LEFT_DOOR);
        assertArrayEquals(new int[]{0, 1}, map.getLocation());
        //go right to start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.RIGHT_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());

        //go down from start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.BOTTOM_DOOR);
        assertArrayEquals(new int[]{1, 2}, map.getLocation());
        //go up to start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.TOP_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());

        //go right from start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.RIGHT_DOOR);
        assertArrayEquals(new int[]{2, 1}, map.getLocation());
        //go left to start
        assertTrue(map.getCurrentRoom().isSafe());
        map.enterDoor(GameMap.LEFT_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());
    }

    @Test
    public void testUnsafeRoomNavigation() {
        map.unlockBossRoom();

        SampleMonster m = new SampleMonster();
        map.getCurrentRoom().addMonster(m);
        assertFalse(map.getCurrentRoom().isSafe());

        //try to travel to four rooms, but all attempts should be useless
        map.enterDoor(GameMap.TOP_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());
        map.enterDoor(GameMap.TOP_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());
        map.enterDoor(GameMap.TOP_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());
        map.enterDoor(GameMap.TOP_DOOR);
        assertArrayEquals(new int[]{1, 1}, map.getLocation());

        //after attempted traversals, start room should remain unsafe
        assertFalse(map.getCurrentRoom().isSafe());

        assertTrue(map.getCurrentRoom().removeMonster(m));
        assertTrue(map.getCurrentRoom().isSafe());
        //after room is cleared, traversal should work normally
        testSafeRoomNavigation();
    }
}
