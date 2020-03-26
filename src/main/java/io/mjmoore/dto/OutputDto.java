package io.mjmoore.dto;

import io.mjmoore.model.Cleaner;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder(setterPrefix = "add")
public class OutputDto {

    @Singular @Getter
    private final List<Cleaner.Assignment> assignments;
}
