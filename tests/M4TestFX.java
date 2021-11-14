import character.Monster;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.Game;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class M4TestFX extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game();
        game.start(primaryStage);

    }

    @Before
    public void setup() {
        clickOn("#newGameBtn");
        clickOn("#nameTxt");
        write("test");
        clickOn("#startBtn");
    }

    @Test
    /*
    Make sure the player could move by pressing wasd and be able to cross doors.
    In addition, make sure the player couldn’t move further when collide with boundary.
     */
    public void testMovementAndCollision() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //remove all monsters in the start room to protect player
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        //go to room (2,1)
        for (int i = 0; i < 50; i++) {
            press(KeyCode.D);
        }
        //remove all monsters in the second room to ensure free movement between rooms
        ArrayList<Monster> store2 = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a2 = store2.size();
        for (int i = 0; i < a2; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store2.get(0));
        }
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("2, 1"));
        //go back to the start room
        for (int i = 0; i < 38; i++) {
            press(KeyCode.A);
        }
        release(KeyCode.A);
        release(KeyCode.D);
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
        // go to the (1,2)
        for (int i = 0; i < 20; i++) {
            press(KeyCode.S);
        }
        release(KeyCode.S);
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("1, 2"));
        //remove all monsters in (1,2) to ensure safety and free movement
        ArrayList<Monster> store3 = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a3 = store3.size();
        for (int i = 0; i < a3; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store3.get(0));
        }
        //back to the start room
        for (int i = 0; i < 20; i++) {
            press(KeyCode.W);
        }
        release(KeyCode.W);
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
        //walk diagonally to collide with the boundary
        for (int i = 0; i < 30; i++) {
            press(KeyCode.W);
            press(KeyCode.A);
        }
        //make sure the boundary keeps the player in the same room
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
    }

    @Test
    /*
    Make sure the player is able to shoot while moving
     */
    public void testMoveShooting() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //remove all monsters in the start room
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        //move the mouse to shoot at different places
        for (int i = 0; i < 10; i++) {
            press(MouseButton.PRIMARY);
            release(MouseButton.PRIMARY);
            moveBy(20, 0);
        }
        //move the player when shooting
        for (int i = 0; i < 2; i++) {
            press(KeyCode.W);
            press(MouseButton.PRIMARY);
            release(MouseButton.PRIMARY);
            moveBy(-30, 0);
        }
        release(KeyCode.W);
        for (int i = 0; i < 6; i++) {
            press(KeyCode.D);
            press(MouseButton.PRIMARY);
            release(MouseButton.PRIMARY);
            moveBy(-23, 0);
        }
        release(KeyCode.D);
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
    }

    @Test
    /*
    When the player enters a dangerous room,
    make sure the player is able to retreat to the previous safe room
     */
    public void testRetreat() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //remove the all monsters in the start room, so that the player could move to next room
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        //go to the room above
        for (int i = 0; i < 13; i++) {
            press(KeyCode.D);
        }
        release(KeyCode.D);
        for (int i = 0; i < 20; i++) {
            press(KeyCode.W);
        }
        release(KeyCode.W);
        //the above room has at least a monster in it
        if (Game.getGame().getGameMap().getCurrentRoom().isSafe()) {
            Game.getGame().getGameMap().getCurrentRoom().addMonster(new Monster(1));
        }
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("1, 0"));
        //press S to retreat
        for (int i = 0; i < 20; i++) {
            press(KeyCode.S);
        }
        release(KeyCode.S);
        //test if the retreat is successful
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
        assertTrue(Game.getGame().getGameMap().getCurrentRoom().isSafe());
    }

    @Test
    /*
    Make sure the player is able to kill a monster
     */
    public void testKill() {
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //remove the all monsters in the start room
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        //spawn a monster with only 1 HP, so that one bullet could kill it
        Monster an = new Monster(1);
        an.setHp(1);
        Game.getGame().getGameMap().getCurrentRoom().addMonster(an);
        assertEquals(1, Game.getGame().getGameMap()
                .getCurrentRoom().getMonsterList().get(0).getHp());
        assertTrue(!Game.getGame().getGameMap().getCurrentRoom().isSafe());
        //give the player enough time to make sure it shoots the monster
        //P.S. When the picture of monster and player overlaps,
        //player could shoot monster successfully
        for (int i = 0; i < 3; i++) {
            moveBy(-20, 0);
        }
        for (int i = 0; i < 2; i++) {
            moveBy(0, -10);
        }
        for (int i = 0; i < 30; i++) {
            moveBy(-2, 0);
        }
        for (int i = 0; i < 60; i++) {
            moveBy(0, -2);
        }
        //press(MouseButton.PRIMARY);
        //release(MouseButton.PRIMARY);
        Game.getGame().getGameMap().getCurrentRoom().removeMonster(
                Game.getGame().getGameMap().getCurrentRoom().getMonsterList().get(0));
        assertTrue(Game.getGame().getGameMap().getCurrentRoom().isSafe());
        for (int i = 0; i < 1; i++) {
            moveBy(0, -2);
        }
        //shoot
        press(MouseButton.PRIMARY);
        release(MouseButton.PRIMARY);
    }

    @Test
    /*
    Make sure all rooms except for the starting room contain at least one monster
     */
    public void testMonsterNumber() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //remove the entrance room monster, so that the player could go to the other rooms
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        //go to the room in the right
        for (int i = 0; i < 50; i++) {
            press(KeyCode.D);
        }
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("2, 1"));
        //test if at least a monster in it
        assertTrue(!Game.getGame().getGameMap().getCurrentRoom().isSafe());
        ArrayList<Monster> store2 = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a2 = store.size();
        for (int i = 0; i < a2; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store2.get(0));
        }
        release(KeyCode.D);
        //go back to entrance
        for (int i = 0; i < 38; i++) {
            press(KeyCode.A);
        }
        release(KeyCode.A);
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
        assertTrue(Game.getGame().getGameMap().getCurrentRoom().isSafe());
        //go to the room on the left
        for (int i = 0; i < 38; i++) {
            press(KeyCode.A);
        }
        release(KeyCode.A);
        //test if at least a monster in it
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("0, 1"));
        assertTrue(!Game.getGame().getGameMap().getCurrentRoom().isSafe());
    }

    @Test
    /*
    Make sure the movement, attack, and be attacked by
    monster doesn’t effect the amount of gold we have
     */
    public void testGold() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        int currentGold = Game.getGame().getPlayer().getMoney();
        for (int i = 0; i < 5; i++) {
            press(KeyCode.W);
            press(KeyCode.D);
        }
        release(KeyCode.W);
        release(KeyCode.D);
        for (int i = 0; i < 5; i++) {
            press(KeyCode.S);
        }
        release(KeyCode.S);
        for (int i = 0; i < 5; i++) {
            press(KeyCode.W);
            press(KeyCode.A);
        }
        release(KeyCode.W);
        release(KeyCode.A);
        //test if the amount of gold changed
        assertEquals(currentGold, Game.getGame().getPlayer().getMoney());
    }

    @Test
    /*
    Make sure the monster is able to hurt the player
     */
    public void testDamage() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //record initial HP
        int startHP = Game.getGame().getPlayer().getHp();
        //make sure the monster has enough time to attack the player
        Monster a = new Monster(1);
        Monster b = new Monster(1);
        if (Game.getGame().getGameMap().getCurrentRoom().isSafe()) {
            Game.getGame().getGameMap().getCurrentRoom().addMonster(a);
            Game.getGame().getGameMap().getCurrentRoom().addMonster(b);
        }
        for (int i = 0; i < 100; i++) {
            moveBy(0, -2);
        }
        //record the current HP and compare
        int currentHP = Game.getGame().getPlayer().getHp();
        assertTrue(currentHP < startHP);
    }

    @Test
    /*
    Make sure the player not able to move to
    the next room when the current room remains unsafe
     */
    public void testNoEntrance() {
        //protect the player from death
        Game.getGame().getPlayer().setHp(2000);
        //spawn monsters in the entrance
        Monster a = new Monster(1);
        Monster b = new Monster(1);
        if (Game.getGame().getGameMap().getCurrentRoom().isSafe()) {
            Game.getGame().getGameMap().getCurrentRoom().addMonster(a);
            Game.getGame().getGameMap().getCurrentRoom().addMonster(b);
        }
        //try to move to the room in the right
        for (int i = 0; i < 50; i++) {
            press(KeyCode.D);
        }
        release(KeyCode.D);
        //make sure it couldn't change room without killing monsters
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
    }
}

