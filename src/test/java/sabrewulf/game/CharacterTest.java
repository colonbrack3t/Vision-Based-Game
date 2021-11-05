package sabrewulf.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CharacterTest {

    GameObject RED_OBJECT, BLUE_OBJECT;
    Character blueCharacter, redCharacter;

    @Before
    public void setup_test() {
        blueCharacter = new Character(new Vector(10, 10), Color.BLUE);
        redCharacter = new Character(new Vector(10, 20), Color.RED);

        Vector defaultDimensions = new Vector(5, 5);
        BLUE_OBJECT = new GameObject(new Vector(20, 10), defaultDimensions, Color.BLUE, "");
        RED_OBJECT = new GameObject(new Vector(20, 20), defaultDimensions, Color.RED, "");
    }

    @Test
    public void has_a_position() {
        assertNotNull(blueCharacter.position());
    }

    // visibility is now in level
    // @Test
    // public void can_see_what_he_is_allowed_to() {
    //    assertTrue(blueCharacter.canSee(BLUE_OBJECT.type()));
    //    assertFalse(blueCharacter.canSee(RED_OBJECT.type()));
    // }

    // @Test
    // public void can_see_other_characters() {
    //    assertTrue(blueCharacter.canSee(redCharacter.type()));
    //    assertTrue(redCharacter.canSee(blueCharacter.type()));
    // }

    @Override
    public String toString() {
        return "CharacterTest []";
    }
}
