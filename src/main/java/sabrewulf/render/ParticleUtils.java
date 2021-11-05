package sabrewulf.render;

import sabrewulf.game.Vector;

import java.util.Random;

/**
 * This class fully static. It contains utility methods mainly used in
 * {@link ParticleSystem}. Operations include: calculating distances between
 * points, checking if a point is within a rectangle, generating random
 * positions, and generating random velocities.
 */

public class ParticleUtils {

    // All methods are static, so constructor should not be called
    private ParticleUtils() { }

    /**
     * Calculate the squared distance between two points
     * @param origin point one
     * @param destination point two
     * @return the squared distance between points
     */
    public static int squaredDistance(Vector origin, Vector destination) {
        int deltaX = origin.x() - destination.x();
        int deltaY = origin.y() - destination.y();

        return (deltaX * deltaX) + (deltaY * deltaY);
    }

    /**
     * Check if a point is within a rectangle
     * @param point the coordinates of the point
     * @param origin the origin of the rectangle
     * @param bounds the width and height of the rectangle
     * @return true if point is inside the rectangle
     */
    public static boolean withinBounds(Vector point, Vector origin, Vector bounds) {
        int x = point.x();
        int y = point.y();

        return (
                x > origin.x() && x < origin.x() + bounds.x() &&
                y > origin.y() && y < origin.y() + bounds.y()
        );
    }

    /**
     * Generate a random int between min and max inclusive.
     * @param min the lower bound
     * @param max the upped bound
     * @return random int between min and max inclusive
     */
    public static int random(int min, int max) {
        Random random = new Random();

        int range = max - min;

        return min + random.nextInt(range + 1);
    }

    /**
     * Generate a random position inside a rectangle
     * @param origin the origin of the rectangle
     * @param bounds the width and height of the rectangle
     * @return random point inside a rectangle
     */
    public static Vector randomPosition(Vector origin, Vector bounds) {
        int x = random(origin.x(), origin.x() + bounds.x());
        int y = random(origin.y(), origin.y() + bounds.y());

        return new Vector(x, y);
    }

    /**
     * Generate a random velocity
     * @param minVelocity the lower bound
     * @param maxVelocity the upper bound
     * @return a random velocity between the bounds
     */
    public static Vector randomVelocity(Vector minVelocity, Vector maxVelocity) {
        int x = random(minVelocity.x(), maxVelocity.x());
        int y = random(minVelocity.y(), maxVelocity.y());

        return new Vector(x, y);
    }

}
