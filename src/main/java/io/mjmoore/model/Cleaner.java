package io.mjmoore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    class Assignment {
        @Getter
        private int seniors;
        @Getter
        private int juniors;
    }
}
