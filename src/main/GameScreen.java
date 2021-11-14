package main;

import character.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;



//import javafx.scene.robot.Robot;
import map.ChallengeRoom;
import map.GameMap;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This is the actual game screen
 *
 * @author Taiwei Shi
 */
public class GameScreen implements Initializable {
    @FXML
    private Label moneyLbl;
    @FXML
    private Label healthLbl;
    @FXML
    private Label roomLbl;
    @FXML
    private Canvas canvas;
    @FXML
    private StackPane stackPane;

    //rocks that block inaccessible doors
    @FXML
    private ImageView blockTop;
    @FXML
    private ImageView blockRight;
    @FXML
    private ImageView blockBottom;
    @FXML
    private ImageView blockLeft;

    //escape portal
    @FXML
    private ImageView portal;
    @FXML
    private Label portalLbl;

    private final ArrayList<String> keyPressedList = new ArrayList<>();
    private final ArrayList<Sprite> laserList = new ArrayList<>();
    private ArrayList<Monster> monsterList = new ArrayList<>();
    private ArrayList<Collectable> droppedItem = new ArrayList<>();
    private GraphicsContext context;
    private AnimationTimer gameLoop;
    private Player player; //
    private Sprite background;
    private final double fps = 60.0;
    private double mouseX;
    private double mouseY;

    /**
     * Initialize the controller
     *
     * @param url FXML file
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCanvas();
        initPlayer();
        initBackground();

        //initialize map
        Game.getGame().getGameMap().generateMap();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {

                //handle all the user input
                handleKeyEvents();

                //render background
                renderBackground();

                //render in-game objects
                renderInGameObjects();

                //update
                player.update(1 / fps);
                updateLaser();
                updateBlockade();
                updateItems();
                updateMonsters();
                updatePlayerAttack();
                updatePotionEffect();
                try {
                    updateMonsterAttack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    onPortal();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //render
                player.render(context);
                renderLaser();
                renderMonsters();
                renderItems();
            }
        };

        gameLoop.start();

    }

    /**
     * handler of the OnKeyPressed
     *
     * @param event OnKeyPressed event
     */
    @FXML
    protected void handleOnKeyPressed(KeyEvent event) {
        String keyName = event.getCode().toString();
        if (!keyPressedList.contains(keyName)) {
            keyPressedList.add(keyName);
        }
    }

    /**
     * handler of the OnKeyReleased
     *
     * @param event OnKeyReleased event
     */
    @FXML
    private void handleOnKeyReleased(KeyEvent event) {
        String keyName = event.getCode().toString();
        keyPressedList.remove(keyName);
    }

    @FXML
    private void handleMousePressed(MouseEvent event) {
        mouseY = event.getY();
        mouseX = event.getX();
        spawnLaser();
        updateWeapon();
    }

    /**
     * when player overlaps with portal
     * either start challenge or exit the maze and go to the win screen
     * @throws IOException not finding win_screen.fxml
     */
    @FXML
    private void onPortal() throws IOException {
        GameMap map = Game.getGame().getGameMap();
        if (player.overlaps(portal) && portal.isVisible()) {
            if (map.getCurrentRoom().toString().equals("EXIT")) {
                Game.getGame().switchScreen("config/win_screen.fxml");
                gameLoop.stop();
            } else if (map.getCurrentRoom() instanceof ChallengeRoom) {
                ((ChallengeRoom) map.getCurrentRoom()).enterChallenge();
            }
        }
    }

    /**
     * By default, key event can only be received by scene. However,
     * I have no idea about passing the scene to the controller,
     * so I change the default setting by letting the borderpane,
     * which is also the root of this scene, to receive the key events
     * Apparently this is not a good idea. Scene is really
     * important especially the game will become quite
     * complex in the future.
     *
     * @author Taiwei Shi
     *
     */
    public void focus() {
        stackPane.requestFocus();
    }

    /**
     * Initialize background
     */
    private void initBackground() {
        background = new Sprite("res/general_room1.png");
        background.setPosition(400, 300);
        background.render(context);
    }

    /**
     * Initialize canvas.
     */
    private void initCanvas() {
        context = canvas.getGraphicsContext2D();
        context.setFill(Color.BLACK);
    }

    /**
     * Initialize the player.
     */
    private void initPlayer() {
        player = Game.getGame().getPlayer();
        player.setPosition(100, 300);
        player.getWeapon().setPosition(100, 305);
        player.render(context);
    }

