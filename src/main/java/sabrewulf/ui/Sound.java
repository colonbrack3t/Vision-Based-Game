package sabrewulf.ui;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

import java.nio.*;
import org.lwjgl.openal.*;
import org.lwjgl.system.*;

/**
 * Class for initialising, controlling and deleting sounds. Requires getters and
 * setters for the different variables so it can be used for multiple sounds
 */
public class Sound {

    int channels;
    int format;
    ShortBuffer rawAudioBuffer;
    int sampleRate;
    int bufferPointer;
    int sourcePointer;
    static long context;
    static long device;
    public static boolean sfx = true;
    public static boolean music = true;

    public Sound(String fileName) {
        initSound(fileName);
    }

    public static void init() {
        // Initialisation
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        setDevice(defaultDeviceName);

        int[] attributes = { 0 };
        setContext(attributes);
        alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }

    /**
     * used to initialise an individual sound
     *
     * @param fileName The sound file to be loaded
     */
    public void initSound(String fileName) {
        try (MemoryStack stack = stackPush()) {
            // Allocate space to store return information from the function
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);

            setRABuffer(fileName, channelsBuffer, sampleRateBuffer);

            // Retreive the extra information that was stored in the buffers by the function
            setChannels(channelsBuffer);
            setSampleRate(sampleRateBuffer);
        }
        findFormat();
        bufferAndMemory();
    }

    public void findFormat() {
        // Find the correct OpenAL format
        setFormat(-1);
        if (getChannels() == 1) {
            setFormat(AL_FORMAT_MONO16);
        } else if (getChannels() == 2) {
            setFormat(AL_FORMAT_STEREO16);
        }
    }

    public void bufferAndMemory() {
        setBufferPointer();

        // Send the data to OpenAL
        alBufferData(getBufferPointer(), getFormat(), getRABuffer(), getSampleRate());

        // Free the memory allocated by STB
        free(getRABuffer());

        setSourcePointer();

        // Assign the loaded sound to the source
        alSourcei(getSourcePointer(), AL_BUFFER, getBufferPointer());
    }

    public void playSong() {
        if (alGetSourcei(getSourcePointer(), AL_SOURCE_STATE) == AL10.AL_PLAYING) { // if the sound is already playing
                                                                                    // then pause it otherwise
                                                                                    // play it
            alSourcePause(getSourcePointer());
        } else {
            // Play the sound
            alSourcePlay(getSourcePointer());
        }

        // _wait();
    }

    /**
     * This method plays the click sound which occurs when a button is pressed. It
     * then calls the wait method to ensure there is a delay before calling the next
     * scene otherwise buttons on the next scene could accidentally be clicked
     */
    public void playClickSound() {
        if (sfx) {
            alSourcePlay(getSourcePointer());
        }

        _wait();
    }

    /** This method plays short sound effects */
    public void playSound() {
        if (sfx) {
            alSourcePlay(getSourcePointer());
        }
    }

    /**
     * This method can be called to either start or stop a continuous sound effect
     */
    public void playLongSound() {
        if (sfx) {
            if (alGetSourcei(getSourcePointer(), AL_SOURCE_STATE) == AL10.AL_PLAYING) { // if the sound is already
                                                                                        // playing then pause it
                                                                                        // otherwise
                                                                                        // play it
                alSourceStop(getSourcePointer());
            } else {
                // Play the sound
                alSourcePlay(getSourcePointer());
            }
        }
    }
    
    public void stopLongSound() {
        System.out.println(alGetSourcei(getSourcePointer(), AL_SOURCE_STATE) == AL10.AL_PLAYING);
        alSourcePause(getSourcePointer());
        alSourceStop(getSourcePointer());
        System.out.println(alGetSourcei(getSourcePointer(), AL_SOURCE_STATE) == AL10.AL_PLAYING);
    }

    public static void _wait() {
        try {
            // Wait
            Thread.sleep(220);
        } catch (InterruptedException ignored) {

        }
    }

    /** increases the volume by 10% */
    public static void volumeUp() {
        float currentVol = alGetListenerf(AL_GAIN);
        if (currentVol < 0.9) {
            alListenerf(AL_GAIN, currentVol + 0.1f);
        } else {
            alListenerf(AL_GAIN, 1.0f);
        }
    }

    /** decreases the volume by 10% */
    public static void volumeDown() {
        float currentVol = alGetListenerf(AL_GAIN);
        if (currentVol > 0.1) {
            alListenerf(AL_GAIN, currentVol - 0.1f);
        } else {
            alListenerf(AL_GAIN, 0.0f);
        }
    }

    /**
     * @return current volume so can know whether it can be increased or decreased
     *         further
     */
    public static float getVolume() {
        return alGetListenerf(AL_GAIN);
    }

    /**
     * used to find current position in the sound
     *
     * @return how many seconds of the sound have been played so far
     */
    public float getOffset() {
        // FloatBuffer offset = FloatBuffer.allocate(1);
        float offset = AL11.alGetSourcef(getSourcePointer(), AL_SEC_OFFSET);
        return offset;
    }

    /**
     * sets the time the sound should be played from
     *
     * @param offset number of seconds to start playing the sound from
     */
    public void setOffset(float offset) {
        // offset.rewind();
        // float off = offset.get();
        AL11.alSourcef(getSourcePointer(), AL_SEC_OFFSET, offset);
        // AL11.alSour
    }

    /** @return true if the sound is playing false otherwise */
    public boolean isPlaying() {
        return alGetSourcei(getSourcePointer(), AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    private static void setDevice(String defaultDeviceName) {
        device = alcOpenDevice(defaultDeviceName);
    }

    private void setFormat(int fmat) {
        this.format = fmat;
    }

    public int getFormat() {
        return this.format;
    }

    private static void setContext(int[] attributes) {
        context = alcCreateContext(device, attributes);
    }

    private void setChannels(IntBuffer channelsBuffer) {
        this.channels = channelsBuffer.get(0);
    }

    public int getChannels() {
        return this.channels;
    }

    private void setRABuffer(String fileName, IntBuffer channelsBuffer, IntBuffer sampleRateBuffer) {
        this.rawAudioBuffer = stb_vorbis_decode_filename(fileName, channelsBuffer, sampleRateBuffer);
    }

    public ShortBuffer getRABuffer() {
        return this.rawAudioBuffer;
    }

    private void setSampleRate(IntBuffer sampleRateBuffer) {
        this.sampleRate = sampleRateBuffer.get(0);
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    private void setBufferPointer() {
        this.bufferPointer = alGenBuffers();
    }

    public int getBufferPointer() {
        return this.bufferPointer;
    }

    private void setSourcePointer() {
        this.sourcePointer = alGenSources();
    }

    public int getSourcePointer() {
        return this.sourcePointer;
    }

    public static void end() {
        // Terminate OpenAL for all sounds
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    public void endSound() {
        // Terminate OpenAL for individual sound
        alDeleteSources(getSourcePointer());
        alDeleteBuffers(getBufferPointer());
    }
}
