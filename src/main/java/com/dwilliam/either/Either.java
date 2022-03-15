package com.dwilliam.either;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a value of one of two possible types (a disjoint union).
 *
 * @param <L> The type of the left side
 * @param <R> The type of the right side
 */
public sealed interface Either<L,R> {

    /**
     * Returns the value from this {@code Either} if it's a {@code Left}
     * or throws {@code NoSuchElementException} if this {@code Either} is a {@code Right}.
     *
     * @return the value from this {@code Either} if it's a {@code Left}
     * @throws NoSuchElementException if this {@code Either} is a {@code Right}
     */
    L left();

    /**
     * Returns the value from this {@code Either} if it's a {@code Right}
     * or throws {@code NoSuchElementException} if this {@code Either} is a {@code Left}.
     *
     * @return the value from this {@code Either} if it's a {@code Right}
     * @throws NoSuchElementException if this {@code Either} is a {@code Left}
     */
    R right();

    /**
     * Returns {@code true} if this {@code Either} is a {@code Left}, {@code false} otherwise.
     *
     * @return {@code true} if this {@code Either} is a {@code Left}, {@code false} otherwise
     */
    boolean isLeft();

    /**
     * Returns {@code true} if this {@code Either} is a {@code Right}, {@code false} otherwise.
     *
     * @return {@code true} if this {@code Either} is a {@code Right}, {@code false} otherwise
     */
    boolean isRight();

    /**
     * Returns the value from this {@code Either} if it's a {@code Left}.
     * Returns {@code elseLeft} if this {@code Either} is a {@code Right}.
     *
     * @return the value from this {@code Either} if it's a {@code Left},
     *         {@code elseLeft} otherwise
     * @param elseLeft the value to be returned if this {@code Either} is a {@code Right}
     */
    default L leftOrElse(L elseLeft) {
        return isLeft() ? left() : elseLeft;
    }

    /**
     * Returns the value from this {@code Either} if it's a {@code Right}.
     * Returns {@code elseRight} if this {@code Either} is a {@code Left}.
     *
     * @return the value from this {@code Either} if it's a {@code Right},
     *         {@code elseRight} otherwise
     * @param elseRight the value to be returned if this {@code Either} is a {@code Left}
     */
    default R rightOrElse(R elseRight) {
        return isRight() ? right() : elseRight;
    }

    /**
     * Returns a {@code Optional} containing the value from this {@code Either} if it's a {@code Left}.
     * Returns {@code Optional.empty()} if this {@code Either} is a {@code Right}.
     *
     * @return the value from this {@code Either} if it's a {@code Left},
     *         {@code Optional.empty()} otherwise
     */
    default Optional<L> optionalLeft() {
        return isLeft() ? Optional.ofNullable(left()) : Optional.empty();
    }

    /**
     * Returns a {@code Optional} containing the value from this {@code Either} if it's a {@code Right}.
     * Returns {@code Optional.empty()} if this {@code Either} is a {@code Left}.
     *
     * @return the value from this {@code Either} if it's a {@code Right},
     *         {@code Optional.empty()} otherwise
     */
    default Optional<R> optionalRight() {
        return isRight() ? Optional.ofNullable(right()) : Optional.empty();
    }

    /**
     * Returns the value from this {@code Either}.
     *
     * @return the value from this {@code Either}
     */
    default Object get() {
        return isLeft() ? left() : right();
    }

    /**
     * Returns {@code true} if this {@code Either} is a {@code Left} and {@code left} matches the value,
     * or if this {@code Either} is a {@code Right} and {@code right} matches the value.
     * Returns {@code false} otherwise.
     *
     * @param left the predicate to apply to a value, if this {@code Either} is a {@code Left}
     * @param right the predicate to apply to a value, if this {@code Either} is a {@code Right}
     * @return {@code true} if this {@code Either} is a {@code Left} and {@code left} matches the value,
     *         or if this {@code Either} is a {@code Right} and {@code right} matches the value,
     *         @code false} otherwise
     * @throws NullPointerException if the one or both predicate are {@code null}
     */
    default boolean match(Predicate<L> left, Predicate<R> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        return isLeft() ? left.test(left()) : right.test(right());
    }

