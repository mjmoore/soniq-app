package io.mjmoore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
public class InputDto {

    @NotEmpty
    private List<Integer> rooms;

    @NotNull
    @PositiveOrZero
    private Integer senior;

    @NotNull
    @PositiveOrZero
    private Integer junior;
}
