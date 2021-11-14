package character;
import java.util.Random;

public class Potion extends Collectable {

    private final String type;

    public Potion(String type) {
        super("/res/" + type + ".png", type);

        this.type = type;
        setScale(0.1);
    }

    public static Potion getRandomPotion() {
        int t = (new Random()).nextInt(3);
        switch (t) {
        case 0:
            return new Potion("attackPotion");
        case 1:
            return new Potion("healthPotion");
        case 2:
            return new Potion("otherPotion");
        default:
            break;
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
