package sabrewulf.events;

import java.io.Serializable;

public interface Event<T> extends Serializable {

    /** Every event transports some information This function should return this information */
    public T get();
}
