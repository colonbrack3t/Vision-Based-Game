package sabrewulf.game;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static sabrewulf.game.Color.BLUE;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CollisionTest {

    private final Vector defaultPosition = new Vector(0, 0);
    private final Vector objectDimensions = new Vector(2, 2);
    private GameObject objectOne, objectTwo;

    @Before
    public void setup_test() {
        objectOne = new GameObject(defaultPosition, objectDimensions, BLUE, "");

        objectTwo = new GameObject(defaultPosition, objectDimensions, BLUE, "");
    }

    @Test
    @Ignore
    public void objects_dont_collide_with_themselfs() {
        assertMiss(objectOne, objectOne);
    }

    @Test
    public void collision_is_detected_when_on_same_position() {
        objectOne.setPosition(defaultPosition);
        objectTwo.setPosition(defaultPosition);

        assertCollision(objectOne, objectTwo);
    }

    @Test
    public void no_collision_is_detected_when_objects_are_away_from_each_other() {
        objectOne.setPosition(defaultPosition);
        objectTwo.setPosition(100, 100);

        assertMiss(objectOne, objectTwo);
    }

    @Test
    public void objects_can_miss_each_other_vertically() {
        int X_POSITION = 0;

        objectOne.setPosition(X_POSITION, 0);
        objectTwo.setPosition(X_POSITION, 3);

        assertMiss(objectOne, objectTwo);
    }

    @Test
    public void objects_can_miss_each_other_horizontally() {
        int Y_POSITION = 0;
        objectOne.setPosition(0, Y_POSITION);
        objectTwo.setPosition(3, Y_POSITION);

        assertMiss(objectOne, objectTwo);
    }

    @Test
    public void collision_is_detected_when_upper_right_lower_left_corner_intersect() {
        objectOne.setPosition(defaultPosition);
        objectTwo.setPosition(1, 1);
        assertCollision(objectOne, objectTwo);
    }

    @Test
    public void collision_is_detected_when_upper_right_lower_right_corner_intersect() {
        objectOne.setPosition(1, 0);
        objectTwo.setPosition(0, 1);

        assertCollision(objectOne, objectTwo);
    }

    @Test
    @Ignore
    public void collision_is_detected_when_only_the_borders_in_x_direction_intersect() {
        // the objects just intersect on the border
        objectOne.setPosition(0, 0);
        objectTwo.setPosition(2, 0);

        assertCollision(objectOne, objectTwo);
    }

    @Test
    @Ignore
    public void collision_is_detected_when_only_the_borders_in_y_direction_intersect() {
        // the objects just intersect on the border
        objectOne.setPosition(0, 0);
        objectTwo.setPosition(0, 2);

        assertCollision(objectOne, objectTwo);
    }

    private void assertCollision(GameObject one, GameObject two) {
        assertTrue(isSymmetricCollision(one, two));
    }

    private void assertMiss(GameObject one, GameObject two) {
        assertFalse(isSymmetricCollision(one, two));
    }

    /** a collision should be symmmetric a collidesWith b => b collidesWith b */
    private boolean isSymmetricCollision(GameObject one, GameObject two) {
        boolean resultOne = one.collidesWith(two);
        boolean resultTwo = two.collidesWith(one);

        if (resultOne != resultTwo) {
            throw new AssertionError(
                    "Collision logic of " + one + ", " + two + " is not symmetric");
        }

        return resultOne && resultTwo;
    }
}
