package brain.factions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Annot {
    Side[] value();

    enum Side {
        CLIENT, SERVER
    }
}
