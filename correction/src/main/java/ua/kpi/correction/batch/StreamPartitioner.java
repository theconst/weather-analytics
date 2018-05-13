package ua.kpi.correction.batch;

import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class StreamPartitioner {

    public <T> Stream<List<T>> partition(Stream<T> stream, int size) {
        return Streams.stream(Iterators.partition(stream.iterator(), size));
    }
}
