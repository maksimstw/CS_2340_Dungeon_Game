package map;

import main.Game;
import shape.Rect;

import java.io.IOException;
import java.io.Serializable;

public class GameMap implements Serializable {
    public static final int MAP_SIZE = 6;

    private int currX;
    private int currY;

    private MazeRoom[][] gameMap = new MazeRoom[MAP_SIZE][MAP_SIZE];
    private MazeRoom currentRoom;

    //rectangular boundaries for four doors
    private Rect doorTopBounds;
    private Rect doorRightBounds;
    private Rect doorBottomBounds;
    private Rect doorLeftBounds;

    private static final double DOOR_W = 80;
    private static final double DOOR_H = 40;

    private boolean bossRoomLocked;

    public GameMap() {
        generateMap();
    }

    /**
     * generate gameMap, the starting room is always at 1, 1,
     * the exit room is always at lower right corner
     */
    public void generateMap() {
        //always start at 1, 1
        bossRoomLocked = true;
        currX = 1;
        currY = 1;

        gameMap = new MazeRoom[MAP_SIZE][MAP_SIZE];
        //start room is always at 1, 1
        currentRoom = new MazeRoom("START", true);
        gameMap[1][1] = currentRoom;

        //make sure rooms next to 1, 1 are filled by random, non-boss rooms
        gameMap[0][1] = MazeRoom.getRandomRoom();
        gameMap[1][0] = MazeRoom.getRandomRoom();
        gameMap[2][1] = MazeRoom.getRandomRoom();
        gameMap[1][2] = MazeRoom.getRandomRoom();

        int cursorX = 2;
        int cursorY = 1;
        //random walk from 1, 1 to lower right corner
        //fill the walk path with random rooms
        int step = 0;
        //the third & second last rooms from start to exit will always be challenge
        int challenge1 = 1;
        int challenge2 = 2 * MAP_SIZE - 6;

        while (cursorX != MAP_SIZE - 1 || cursorY != MAP_SIZE - 1) {
            if (cursorX < MAP_SIZE - 1 && Math.random() < 0.5) {
                cursorX++;
            } else if (cursorY < MAP_SIZE - 1) {
                cursorY++;
            } else {
                cursorX++;
            }

            step++;
            if (step == challenge1 || step == challenge2) {
                gameMap[cursorX][cursorY] = new ChallengeRoom(false);
            } else {
                gameMap[cursorX][cursorY] = MazeRoom.getRandomRoom();
            }
        }
        //exit room is always at lower right corner
        gameMap[MAP_SIZE - 1][MAP_SIZE - 1] = new MazeRoom("EXIT", false);
        //printMap();

        //initialize door boundaries
        if (Game.getGame() != null) {
            double w = Game.getGame().getScreenWidth();
            double h = Game.getGame().getScreenHeight();
            doorTopBounds = Rect.getRectByCenterSize(w / 2, 0, DOOR_W, DOOR_H);
            doorRightBounds = Rect.getRectByCenterSize(w, h / 2, DOOR_H, DOOR_W);
            doorBottomBounds = Rect.getRectByCornerSize(w / 2, h, DOOR_W, DOOR_H);
            doorLeftBounds = Rect.getRectByCenterSize(0, h / 2, DOOR_H, DOOR_W);
        }
    }

    public static final int TOP_DOOR = 1;
    public static final int RIGHT_DOOR = 2;
    public static final int BOTTOM_DOOR = 3;
    public static final int LEFT_DOOR = 4;
    public static final int NOT_IN_ANY_DOOR = 0;
    /**
     * test whether the player is inside any door transition boundary
     * @param r the player sprite's collider
     * @return which door the player went through
     */

    public int testDoorBounds(Rect r) {
        if (doorTopBounds.intersects(r)) {
            return TOP_DOOR;
        } else if (doorRightBounds.intersects(r)) {
            return RIGHT_DOOR;
        } else if (doorBottomBounds.intersects(r)) {
            return BOTTOM_DOOR;
        } else if (doorLeftBounds.intersects(r)) {
            return LEFT_DOOR;
        } else {
            return NOT_IN_ANY_DOOR;
        }
    }

