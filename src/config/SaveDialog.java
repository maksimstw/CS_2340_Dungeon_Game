package config;

import character.Collectable;
import character.Player;
import character.Potion;
import character.Weapon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import main.Game;
import shape.Vector;

import java.io.IOException;

public class SaveDialog extends CustomDialog {

    @FXML
    private ImageView weapon;
    @FXML
    private Label weaponName;
    @FXML
    private ListView<Collectable> weapons;
    @FXML
    private ListView<Collectable> potions;

    //cell factory converting collectable objects to custom cell objects
    private static Callback<ListView<Collectable>, ListCell<Collectable>> colToCell =
        collectableListView -> new CollectableListCell();

    //use potion
    private EventHandler<MouseEvent> usePotion = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && mouseEvent.getClickCount() == 2) {
                Potion p = (Potion) potions.getSelectionModel().getSelectedItem();
                Game.getGame().getPlayer().usePotion(p);
            }
        }
    };

    //use weapon (i.e. swap the current weapon with the selected weapon
    private EventHandler<MouseEvent> useWeapon = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                    && mouseEvent.getClickCount() == 2) {
                Weapon w = (Weapon) weapons.getSelectionModel().getSelectedItem();
                Player player = Game.getGame().getPlayer();
                player.swapWeapon(w);
                weapon.setImage(w.getImage());
                weaponName.setText(w.getName());
                Vector position = player.getPosition();
                player.getWeapon().setPosition(position.getX(), position.getY() + 5);
            }
        }
    };

    @FXML
    public void initialize() {
        weapons.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        potions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        weapons.setCellFactory(colToCell);
        potions.setCellFactory(colToCell);
        weapons.setItems(Game.getGame().getPlayer().getWeaponInventory());
        potions.setItems(Game.getGame().getPlayer().getPotionInventory());

        weapon.setImage(Game.getGame().getPlayer().getWeapon().getImage());
        weaponName.setText(Game.getGame().getPlayer().getWeapon().getName());

        weapons.setOnMouseClicked(useWeapon);
        potions.setOnMouseClicked(usePotion);
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) throws IOException {
        Game.getGame().saveGame();
        super.resume(event);
    }

    @FXML
    private void handleLoadBtn(ActionEvent event) throws IOException, ClassNotFoundException {
        Game.getGame().loadGame();
        super.resume(event);
    }

    @FXML
    protected void resume(ActionEvent event) {
        super.resume(event);
    }
}