    /**
     * Returns {@code true} if this {@code Either} is a {@code Left} and {@code left} matches the value.
     * Returns {@code false} otherwise.
     *
     * @param left the predicate to apply to a value, if this {@code Either} is a {@code Left}
     * @return {@code true} if this {@code Either} is a {@code Left} and {@code left} matches the value
     *         {@code false} otherwise.
     * @throws NullPointerException if the predicate is {@code null}
     */
    default boolean matchLeft(Predicate<L> left) {
        Objects.requireNonNull(left);
        return isLeft() && left.test(left());
    }

    /**
     * Returns {@code true} if this {@code Either} is a {@code Right} and {@code right} matches the value.
     * Returns {@code false} otherwise.
     *
     * @param right the predicate to apply to a value, if this {@code Either} is a {@code Right}
     * @return {@code true} if this {@code Either} is a {@code Right} and {@code right} matches the value
     *         {@code false} otherwise.
     * @throws NullPointerException if the predicate is {@code null}
     */
    default boolean matchRight(Predicate<R> right) {
        Objects.requireNonNull(right);
        return isRight() && right.test(right());
    }

    /**
     * Returns a {@code Optional} containing the value from this {@code Either} if it's a {@code Left} and {@code left} matches the value.
     * Returns {@code Optional.empty()} otherwise.
     *
     * @param left the predicate to apply to a value, if this {@code Either} is a {@code Left}
     * @return {a {@code Optional} containing the value from this {@code Either} if it's a {@code Left} and {@code left} matches the value,
     *         {@code Optional.empty()} otherwise.
     * @throws NullPointerException if the predicate is {@code null}
     */
    default Optional<L> filterLeft(Predicate<L> left) {
        Objects.requireNonNull(left);
        return isLeft() && left.test(left()) ? Optional.ofNullable(left()): Optional.empty();
    }

    /**
     * Returns a {@code Optional} containing the value from this {@code Either} if it's a {@code Right} and {@code right} matches the value.
     * Returns {@code Optional.empty()} otherwise.
     *
     * @param right the predicate to apply to a value, if this {@code Either} is a {@code Right}
     * @return {a {@code Optional} containing the value from this {@code Either} if it's a {@code Right} and {@code right} matches the value,
     *         {@code Optional.empty()} otherwise.
     * @throws NullPointerException if the predicate is {@code null}
     */
    default Optional<R> filterRight(Predicate<R> right) {
        Objects.requireNonNull(right);
        return isRight() && right.test(right()) ? Optional.ofNullable(right()): Optional.empty();
    }

    /**
     * Returns a {@code Left} if this {@code Either} is a {@code Right}.
     * Returns a {@code Right} if this {@code Either} is a {@code Left}.
     *
     * @return a {@code Left} if this {@code Either} is a {@code Right} or
     *         a {@code Right} if this {@code Either} is a {@code Left}
     */
    default Either<R,L> swap() {
        return isLeft() ? ofRight(left()) : ofLeft(right());
    }

    /**
     * Applies {@code left} if this {@code Either} is a {@code Left} or
     * {@code right} if this {@code Either} is a {@code Right}.
     *
     * @param left the function to apply if this {@code Either} is a {@code Left}
     * @param right the function to apply if this {@code Either} is a {@code Right}
     * @param <T> the type of the results of applying the function
     * @return the results of applying the function
     * @throws NullPointerException if {@code left} or {@code right} are {@code null}
     */
    default <T> T fold(Function<? super L, ? extends T> left, Function<? super R, ? extends T> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        return isLeft() ? left.apply(left()) : right.apply(right());
    }

    /**
     * Applies {@code left} if this {@code Either} is a {@code Left} or
     * the value contained in this {@code Either} if it's a {@code Right}.
     *
     * @param left the function to apply if this {@code Either} is a {@code Left}
     * @return the results of applying the function or
     *         the value contained in this {@code Either} if it's a {@code Right}
     * @throws NullPointerException if {@code left} is {@code null}
     */
    default R foldLeft(Function<? super L, ? extends R> left){
        Objects.requireNonNull(left);
        return isRight() ? right() : left.apply(left());
    }

