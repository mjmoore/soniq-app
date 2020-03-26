package io.mjmoore.controller;

import io.mjmoore.dto.InputDto;
import io.mjmoore.model.Cleaner.Assignment;
import io.mjmoore.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/assignments")
    public @ResponseBody List<Assignment> getAssignments(@Valid @RequestBody final InputDto dto) {

        return assignmentService.getAssignments(dto);
    }
}
