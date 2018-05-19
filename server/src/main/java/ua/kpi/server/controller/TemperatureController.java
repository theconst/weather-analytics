package ua.kpi.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.server.util.Average;
import ua.kpi.server.util.StreamPartitioner;
import ua.kpi.server.model.TemperatureDataPointProjection;
import ua.kpi.server.repository.RawWeatherDataRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static ua.kpi.server.controller.ApiConstants.DATE_TIME_FORMAT;


@Transactional
@RestController
@RequestMapping("temperatureData")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TemperatureController {

    private final RawWeatherDataRepository weatherDataRepository;

    @GetMapping
    public List<TemperatureDataPointProjection> getTemperatureDataPointsBetween(
            @RequestParam(name = "from") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime from,
            @RequestParam(name = "to") @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime to,
            @RequestParam(name = "limit", required = false, defaultValue = "2000") Integer limit) {
        final int count = weatherDataRepository.countByTimestampBetween(from, to);
        final int numberOfBuckets = (count / limit) + 1;

        return StreamPartitioner.partition(getTemperatureData(from, to), numberOfBuckets)
                .map(bucket -> Average.ofInteger(bucket, TemperatureDataPointProjection::of))
                .collect(toList());
    }

    private Stream<Map.Entry<LocalDateTime, Integer>> getTemperatureData(LocalDateTime from, LocalDateTime to) {
        return weatherDataRepository.findTemperatureDataBetweenOrderByTimestamp(from, to)
                .map(TemperatureDataPointProjection::toTemperatureDataPoint);
    }

}