    /**
     * Applies {@code right} if this {@code Either} is a {@code Right} or
     * the value contained in this {@code Either} if it's a {@code Left}.
     *
     * @param right the function to apply if this {@code Either} is a {@code Right}
     * @return the results of applying the function or
     *         the value contained in this {@code Either} if it's a {@code Left}
     * @throws NullPointerException if {@code right} is {@code null}
     */
    default L foldRight(Function<? super R, ? extends L> right) {
        Objects.requireNonNull(right);
        return isLeft() ? left() : right.apply(right());
    }

    /**
     * Applies {@code function} to this {@code Either}.
     *
     * @param function the function to apply
     * @param <T> the type of the results of applying the function
     * @return the results of applying the function
     * @throws NullPointerException if {@code function} is {@code null}
     */
    default <T> T fold(Function<? super Either<L,R>, ? extends T> function) {
        Objects.requireNonNull(function);
        return function.apply(this);
    }

    /**
     * Returns an {@code Either} containing the result of the application of one of the given
     * functions to the value of this {@code Either}.
     *
     * @param left the function to apply to the value contained in this {@code Either} if it's a {@code Left}
     * @param right the function to apply to the value contained in this {@code Either} if it's a {@code Right}
     * @param <L2> The type of the left side of the new {@code Either}
     * @param <R2> The type of the right side of the new {@code Either}
     * @return a new {@code Either}
     * @throws NullPointerException if {@code left} or {@code right} are {@code null}
     */
    default <L2,R2> Either<L2,R2> map(Function<? super L, ? extends L2> left, Function<? super R, ? extends R2> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        return isLeft() ? ofLeft(left.apply(left())) : ofRight(right.apply(right()));
    }

    /**
     * Returns a {@code Right} containing the value of this {@code Either} if it's a {@code Right}.
     * Returns an {@code Either} containing the result of the application of the given
     * function to the value of this {@code Either} if it's a {@code Left}.
     *
     * @param left the function to apply to the value contained in this {@code Either} if it's a {@code Left}
     * @param <L2> The type of the left side of the new {@code Either}
     * @return a new {@code Either}
     * @throws NullPointerException if {@code left} is {@code null}
     */
    default <L2> Either<L2,R> mapLeft(Function<? super L, ? extends L2> left) {
        Objects.requireNonNull(left);
        return isRight() ? ofRight(right()) : ofLeft(left.apply(left()));
    }

    /**
     * Returns a {@code Left} containing the value of this {@code Either} if it's a {@code Left}.
     * Returns an {@code Either} containing the result of the application of the given
     * function to the value of this {@code Either} if it's a {@code Right}.
     *
     * @param right the function to apply to the value contained in this {@code Either} if it's a {@code Right}
     * @param <R2> The type of the right side of the new {@code Either}
     * @return a new {@code Either}
     * @throws NullPointerException if {@code right} is {@code null}
     */
    default <R2> Either<L,R2> mapRight(Function<? super R, ? extends R2> right) {
        Objects.requireNonNull(right);
        return isLeft() ? ofLeft(left()) : ofRight(right.apply(right()));
    }

    /**
     * Returns the result of the application of one of the given
     * functions to the value of this {@code Either}.
     *
     * @param left the function to apply to the value contained in this {@code Either} if it's a {@code Left}
     * @param right the function to apply to the value contained in this {@code Either} if it's a {@code Right}
     * @param <L2> The type of the left side of the new {@code Either}
     * @param <R2> The type of the right side of the new {@code Either}
     * @return a new {@code Either}
     * @throws NullPointerException if {@code left} or {@code right} are {@code null}
     */
    default <L2,R2> Either<L2,R2> flatMap(Function<? super L, Either<L2, R2>> left, Function<? super R, Either<L2, R2>> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        return isLeft() ? left.apply(left()) : right.apply(right());
    }

