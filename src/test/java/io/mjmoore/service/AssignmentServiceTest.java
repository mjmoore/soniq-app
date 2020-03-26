package io.mjmoore.service;

import io.mjmoore.dto.InputDto;
import io.mjmoore.dto.OutputDto;
import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Basic service level test. More advanced tests are contained
 * in {@link AssignmentCalculatorTest}.
 */
public class AssignmentServiceTest {

    final AssignmentService service = new AssignmentService();

    @Test
    public void testGetAssignment() {
        val dto = new InputDto(Collections.singletonList(35), 10, 6);
        val assignments = service.getAssignments(dto);

        assertEquals(1, assignments.getAssignments().size());

        val assignment = assignments.getAssignments().get(0);
        assertEquals(3, assignment.getSeniors());
        assertEquals(1, assignment.getJuniors());
    }

    @Test
    public void testGetAssignments() {
        val dto = new InputDto(Arrays.asList(35, 21), 10, 6);
        val assignments = service.getAssignments(dto);

        assertEquals(2, assignments.getAssignments().size());

        var assignment = assignments.getAssignments().get(0);
        assertEquals(3, assignment.getSeniors());
        assertEquals(1, assignment.getJuniors());

        assignment = assignments.getAssignments().get(1);
        assertEquals(1, assignment.getSeniors());
        assertEquals(2, assignment.getJuniors());
    }
}
