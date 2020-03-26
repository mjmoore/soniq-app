package io.mjmoore.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
public class InputDto {

    @NotEmpty
    private final List<Integer> rooms;

    @PositiveOrZero
    private final int seniorCapacity;

    @PositiveOrZero
    private final int juniorCapacity;
}
