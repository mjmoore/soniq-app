package io.mjmoore.service;

import io.mjmoore.model.Cleaner;
import lombok.val;
import org.jooq.lambda.Seq;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssignmentCalculatorTest {

    private AssignmentService.AssignmentCalculator assigner;


    /**
     *    In: { “rooms”: [35, 21, 17, 28], “senior”: 10, “junior”: 6 }
     *    Out: [ {senior: 3, junior: 1}, {senior: 1, junior: 2}, {senior: 2, junior: 0}, {senior: 1, junior: 3} ]
     */
    @Test
    public void exampleOne() {

        val senior = Cleaner.of(Cleaner.Type.Senior, 10);
        val junior = Cleaner.of(Cleaner.Type.Junior, 6);

        val rooms = Seq.of(35, 21, 17, 28);
        val expected = Seq.of(
                Cleaner.Assignment.of(3, 1),
                Cleaner.Assignment.of(1, 2),
                Cleaner.Assignment.of(2, 0),
                Cleaner.Assignment.of(1, 3));

        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);
    }

    /**
     *    In: { “rooms”: [24, 28], “senior”: 11, “junior”: 6 }
     *    Out: [ {senior: 2, junior: 1}, {senior: 2, junior: 1} ]
     */
    @Test
    public void exampleTwo() {

        val senior = Cleaner.of(Cleaner.Type.Senior, 11);
        val junior = Cleaner.of(Cleaner.Type.Junior, 6);

        val rooms = Seq.of(24, 28);
        val expected = Seq.of(
                Cleaner.Assignment.of(2, 1),
                Cleaner.Assignment.of(2, 1));


        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);
    }

    /**
     * Test secondary ordering of cleaners.
     * Given:
     *  - Room size 20
     *  - Senior capacity of 10
     *  - Junior capacity of 5
     *
     *  We can have 1 senior and 2 juniors, or 2 seniors.
     *  We want 2 seniors as it's less people.
     */
    @Test
    public void fewestCleanersRequired() {
        val senior = Cleaner.of(Cleaner.Type.Senior, 10);
        val junior = Cleaner.of(Cleaner.Type.Junior, 5);

        val rooms = Seq.of(20, 30, 40, 50);
        val expected = Seq.of(
                Cleaner.Assignment.of(2, 0),
                Cleaner.Assignment.of(3, 0),
                Cleaner.Assignment.of(4, 0),
                Cleaner.Assignment.of(5, 0));

        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);
    }

    /**
     * Given an room size which cannot be fit completely, then expect
     * that the smallest capacity is assigned.
     */
    @Test
    public void lowestCapacityExpected() {
        val senior = Cleaner.of(Cleaner.Type.Senior, 10);
        val junior = Cleaner.of(Cleaner.Type.Junior, 5);

        val rooms = Seq.of(21, 32, 43, 54);
        val expected = Seq.of(
                Cleaner.Assignment.of(2, 1),
                Cleaner.Assignment.of(3, 1),
                Cleaner.Assignment.of(4, 1),
                Cleaner.Assignment.of(5, 1));

        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);
    }

    /**
     * We expect that given a choice between exceeding the capacity
     * and providing more people to fit the exact capacity, then
     * we should provide more people to fit the exact capacity.
     *
     * Example:
     *  - Room size = 28
     *  - Senior capacity = 10
     *  - Junior capacity = 6
     *
     * We have the choice between:
     *  - 3 seniors (total capacity = 30, exceeds by two)
     *  - 1 seniors + 3 juniors (total capacity = 28, exact match)
     *
     * In this case we want 1 senior and 3 juniors to avoid
     * overcapacity.
     */
    @Test
    public void exactCapacityFitExpected() {
        val senior = Cleaner.of(Cleaner.Type.Senior, 10);
        val junior = Cleaner.of(Cleaner.Type.Junior, 6);

        val rooms = Seq.of(22, 28, 32, 54, 58);
        val expected = Seq.of(
                Cleaner.Assignment.of(1, 2),
                Cleaner.Assignment.of(1, 3),
                Cleaner.Assignment.of(2, 2),
                Cleaner.Assignment.of(3, 4),
                Cleaner.Assignment.of(4, 3));

        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);
    }

    /**
     * Juniors cannot do their job (capacity = 0). We expect only
     * seniors to be assigned.
     */
    @Test
    public void juniorsDoNotWork() {
        val senior = Cleaner.of(Cleaner.Type.Senior, 10);
        val junior = Cleaner.of(Cleaner.Type.Junior, 0);

        val rooms = Seq.of(3, 10, 14, 19, 20, 24, 31);
        val expected = Seq.of(
                Cleaner.Assignment.of(1, 0),
                Cleaner.Assignment.of(1, 0),
                Cleaner.Assignment.of(2, 0),
                Cleaner.Assignment.of(2, 0),
                Cleaner.Assignment.of(2, 0),
                Cleaner.Assignment.of(3, 0),
                Cleaner.Assignment.of(4, 0));

        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);
    }

    /**
     * Seniors are only supervising and do no work. We expect only
     * 1 senior per room and the rest are juniors.
     */
    @Test
    public void seniorsDoNotWork() {
        val senior = Cleaner.of(Cleaner.Type.Senior, 0);
        val junior = Cleaner.of(Cleaner.Type.Junior, 4);

        val rooms = Seq.of(3, 10, 14, 19, 20, 24, 31);
        val expected = Seq.of(
                Cleaner.Assignment.of(1, 1),
                Cleaner.Assignment.of(1, 3),
                Cleaner.Assignment.of(1, 4),
                Cleaner.Assignment.of(1, 5),
                Cleaner.Assignment.of(1, 5),
                Cleaner.Assignment.of(1, 6),
                Cleaner.Assignment.of(1, 8));

        assigner = new AssignmentService.AssignmentCalculator(senior, junior);

        validate(rooms, expected);

    }

    private void validate(final Seq<Integer> rooms, final Seq<Cleaner.Assignment> expected) {
        rooms.zip(expected)
                .collect(Collectors.toList())
                .forEach(tuple -> {
                    val assignment = assigner.assign(tuple.v1());

                    assertEquals(tuple.v2().getSeniors(), assignment.getSeniors(), () ->
                            String.format("Seniors for room size %d", tuple.v1()));

                    assertEquals(tuple.v2().getJuniors(), assignment.getJuniors(), () ->
                            String.format("Juniors for room size %d", tuple.v1()));
        });
    }
}
