import character.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.Game;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

public class M6TestFX extends ApplicationTest {

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
    public void testBoss() {
        Game.getGame().getPlayer().setHp(3000);
        Boss finalBoss = new Boss();
        Game.getGame().getGameMap().getCurrentRoom().getMonsterList().add(finalBoss);
        for (int i = 0; i < 13; i++) {
            press(KeyCode.D);
        }
        release(KeyCode.D);
        clickOn(MouseButton.PRIMARY);
        for (int i = 0; i < 3; i++) {
            moveBy(0, -13);
        }
        press(KeyCode.SPACE);
        for (int i = 0; i < 30; i++) {
            moveBy(1, 0);
        }
        release(KeyCode.SPACE);
        assertTrue(Game.getGame().getPlayer().getHp() < 3000);
    }

    @Test
    public void testPopulate() {
        Game.getGame().getPlayer().setHp(2000);
        Game.getGame().getGameMap().getCurrentRoom().populate();
        assertEquals(0, Game.getGame().getGameMap().getCurrentRoom().getMonsterList().size());
        for (int i = 0; i < 50; i++) {
            press(KeyCode.D);
        }
        release(KeyCode.D);
        Monster a1 = new Monster(1);
        Game.getGame().getGameMap().getCurrentRoom().addMonster(a1);
        Monster a2 = new Monster(1);
        Game.getGame().getGameMap().getCurrentRoom().addMonster(a2);
        Monster a3 = new Monster(1);
        Game.getGame().getGameMap().getCurrentRoom().addMonster(a3);
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        Game.getGame().getGameMap().getCurrentRoom().populate();
        assertTrue(Game.getGame().getGameMap().getCurrentRoom().getMonsterList().size() > 0);
    }

    @Test
    public void testHeavyFire() {
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        release(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
    }

    @Test
    public void testInitialScreen() {
        Game.getGame().getPlayer().setHp(1);
        if (Game.getGame().getGameMap().getCurrentRoom().isSafe()) {
            Monster a = new Monster(1);
            Game.getGame().getGameMap().getCurrentRoom().addMonster(a);
        }
        for (int i = 0; i < 120; i++) {
            moveBy(0, -1);
        }
        for (int i = 0; i < 120; i++) {
            moveBy(0, 1);
        }
        clickOn("#returnMenu");
        verifyThat("#newGameBtn", NodeMatchers.isNotNull());
        clickOn("#newGameBtn");
    }

    @Test
    public void testMonsterType() {
        Game.getGame().getPlayer().setHp(2000);
        Monster add;
        for (int i = 1; i < 6; i++) {
            add = new Monster(i);
            Game.getGame().getGameMap().getCurrentRoom().addMonster(add);
        }
        for (int i = 0; i < 10; i++) {
            moveBy(1, 0);
        }
        assertTrue(Game.getGame().getGameMap().getCurrentRoom().getMonsterList().size() == 5);
    }

    @Test
    public void testLoseScreen() {
        Game.getGame().getPlayer().setHp(1);
        Game.getGame().getPlayer().setHp(1);
        if (Game.getGame().getGameMap().getCurrentRoom().isSafe()) {
            Monster a = new Monster(1);
            Game.getGame().getGameMap().getCurrentRoom().addMonster(a);
        }
        for (int i = 0; i < 120; i++) {
            moveBy(0, -1);
        }
        for (int i = 0; i < 120; i++) {
            moveBy(0, 1);
        }
        verifyThat("#returnMenu", NodeMatchers.isNotNull());
        FxAssert.verifyThat("#returnMenu", LabeledMatchers.hasText("Return to Menu"));
    }

    @Test
    public void testDiffWeaponEffect() {
        Weapon test1 = Weapon.getRandomWeapon();
        Weapon test2 = Weapon.getRandomWeapon();
        Weapon test3 = Weapon.getRandomWeapon();
        Game.getGame().getPlayer().getWeaponInventory().add(test1);
        Game.getGame().getPlayer().getWeaponInventory().add(test2);
        Game.getGame().getPlayer().getWeaponInventory().add(test3);
        for (int i = 0; i < 3; i++) {
            moveBy(0, 10);
        }
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        release(KeyCode.SPACE);
        Game.getGame().getPlayer().swapWeapon(test3);
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        release(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        Game.getGame().getPlayer().swapWeapon(test2);
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        release(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        Game.getGame().getPlayer().swapWeapon(test1);
        clickOn(MouseButton.PRIMARY);
        press(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        release(KeyCode.SPACE);
        for (int i = 0; i < 3; i++) {
            moveBy(1, 0);
        }
        assertEquals(0, Game.getGame().getPlayer().getPotionInventory().size());
    }
}
