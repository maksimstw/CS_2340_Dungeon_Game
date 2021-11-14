import character.Player;
import character.Potion;
import character.Weapon;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.NoSuchElementException;

public class PlayerTestM5 extends ApplicationTest {
    private Player player;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //the fx component is just to kick start javafx graphics
        //DO NOT replace with game itself！
        //it is meant to be empty window!
        //primaryStage.show();
        player = new Player();
        player.setWeapon(new Weapon("SAMPLE WEAPON"));
    }

    @Test
    public void testCollectableCategorization() {
        for (int i = 0; i < 100; i++) {
            Weapon w = Weapon.getRandomWeapon();
            Potion p = Potion.getRandomPotion();
            player.collectItem(w);
            player.collectItem(p);

            Assert.assertTrue(player.getWeaponInventory().contains(w));
            Assert.assertFalse(player.getPotionInventory().contains(w));
            Assert.assertTrue(player.getPotionInventory().contains(p));
            Assert.assertFalse(player.getWeaponInventory().contains(p));
        }
        Assert.assertEquals(100, player.getWeaponInventory().size());
        Assert.assertEquals(100, player.getPotionInventory().size());
    }

    @Test
    public void testWeaponSwap() {
        boolean exceptionEncountered = false;
        try {
            player.swapWeapon(new Weapon("obviously not in the inventory"));
        } catch (NoSuchElementException e) {
            exceptionEncountered = true;
        }
        Assert.assertTrue(exceptionEncountered);
        Weapon w = new Weapon("this one's in the inventory");
        player.collectItem(w);
        Weapon oldWeapon = player.getWeapon();
        player.swapWeapon(w);
        Assert.assertSame(w, player.getWeapon());
        //new weapon should no longer be in the inventory
        Assert.assertFalse(player.getWeaponInventory().contains(w));
        //old weapon should now be in the inventory
        Assert.assertTrue(player.getWeaponInventory().contains(oldWeapon));
    }
}
