package config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.Game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOver implements Initializable {
    @FXML
    private void returnToMenu() throws IOException {
        Game.getGame().switchScreen("config/welcome_screen.fxml");
    }
    @FXML
    private Label stats1;
    @FXML
    private Label stats2;
    @FXML
    private Label stats3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        render();
    }
    private void render() {
        if (Game.getGame().getPlayer().getHp() > 0) {
            stats1.setText(String.valueOf(Game.getGame().getPlayer().getHp()));
            int numPotion = Game.getGame().getPlayer().getPotionInventory().size();
            int numWeapon = Game.getGame().getPlayer().getWeaponInventory().size();
            int used = Game.getGame().getPlayer().getUsedPotionList().size();
            stats2.setText(String.valueOf(numWeapon));
            stats3.setText(String.valueOf(numPotion + used));
        }
    }
}
