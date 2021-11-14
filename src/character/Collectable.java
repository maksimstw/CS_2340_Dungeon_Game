package character;

/**
 * common class for collectable objects, including weapon and potion
 * Let Collectable inherit Sprite. Assume there are necessary features
 * of Collectable will be implemented in the future.
 * @author Alan, Zijing
 */
public abstract class Collectable extends Sprite {
    public Collectable(String imageFileNamee, String name) {
        super(imageFileNamee);
        this.setName(name);
    }
}
