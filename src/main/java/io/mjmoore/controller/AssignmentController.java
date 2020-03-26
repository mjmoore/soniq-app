package io.mjmoore.controller;

import io.mjmoore.dto.InputDto;
import io.mjmoore.dto.OutputDto;
import io.mjmoore.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/assignments")
    public @ResponseBody OutputDto getAssignments(@RequestBody final InputDto dto) {

        return assignmentService.getAssignments(dto);
    }
}