    /**
     * Returns a {@code Right} containing the value of this {@code Either} if it's a {@code Right}.
     * Returns the result of the application of the given function to the value of this {@code Either} if it's a {@code Left}.
     *
     * @param left the function to apply to the value contained in this {@code Either} if it's a {@code Left}
     * @param <L2> The type of the left side of the new {@code Either}
     * @return a new {@code Either}
     * @throws NullPointerException if {@code left} is {@code null}
     */
    default <L2> Either<L2, R> flatMapLeft(Function<? super L, Either<L2, R>> left) {
        Objects.requireNonNull(left);
        return isLeft() ? left.apply(left()) : ofRight(right());
    }

    /**
     * Returns a {@code Left} containing the value of this {@code Either} if it's a {@code Left}.
     * Returns the result of the application of the given function to the value of this {@code Either} if it's a {@code Right}.
     *
     * @param right the function to apply to the value contained in this {@code Either} if it's a {@code Right}
     * @param <R2> The type of the right side of the new {@code Either}
     * @return a new {@code Either}
     * @throws NullPointerException if {@code right} is {@code null}
     */
    default <R2> Either<L, R2> flatMapRight(Function<? super R, Either<L, R2>> right) {
        Objects.requireNonNull(right);
        return isRight() ? right.apply(right()) : ofLeft(left());
    }

    /**
     * Returns an {@code Either} consisting of the value of this either, additionally
     * performing the provided action on the value.
     *
     * @param left the action to perform on the value if this {@code Either} is a {@code Left}
     * @param right the action to perform on the value if this {@code Either} is a {@code Right}
     * @return the new {@code Either}
     * @throws NullPointerException if {@code left} or {@code right} are {@code null}
     */
    default Either<L,R> peek(Consumer<? super L> left, Consumer<? super R> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        if (isLeft()) {
            L value = left();
            left.accept(value);
            return ofLeft(value);
        }
        else {
            R value = right();
            right.accept(value);
            return ofRight(value);
        }
    }

    /**
     * Returns an {@code Either} consisting of the value of this either, additionally
     * performing the provided action on the value if this {@code Either} is a {@code Left}.
     *
     * @param left left the action to perform on the value if this {@code Either} is a {@code Left}
     * @return the new {@code Either}
     * @throws NullPointerException if {@code left} is {@code null}
     */
    default Either<L,R> peekLeft(Consumer<? super L> left) {
        Objects.requireNonNull(left);
        if (isLeft()) {
            L value = left();
            left.accept(value);
            return ofLeft(value);
        }
        else return ofRight(right());
    }

    /**
     * Returns an {@code Either} consisting of the value of this either, additionally
     * performing the provided action on the value if this {@code Either} is a {@code Right}.
     *
     * @param right left the action to perform on the value if this {@code Either} is a {@code Right}
     * @return the new {@code Either}
     * @throws NullPointerException if {@code right} is {@code null}
     */
    default Either<L,R> peekRight(Consumer<? super R> right) {
        Objects.requireNonNull(right);
        if (isRight()) {
            R value = right();
            right.accept(value);
            return ofRight(value);
        }
        else return ofLeft(left());
    }

    /**
     * Performs an action on the value of this {@code Either}.
     *
     * @param left the action to perform on the value if this {@code Either} is a {@code Left}
     * @param right right the action to perform on the value if this {@code Either} is a {@code Right}
     * @throws NullPointerException if {@code left} or {@code right} are {@code null}
     */
    default void forEach(Consumer<? super L> left, Consumer<? super R> right) {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        if (isLeft()) left.accept(left());
        else right.accept(right());
    }

    /**
     * Performs an action on the value of this {@code Either} if it's a {@code Left}.
     *
     * @param left the action to perform on the value if this {@code Either} is a {@code Left}
     * @throws NullPointerException if {@code left} is {@code null}
     */
    default void forEachLeft(Consumer<? super L> left) {
        Objects.requireNonNull(left);
        if (isLeft()) left.accept(left());
    }

