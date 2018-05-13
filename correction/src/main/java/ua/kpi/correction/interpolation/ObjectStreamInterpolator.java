package ua.kpi.correction.interpolation;

import com.google.common.collect.Streams;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ObjectStreamInterpolator<K, V, T> implements StreamInterpolator<T> {

    private final Function<T, K> keyGetter;

    private final Function<T, V> valueGetter;

    private final BiConsumer<T, V> valueSetter;

    public Stream<T> interpolate(Stream<T> stream) {
        return Streams.stream(InterpolatingIterator.of(
                stream.map(item -> EntryAdaptor.<K, V, T>builder()
                        .keyGetter(keyGetter)
                        .valueGetter(valueGetter)
                        .valueSetter(valueSetter)
                        .original(item)
                        .build())
                        .iterator()))
                .map(EntryAdaptor::getOriginal);
    }

    public static <K, V, T> StreamInterpolator<T> interpolator(
            Function<T, K> keyGetter,
            Function<T, V> valueGetter,
            BiConsumer<T, V> valueSetter
    ) {
        return new ObjectStreamInterpolator<>(keyGetter, valueGetter, valueSetter);
    }

}
