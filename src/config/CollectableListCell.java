package config;

import character.Collectable;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

public class CollectableListCell extends ListCell<Collectable> {

    @Override
    protected void updateItem(Collectable collectable, boolean empty) {
        super.updateItem(collectable, empty);
        if (empty || collectable == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(collectable.getName());
            ImageView icon = new ImageView();
            icon.setImage(collectable.getImage());
            setGraphic(icon);
        }
    }
}
