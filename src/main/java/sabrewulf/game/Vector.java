package sabrewulf.game;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.Serializable;
import java.util.List;

public class Vector implements Serializable {
    private static final long serialVersionUID = 7737683894113393770L;

    private int x, y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public static Vector subtract(Vector one, Vector two) {
        return new Vector(one.x() - two.x(), one.y() - two.y());
    }

    public static double size(Vector vector) {
        return Math.sqrt((vector.x() * vector.x()) + (vector.y() * vector.y()));
    }

    public static Vector divide(Vector one, double two) {
        return new Vector((int) (one.x() / two), (int) (one.y() / two));
    }

    public static int scalarProduct(Vector one, Vector two) {
        return (one.x() * two.x()) + (one.y() * two.y());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vector && ((Vector) obj).x == x && ((Vector) obj).y == y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static Vector add(Vector one, Vector two) {
        return new Vector(one.x() + two.x(), one.y() + two.y());
    }

    public Vector add(int x, int y) {
        return new Vector(this.x + x, this.y + y);
    }

    public static Vector mult(Vector one, Vector two) {
        return new Vector(one.x() * two.x(), one.y() * two.y());
    }

    public static Vector mult(Vector one, float two) {
        return new Vector((int) (one.x() * two), (int) (one.y() * two));
    }

    public static Vector addAll(List<Vector> vectors) {
        Vector total = new Vector(0, 0);
        for (Vector v : vectors) {
            total = Vector.add(total, v);
        }
        return total;
    }

    public Vector limit(Vector lowerBound, Vector upperBound) {
        return new Vector(
                inRange(x, lowerBound.x(), upperBound.x()),
                inRange(y, lowerBound.y(), upperBound.y()));
    }

    private static int inRange(int n, int max, int min) {
        n = max(n, max);
        n = min(n, min);
        return n;
    }

    public static boolean withinSquare(Vector point, Vector position, Vector dimensions) {
        return point.x() > position.x()
                && point.x() < position.x() + dimensions.x()
                && point.y() > position.y()
                && point.y() < position.y() + dimensions.y();
    }
}
