package config;

import character.Weapon;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import main.Game;

/**
 * This class controls the initial configuration screen, which links to
 * initial_configuration_screen.fxml for layout
 *
 * @author alan tao, Taiwei Shi
 */
public class InitialConfigurationScreen {
    //several UI components of the page, see corresponding fxid values in .fxml file
    @FXML
    private TextField nameTxt;
    @FXML
   private Label errorLbl;
    @FXML
    private Slider difficultySlider;
    @FXML
    private RadioButton weapon1Btn;
    @FXML
    private RadioButton weapon2Btn;

    private String errorMessage;

    /**
     * Checks for player values, then starts game if everything is valid
     */
    @FXML
    private void startButtonOnClick() {
        if (checkNameValidity(nameTxt.getText())) {
            //set attributes of the player
            init();

            //launch game, see respective method for implementation
            startGame();
        } else {
            //not all parameters valid, then update error message on screen and not launch
            nameTxt.clear();
            errorLbl.setText(errorMessage);
        }
    }

    /**
     * This method checks whether the character name is valid and
     * updates the errorMessage on this screen
     *
     * @param name the name of the character
     * @return whether the name is valid
     */
    private boolean checkNameValidity(String name) {
        if (name == null) {
            errorMessage = "name cannot be null";
            return false;
        } else if (name.length() == 0) {
            errorMessage = "name cannot be empty";
            return false;
        } else if (name.trim().length() == 0) {
            errorMessage = "name cannot be all white space";
            return false;
        } else {
            errorMessage = "";
            return true;
        }
    }

    /**
     * only called when start button clicked, and all parameters are legal
     * transitions to the next screen
     */
    private void startGame() {
        try {
            Game.getGame().switchScreen("main/game_screen.fxml");
        } catch (IOException e) {
            System.out.println("make sure there is a file called maze_screen.fxml!");
            e.printStackTrace();
        }
    }

    /**
     * This is the switch screen button, which links to initial_configuration_screen.fxml
     *
     * @throws IOException potential IOException
     */
    public void clickStartButton() throws IOException {
        Game.getGame().switchScreen("config/initial_configuration_screen.fxml");
    }

    /**
     * Initialize configuration.
     * (This should be private. Make it public for testing purpose only.)
     *
     * game attribute accessor/mutators added
     * @author Taiwei Shi, Xiang Li, Alan Tao
     */
    private void init() {
        Game.getGame().getPlayer().setName(nameTxt.getText());
        //set difficulty and related gold amount
        Game.getGame().getPlayer().setDifficulty((int) difficultySlider.getValue());
        switch (Game.getGame().getPlayer().getDifficulty()) {
        case 1:
            Game.getGame().getPlayer().setMoney(9999999);
            Game.getGame().getPlayer().setHp(300);
            break;
        case 2:
            Game.getGame().getPlayer().setMoney(999);
            Game.getGame().getPlayer().setHp(200);
            break;
        default:
            Game.getGame().getPlayer().setMoney(0);
            Game.getGame().getPlayer().setHp(100);
            break;
        }
        if (weapon1Btn.isSelected()) {
            Game.getGame().getPlayer().setWeapon(new Weapon(1));
        } else if (weapon2Btn.isSelected()) {
            Game.getGame().getPlayer().setWeapon(new Weapon(2));
        } else {
            Game.getGame().getPlayer().setWeapon(new Weapon(3));
        }
        Game.getGame().getPlayer().clearInventory();
    }
}
