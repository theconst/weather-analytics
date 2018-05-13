package ua.kpi.correction.interpolation;

import java.util.stream.Stream;

@FunctionalInterface
public interface StreamInterpolator<T> {


    Stream<T> interpolate(Stream<T> data);

    default StreamInterpolator<T> compose(final StreamInterpolator<T> other) {
        return data -> other.interpolate(interpolate(data));
    }

    static <T> StreamInterpolator<T> identity() {
        return data -> data;
    }

}
