package ua.kpi.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.server.batch.StreamPartitioner;
import ua.kpi.server.model.TemperatureDataPointProjection;
import ua.kpi.server.repository.RawWeatherDataRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


@Transactional
@RestController("temperatureData")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TemperatureController {

    private final RawWeatherDataRepository weatherDataRepository;

    @GetMapping
    public List<TemperatureDataPointProjection> getTemperatureDataPointsBetween(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to,
            @RequestParam(name = "limit", required = false, defaultValue = "1000") Integer limit) {
        final LocalDateTime fromTime = LocalDateTime.parse(from);
        final LocalDateTime toTime = LocalDateTime.parse(to);

        final int count = weatherDataRepository.countByTimestampBetween(fromTime, toTime);
        final int numberOfBuckets = (count / limit) + 1;

        return StreamPartitioner.partition(getTemperatureData(fromTime, toTime), numberOfBuckets)
                .map(TemperatureController::averageTemperature)
                .collect(toList());
    }

    private Stream<TemperatureDataPointProjection> getTemperatureData(LocalDateTime from, LocalDateTime to) {
        return weatherDataRepository.findTemperatureDataBetween(from, to);
    }

    private static TemperatureDataPointProjection averageTemperature(List<TemperatureDataPointProjection> bucket) {
        final TemperatureDataPointProjection middle = bucket.get(bucket.size() / 2);
        return TemperatureDataPointProjection.of(
                middle.getTimestamp(),
                (int) Math.round(bucket.stream()
                        .mapToInt(TemperatureDataPointProjection::getTemperature)
                        .average()
                        .orElseThrow(() -> new IllegalArgumentException("Empty bucket"))));
    }

}
