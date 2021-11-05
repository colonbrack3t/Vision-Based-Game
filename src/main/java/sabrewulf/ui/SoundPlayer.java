package sabrewulf.ui;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.openal.AL10.*;

public class SoundPlayer implements Serializable {
    private static final long serialVersionUID = -7659826204813551430L;


    public static Sound jumpSound;
    public static Sound collideSound;
    public static Sound painSound;
    public static Sound landSound;
    public static Sound runSound;
    public static Sound sawSound;
    public static Sound sawStartSound;
    public static Sound sawEndSound;

    public static Sound clickSound;
    public static Sound checkpointSound;
    public static Sound finishedSound;
    public static Sound spikeSound;
    public static boolean init = false;

    public SoundPlayer() {
        if (!init) {
            init = true;
            initSounds();
        }
    }

    /**
     * This method initialises the necessary OpenAL features to allow sounds to be
     * played then loads each sound file ready to be played
     */
    public void initSounds() {
        Sound.init(); // initialise context, device and capabilities for all sounds

        clickSound = new Sound("assets/sounds/Click.ogg");
        jumpSound = new Sound("assets/sounds/jump.ogg");
        collideSound = new Sound("assets/sounds/collide.ogg");
        painSound = new Sound("assets/sounds/pain.ogg");
        landSound = new Sound("assets/sounds/land.ogg");
        runSound = new Sound("assets/sounds/running.ogg");
        sawSound = new Sound("assets/sounds/sawing.ogg");
        sawStartSound = new Sound("assets/sounds/saw-start.ogg");
        sawEndSound = new Sound("assets/sounds/saw-end.ogg");
        checkpointSound = new Sound("assets/sounds/checkpoint.ogg");
        finishedSound = new Sound("assets/sounds/congrats.ogg");
        spikeSound = new Sound("assets/sounds/spike.ogg");
    }

    /**
     * Calls the method in the sound class which plays the click sound and delays
     * before the next scene appears
     */
    public void playClickSound() {
        clickSound.playClickSound();
    }

    public void playJumpSound() {
        jumpSound.playSound();
    }

    public void playCollideSound() {
        collideSound.playSound();
    }

    public void playPainSound() {
        painSound.playSound();
    }

    public void playSawSound() {
        if (!(sawSound.isPlaying() || sawStartSound.isPlaying())) {
            playSawStartSound();
            sawSound.playLongSound();
        }
    }

    /**
     * 
     * @param end This is true if the game loop is being ended and 
     * therefore long sounds need to be completely stopped
     */
    public void stopSawSound(boolean end) {
        if (sawSound.isPlaying()) {
            if (end) {
                sawSound.stopLongSound();
            } else {
                playSawEndSound();
                sawSound.playLongSound();
            }
        }
    }

    public void playLandSound() {
        landSound.playSound();
    }

    public void playRunSound() {

        if (!runSound.isPlaying())
            runSound.playLongSound();

    }

    /**
     * 
     * @param end This is true if the game loop is being ended and 
     * therefore long sounds need to be completely stopped
     */
    public void stopRunSound(boolean end) {
        if (runSound.isPlaying()) {
            if (end) {
                runSound.stopLongSound();
            } else {
                runSound.playLongSound();
            }
        }
    }

    public void playSawStartSound() {
        sawStartSound.playSound();
    }

    public void playSawEndSound() {
        sawEndSound.playSound();
    }


    public void playCheckpointSound(){
        checkpointSound.playSound();
    }
    
    public void playSpikeSound(){
        spikeSound.playSound();
    }
    
    public void playFinishedSound(){
        finishedSound.playSound();
    }
    
    /**
     * This method calls the methods in the sound class that ensure the sounds are
     * ended properly
     */
    public void endSounds() {
        stopSawSound(false);
        stopRunSound(false);
        clickSound.endSound();
        jumpSound.endSound();
        collideSound.endSound();
        painSound.endSound();
        landSound.endSound();
        runSound.endSound();
        sawSound.endSound();
        sawStartSound.endSound();
        sawEndSound.endSound();
        spikeSound.endSound();
        checkpointSound.endSound();
        finishedSound.endSound();
        Sound.end();
    }
}
