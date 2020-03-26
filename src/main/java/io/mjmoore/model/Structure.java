package io.mjmoore.model;

import lombok.Data;

@Data(staticConstructor = "withRooms")
public class Structure {
    private final int rooms;
}
