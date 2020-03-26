package io.mjmoore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mjmoore.dto.InputDto;
import io.mjmoore.model.Cleaner;
import io.mjmoore.service.AssignmentService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(SpringExtension.class)
class AssignmentControllerTest {

    private final TypeReference<List<Cleaner.Assignment>> type
            = new TypeReference<List<Cleaner.Assignment>>() {};

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private AssignmentService assignmentService;

    @Autowired
    private MockMvc assignmentController;

    @Test
    public void getAssignments() throws Exception {
        val dummyAssignment = Collections.singletonList(Cleaner.Assignment.of(0, 0));
        final InputDto dummyDto = new InputDto(Collections.singletonList(0), 0, 0);

        when(assignmentService.getAssignments(any(InputDto.class)))
                .thenReturn(dummyAssignment);

        val thing = assignmentService.getAssignments(dummyDto);

        final MockHttpServletResponse body = assignmentController.perform(post("/assignments")
                    .content(mapper.writeValueAsString(dummyDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(dummyAssignment, mapper.readValue(body.getContentAsString(), type));
    }
}