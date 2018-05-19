package ua.kpi.server.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Average {


    public static <R, T extends Map.Entry<LocalDateTime, ? extends Number>> R
    ofInteger(List<T> bucket, BiFunction<LocalDateTime, Integer, R> constructor) {
        final T middle = bucket.get(bucket.size() / 2);
        return constructor.apply(
                middle.getKey(),
                (int) bucket.stream()
                        .map(Map.Entry::getValue)
                        .filter(Objects::nonNull)
                        .mapToInt(Number::intValue)
                        .average()
                        .orElseThrow(() -> new IllegalArgumentException("Empty bucket")));
    }

    public static <R, T extends Map.Entry<LocalDateTime, ? extends Number>> R
    ofDouble(List<T> bucket, BiFunction<LocalDateTime, Double, R> constructor) {
        final T middle = bucket.get(bucket.size() / 2);
        return constructor.apply(
                middle.getKey(),
                bucket.stream()
                        .map(Map.Entry::getValue)
                        .filter(Objects::nonNull)
                        .mapToDouble(Number::doubleValue)
                        .average()
                        .orElseThrow(() -> new IllegalArgumentException("Empty bucket")));
    }

}
