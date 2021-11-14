import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.Game;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

public class M3TestFX extends ApplicationTest {

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
    public void testInitialRoom() {
        assertEquals(1, Game.getGame().getGameMap().getLocation()[0]);
        assertEquals(1, Game.getGame().getGameMap().getLocation()[1]);

        verifyThat("#moneyLbl", NodeMatchers.isNotNull());
        FxAssert.verifyThat("#moneyLbl", LabeledMatchers.hasText("9999999"));
        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("Start room"));
    }

    @Test
    public void testRoomEntering() {
        for (int i = 0; i < 50; i++) {
            press(KeyCode.UP);
        }
        assertEquals(2, Game.getGame().getGameMap().getLocation()[0]);
        assertEquals(1, Game.getGame().getGameMap().getLocation()[1]);

        FxAssert.verifyThat("#roomLbl", LabeledMatchers.hasText("2, 1"));
    }
}
