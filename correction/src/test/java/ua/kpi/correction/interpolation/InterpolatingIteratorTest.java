package ua.kpi.correction.interpolation;

import com.google.common.collect.Iterators;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InterpolatingIteratorTest {

    @Test
    public void shouldInterpolateFirstItem() {
        final List<Entry<Integer, String>> input = asList(
                entry(1, null),
                entry(2, null),
                entry(3, "three")
        );

        final List<Entry<Integer, String>> actual = list(InterpolatingIterator.of(input.iterator()));

        final List<Entry<Integer, String>> expected = asList(
                entry(1, "three"),
                entry(2, "three"),
                entry(3, "three")
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInterpolateLastItem() {
        final List<Entry<Integer, String>> input = asList(
                entry(1, "one"),
                entry(2, null),
                entry(3, null)
        );

        final List<Entry<Integer, String>> actual = list(InterpolatingIterator.of(input.iterator()));

        final List<Entry<Integer, String>> expected = asList(
                entry(1, "one"),
                entry(2, "one"),
                entry(3, "one")
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInterpolateEvenNumberOfPoints() {
        final List<Entry<Integer, String>> input = asList(
                entry(1, "one"),
                entry(2, null),
                entry(3, null),
                entry(4, "four")
        );

        final List<Entry<Integer, String>> actual = list(InterpolatingIterator.of(input.iterator()));

        final List<Entry<Integer, String>> expected = asList(
                entry(1, "one"),
                entry(2, "one"),
                entry(3, "four"),
                entry(4, "four")
        );

        assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void shouldInterpolateOddNumberOfPoints() {
        final List<Entry<Integer, String>> input = asList(
                entry(1, "one"),
                entry(2, null),
                entry(3, null),
                entry(4, null),
                entry(5, "five")
        );

        final List<Entry<Integer, String>> actual = list(InterpolatingIterator.of(input.iterator()));

        final List<Entry<Integer, String>> expected = asList(
                entry(1, "one"),
                entry(2, "one"),
                entry(3, "one"),
                entry(4, "five"),
                entry(5, "five")
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullSequence() {
        final List<Entry<Integer, String>> input = asList(
                entry(1, null),
                entry(2, null),
                entry(3, null),
                entry(4, null)
        );

        assertThatThrownBy(() -> list(InterpolatingIterator.of(input.iterator())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldReturnEmptyIteratorForEmptySequence() {
        final List<Entry<Integer, String>> actual = list(InterpolatingIterator.of(Iterators.cycle()));

        assertThat(actual).isEmpty();
    }


    @Test
    public void shouldReturnTheSameSequenceIfNoGapsPresent() {
        final List<Entry<Integer, String>> input = asList(
                entry(1, "one"),
                entry(2, "two"),
                entry(3, "three"),
                entry(4, "four")
        );

        final List<Entry<Integer, String>> actual = list(InterpolatingIterator.of(input.iterator()));

        final List<Entry<Integer, String>> expected = asList(
                entry(1, "one"),
                entry(2, "two"),
                entry(3, "three"),
                entry(4, "four")
        );

        assertThat(actual).isEqualTo(expected);
    }

    private static <T> List<T> list(Iterator<T> it) {
        return Lists.newArrayList(it);
    }

    private static <K, V> Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
}