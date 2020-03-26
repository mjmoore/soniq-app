package io.mjmoore.model;

import lombok.Builder;
import lombok.Data;

public interface Cleaner {

    static Cleaner of(final Type t, final int capacity) {
        return CleanerImpl.of(t, capacity);
    }

    int getCapacity();

    enum Type {
        Senior("senior"),
        Junior("junior");

        final String name;

        Type(final String name) {
            this.name = name;
        }
    }

    @Data(staticConstructor = "of")
    class CleanerImpl implements Cleaner {
        private final Type type;
        private final int capacity;
    }

    @Builder
    class Assignment {
        private final int seniors;
        private final int juniors;
    }
}
