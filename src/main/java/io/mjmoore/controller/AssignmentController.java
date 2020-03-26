package io.mjmoore.controller;

import io.mjmoore.dto.InputDto;
import io.mjmoore.model.Cleaner.Assignment;
import io.mjmoore.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/assignments")
    public @ResponseBody List<Assignment> getAssignments(@RequestBody final InputDto dto) {

        return assignmentService.getAssignments(dto);
    }
}
