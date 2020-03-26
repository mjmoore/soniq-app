package io.mjmoore.service;

import io.mjmoore.dto.InputDto;
import io.mjmoore.dto.OutputDto;
import io.mjmoore.model.Cleaner;
import lombok.RequiredArgsConstructor;
import lombok.val;

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
        private int juniors = 0;

        public Cleaner.Assignment assign(final int roomSize) {

            while(assignedCapacity < roomSize) {

                if(assignInitialSenior()) continue;

                final int remainingCapacity = roomSize - assignedCapacity;

                final int seniorsToComplete = getAssignmentsForCompletion(senior, remainingCapacity);
                final int juniorsToComplete = getAssignmentsForCompletion(junior, remainingCapacity);

                // Prefer juniors to seniors
                if(seniorsToComplete > juniorsToComplete) {
                    assignSenior();
                    continue;
                }

                assignJunior();
            }

            return Cleaner.Assignment.builder()
                    .juniors(this.juniors)
                    .seniors(this.seniors)
                    .build();

        }

        private boolean assignInitialSenior() {
            // Always one senior
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

        private void assignJunior() {
            juniors++;
            assignedCapacity += junior.getCapacity();
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
            final int assignmentsToComplete = capacity % cleaner.getCapacity();

            if(assignmentsToComplete * cleaner.getCapacity() < capacity) {
                return assignmentsToComplete + 1;
            }

            return assignmentsToComplete;
        }
    }
}
