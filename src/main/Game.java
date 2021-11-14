package main;

import character.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import map.GameMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game extends Application {
    //screen size
    private final int screenWidth = 800;
    private final int screenHeight = 600;

    //singleton for Game class, call Game.instance.METHOD to invoke METHOD for the Game class
    private static Game instance;
    private Stage primaryStage;
    private Player player;
    private GameMap gameMap;
    private GameScreen gameScreen;

    /**
     * Initialize game object
     * do NOT add parameters to this constructor
     */
    public Game() {
        instance = this;
        player = new Player("res/player1.png");
        gameMap = new GameMap();
    }

    /**
     * getter method for Game
     * @return game instance
     */
    public static Game getGame() {
        return instance;
    }

    /**
     * getter method for player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        //load and present welcome screen at launch
        Parent root = FXMLLoader.load(getClass().getResource("../config/welcome_screen.fxml"));
        Scene scene = new Scene(root, screenWidth, screenHeight);

        primaryStage.setTitle("Dungeon Crawler Adventure Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Switch to the screen corresponding to the provided fxml file name
     *
     * @param destinationFXML the fxml corresponding to the screen
     * @throws IOException by fxml loader
     */
    public void switchScreen(String destinationFXML) throws IOException {
        Parent targetFXML;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource("/" + destinationFXML));
            targetFXML = loader.load();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                    "destination called " + destinationFXML + " does not exist");
        }

        Scene scene;
        if (primaryStage != null) {
            scene = new Scene(targetFXML);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
        } else {
            throw new NullPointerException("primary stage is empty!");
        }

        if (destinationFXML.equals("main/game_screen.fxml")) {
            gameScreen = loader.getController();
            gameScreen.focus();
        }
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void saveGame() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("save");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(player);
        out.writeObject(gameMap);
        out.close();
    }

    public void loadGame() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("save");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        player = (Player) in.readObject();
        gameMap = (GameMap) in.readObject();
        in.close();
    }

    public void spawnDialog(String dialogPath) throws IOException {
        if (primaryStage.isFocused()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/" + dialogPath));
            Parent parent = fxmlLoader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * entrance for the game
     *
     * @param args parameters given to the game
     */
    public static void main(String[] args) {
        launch(args);
    }
}