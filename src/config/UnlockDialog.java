package config;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.Game;

public class UnlockDialog extends CustomDialog {
    @FXML
    private void unlock(ActionEvent event) {
        if (Game.getGame() != null) {
            Game.getGame().getGameMap().unlockBossRoom();
        }
        super.resume(event);
    }
    @FXML
    protected void resume(ActionEvent event) {
        super.resume(event);
        if (Game.getGame() != null) {
            Game.getGame().getPlayer().lockDoorCollision();
        }
    }
}
