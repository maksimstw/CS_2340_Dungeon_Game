package shape;

/**
 * represents an abstract boundary in the game
 * @param <T> another boundary class
 */
public interface Boundary<T extends Boundary> {
    /**
     * determine whether a point falls within the boundary
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return whether there is an intersection
     */
    boolean inBounds(double x, double y);

    /**
     * determine whether another boundary of type T intersects with this boundary
     * @param other the other boundary to be judged against
     * @return whether there is an intersection
     */
    boolean intersects(T other);
}
