package io.mjmoore.service;

import io.mjmoore.dto.InputDto;
import io.mjmoore.model.Cleaner;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    public List<Cleaner.Assignment> getAssignments(final InputDto dto) {

        val senior = Cleaner.of(Cleaner.Type.Senior, dto.getSenior());
        val junior = Cleaner.of(Cleaner.Type.Junior, dto.getJunior());



        val calculator = new AssignmentCalculator(senior, junior);

        return dto.getRooms()
                .stream().map(calculator::assign)
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    public static class AssignmentCalculator {

        private final Cleaner senior;
        private final Cleaner junior;

        private int assignedCapacity = 0;
        private int seniors = 0;

        /**
         * Calculates all iterations of seniors with remaining juniors for a room
         * and returns the permutation which closely fits the capacity requirement.
         * @param roomSize
         * @return
         */
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

                // Short circuit algorithm, we've already found the best solution
                if(senior.getCapacity() == 0) {
                    break;
                }

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

            // If cleaners can't clean, assume an "infinite" amount needed
            if(cleaner.getCapacity() <= 0) {
                return Integer.MAX_VALUE;
            }

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
            // Secondary comparison on amount of people required
            if(getTotalCapacity().equals(assignment.getTotalCapacity())) {
                return getTotalPeople().compareTo(assignment.getTotalPeople());
            }

            return getTotalCapacity().compareTo(assignment.getTotalCapacity());
        }

        public Integer getTotalCapacity() {

            // If either type can't clean, assume an "infinite" amount required to complete job
            val infiniteSeniors = assignment.getSeniors() == Integer.MAX_VALUE;
            val infiniteJuniors = assignment.getJuniors() == Integer.MAX_VALUE;
            if(infiniteJuniors || infiniteSeniors) {
                return Integer.MAX_VALUE;
            }

            val capacity = (assignment.getJuniors() * junior.getCapacity()) +
                   (assignment.getSeniors() * senior.getCapacity());

            if(capacity <= 0) {
                return Integer.MAX_VALUE;
            }

            return capacity;
        }

        public Integer getTotalPeople() {
            return assignment.getSeniors() + assignment.getJuniors();
        }
    }
}
