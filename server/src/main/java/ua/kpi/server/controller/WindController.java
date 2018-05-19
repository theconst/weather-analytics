package ua.kpi.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.server.model.WindDirection;
import ua.kpi.server.model.WindSpeedAndDirectionProjection;
import ua.kpi.server.model.WindSpeedProjection;
import ua.kpi.server.repository.RawWeatherDataRepository;
import ua.kpi.server.util.Average;
import ua.kpi.server.util.StreamPartitioner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.stream.Collectors.*;
import static ua.kpi.server.controller.ApiConstants.DATE_TIME_FORMAT;

@Transactional
@RestController
@RequestMapping("windData")
public class WindController {

    private final RawWeatherDataRepository weatherDataRepository;

    private final Double calmThreshold;

    @Autowired
    public WindController(
            RawWeatherDataRepository weatherDataRepository,
            @Value("app.wind.calm-threshold:0.5") Double calmThreshold) {
        this.weatherDataRepository = weatherDataRepository;
        this.calmThreshold = calmThreshold;
    }

    @GetMapping("speed")
    public List<WindSpeedProjection> getWindDataBetween(
            @RequestParam(name = "from") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime from,
            @RequestParam(name = "to") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime to,
            @RequestParam(name = "limit", required = false, defaultValue = "2000") Integer limit) {
        final int count = weatherDataRepository.countByTimestampBetween(from, to);
        final int numberOfBuckets = (count / limit) + 1;

        return StreamPartitioner.partition(getWindSpeed(from, to), numberOfBuckets)
                .map(bucket -> Average.ofInteger(bucket, WindSpeedProjection::of))
                .collect(toList());
    }

    @GetMapping("rose")
    public Map<String, Double> getWindRoseBetween(
            @RequestParam(name = "from") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime from,
            @RequestParam(name = "to") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime to) {
        final Map<WindDirection, Long> nonCalmEntriesByDirection = getWindSpeedAndDirection(from, to)
                .filter(windSpeedAndDirection ->
                        firstNonNull(windSpeedAndDirection.getAverageWindSpeed(), Integer.MIN_VALUE) >= calmThreshold)
                .collect(groupingBy(WindSpeedAndDirectionProjection::getWindDirection, counting()));

        final Long total = nonCalmEntriesByDirection.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        return nonCalmEntriesByDirection.entrySet().stream()
                .collect(toMap(e -> e.getKey().getAbbreviation(), e -> e.getValue().doubleValue() / total));
    }

    private Stream<WindSpeedAndDirectionProjection> getWindSpeedAndDirection(LocalDateTime fromTime, LocalDateTime toTime) {
        return weatherDataRepository.findWindSpeedAndDirectionBetweenOrderByTimestamp(fromTime, toTime);
    }

    private Stream<Map.Entry<LocalDateTime, Integer>> getWindSpeed(LocalDateTime from, LocalDateTime to) {
        return weatherDataRepository.findWindSpeedDataBetweenOrderByTimestamp(from, to)
                .map(WindSpeedProjection::toWindSpeedDataPoint);
    }

}