    /**
     * Spawn laser.
     */
    private void spawnLaser() {
        int laserType = player.getWeapon().getType();
        Sprite laser = new Sprite("res/laser" + laserType + ".png");
        double degree = Math.toDegrees(Math.atan2(
                mouseY - player.getPosition().getY(),
                mouseX - player.getPosition().getX()));
        laser.setRotation(degree);
        // let laser released from peripheral
        double extensionDist =  player.getWeapon().getImage().getWidth() * 0.8;
        double rad = Math.toRadians(laser.getRotation());
        double x = player.getPosition().getX() + Math.cos(rad) * extensionDist;
        double y = player.getPosition().getY() + Math.sin(rad) * extensionDist;
        laser.setPosition(x, y);
        laser.getVelocity().setMag(player.getWeapon().getWeaponSpeed());
        laser.getVelocity().setAngle(laser.getRotation());
        laserList.add(laser);
        // flip player and weapon accordingly
        // attack direction is left, then flip == true
        boolean flip = degree <= -90 || degree >= 90;
        player.setFlip(flip);

    }

    /**
     * Render weapon every time an attack is fired
     * deal with position and facing direction update
     * The attack is now laser.
     * @author Zijing
     */
    private void updateWeapon() {
        Weapon weapon = player.getWeapon();
        int isFlip = weapon.getFlip() ? 1 : 0;
        double degree = Math.toDegrees(Math.atan2(
                mouseY - player.getPosition().getY(),
                mouseX - player.getPosition().getX()));

        // degree difference to subtract if weapon image is flipped
        double flipDegree = -degree + Math.signum(degree) * (Math.abs(degree) - 180);
        degree = degree + isFlip * flipDegree;
        weapon.setRotation(degree);
    }

