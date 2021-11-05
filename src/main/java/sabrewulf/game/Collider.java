package sabrewulf.game;

import sabrewulf.ui.SoundPlayer;

public class Collider extends GameObject {
    /**
     *
     */
    private static final long serialVersionUID = -8593401578031789277L;
    AIGameObject aiGameObject;
SoundPlayer soundPlayer = new SoundPlayer();
    public Collider(Vector position, Vector dimension, AIGameObject aiGameObject) {
        super(position, dimension, Color.INVISIBLE, "");
        this.aiGameObject = aiGameObject;
    }

    public static Collider generate(Saw k) {
        return new Collider(new Vector(k.min, k.position().y()), new Vector((k.max - k.min), 70), k);
    }

    public static Collider generate(MovingSpike k) {
        return new Collider(Vector.add(new Vector(0, 70), k.position), new Vector(70, 70), k);
    }

    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {

        aiGameObject.addTarget(obj);
    }
}
