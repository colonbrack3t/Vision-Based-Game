package sabrewulf.game;

import java.util.ArrayList;
import java.util.List;

public class SpikePlatform extends Platform {
    private static final long serialVersionUID = 8901983965122409260L;
    List<MovingSpike> spikes = new ArrayList<>();

    public SpikePlatform(Vector position, Vector dimensions, List<MovingSpike> spikes) {
        super(position, dimensions);
        this.spikes = spikes;
        for (MovingSpike s : this.spikes) {
            s.setMaxY(position.y() + height());
        }
    }

    public void add(MovingSpike spike) {
        spike.setMaxY(position.y() + height());
        spikes.add(spike);
    }

    public static SpikePlatform generate(int x, int level, int width) {
        return generate(x, level, width, new ArrayList<>());
    }

    public static SpikePlatform generate(int x, int level, int width, List<MovingSpike> movingSpikes) {
        int PLATFORM_WIDTH = 70, PLATFORM_HEIGHT = 70;
        int[] levelCoords = new int[] { 0, 180, 360, 540 };
        return new SpikePlatform(new Vector(x * PLATFORM_WIDTH, levelCoords[level]),
                new Vector(width * PLATFORM_WIDTH, PLATFORM_HEIGHT), movingSpikes);
    }

    @Override
    protected void onCollision(Vector nextPos, Vector curPos, GameObject obj) {
        super.onCollision(nextPos, curPos, obj);

        if (obj instanceof Character)
            if (nextPos.y() == position.y() + height())
                for (MovingSpike s : spikes) {

                }
    }
}
