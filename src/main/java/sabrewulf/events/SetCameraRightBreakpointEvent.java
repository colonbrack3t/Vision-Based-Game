package sabrewulf.events;

public class SetCameraRightBreakpointEvent implements Event<Integer> {

    /**
     *
     */
    private static final long serialVersionUID = -529401208721990987L;
    private int x;

    public SetCameraRightBreakpointEvent(int x) {
        this.x = x;
    }

    public Integer get() {
        return x;
    }
}