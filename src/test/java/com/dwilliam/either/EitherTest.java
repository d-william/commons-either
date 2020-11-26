package com.dwilliam.either;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class EitherTest {

    Either<String, String> left;
    Either<String, String> right;

    @BeforeEach
    void beforeEach() {
        left = Either.ofLeft("left");
        right = Either.ofRight("right");
    }

    @Test
    void left() {
        assertEquals("left", left.left());
        assertThrows(NoSuchElementException.class, () -> right.left());
    }

    @Test
    void right() {
        assertThrows(NoSuchElementException.class, () -> left.right());
        assertEquals("right", right.right());
    }

    @Test
    void isLeft() {
        assertTrue(left.isLeft());
        assertFalse(right.isLeft());
    }

    @Test
    void isRight() {
        assertFalse(left.isRight());
        assertTrue(right.isRight());
    }

    @Test
    void leftOrElse() {
        assertEquals("left", left.leftOrElse("other"));
        assertEquals("other", right.leftOrElse("other"));
    }

    @Test
    void rightOrElse() {
        assertEquals("other", left.rightOrElse("other"));
        assertEquals("right", right.rightOrElse("other"));
    }

    @Test
    void optionalLeft() {
        assertEquals(Optional.of("left"), left.optionalLeft());
        assertEquals(Optional.empty(), right.optionalLeft());
    }

    @Test
    void optionalRight() {
        assertEquals(Optional.empty(), left.optionalRight());
        assertEquals(Optional.of("right"), right.optionalRight());
    }

    @Test
    void get() {
        assertEquals("left", left.get());
        assertEquals("right", right.get());
    }

    @Test
    void match() {
        assertTrue(left.match(l -> true, r -> false));
        assertTrue(right.match(l -> false, r -> true));
        assertFalse(left.match(l -> false, r -> true));
        assertFalse(right.match(l -> true, r -> false));
    }

    @Test
    void matchLeft() {
        assertTrue(left.matchLeft(l -> true));
        assertFalse(right.matchLeft(l -> true));
    }

    @Test
    void matchRight() {
        assertFalse(left.matchRight(r -> true));
        assertTrue(right.matchRight(r -> true));
    }

    @Test
    void filterLeft() {
        assertEquals(Optional.of("left"), left.filterLeft(l -> true));
        assertEquals(Optional.empty(), right.filterLeft(l -> true));
    }

    @Test
    void filterRight() {
        assertEquals(Optional.empty(), left.filterRight(r -> true));
        assertEquals(Optional.of("right"), right.filterRight(r -> true));
    }

    @Test
    void swap() {
        assertEquals(Either.ofRight("left"), left.swap());
        assertEquals(Either.ofLeft("right"), right.swap());
    }

    @Test
    void fold() {
        assertEquals("l", left.fold(s -> "l", s -> "r"));
        assertEquals("r", right.fold(s -> "l", s -> "r"));

        assertEquals("left", left.fold(Either::left));
        assertEquals("right", right.fold(Either::right));
    }

    @Test
    void foldLeft() {
        assertEquals("l", left.foldLeft(s -> "l"));
        assertEquals("right", right.foldLeft(s -> "l"));
    }

    @Test
    void foldRight() {
        assertEquals("left", left.foldRight(s -> "l"));
        assertEquals("r", right.foldRight(s -> "r"));
    }

    @Test
    void map() {
        assertEquals(Either.ofLeft("l"), left.map(s -> "l", s -> "r"));
        assertEquals(Either.ofRight("r"), right.map(s -> "l", s -> "r"));
    }

    @Test
    void mapLeft() {
        assertEquals(Either.ofLeft("l"), left.mapLeft(s -> "l"));
        assertEquals(right, right.mapLeft(s -> "l"));
    }

    @Test
    void mapRight() {
        assertEquals(left, left.mapRight(s -> "l"));
        assertEquals(Either.ofRight("r"), right.mapRight(s -> "r"));
    }

    @Test
    void flatMap() {
        Either<Either<String, String>, Integer> left_left = Either.ofLeft(Either.ofLeft("left_left"));
        Either<Either<String, String>, Integer> left_right = Either.ofLeft(Either.ofRight("left_right"));
        Either<Integer, Either<String, String>> right_left = Either.ofRight(Either.ofLeft("right_left"));
        Either<Integer, Either<String, String>> right_right = Either.ofRight(Either.ofRight("right_right"));
        assertEquals(Either.ofLeft("left_left"), left_left.flatMap(Eithers.leftFlatten(), Either::ofRight));
        assertEquals(Either.ofLeft("left_right"), left_right.flatMap(Eithers.leftFlatten(), Either::ofRight));
        assertEquals(Either.ofRight("right_left"), right_left.flatMap(Either::ofLeft, Eithers.rightFlatten()));
        assertEquals(Either.ofRight("right_right"), right_right.flatMap(Either::ofLeft, Eithers.rightFlatten()));
    }

    @Test
    void flatMapLeft() {
        Either<Either<String, String>, Integer> left_left = Either.ofLeft(Either.ofLeft("left_left"));
        Either<Either<String, String>, Integer> left_right = Either.ofLeft(Either.ofRight("left_right"));
        Either<Either<String, String>, Integer> right = Either.ofRight(0);
        assertEquals(Either.ofLeft("left_left"), left_left.flatMapLeft(Eithers.leftFlatten()));
        assertEquals(Either.ofLeft("left_right"), left_right.flatMapLeft(Eithers.leftFlatten()));
        assertEquals(Either.ofRight(0), right.flatMapLeft(Eithers.leftFlatten()));
    }

    @Test
    void flatMapRight() {
        Either<Either<String, String>, Integer> left_left = Either.ofLeft(Either.ofLeft("left_left"));
        Either<Either<String, String>, Integer> left_right = Either.ofLeft(Either.ofRight("left_right"));
        Either<Integer, Either<String, String>> right_left = Either.ofRight(Either.ofLeft("right_left"));
        Either<Integer, Either<String, String>> right_right = Either.ofRight(Either.ofRight("right_right"));
        assertEquals(Either.ofLeft(Either.ofLeft("left_left")), left_left.flatMapRight(Either::ofRight));
        assertEquals(Either.ofLeft(Either.ofRight("left_right")), left_right.flatMapRight(Either::ofRight));
        assertEquals(Either.ofRight("right_left"), right_left.flatMapRight(Eithers.rightFlatten()));
        assertEquals(Either.ofRight("right_right"), right_right.flatMapRight(Eithers.rightFlatten()));
    }

    @Test
    void peek() {
        AtomicBoolean bLeft = new AtomicBoolean(false);
        AtomicBoolean bRight = new AtomicBoolean(false);
        assertEquals(left, left.peek(l -> bLeft.set(true), r -> bLeft.set(false)));
        assertEquals(right, right.peek(l -> bRight.set(false), r -> bRight.set(true)));
        assertTrue(bLeft.get());
        assertTrue(bRight.get());
    }

    @Test
    void peekLeft() {
        AtomicBoolean bLeft = new AtomicBoolean(false);
        AtomicBoolean bRight = new AtomicBoolean(true);
        assertEquals(left, left.peekLeft(l -> bLeft.set(true)));
        assertEquals(right, right.peekLeft(l -> bRight.set(false)));
        assertTrue(bLeft.get());
        assertTrue(bRight.get());
    }

    @Test
    void peekRight() {
        AtomicBoolean bLeft = new AtomicBoolean(true);
        AtomicBoolean bRight = new AtomicBoolean(false);
        assertEquals(left, left.peekRight(r -> bLeft.set(false)));
        assertEquals(right, right.peekRight(r -> bRight.set(true)));
        assertTrue(bLeft.get());
        assertTrue(bRight.get());
    }

    @Test
    void forEach() {
        AtomicBoolean bLeft = new AtomicBoolean(false);
        AtomicBoolean bRight = new AtomicBoolean(false);
        left.forEach(l -> bLeft.set(true), r -> bLeft.set(false));
        right.forEach(l -> bRight.set(false), r -> bRight.set(true));
        assertTrue(bLeft.get());
        assertTrue(bRight.get());
    }

    @Test
    void forEachLeft() {
        AtomicBoolean bLeft = new AtomicBoolean(false);
        AtomicBoolean bRight = new AtomicBoolean(true);
        left.forEachLeft(l -> bLeft.set(true));
        right.forEachLeft(l -> bRight.set(false));
        assertTrue(bLeft.get());
        assertTrue(bRight.get());
    }

    @Test
    void forEachRight() {
        AtomicBoolean bLeft = new AtomicBoolean(true);
        AtomicBoolean bRight = new AtomicBoolean(false);
        left.forEachRight(r -> bLeft.set(false));
        right.forEachRight(r -> bRight.set(true));
        assertTrue(bLeft.get());
        assertTrue(bRight.get());
    }

    @Test
    void merge() {
        assertEquals("left", Eithers.merge(left));
        assertEquals("right", Eithers.merge(right));
    }

    @Test
    void testEquals() {
        assertEquals(Either.ofLeft("left"), left);
        assertEquals(Either.ofRight("right"), right);
    }

    @Test
    void testHashCode() {
        assertEquals(Either.ofLeft("left").hashCode(), left.hashCode());
        assertEquals(Either.ofRight("right").hashCode(), right.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(Either.ofLeft("left").toString(), left.toString());
        assertEquals(Either.ofRight("right").toString(), right.toString());
    }

}