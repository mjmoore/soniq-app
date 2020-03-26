package io.mjmoore.dto;

import lombok.Data;

import java.util.List;

@Data
public class InputDto {
    private final List<Integer> rooms;
    private final int seniorCapacity;
    private final int juniorCapacity;
}