    /**
     * enter a door on the map
     * @param doorCode integer representing the designated door
     * @return the new player position, if no door entered, then return (-1, -1)
     */
    public double[] enterDoor(int doorCode) {
        double w;
        double h;
        if (Game.getGame() != null) {
            w = Game.getGame().getScreenWidth();
            h = Game.getGame().getScreenHeight();
        } else {
            w = 800;
            h = 600;
        }

        switch (doorCode) {
        case (TOP_DOOR):
            if (switchRoom(currX, currY - 1)) {
                return new double[]{w / 2, h - 30};
            }
            break;
        case (RIGHT_DOOR):
            if (switchRoom(currX + 1, currY)) {
                return new double[]{30, h / 2};
            }
            break;
        case (BOTTOM_DOOR):
            if (switchRoom(currX, currY + 1)) {
                return new double[]{w / 2, 30};
            }
            break;
        case (LEFT_DOOR):
            if (switchRoom(currX - 1, currY)) {
                return new double[]{w - 30, h / 2};
            }
            break;
        default:
            break;
        }
        return new double[]{-1, -1};
    }

    /**
     * unlocks the boss room so the player can enter it
     */
    public void unlockBossRoom() {
        bossRoomLocked = false;
    }

    /**
     * entering a room
     * @author alan
     * @param roomX the x coordinate of the new room
     * @param roomY the y coordinate of the new room
     * @return whether the room is valid
     */
    public boolean switchRoom(int roomX, int roomY) {
        if (checkRoomLegitimacy(roomX, roomY)) {
            //when the current room is unsafe, the player can only enter visited rooms
            if (!currentRoom.isSafe()) {
                if (!gameMap[roomX][roomY].getVisited()) {
                    return false;
                }
            }

            if (Game.getGame() != null) {
                if (gameMap[roomX][roomY].toString().equals("EXIT")
                    && bossRoomLocked) {
                    //pop up dialogue
                    try {
                        Game.getGame().spawnDialog("config/unlock_dialog.fxml");
                    } catch (IOException e) {

                    }
                    return false;
                }
            }

            //room index is legitimate
            currX = roomX;
            currY = roomY;

            currentRoom = gameMap[roomX][roomY];
            currentRoom.visit();

            System.out.println("entered room " + currentRoom + " at: " + roomX + ", " + roomY
                    + " with neighbors:"
                    + (checkRoomLegitimacy(roomX, roomY - 1) ? " up" : "")
                    + (checkRoomLegitimacy(roomX + 1, roomY) ? " right" : "")
                    + (checkRoomLegitimacy(roomX, roomY + 1) ? " down" : "")
                    + (checkRoomLegitimacy(roomX - 1, roomY) ? " left" : ""));
            //load room content
            //System.out.println("PLEASE IMPLEMENT ROOM LOADING!");
            return true;
        }
        // else: room index is illegitimate, do nothing
        return false;
    }

    /**
     * checks if (x, y) is a legitimate coordinate on the map
     * @author alan
     * @param x horizontal coordinate to check
     * @param y vertical coordinate to check
     * @return whether (x, y) is a legitimate room
     */
    public boolean checkRoomLegitimacy(int x, int y) {
        //during challenge mode, all doors are locked!
        if (gameMap[currX][currY] != null
                && gameMap[currX][currY] instanceof ChallengeRoom
                && !gameMap[currX][currY].isSafe()) {
            return false;
        }
        //otherwise, check legitimacy by seeing if map entry is non-null
        if (x >= 0 && y >= 0 && x < MAP_SIZE && y < MAP_SIZE) {
            return gameMap[x][y] != null;
        }
        return false;
    }

    /**
     * returns the current map
     * @return the map of the maze
     * @author alan
     */
    public MazeRoom[][] getMap() {
        return gameMap;
    }

    /**
     * prints the current map, for testing purpose only
     * @author alan
     */
    private void printMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (gameMap[j][i] != null) {
                    System.out.print(gameMap[j][i] + "\t");
                } else {
                    System.out.print("null\t");
                }
            }
            System.out.println();
        }
    }

    /**
     * return current room in the maze
     * @return the current maze room
     */
    public MazeRoom getCurrentRoom() {
        return currentRoom;
    }

    /**
     * get current x,y indices of the player on the map
     * @return {x, y}
     */
    public int[] getLocation() {
        return new int[]{currX, currY};
    }
}
