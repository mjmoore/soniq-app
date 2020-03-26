package io.mjmoore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
public class InputDto {

    @NotEmpty
    private List<Integer> rooms;

    @PositiveOrZero
    private int seniorCapacity;

    @PositiveOrZero
    private int juniorCapacity;
}
