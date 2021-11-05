package sabrewulf.game;

import sabrewulf.events.Event;

public class SoundEvent implements Event<SoundType> {
    /**
     *
     */
    private static final long serialVersionUID = -1371132247358747090L;
    private SoundType soundType;

    public SoundEvent(SoundType soundType) {
        this.soundType = soundType;
    }

    @Override
    public SoundType get() {
        return soundType;
    }
}
