package com.dwilliam.either;

import java.util.Objects;
import java.util.function.Function;

/**
 * This class consists exclusively of static methods that operate on {@code Either}.
 *
 * @see Either
 */
public final class Eithers {

    // Suppresses default constructor, ensuring non-instantiability.
    private Eithers() {}

    /**
     * Extracts the value from an {@code Either} instances regardless of whether they are Left or Right.
     *
     * @param either the {@code Either} to merge
     * @param <T> the type of the value to extract
     * @return the value contained in the {@code Either}
     * @throws NullPointerException if {@code either} is {@code null}
     */
    public static <T> T merge(Either<? extends T, ? extends T> either) {
        Objects.requireNonNull(either);
        return either.isLeft() ? either.left() : either.right();
    }

    /**
     * Returns a function that flattens the left side of an {@code Either}.
     *
     * @param <T> the type of both side of the left {@code Either} in input and
     *            the type of the left side of the {@code Either} in output
     * @param <U> the type of the right side of the {@code Either} in output
     * @return a function that flattens le left side of an {@code Either}
     */
    public static <T, U> Function<Either<T,T>, Either<T,U>> leftFlatten() {
        return e -> Either.ofLeft(merge(e));
    }

    /**
     * Returns a function that flattens the right side of an {@code Either}.
     *
     * @param <T> the type of both side of the right {@code Either} in input and
     *            the type of the right side of the {@code Either} in output
     * @param <U> the type of the left side of the {@code Either} in output
     * @return a function that flattens le right side of an {@code Either}
     */
    public static <T, U> Function<Either<T,T>, Either<U,T>> rightFlatten() {
        return e -> Either.ofRight(merge(e));
    }

    /**
     * Flattens the left side of an {@code Either}.
     *
     * @param either the {@code Either} to flatten
     * @param <L> the type of the left side of the {@code Either}
     * @param <R> the type of the right side of the {@code Either}
     * @return the {@code Either} flattens
     * @throws NullPointerException if {@code either} is {@code null}
     */
    public static <L, R> Either<L, R> flattenLeft(Either<Either<L, L>, R> either) {
        return Objects.requireNonNull(either).flatMapLeft(leftFlatten());
    }

    /**
     * Flattens the right side of an {@code Either}.
     *
     * @param either the {@code Either} to flatten
     * @param <L> the type of the left side of the {@code Either}
     * @param <R> the type of the right side of the {@code Either}
     * @return the {@code Either} flattens
     * @throws NullPointerException if {@code either} is {@code null}
     */
    public static <L, R> Either<L, R> flattenRight(Either<L,Either<R, R>> either) {
        return Objects.requireNonNull(either).flatMapRight(rightFlatten());
    }

    /**
     * Flattens an {@code Either}.
     *
     * @param either the {@code Either} to flatten
     * @param <L> the type of the left side of the {@code Either}
     * @param <R> the type of the right side of the {@code Either}
     * @return the {@code Either} flattens
     * @throws NullPointerException if {@code either} is {@code null}
     */
    public static <L, R> Either<L, R> flatten(Either<Either<L, L>,Either<R, R>> either) {
        return Objects.requireNonNull(either).flatMap(leftFlatten(), rightFlatten());
    }

}
