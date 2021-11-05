package sabrewulf.game;

import java.util.HashSet;
import java.util.Set;

public class AIGameObject extends GameObject {
    protected float damage = 6 / 60f;
    private static final long serialVersionUID = -6128260661830533728L;
    /**
     * Constructor. GameObject is the base class for all interactable objects in the
     * game. This class provides basic collision physics as well as primitve
     * attributes such as position dimensions texture and game id
     *
     * @param position    Position Vector
     * @param dimensions  Hitbox
     * @param type        Color
     * @param textureName Texture Name
     */
    Set<GameObject> targets = new HashSet<GameObject>();

    public AIGameObject(Vector position, Vector dimensions, Color type, String textureName) {
        super(position, dimensions, type, textureName);
    }

    public void addTarget(GameObject obj) {
        targets.add(obj);
    }

    /**
     * deal damage to obj if theyre a player
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos  Current Corner of the colliding gameObject
     * @param obj     gameobject that collided with this
     */
    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {

        if (obj instanceof Character) {
            Character c = (Character) obj;
            c.changeVision(-damage);
            c.checkpointCheck();
        }
    }
}
