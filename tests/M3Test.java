import main.Game;
import map.GameMap;
import map.MazeRoom;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class M3Test {
    private GameMap map;
    private final Game game = new Game();
    private static final int TIMEOUT = 200;

    @Before
    public void setup() {
        map = game.getGameMap();
    }

    @Test (timeout = TIMEOUT)
    public void testGracefulEntrance() {
        for (int i = -100; i < GameMap.MAP_SIZE + 100; i++) {
            for (int j = -100; j < GameMap.MAP_SIZE + 100; j++) {
                map.switchRoom(i, j);
                if (map.checkRoomLegitimacy(i, j)) {
                    assertEquals(i, map.getLocation()[0]);
                    assertEquals(j, map.getLocation()[1]);
                }
                assertNotNull(map.getCurrentRoom());
            }
        }
    }

    @Test (timeout = TIMEOUT)
    public void testEnterDoor() {
        double[] pos;
        pos = map.enterDoor(GameMap.TOP_DOOR);
        assertTrue(pos[0] >= 0);
        assertTrue(pos[1] >= 0);

        map.switchRoom(1, 1);
        pos = map.enterDoor(GameMap.RIGHT_DOOR);
        assertTrue(pos[0] >= 0);
        assertTrue(pos[1] >= 0);

        map.switchRoom(1, 1);
        pos = map.enterDoor(GameMap.BOTTOM_DOOR);
        assertTrue(pos[0] >= 0);
        assertTrue(pos[1] >= 0);

        map.switchRoom(1, 1);
        pos = map.enterDoor(GameMap.LEFT_DOOR);
        assertTrue(pos[0] >= 0);
        assertTrue(pos[1] >= 0);

        map.switchRoom(1, 1);
        pos = map.enterDoor(GameMap.NOT_IN_ANY_DOOR);
        assertEquals(-1, pos[1], 0.0);
        assertEquals(-1, pos[1], 0.0);

        for (int i = -100; i < 0; i++) {
            pos = map.enterDoor(i);
            assertEquals(-1, pos[1], 0.0);
            assertEquals(-1, pos[1], 0.0);
        }

        for (int i = 10; i < 100; i++) {
            pos = map.enterDoor(i);
            assertEquals(-1, pos[1], 0.0);
            assertEquals(-1, pos[1], 0.0);
        }
    }

    @Test (timeout = TIMEOUT)
    public void testExitRoomReachable() {
        map.generateMap();
        while (true) {
            map.enterDoor(GameMap.RIGHT_DOOR);
            if (map.getCurrentRoom().toString().equals("EXIT")) {
                return;
            }
            map.enterDoor(GameMap.BOTTOM_DOOR);
            if (map.getCurrentRoom().toString().equals("EXIT")) {
                return;
            }
        }
    }

    @Test (timeout = TIMEOUT)
    public void testNumberOfRoom() {
        map.generateMap();
        int room = 0;
        MazeRoom[][] mazeMap = map.getMap();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (mazeMap[i][j] != null) {
                    room += 1;
                }
            }
        }
        assertTrue(room >= 8);

    }

    @Test (timeout = TIMEOUT)
    public void testDistanceBetweenStartExit() {
        map.generateMap();
        int room = 0;
        MazeRoom[][] mazeMap = map.getMap();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (mazeMap[i][j] != null && mazeMap[i][j].toString().equals("SAMPLE")) {
                    room += 1;
                }
            }
        }
        assertTrue(room - 3 >= 6);

    }
    
    @Test (timeout = TIMEOUT)
    //when map generates, there exist only one random paths to the end
    public void testRandomWalk() {
        map.generateMap();
        MazeRoom[][] gameMap = map.getMap();
        int count = 0;
        for (int i = 2; i < 6; i++) {
            if (gameMap[0][i] != null) {
                count += 1;
            }
        }
        for (int i = 2; i < 6; i++) {
            if (gameMap[i][0] != null) {
                count += 1; 
            }
        }
        assertEquals(0, count); // no existing rooms in (0,2) to (0,5), (2,0) to (5,0)
        int step = -2;
        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                if (gameMap[i][j] != null) {
                    step += 1;
                }
            }
        }
        assertEquals(8, step);  //8 random steps to exit room
    }
    
    @Test (timeout = TIMEOUT)
    public void testRoomType() {
        map.generateMap();
        MazeRoom[][] mazeMap = map.getMap();
        assertEquals("EXIT", mazeMap[5][5].toString());
        assertEquals("START", mazeMap[1][1].toString());
        assertEquals("SAMPLE", mazeMap[0][1].toString());
        assertEquals("SAMPLE", mazeMap[1][0].toString());
        assertEquals("SAMPLE", mazeMap[2][1].toString());
        assertEquals("SAMPLE", mazeMap[1][2].toString());
    }
}
