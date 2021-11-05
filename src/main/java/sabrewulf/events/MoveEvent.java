package sabrewulf.events;

import sabrewulf.game.Direction;

public class MoveEvent implements Event<Direction> {
    private static final long serialVersionUID = -756980055340818781L;
    private Direction direction;

    public MoveEvent(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction get() {
        return direction;
    }
}
