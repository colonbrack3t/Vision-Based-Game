package sabrewulf.game;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GravityTest {

    @Before
    public void setup_test() {}

    @Test
    public void character_falls_with_predetermined_speed() {
        Character character = new Character(new Vector(1, 1), Color.BLUE);

        character.updatePosition(List.of());

        assertEquals(new Vector(1, 0), character.position());
    }
}
