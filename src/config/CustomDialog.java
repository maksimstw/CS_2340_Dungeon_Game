package config;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import main.Game;

public class CustomDialog {
    /**
     * pause the game when initializing
     */
    public void initialize() {
        if (Game.getGame() != null) {
            Game.getGame().getGameScreen().pauseGame(true);
        }
    }

    /**
     * resume game and close dialogg when closed
     * @param event the mouse click event, used to fetch the dialog stage
     */
    protected void resume(ActionEvent event) {
        if (Game.getGame() != null) {
            Game.getGame().getGameScreen().pauseGame(false);
        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
