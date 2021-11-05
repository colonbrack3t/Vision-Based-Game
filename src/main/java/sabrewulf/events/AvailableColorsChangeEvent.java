package sabrewulf.events;

import java.util.Set;
import sabrewulf.game.Color;

public class AvailableColorsChangeEvent implements Event<Set<Color>> {

    /** */
    private static final long serialVersionUID = -2782118542368775883L;

    private Set<Color> colors;

    public AvailableColorsChangeEvent(Set<Color> colors) {
        this.colors = colors;
    }

    @Override
    public Set<Color> get() {
        // TODO Auto-generated method stub
        return colors;
    }

    public Set<Color> getColors() {
        return get();
    }
}
