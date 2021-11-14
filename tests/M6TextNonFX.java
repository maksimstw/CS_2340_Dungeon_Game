import character.Monster;
import javafx.stage.Stage;
import main.Game;
import map.MazeRoom;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * although the class extends application test
 * it is not meant to actually run the game
 * the application test is so that "internal graphics not initialized" errors are suppressed
 *
 */
public class M6TextNonFX extends ApplicationTest {

    private Game game;
    public void start(Stage primaryStage) {
        game = new Game();
    }

    @Test
    public void testChallengeRoomSpawned() {
        for (int i = 0; i < 100; i++) {
            game.getGameMap().generateMap();
            MazeRoom[][] map = game.getGameMap().getMap();
            ArrayList<MazeRoom> challenges = new ArrayList<>();
            for (MazeRoom[] mazeRooms : map) {
                for (MazeRoom m : mazeRooms) {
                    if (m != null && m.toString().equals("CHALLENGE")) {
                        challenges.add(m);
                    }
                }
            }

            Assert.assertEquals(2, challenges.size());
            for (MazeRoom m : challenges) {
                Assert.assertTrue(m.isSafe());
            }
        }
    }



    @Test
    public void testBossToughness() {
        game.getGameMap().generateMap();
        MazeRoom m = game.getGameMap().getMap()[5][5];
        assertEquals(1, m.getMonsterList().size());
        int bossHp = m.getMonsterList().get(0).getHp();
        Monster normalMonster;
        for (int i = 1; i < 6; i++) {
            normalMonster = new Monster(i);
            assertTrue(bossHp > normalMonster.getHp());
        }
    }


    @Test
    public void testBossRoom() {
        for (int i = 0; i < 100; i++) {
            game.getGameMap().generateMap();
            MazeRoom[][] map = game.getGameMap().getMap();
            ArrayList<MazeRoom> boss = new ArrayList<>();
            for (MazeRoom[] mazeRooms : map) {
                for (MazeRoom m : mazeRooms) {
                    if (m != null && m.toString().equals("EXIT")) {
                        assertEquals(map[5][5], m);
                        boss.add(m);
                    }
                }
            }
            Assert.assertEquals(1, boss.size());
            for (MazeRoom m : boss) {
                Assert.assertTrue(!m.isSafe());
            }
        }
    }

}


