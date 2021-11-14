package shape;

/**
 * this is a 2d vector
 *
 * @author Taiwei Shi
 */
public class Vector {
    private double x;
    private double y;

    /**
     * Default constructor that set the position to (0,0)
     */
    public Vector() {
        this(0, 0);
    }

    /**
     * A second constructor that set the position of (x,y)
     *
     * @param x x component of the vector
     * @param y y component of the vector
     */
    public Vector(double x, double y) {
        this.set(x, y);
    }

    /**
     * setter of the position
     *
     * @param x x component of the vector
     * @param y y component of the vector
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getter of the x component of the vector
     *
     * @return x component of the vector
     */
    public double getX() {
        return x;
    }

    /**
     * getter of the y component of the vector
     *
     * @return y component of the vector
     */
    public double getY() {
        return y;
    }

    /**
     * adder of the vector
     *
     * @param dx the amount of increase in the x component of the vector
     * @param dy the amount of increase in the y component of the vector
     */
    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * multiplier of the vector
     *
     * @param m the multiplier
     */
    public void multiply(double m) {
        this.x *= m;
        this.y *= m;
    }

    /**
     * get the magnitude(length) of this vector
     *
     * @return the magnitude(length) of this vector
     */
    public double getMag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * set the magnitude(length) of this vector
     *
     * @param l the magnitude(length) of this vector
     */
    public void setMag(double l) {
        if (this.getMag() == 0) {
            this.set(l, 0);
        } else {
            this.multiply(1 / this.getMag());
            this.multiply(l);
        }
    }

    /**
     * get the angle of the vector
     *
     * @return the angle of the vector
     */
    public double getAngle() {
        return Math.toDegrees(Math.atan2(this.y, this.x));
    }

    /**
     * set the angle of the vector
     *
     * @param angleDegrees the angle of the vector
     */
    public void setAngle(double angleDegrees) {
        double mag = this.getMag();
        this.x = mag * Math.cos(Math.toRadians(angleDegrees));
        this.y = mag * Math.sin(Math.toRadians(angleDegrees));
    }
}
