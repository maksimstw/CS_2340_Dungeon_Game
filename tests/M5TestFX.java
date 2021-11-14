import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import character.Collectable;
import main.Game;
import character.Monster;
import character.Potion;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class M5TestFX extends ApplicationTest {

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
    public void testDeath() {
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
        clickOn("#newGameBtn");
        //clickOn("weapon2Btn");
        clickOn("#nameTxt");
        write("secondGame!");
        clickOn("#startBtn");
        for (int i = 0; i < 30; i++) {
            moveBy(0, -1);
        }
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
    }

    @Test
    public void testDrop() {
        Game.getGame().getPlayer().setHp(2000);
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
        for (int i = 0; i < 30; i++) {
            moveBy(0, -1);
        }
        assertTrue(Game.getGame().getGameMap().getCurrentRoom().getItemsList().size() > 0);
    }

    @Test
    public void testInventory() {
        Game.getGame().getPlayer().setHp(2000);
        press(KeyCode.ESCAPE);
        for (int i = 0; i < 10; i++) {
            moveBy(0, -1);
        }
        clickOn("#res");
        for (int i = 0; i < 50; i++) {
            press(KeyCode.D);
        }
        release(KeyCode.D);
        press(MouseButton.PRIMARY);
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("2, 1"));
    }

    @Test
    public void testCollect() {
        Game.getGame().getPlayer().setHp(2000);
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
        Monster a4 = new Monster(1);
        Game.getGame().getGameMap().getCurrentRoom().addMonster(a4);
        Monster a5 = new Monster(1);
        Game.getGame().getGameMap().getCurrentRoom().addMonster(a5);
        for (int i = 0; i < 5; i++) {
            moveBy(0, -1);
        }
        ArrayList<Monster> store = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        int a = store.size();
        for (int i = 0; i < a; i++) {
            Game.getGame().getGameMap().getCurrentRoom().removeMonster(store.get(0));
        }
        for (int i = 0; i < 5; i++) {
            moveBy(0, 1);
        }
        ArrayList<Collectable> items = Game.getGame().getGameMap().getCurrentRoom().getItemsList();
        Game.getGame().getPlayer().collectItem(items.get(0));
        for (int i = 0; i < 5; i++) {
            moveBy(0, 1);
        }
        press(KeyCode.ESCAPE);
        for (int i = 0; i < 20; i++) {
            moveBy(0, -1);
        }
        assertTrue(Game.getGame().getPlayer().getPotionInventory().size() > 0);
    }

    @Test
    public void testHealthPotion() {
        Game.getGame().getPlayer().setHp(250);
        Potion a = new Potion("healthPotion");
        Game.getGame().getPlayer().getPotionInventory().add(a);
        press(KeyCode.ESCAPE);
        for (int i = 0; i < 20; i++) {
            moveBy(0, 1);
        }
        clickOn("#res");
        Game.getGame().getPlayer().usePotion((Potion)
                (Game.getGame().getPlayer().getPotionInventory().get(0)));
        assertTrue(Game.getGame().getPlayer().getPotionInventory().size() == 0);
        for (int i = 0; i < 20; i++) {
            moveBy(0, 1);
        }
        assertTrue(Game.getGame().getPlayer().getHp() == 300);
    }

    @Test
    public void testAttackPotion() {
        int prevDamage = Game.getGame().getPlayer().getWeapon().getWeaponDamage();
        Potion a = new Potion("attackPotion");
        Game.getGame().getPlayer().getPotionInventory().add(a);
        press(KeyCode.ESCAPE);
        for (int i = 0; i < 20; i++) {
            moveBy(0, 1);
        }
        clickOn("#res");
        Game.getGame().getPlayer().usePotion((Potion)
                (Game.getGame().getPlayer().getPotionInventory().get(0)));
        assertTrue(Game.getGame().getPlayer()
                .getPotionInventory().size() == 0);
        int currDamage = Game.getGame().getPlayer().getWeapon().getWeaponDamage();
        for (int i = 0; i < 20; i++) {
            moveBy(0, 1);
        }
        assertTrue(currDamage < prevDamage);
    }

    @Test
    public void testOtherPotion() {
        double prevSpeed = Game.getGame().getPlayer().getSpeed();
        Potion a = new Potion("otherPotion");
        Game.getGame().getPlayer().getPotionInventory().add(a);
        press(KeyCode.ESCAPE);
        for (int i = 0; i < 20; i++) {
            moveBy(0, 1);
        }
        clickOn("#res");
        Game.getGame().getPlayer().usePotion((Potion)
                (Game.getGame().getPlayer().getPotionInventory().get(0)));
        assertTrue(Game.getGame().getPlayer().getPotionInventory().size() == 0);
        double currSpeed = Game.getGame().getPlayer().getSpeed();
        for (int i = 0; i < 20; i++) {
            moveBy(0, 1);
        }
        assertTrue(currSpeed < prevSpeed);
    }

    @Test
    public void testUsedPotion() {
        Potion a = new Potion("healthPotion");
        Game.getGame().getPlayer().getPotionInventory().add(a);
        Potion b = new Potion("attackPotion");
        Game.getGame().getPlayer().getPotionInventory().add(b);
        Potion c = new Potion("otherPotion");
        Game.getGame().getPlayer().getPotionInventory().add(c);
        press(KeyCode.ESCAPE);
        for (int i = 0; i < 22; i++) {
            moveBy(0, -10);
        }
        //scroll(3);
        clickOn("#res");
        for (int i = 0; i < 3; i++) {
            Game.getGame().getPlayer().usePotion((Potion)
                    (Game.getGame().getPlayer().getPotionInventory().get(0)));
        }
        press(KeyCode.ESCAPE);
        assertTrue(Game.getGame().getPlayer().getPotionInventory().size() == 0);
        assertEquals(2, Game.getGame().getPlayer().getUsedPotionList().size());
    }
}
