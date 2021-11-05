package sabrewulf.game;

import static java.lang.Math.round;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import sabrewulf.render.RenderInfo;
import sabrewulf.render.Texture;
import sabrewulf.ui.SoundPlayer;

public class GameObject implements Renderable, Serializable {
    private static final long serialVersionUID = 5546813146700035171L;
    SoundPlayer soundPlayer = new SoundPlayer();
    static final Vector minVelocity = new Vector(-50, -10), maxVelocity = new Vector(50, 20);

    protected Color type;
    protected Vector position, dimensions, velocity;
    protected String id;

    protected boolean onGround = false, leftCollision = false, rightCollision = false, upCollision = false;
    protected String textureName;

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
    public GameObject(Vector position, Vector dimensions, Color type, String textureName) {
        this.position = position;
        this.type = type;
        this.textureName = textureName;
        this.dimensions = dimensions;
        this.velocity = new Vector(0, 0);

        this.id = genId();
    }

    /**
     * generates new unique id
     * 
     * @return
     */
    private String genId() {
        UUID uid = UUID.randomUUID();
        return uid.toString();
    }

    /**
     * @param textureName the textureName to set
     */
    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    public RenderInfo renderInfo() {
        return new RenderInfo(this);
    }

    public Vector position() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Vector(x, y);
    }

    public String id() {
        return id;
    }

    public Color type() {
        return this.type;
    }

    /**
     * Applies limits and friction to velocity, then calculates collisions, before
     * updating position with velocity
     *
     * @param surrounding Surrounding GameObjects
     */
    public void updatePosition(List<GameObject> surrounding) {
        // TODO: What is the correct order of updating
        velocity = velocity.limit(minVelocity, maxVelocity);
        applyFriction();
        if (velocity.isZero()) {
            return;
        }
        applyCollisions(surrounding);
        position = Vector.add(position, velocity);
    }

    /**
     * Applies simple deceleration factor, to give objects a more natural
     * deceleration
     */
    protected void applyFriction() {
        float decelerationFactor = 1.4f;
        int x = round(velocity.x() / decelerationFactor);
        velocity = new Vector(x == 1 || x == -1 ? 0 : x, velocity.y());

    }

    /**
     * Calls OnCollision() with every object that would be collided with
     *
     * @param surrounding Surrounding GameObjects
     */
    protected void applyCollisions(List<GameObject> surrounding) {
        onGround = upCollision = leftCollision = rightCollision = false;
        for (GameObject obj : surrounding) {
            if (collidesWith(obj)) {

                List<Vector> currentCorners = getCorners(position);
                for (int i = 0; i < 6; i++) {
                    List<Vector> nextCorners = getCorners(nextPosition());
                    Vector corner = nextCorners.get(i), curCorner = currentCorners.get(i);

                    obj.onCollision(corner, curCorner, this);
                }
            }
        }
    }

    /**
     * Called when another gameobject collided with this
     *
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos  Current Corner of the colliding gameObject
     * @param obj     gameobject that collided with this
     */
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        // Special effects that happen upon collision
        obj.velocity = calculateCollision(nextPos, curPos, obj);
    }

    /**
     * Calculate the velocity (and set position where neccessary) of Object after
     * collision
     *
     * @param nextPos Corner of the colliding gameobject + its velocity
     * @param curPos  Current Corner of the colliding gameObject
     * @param obj     gameobject that collided with this
     * @return Velocity after Collision
     */
    private Vector calculateCollision(Vector nextPos, Vector curPos, GameObject obj) {

        Vector xNewPos = new Vector(nextPos.x(), curPos.y());
        Vector yNewPos = new Vector(curPos.x(), nextPos.y());
        int x = obj.velocity.x(), y = obj.velocity.y();

        if (withinBounds(xNewPos.y(), position.y(), position.y() + height())) {
            if (x > 0) {
                obj.position.setX(position.x() - obj.width());
                nextPos.setX(position.x() - obj.width());
                obj.rightCollision = true;
            }
            if (x < 0) {
                obj.position.setX(position.x() + width());
                nextPos.setX(position.x() + obj.width());
                obj.leftCollision = true;
            }
            x = 0;
        }
        if (withinBounds(yNewPos.x(), position.x(), position.x() + width())) {
            if (y > 0) {
                obj.position.setY(position.y() - obj.height());
                nextPos.setY(position.y() - obj.height());
                obj.upCollision = true;
            }
            if (y < 0) {
                obj.position.setY(position.y() + height());
                nextPos.setY(position.y() + height());
                obj.onGround = true;
            }
            y = 0;
        }
        return new Vector(x, y);
    }

    private boolean withinBounds(float x, float lower, float upper) {
        return x > lower && x < upper;
    }

    /**
     * Generates all 4 corners and 2 extra middle left and middle right points of
     * gameobject if it was in the given position
     *
     * @param pos Position reference
     * @return List of Corners
     */
    private List<Vector> getCorners(Vector pos) {
        return List.of(pos, Vector.add(pos, new Vector(width(), 0)), Vector.add(pos, new Vector(0, height())),
                Vector.add(pos, new Vector(width(), height())), Vector.add(pos, new Vector(width(), height() / 2)),
                Vector.add(pos, new Vector(0, height() / 2)));
    }

    /**
     * Checks if other collided with this
     *
     * @param other Gameobject to check
     * @return true if other will collide with this
     */
    public boolean collidesWith(GameObject other) {
        return !(other instanceof Character) && this.withinBoundaries(other.position)
                && other.withinBoundaries(nextPosition());
    }

    protected boolean withinBoundaries(Vector point) {
        return withinX(point) && withinY(point);
    }

    private boolean withinX(Vector point) {
        return point.x() < nextPosition().x() + width();
    }

    private boolean withinY(Vector point) {
        return point.y() < nextPosition().y() + height();
    }

    public int height() {
        return dimensions.y();
    }

    public int width() {
        return dimensions.x();
    }

    /**
     * add a Vector to velocity
     *
     * @param force
     */
    public void addForce(Vector force) {
        velocity = Vector.add(velocity, force);
    }

    public Vector velocity() {
        return velocity;
    }

    public Vector nextPosition() {
        return Vector.add(position, velocity);
    }

    @Override
    public String toString() {
        return super.toString() + " Type : " + type + " Position : " + position + " Dimensions : " + dimensions;
    }

    public Vector getDimensions() {
        return dimensions;
    }

    public Texture getTexture() {
        return Texture.create(textureName);
    }
}
