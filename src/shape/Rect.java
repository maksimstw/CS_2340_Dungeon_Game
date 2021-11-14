package shape;

import java.io.Serializable;

/**
 * a rectangular shape on the map
 * could judge whether a point on the map falls inside the rectanbgle
 */
public class Rect implements Boundary<Rect>, Serializable {
    private final double[] min; //upper left corner of the rectangle
    private final double[] size; //width and height of the rectangle

    /**
     * generate a rectangular boundary by center + size
     * @param centerX x coordinate for center
     * @param centerY y coordinate for center
     * @param width width
     * @param height height
     * @return a new rectangle boundary
     */
    public static Rect getRectByCenterSize(
            double centerX, double centerY, double width, double height
    ) {
        return new Rect(
              centerX - width / 2.0, centerY - height / 2.0, width, height
            );
    }

    /**
     * generate a rectangular boundary by corner (upper left) + size
     * @param upperleftX x coordinate for upper left corner
     * @param upperleftY y coordinate for upper left corner
     * @param width width
     * @param height height
     * @return a new rectangle boundary
     */
    public static Rect getRectByCornerSize(
            double upperleftX, double upperleftY, double width, double height
    ) {
        return new Rect(upperleftX, upperleftY, width, height);
    }

    /**
     * default constructor of a rectangle
     * @param xMin the left most x coordinate
     * @param yMin the upper most y coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public Rect(double xMin, double yMin, double width, double height) {

        if (width < 0) {
            throw new IllegalArgumentException("width cannot be negative: " + width);
        }
        if (height < 0) {
            throw new IllegalArgumentException("height cannot be negative: " + height);
        }

        min = new double[] {xMin, yMin};
        size = new double[] {width, height};
    }

    @Override
    public boolean inBounds(double x, double y) {
        return x >= min[0] && x <= min[0] + size[0] && y >= min[1] && y <= min[1] + size[1];
    }

    @Override
    public boolean intersects(Rect r) {
        double xMin = Math.min(r.min[0], min[0]);
        double xMax = Math.max(r.min[0] + r.size[0], min[0] + size[0]);
        double yMin = Math.min(r.min[1], min[1]);
        double yMax = Math.max(r.min[1] + r.size[1], min[1] + size[1]);

        return ((xMax - xMin) <= r.size[0] + size[0])
                && ((yMax - yMin) <= r.size[1] + size[1]);
    }
}