    /**
     * Performs an action on the value of this {@code Either} if it's a {@code Right}.
     *
     * @param right the action to perform on the value if this {@code Either} is a {@code Right}
     * @throws NullPointerException if {@code right} is {@code null}
     */
    default void forEachRight(Consumer<? super R> right) {
        Objects.requireNonNull(right);
        if (isRight()) right.accept(right());
    }

    /**
     * The left side of an {@code Either}.
     *
     * @param <L> The type of the left side
     * @param <R> The type of the right side
     */
    record Left<L,R>(L left) implements Either<L, R> {

        /**
         * Throws {@code NoSuchElementException}
         *
         * @throws NoSuchElementException it's not a {@code Right}
         */
        @Override
        public R right() {
            throw new NoSuchElementException("Not a Right");
        }

        /**
         * @return true
         */
        @Override
        public boolean isLeft() {
            return true;
        }

        /**
         * @return false
         */
        @Override
        public boolean isRight() {
            return false;
        }

        /**
         * Indicates whether some other object is "equal to" this {@code Left}.
         * The other object is considered equal if:
         * <ul>
         * <li>it is also an {@code Left} and;
         * <li>the present values are "equal to" each other via {@code equals()}.
         * </ul>
         *
         * @param obj an object to be tested for equality
         * @return {@code true} if the other object is "equal to" this object,
         *         otherwise {@code false}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return switch (obj) {
                case Left<?, ?> l -> Objects.equals(left, l.left);
                case null, default -> false;
            };
        }

        /**
         * Returns the hash code of the value.
         *
         * @return hash code value of the value
         */
        @Override
        public int hashCode() {
            return Objects.hash(left);
        }

        /**
         * Returns a non-empty string representation of this {@code Left}
         * suitable for debugging.
         *
         * @return the string representation of this instance
         */
        @Override
        public String toString() {
            return String.format("Left[%s]", this.left);
        }

    }

    /**
     * The right side of an {@code Either}.
     *
     * @param <L> The type of the left side
     * @param <R> The type of the right side
     */
    record Right<L, R>(R right) implements Either<L, R> {

        /**
         * Throws {@code NoSuchElementException}
         *
         * @throws NoSuchElementException it's not a {@code Left}
         */
        @Override
        public L left() {
            throw new NoSuchElementException("Not a Left");
        }

        /**
         * @return false
         */
        @Override
        public boolean isLeft() {
            return false;
        }

        /**
         * @return true
         */
        @Override
        public boolean isRight() {
            return true;
        }

        /**
         * Indicates whether some other object is "equal to" this {@code Right}.
         * The other object is considered equal if:
         * <ul>
         * <li>it is also an {@code Right} and;
         * <li>the present values are "equal to" each other via {@code equals()}.
         * </ul>
         *
         * @param obj an object to be tested for equality
         * @return {@code true} if the other object is "equal to" this object,
         * otherwise {@code false}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return switch (obj) {
                case Right<?, ?> r -> Objects.equals(right, r.right);
                case null, default -> false;
            };
        }

        /**
         * Returns the hash code of the value.
         *
         * @return hash code value of the value
         */
        @Override
        public int hashCode() {
            return Objects.hash(right);
        }

        /**
         * Returns a non-empty string representation of this {@code Right}
         * suitable for debugging.
         *
         * @return the string representation of this instance
         */
        @Override
        public String toString() {
            return String.format("Right[%s]", this.right);
        }

    }

    /**
     * Returns an {@code Either} describing the given value on the left side.
     *
     * @param left the value to describe
     * @param <L> the type of the left side
     * @param <R> the type of the right side
     * @return an {@code Either} with the value  on the left side
     */
    static <L,R> Either<L,R> ofLeft(L left) {
        return new Left<>(left);
    }

    /**
     * Returns an {@code Either} describing the given value on the right side.
     *
     * @param right the value to describe
     * @param <L> the type of the left side
     * @param <R> the type of the right side
     * @return an {@code Either} with the value  on the right side
     */
    static <L,R> Either<L,R> ofRight(R right) {
        return new Right<>(right);
    }

}
