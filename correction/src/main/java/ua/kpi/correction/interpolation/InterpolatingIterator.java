package ua.kpi.correction.interpolation;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.Map.Entry;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Objects.isNull;

/**
 * Interpolates sequence of entries by mutating its items if their values are null
 *
 * @param <K> key
 * @param <V> value
 * @param <T> type interpolator stream
 */
@RequiredArgsConstructor
class InterpolatingIterator<K, V, T extends Entry<K, V>> implements Iterator<T> {

    private final Iterator<T> sequence;

    private final Queue<T> nextItems = new ArrayDeque<>();

    private V lastValue = null;

    @Override
    public boolean hasNext() {
        return sequence.hasNext() || !nextItems.isEmpty();
    }

    //TODO: eliminate side effects
    @Override
    public T next() {
        if (!nextItems.isEmpty()) {
            return nextItems.poll();
        }

        // sanity check
        if (!sequence.hasNext()) {
            throw new NoSuchElementException("Underlying sequence is exhausted");
        }

        final V firstValue = lastValue;
        final List<T> skippedItems = new ArrayList<>();

        do {
            final T next = sequence.next();
            lastValue = next.getValue();
            skippedItems.add(next);
        } while (isNull(lastValue) && sequence.hasNext());

        if (isNull(firstValue) && isNull(lastValue)) {
            throw new IllegalArgumentException("Null sequence supplied");
        }

        final int undefinedValuesCount = skippedItems.size();
        final int middle = undefinedValuesCount / 2;
        final List<T> filledWithFirst = skippedItems.subList(0, middle);
        final List<T> filledWithLast = skippedItems.subList(middle, undefinedValuesCount);

        filledWithFirst.forEach(item -> {
            item.setValue(firstNonNull(firstValue, lastValue));
            nextItems.offer(item);
        });
        filledWithLast.forEach(item -> {
            item.setValue(firstNonNull(lastValue, firstValue));
            nextItems.offer(item);
        });

        return nextItems.poll();
    }

    public static <K, V, T extends Entry<K, V>> InterpolatingIterator<K, V, T> of(Iterator<T> it) {
        return new InterpolatingIterator<>(it);
    }

}