    /**
     * Handle user input.
     */
    private void handleKeyEvents() {
        if (keyPressedList.contains("A")) {
            player.getVelocity().setMag(300);
            if (keyPressedList.contains("S")) {
                player.getVelocity().setAngle(135);
            } else if (keyPressedList.contains("W")) {
                player.getVelocity().setAngle(225);
            } else {
                player.getVelocity().setAngle(180);
            }
            //player.setRotation(player.getRotation() - 3);
        } else if (keyPressedList.contains("D")) {
            player.getVelocity().setMag(300);
            if (keyPressedList.contains("S")) {
                player.getVelocity().setAngle(45);
            } else if (keyPressedList.contains("W")) {
                player.getVelocity().setAngle(315);
            } else {
                player.getVelocity().setAngle(0);
            }
            //player.setRotation(player.getRotation() + 3);
        } else if (keyPressedList.contains("S")) {
            player.getVelocity().setMag(300);
            player.getVelocity().setAngle(90);
        } else if (keyPressedList.contains("W")) {
            player.getVelocity().setMag(300);
            player.getVelocity().setAngle(270);
        } else {
            player.getVelocity().setMag(0);
        }

        if (keyPressedList.contains("SPACE")) {
            spawnLaser();
            updateWeapon();
        }

        if (keyPressedList.contains("ESCAPE")) {
            keyPressedList.remove("ESCAPE");
            try {
                Game.getGame().spawnDialog("config/save_dialog.fxml");
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * try to set the pause state of the game, if it is already running
     * @param paused true for pause, false for not pause
     */
    public void pauseGame(boolean paused) {
        if (paused) {
            gameLoop.stop();
        } else {
            keyPressedList.clear();
            gameLoop.start();
        }
    }

    /**
     * Render in-game objects
     * currently just spawns a portal for the exit level
     */
    private void renderInGameObjects() {
        GameMap map = Game.getGame().getGameMap();

        moneyLbl.setText(String.valueOf(Game.getGame().getPlayer().getMoney()));
        healthLbl.setText(String.valueOf(Game.getGame().getPlayer().getHp()));
        portal.setVisible(false);
        portalLbl.setVisible(false);

        if (map.getCurrentRoom().toString().equals("EXIT") && map.getCurrentRoom().isSafe()) {
            portal.setVisible(true);
            portalLbl.setText("escape");
            portalLbl.setVisible(true);
            roomLbl.setText("Exit room");
        } else if (map.getCurrentRoom() instanceof ChallengeRoom) {
            if (map.getCurrentRoom().isSafe()) {
                ((ChallengeRoom) map.getCurrentRoom()).exitChallenge();
            }
            if (((ChallengeRoom) map.getCurrentRoom()).getPortalVisibility()) {
                portal.setVisible(true);
                portalLbl.setText("start challenge");
                portalLbl.setVisible(true);
                roomLbl.setText("Challenge room");
            }
        } else if (map.getCurrentRoom().toString().equals("START")) {
            roomLbl.setText("Start room");
        } else {
            roomLbl.setText(map.getLocation()[0] + ", " + map.getLocation()[1]);
        }
    }

    /**
     * Render background
     */
    //make it public
    public void renderBackground() {
        context.fillRect(0, 0, Game.getGame().getScreenWidth(), Game.getGame().getScreenHeight());
        background.render(context);
    }

    /**
     * Update Laser
     */
    private void updateLaser() {
        for (int i = 0; i < laserList.size(); i++) {
            Sprite laser = laserList.get(i);
            laser.update(1 / fps);
            if (laser.getElapsedTime() > player.getWeapon().getWeaponRange()) {
                laserList.remove(i);
            }
        }
    }

    /**
     * update the blockades that block inaccessible entries
     */
    private void updateBlockade() {
        GameMap map = Game.getGame().getGameMap();
        int currX = map.getLocation()[0];
        int currY = map.getLocation()[1];

        blockTop.setVisible(!map.checkRoomLegitimacy(currX, currY - 1));
        blockRight.setVisible(!map.checkRoomLegitimacy(currX + 1, currY));
        blockBottom.setVisible(!map.checkRoomLegitimacy(currX, currY + 1));
        blockLeft.setVisible(!map.checkRoomLegitimacy(currX - 1, currY));
    }

    /**
     * update monsters
     */
    private void updateMonsters() {
        monsterList = Game.getGame().getGameMap().getCurrentRoom().getMonsterList();
        for (Monster m : monsterList) {
            m.update(1 / fps);
        }
    }

    /**
     * update items
     */
    private void updateItems() {
        droppedItem = Game.getGame().getGameMap().getCurrentRoom().getItemsList();
        for (int i = 0; i < droppedItem.size(); i++) {
            Collectable item = droppedItem.get(i);
            if (player.overlaps((item))) {
                droppedItem.remove(item);
                player.collectItem(item);
            }
        }
    }

    /**
     * render monsters
     */
    private void renderMonsters() {
        for (Monster m : monsterList) {
            m.render(context);
        }
    }

    /**
     * render laser
     */
    private void renderLaser() {
        for (Sprite laser : laserList) {
            laser.render(context);
        }
    }

    /**
     * render items
     */
    private void renderItems() {
        for (Collectable item: droppedItem) {
            item.render(context);
        }
    }

    /**
     * update player attack
     */
    private void updatePlayerAttack() {
        for (int i = 0; i < laserList.size(); i++) {
            Sprite laser = laserList.get(i);
            try {
                for (Sprite monster : monsterList) {
                    if (laser.overlaps(monster)) {
                        laserList.remove(i);
                        monster.changeHp(player.getWeapon().getWeaponDamage());
                    }
                    if (monster.getHp() <= 0) {
                        Game.getGame().getGameMap().getCurrentRoom().
                                removeMonster((Monster) monster);

                    }
                }
            } catch (Exception ignored) {
                break;
            }
        }
    }

    /**
     * update monster attack
     * @throws IOException if the player dies, switch to lost screen.
     */
    private void updateMonsterAttack() throws IOException {
        for (Sprite monster : monsterList) {
            if (player.overlaps(monster)) {
                int damage = monster instanceof Boss ? 5 : 1;
                player.changeHp(-1 * damage);
            }
            if (player.getHp() <= 0) {
                gameLoop.stop();
                Game.getGame().switchScreen("config/lose_screen.fxml");
            }
        }
    }

    private void updatePotionEffect() {
        for (int i = 0; i < player.getUsedPotionList().size(); i++) {
            if (player.getElapsedTime() > player.getUsedPotionList().get(i).getElapsedTime() + 10) {
                switch (player.getUsedPotionList().get(i).getType()) {
                case "attackPotion":
                    player.getWeapon().setWeaponDamage(player.getWeapon().getWeaponDamage() + 5);
                    System.out.println("attack potion effect ends");
                    break;
                case "otherPotion":
                    player.setSpeed(player.getSpeed() + 100);
                    System.out.println("other potion effect ends");
                    break;
                default:
                    break;
                }
                player.getUsedPotionList().remove(player.getUsedPotionList().get(i));
                --i;
            }
        }
    }
}
