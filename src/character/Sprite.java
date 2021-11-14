package character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import main.Game;
import shape.Vector;

/**
 * This class is all the entity in the game that can move.
 *
 * @author Taiwei Shi
 */
public class Sprite {
    private double rotation;
    private double elapsedTime;
    private double speed;
    private int hp;
    private boolean isFlip;

    private final Vector position;
    private final Vector velocity;
    private final Rectangle boundary;

    private Image image;
    private String name;
    private double scale;
    private String path;

    /**
     * This is the constructor of the sprite class.
     */
    public Sprite() {
        this.position = new Vector();
        this.velocity = new Vector();
        this.boundary = new Rectangle();
        this.rotation = 0;
        this.elapsedTime = 0;
        this.isFlip = false;
    }

    /**
     * This is the second constructor of the class.
     *
     * @param imageFileName the image of the sprite
     */
    public Sprite(String imageFileName) {
        this();
        setImage(imageFileName);
        scale = 1;
        path = imageFileName;
    }

    public Image getImage() {
        return image;
    }

    /**
     * set the image of the entity
     *
     * @param imageFileName the path to the image
     */
    public void setImage(String imageFileName) {
        this.image = new Image(imageFileName);
        this.boundary.setHeight(this.image.getHeight());
        this.boundary.setWidth(this.image.getWidth());
        path = imageFileName;
    }

    /**
     * get the boundary(hit box) of this entity
     *
     * @return the boundary(hit box) of this entity
     */
    public Rectangle getBoundary() {
        this.boundary.setX(this.position.getX());
        this.boundary.setY(this.position.getY());
        return this.boundary;
    }

    /**
     * check if two entities are overlapped
     *
     * @param other the other entity
     * @return if two entities are overlapped
     */
    public Boolean overlaps(Sprite other) {
        return !(this.position.getX() + this.boundary.getWidth() < other.position.getX()
            || other.position.getX() + other.boundary.getWidth() < this.position.getX()
            || this.position.getY() + this.boundary.getHeight() < other.position.getY()
            || other.position.getY() + other.boundary.getHeight() < this.position.getY());
    }

    /**
     * check if the player overlaps with exit
     *
     * @param other image of exit
     * @return if two entities are overlapped
     */
    public Boolean overlaps(ImageView other) {

        return !(this.position.getX() + this.boundary.getWidth() < other.getX()
            || other.getX() + other.getFitWidth() < this.position.getX()
            || this.position.getY() + this.boundary.getHeight() < other.getY()
            || other.getY() + other.getFitHeight() < this.position.getY());
    }

    /**
     * During update(), this method could make sure that the entity does not move out of the screen.
     * @param sprite the sprite to wrap
     */
    public void wrap(Sprite sprite) {
        if (sprite.position.getX() < 0) {
            sprite.position.set(0, sprite.position.getY());
        } else if (sprite.position.getX()
            > Game.getGame().getScreenWidth()) {
            sprite.position.set(Game.getGame().getScreenWidth(),
                    sprite.position.getY());
        }

        if (sprite.position.getY() < 0) {
            sprite.position.set(sprite.position.getX(), 0);
        } else if (sprite.position.getY() > Game.getGame().getScreenHeight()) {
            sprite.position.set(sprite.position.getX(), Game.getGame().getScreenHeight());
        }
    }

    /**
     * update the position and lifetime of the entity
     *
     * @param deltaTime time elapsed per frame
     */
    public void update(double deltaTime) {
        this.position.add(this.velocity.getX() * deltaTime, this.velocity.getY() * deltaTime);
        elapsedTime += deltaTime;
    }

    public void setScale(double scale) {
        if (path == null || path.isEmpty()) {
            throw new NullPointerException("cannot scale empty image");
        }
        if (scale <= 0) {
            throw new IllegalArgumentException("cannot scale image to <= 0: " + scale);
        }
        Image temp = image;
        image = new Image(path,
                temp.getWidth() * scale, temp.getHeight() * scale, //new bounds
                true, true); //preserve height and smoothen image
        boundary.setWidth(image.getWidth());
        boundary.setHeight(image.getHeight());
    }

    /**
     * draw the entity on canvas(screen).
     * @param context the description of the screen
     */
    public void render(GraphicsContext context) {
        context.save();
        renderTranslate(context, this);
        context.restore();
    }

    /**
     * A helper method for render(GraphicsContext context) to translate
     * a sprite object
     * @author Zijing
     * @param context the context that the sprite is drawn on
     * @param sprite the sprite that is drawn
     */
    private void renderTranslate(GraphicsContext context, Sprite sprite) {
        context.translate(sprite.position.getX(), sprite.position.getY());
        context.rotate(sprite.rotation);
        context.translate(-sprite.image.getWidth() / 2, -sprite.image.getHeight() / 2);
        if (!isFlip) {
            context.drawImage(sprite.image, 0, 0);
        } else {
            context.drawImage(
                sprite.image, 0, 0,
                sprite.image.getWidth(), sprite.image.getHeight(), sprite.image.getWidth(),
                0, -sprite.image.getWidth(), sprite.image.getHeight());
        }
    }

    /**
     * A getter method for name
     * @return name of instance
     */
    public String getName() {
        return name;
    }

    /**
     * a setter method for name
     * @param name name of instance
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A getter method for rotation
     * @return rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * A getter method for position
     * @return position
     */
    public Vector getPosition() {
        return position;
    }

    public void setPosition(double x, double y) {
        position.set(x, y);
    }

    public void setPosition(Vector v) {
        position.set(v.getX(), v.getY());
    }

    /**
     * A getter method for velocity
     * @return velocity
     */
    public Vector getVelocity() {
        return velocity;
    }

    /**
     * A getter method for elapsedTime
     * @return elapsedTime
     */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     * a setter method for name
     * @param rotation rotation of sprite
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    /**
     * getter for isFlip
     * @return the hp of the sprite
     */
    public boolean getFlip() {
        return isFlip;
    }

    /**
     * a setter method for isFlip.
     * @param flip whether the image is flip
     */
    public void setFlip(boolean flip) {
        this.isFlip = flip;
    }

    /**
     * accessor for hp
     * @return the hp of the sprite
     */
    public int getHp() {
        return hp;
    }

    /**
     * setter for hp
     * @param hp the new hp
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * another setter for hp, but with change amount instead of new hp value
     * this can be used for damage (negative change) or healing (positive change)
     * @param amountOfChange the amount for hp to change
     */
    public void changeHp(int amountOfChange) {
        this.hp += amountOfChange;
    }

    /**
     * a setter for speed
     * @param speed the magnitude of the velocity
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * a getter for speed
     * @return the magnitude of the velocity
     */
    public double getSpeed() {
        return speed;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}