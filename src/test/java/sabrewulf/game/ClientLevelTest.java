package sabrewulf.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import sabrewulf.events.EventBus;
import sabrewulf.events.GameUpdateEvent;
import sabrewulf.events.IncomingEvent;
import sabrewulf.events.SetClientColorEvent;
import sabrewulf.events.UpdateLevelObjectsEvent;
import sabrewulf.render.RenderInfo;

public class ClientLevelTest {

    private EventBus eventBus;
    private ClientLevel clientLevel;

    private GameObject object1, object2;
    private List<GameObject> allObjects;

    @Before
    public void setup() {
        eventBus = new EventBus();
        clientLevel = new ClientLevel(eventBus);

        Vector position = new Vector(0, 0), dimension = new Vector(10, 10);
        String texture = "";

        object1 = new GameObject(position, dimension, Color.NEUTRAL, texture);
        object2 = new GameObject(position, dimension, Color.NEUTRAL, texture);

        allObjects = List.of(object1, object2);

        eventBus.trigger(addObjects(allObjects));
    }

    @Test
    public void reacts_to_position_changes() {
        Vector newPosition = new Vector(1, 1);

        assertNotEquals(newPosition, object1.position());

        // change position of object1
        eventBus.trigger(moveTo(object1.id(), newPosition));

        assertEquals(newPosition, object1.position());
    }

    @Test
    public void can_set_player_color() {
        assertNotEquals(ClientLevel.getPlayerColor(), Color.BLUE);

        eventBus.trigger(new SetClientColorEvent(Color.BLUE));

        assertEquals(ClientLevel.getPlayerColor(), Color.BLUE);
    }

    @Test
    @Ignore
    public void ignores_position_events_of_unknown_objects() {
        String unkownID = "unkownID";

        eventBus.trigger(moveTo(unkownID, new Vector(-1, -1)));

        var renderInfo = clientLevel.getRenderList();

        assertEquals(object1.position(), renderInfo.get(0).getPosition());
        assertEquals(object2.position(), renderInfo.get(1).getPosition());
    }

    @Test
    @Ignore
    public void can_add_objects_to_it() {
        clientLevel = new ClientLevel(eventBus);
        assertEquals("Level should be created empty", 0, clientLevel.getRenderList().size());

        eventBus.trigger(addObjects(allObjects));

        assertEquals(2, clientLevel.getRenderList().size());
    }

    @Test
    @Ignore
    public void gives_right_render_information() {
        var renderInfo = clientLevel.getRenderList();

        // TODO what to assert else here

        assertCorrectPositions(allObjects, renderInfo);
    }


    private IncomingEvent addObjects(List<GameObject> objects) {
        String sourceIP = "";
        return new IncomingEvent(new UpdateLevelObjectsEvent(objects), sourceIP);
    }

    private IncomingEvent moveTo(String objID, Vector newPosition) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(objID, newPosition);
        return new IncomingEvent(new GameUpdateEvent(updates), "");
    }

    private void assertCorrectPositions(List<GameObject> objects, List<RenderInfo> renderInfo) {
        // renderInfo may not contain every gameObject (since some are invisible)
        for (int i = 0; i < renderInfo.size(); i++) {
            assertEquals(objects.get(i).position(), renderInfo.get(i).getPosition());
        }
    }
}
