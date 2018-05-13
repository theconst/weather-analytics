package ua.kpi.correction.interpolation;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Builder
class EntryAdaptor<K, V, T> implements Map.Entry<K, V> {

    @Getter
    private final T original;

    private final Function<T, K> keyGetter;

    private final Function<T, V> valueGetter;

    private final BiConsumer<T, V> valueSetter;

    @Override
    public K getKey() {
        return keyGetter.apply(original);
    }

    @Override
    public V getValue() {
        return valueGetter.apply(original);
    }

    @Override
    public V setValue(V value) {
        final V result = valueGetter.apply(original);

        valueSetter.accept(original, value);

        return result;
    }

}
