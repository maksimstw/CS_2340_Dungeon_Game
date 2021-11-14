import character.Player;
import character.Weapon;
import config.InitialConfigurationScreen;
import javafx.embed.swing.JFXPanel;
import main.Game;
import main.GameScreen;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class contains junit tests that will be given to the game
 */
public class PlayerTest {

    private Game game;
    private InitialConfigurationScreen ics;
    private GameScreen igs;
    private Player player;

    //JFXPanel must be initialized for testing JavaFX GUI! (By Taiwei Shi, Feb 22)
    private JFXPanel panel = new JFXPanel();

    //DELETE THIS BEFORE RELEASE!
    @Before
    public void setup() {
        game = new Game();
        player = new Player();

    }

    /**
     * Tests the character.Player Name
     * @author Feilian Huang
     */
    @Test
    public void testName() {
        player.setName("Dummy");
        assertEquals("Dummy", player.getName());
        player.setName("");
        assertEquals("", player.getName());
    }

    /**
     * Tests the weapon
     * @author Feilian Huang
     */
    @Test
    public void testWeapon() {
        player.setWeapon(new Weapon("Atomic Bomb"));
        assertEquals("Atomic Bomb", player.getWeapon().getName());
        player.setWeapon(new Weapon(""));
        assertEquals("", player.getWeapon().getName());
    }

    /**
     * Tests the difficulties
     * @author Feilian Huang
     */
    @Test
    public void testDifficulties() {
        player.setDifficulty(-13);
        assertEquals(-13, player.getDifficulty());
        player.setDifficulty(998989867);
        assertEquals(998989867, player.getDifficulty());
    }

    /**
     * Tests the player money attribute
     * @author Feilian Huang
     */
    @Test
    public void testMoney() {
        player.setMoney(-13);
        assertEquals(-13, player.getMoney());
        player.setMoney(998989867);
        assertEquals(998989867, player.getMoney());
    }


}
