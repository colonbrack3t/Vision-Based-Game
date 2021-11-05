package sabrewulf.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class VelocityTest {
    GameObject object, wall, floor;

    final Vector zeroZero = new Vector(0, 0);
    Vector verticalSpeed = new Vector(1, 0);
    Vector gravity = new Vector(0, -1);
    Vector motionless = new Vector(0, 0);

    @Before
    public void setup() {
        object = new GameObject(zeroZero, new Vector(1, 1), Color.RED, "");

        wall = new GameObject(new Vector(1, -100), new Vector(1, 1000), Color.BLUE, "");
        floor = new GameObject(new Vector(-100, 0), new Vector(1000, 1), Color.YELLOW, "");
    }

    @Test
    public void position_is_updated_according_to_speed() {
        object.addForce(new Vector(3, 0));

        assertEquals(0, object.position().x());

        object.updatePosition(List.of());
        assertEquals(2, object.position().x());
    }

    @Test
    public void horizontal_collision_stops_horizonal_movement() {
        object.addForce(verticalSpeed);
        var surrounding = List.of(wall);

        assertEquals(verticalSpeed, object.velocity());

        // update twice: once to reach object, twice for collision
        object.updatePosition(surrounding);
        object.updatePosition(surrounding);

        assertEquals(motionless, object.velocity());
    }

    @Test
    public void vertical_collision_stops_vertical_movement() {
        object.setPosition(new Vector(0, 1));
        object.addForce(gravity);

        var surrounding = List.of(floor);

        assertEquals(gravity, object.velocity());
        object.updatePosition(surrounding);
        assertEquals(motionless, object.velocity());
    }

    @Test
    public void collision_on_the_right_only_stops_movement_to_the_right() {
        object.addForce(gravity);
        object.addForce(verticalSpeed);

        var startSpeed = Vector.add(gravity, verticalSpeed);

        var surrounding = List.of(wall);

        assertEquals(startSpeed, object.velocity());
        object.updatePosition(surrounding);
        assertEquals(gravity, object.velocity());
    }

    @Test
    public void collision_on_the_left_only_stops_movement_to_the_left() {
        Vector verticalSpeed = new Vector(-1, 0);
        object.addForce(verticalSpeed);
        object.addForce(gravity);

        var startSpeed = Vector.add(gravity, verticalSpeed);

        wall.setPosition(-2, -100);
        var surrounding = List.of(wall);

        assertEquals(startSpeed, object.velocity());

        object.updatePosition(surrounding);
        assertEquals(gravity, object.velocity());
    }

    @Test
    public void collision_with_lower_object_only_stops_falling() {
        Vector velocity = new Vector(3, 0);
        Vector realVelocity = new Vector(2, 0); // effect of friction

        object.addForce(gravity);
        object.addForce(velocity);

        var surrounding = List.of(floor);

        object.updatePosition(surrounding);
        assertEquals(Vector.add(gravity, realVelocity), object.velocity());
    }

    @Test
    public void collision_with_higher_object_only_stops_upwards_movement() {
        object.addForce(gravity);
        object.addForce(new Vector(3, 0));

        Vector realVerticalSpeed = new Vector(2, 0); //due to friction

        floor.setPosition(-100, 1);
        var surrounding = List.of(floor);

        object.updatePosition(surrounding);
        assertEquals(Vector.add(gravity, realVerticalSpeed), object.velocity());
    }
}
