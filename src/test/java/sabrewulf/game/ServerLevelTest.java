package sabrewulf.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ServerLevelTest {

    ServerLevel level;

    TestGameObject object1, object2;
    List<GameObject> allObjects;

    @Before
    public void setup() {
        level = new ServerLevel();
        Vector position = new Vector(0, 0);

        object1 = new TestGameObject(position, Color.NEUTRAL);
        object2 = new TestGameObject(position, Color.NEUTRAL);

        allObjects = List.of(object1, object2);

        level.add(object1);
        level.add(object2);
    }

    @Test
    public void can_add_objects_to_it() {
        level = new ServerLevel();

        assertEquals("Level should be created empty", 0, level.generateGameUpdates().size());

        level.add(object1);
        level.add(object2);

        assertEquals(2, level.generateGameUpdates().size());
    }

    @Test
    public void updates_all_game_objects() {
        assertEquals(0, object1.updateCounter);
        assertEquals(0, object2.updateCounter);

        int iterations = (int) (20 * Math.random());

        for (int i = 0; i < iterations; i++) {
            level.update();
        }

        assertEquals(iterations, object1.updateCounter);
        assertEquals(iterations, object2.updateCounter);
    }

    @Test
    // move correctly it can handle an input by a client which causes the character to accelerate
    public void can_move_character() {
        Color color = Color.BLUE;

        Character character = new Character(color);
        Vector startVelocity = character.velocity();

        level.add(character);


        Vector moveRight = new Vector(2, 0);
        level.updateCharacter(color, moveRight, character.position());
        level.update();

        assertNotEquals(character.velocity(), startVelocity);
    }
}


class TestGameObject extends GameObject {

    int updateCounter;

    public TestGameObject(Vector vector, Color type) {
        super(vector, new Vector(5, 5), type, "");
        updateCounter = 0;
    }

    @Override
    public void updatePosition(List<GameObject> surrounding) {
        updateCounter++;
    }
}
