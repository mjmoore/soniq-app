package io.mjmoore.service;

import io.mjmoore.dto.InputDto;
import io.mjmoore.dto.OutputDto;
import io.mjmoore.model.Cleaner;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class AssignmentService {

    public OutputDto getAssignments(final InputDto dto) {

        val senior = Cleaner.of(Cleaner.Type.Senior, dto.getSeniorCapacity());
        val junior = Cleaner.of(Cleaner.Type.Junior, dto.getJuniorCapacity());

        val output = OutputDto.builder();

        val calculator = new AssignmentCalculator(senior, junior);

        return output.addAssignments(dto.getRooms()
                .stream().map(calculator::assign)
                .collect(Collectors.toList()))
                .build();
    }

    @RequiredArgsConstructor
    public static class AssignmentCalculator {

        private final Cleaner senior;
        private final Cleaner junior;

        private int assignedCapacity = 0;
        private int seniors = 0;

        public Cleaner.Assignment assign(final int roomSize) {

            this.assignedCapacity = 0;
            this.seniors = 0;

            val assignments = new ArrayList<Assignment>();

            while(assignedCapacity < roomSize) {

                if(assignInitialSenior()) continue;

                final int remainingCapacity = roomSize - assignedCapacity;

                final int juniorsToComplete = getAssignmentsForCompletion(junior, remainingCapacity);

                // Create assignment with remaining juniors and store
                assignments.add(Assignment.of(
                        Cleaner.Assignment.of(this.seniors, juniorsToComplete), senior, junior));

                // Assign an additional senior until capacity is reached or exceeded
                assignSenior();
            }

            assignments.add(Assignment.of(
                    Cleaner.Assignment.of(this.seniors, 0), senior, junior));

            return Collections.min(assignments, Assignment::compareTo).getAssignment();
        }

        private boolean assignInitialSenior() {
            if(seniors != 0) {
                return false;
            }

            assignSenior();
            return true;
        }

        private void assignSenior() {
            seniors++;
            assignedCapacity += senior.getCapacity();
        }

        /**
         * Determines the amount of cleaners required for a room.
         *
         * For example:
         *
         * <pre>
         * Cleaner senior = Cleaner.of(Cleaner.Type.Senior, 7);
         * int roomSize = 20
         * </pre>
         *
         * Assignments for completion = 3
         */
        private int getAssignmentsForCompletion(final Cleaner cleaner, final int capacity) {
            final int assignmentsToComplete = capacity / cleaner.getCapacity();

            if(assignmentsToComplete * cleaner.getCapacity() < capacity) {
                return assignmentsToComplete + 1;
            }

            return assignmentsToComplete;
        }
    }

    @Data(staticConstructor = "of")
    private static class Assignment implements Comparable<Assignment> {

        private final Cleaner.Assignment assignment;

        private final Cleaner senior;
        private final Cleaner junior;

        @Override
        public int compareTo(final Assignment assignment) {
            return getTotalCapacity().compareTo(assignment.getTotalCapacity());
        }

        public Integer getTotalCapacity() {
            return (assignment.getJuniors() * junior.getCapacity()) +
                   (assignment.getSeniors() * senior.getCapacity());
        }
    }
}
