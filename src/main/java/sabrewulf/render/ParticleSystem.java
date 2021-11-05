package sabrewulf.render;

import sabrewulf.game.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is responsible for managing large groups of {@link Particle}. It
 * has a bunch of fields that specify the behavior of individual particles.
 * Everything (position, velocity, particle count) can be customized.
 * <p>
 * The ParticleSystem renders the particles as a texture, allowing for the
 * falling leaf effect visible in the game.
 * <p>
 * The particle system has a cycle: it is on for a specified period of time, and
 * off for the same period of time.
 * <p>
 * While it is on, it has a 10% chance of spawning a new particle (up until the
 * maximum specified amount of particles is spawned). The particles are spawned
 * at a random location inside the configurable spawn bounds with a random
 * velocity. Afterwards, the existing particles are updated, and if any of the
 * particles moved outside of the configurable alive bounds, their position is
 * reset to the spawning area.
 */

public class ParticleSystem {

    private Vector position;

    private List<Particle> particles;
    private int particleCount;

    private List<Texture> particleTextures;

    private Vector spawnPosition;
    private Vector spawnBounds;

    private Vector alivePosition;
    private Vector aliveBounds;

    private Vector minVelocity;
    private Vector maxVelocity;

    private Vector particleDirection;

    private long startTime;
    private boolean enabled;
    private static final long CYCLE_DURATION = 40 * 1000 * 1000 * 1000L; // 40 seconds

    /**
     * Creates and initialises the ambient weather (falling leaves) effect.
     * @return the ParticleSystem responsible for ambient weather effect
     */
    public static ParticleSystem createWeather() {
        ParticleSystem system = new ParticleSystem(new Vector(0, 0));
        system.initialiseWeather();
        return system;
    }

    // createWeather should be called instead
    private ParticleSystem(Vector position) {
        particles = new ArrayList<>();
        this.position = position;
    }

    /**
     * Convenience method to update the entire ParticleSystem. Spawns
     * particles, updates particles, and renders particles.
     */
    public void update() {
        if (elapsedTime() > CYCLE_DURATION) {
            enabled = !enabled;
            startTimer();
        }

        spawnParticles();
        updateParticles();
        renderParticles();
    }

    /**
     * Spawn particles, only if the amount of particles does not exceed the
     * limit. Additionally, particles have only a 1 in 10 chance of spawning.
     * If the particle system is disabled, no particles are spawned.
     */
    private void spawnParticles() {
        if (enabled && particles.size() < particleCount && ParticleUtils.random(1, 10) == 1) {
            Vector position = ParticleUtils.randomPosition(spawnPosition, spawnBounds);

            Vector randomVelocity = ParticleUtils.randomVelocity(minVelocity, maxVelocity);
            Vector directedVelocity = Vector.mult(randomVelocity, particleDirection);

            int textureIndex = ParticleUtils.random(0, particleTextures.size()-1);
            Texture particleTexture = particleTextures.get(textureIndex);

            Particle particle = new Particle(position, directedVelocity, particleTexture);

            particles.add(particle);
        }
    }

    /**
     * Update all the {@link Particle} in this system. If the particle is
     * outside of the bounds, reset its position back to the spawning area.
     * If the particle system is disabled, remove the particle.
     */
    private void updateParticles() {
        Iterator<Particle> iterator = particles.iterator();
        while(iterator.hasNext()){
            Particle particle = iterator.next();

            particle.update();

            if (!ParticleUtils.withinBounds(particle.getPosition(), alivePosition, aliveBounds)) {
                if (enabled) {
                    particle.setPosition(ParticleUtils.randomPosition(spawnPosition, spawnBounds));
                } else {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Render all the particles on the screen. The particles are rendered
     * based on their relative offset from the {@link #position}.
     */
    private void renderParticles() {
        Projection.temporary();

        for (Particle particle : particles) {
            Vector globalPosition = Vector.add(position, particle.getPosition());
            // Vector particleSize = new Vector(8, 8);

            Sprite.draw(globalPosition, particle.getTexture());
        }

        Projection.dispose();
    }

    /** Starts the timer thats responsible for the on off cycle */
    private void startTimer() {
        startTime = System.nanoTime();
    }

    /** Gets the time elapsed since last {@link #startTimer()} call */
    private long elapsedTime() {
        return System.nanoTime() - startTime;
    }

    /**
     * Customize the particle system to display the falling leaves
     * (ambient weather) effect. Sets the internal fields.
     */
    private void initialiseWeather() {
        final int RAIN_PARTICLE_COUNT = 22;
        final int ALIVE_BORDER = 32;

        final Vector RAIN_MIN_VELOCITY = new Vector(1, 1);
        final Vector RAIN_MAX_VELOCITY = new Vector(3, 3);
        final Vector RAIN_DIRECTION = new Vector(-1, -1);

        int windowWidth = GLWrapper.WINDOW_WIDTH;
        int windowHeight = GLWrapper.WINDOW_HEIGHT;

        particleCount = RAIN_PARTICLE_COUNT;
        spawnPosition = new Vector(0, windowHeight);
        spawnBounds = new Vector(windowWidth, 0);
        alivePosition = new Vector(-ALIVE_BORDER, -ALIVE_BORDER);
        aliveBounds = new Vector(windowWidth + (ALIVE_BORDER*2), windowHeight + (ALIVE_BORDER*2));
        minVelocity = RAIN_MIN_VELOCITY;
        maxVelocity = RAIN_MAX_VELOCITY;
        particleDirection = RAIN_DIRECTION;

        particleTextures = new ArrayList<>();
        String[] textureNames = new String[] {
                "01.png", "02.png", "03.png", "04.png", "05.png", "06.png",
                "07.png", "08.png", "09.png", "10.png", "11.png" };
        for (String textureName : textureNames) {
            particleTextures.add(new Texture("./assets/Leaves/sprite_" + textureName));
        }

        enabled = true;
        startTimer();
    }

    /**
     * Set the position of the particle system. All {@link Particle}
     * positions are relative offsets from this position.
     * @param position the position of this ParticleSystem
     */
    public void setPosition(Vector position) {
        this.position = position;
    }

}
