package ua.kpi.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.server.model.WindDirection;
import ua.kpi.server.util.Average;
import ua.kpi.server.util.StreamPartitioner;
import ua.kpi.server.model.WindSpeedAndDirectionProjection;
import ua.kpi.server.model.WindSpeedProjection;
import ua.kpi.server.repository.RawWeatherDataRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Transactional
@RestController
@RequestMapping("windData")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WindController {

    private final RawWeatherDataRepository weatherDataRepository;

    @GetMapping("speed")
    public List<WindSpeedProjection> getWindDataBetween(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to,
            @RequestParam(name = "limit", required = false, defaultValue = "2000") Integer limit) {
        final LocalDateTime fromTime = LocalDateTime.parse(from);
        final LocalDateTime toTime = LocalDateTime.parse(to);

        final int count = weatherDataRepository.countByTimestampBetween(fromTime, toTime);
        final int numberOfBuckets = (count / limit) + 1;

        return StreamPartitioner.partition(getWindSpeed(fromTime, toTime), numberOfBuckets)
                .map(bucket -> Average.ofInteger(bucket, WindSpeedProjection::of))
                .collect(toList());
    }

    private Stream<WindSpeedAndDirectionProjection> getWindSpeedAndDirection(LocalDateTime fromTime, LocalDateTime toTime) {
        return weatherDataRepository.findWindSpeedAndDirectionBetweenOrderByTimestamp(fromTime, toTime);
    }


    private Stream<Map.Entry<LocalDateTime, Integer>> getWindSpeed(LocalDateTime from, LocalDateTime to) {
        return weatherDataRepository.findWindSpeedDataBetweenOrderByTimestamp(from, to).map(WindSpeedProjection::toWindSpeedDataPoint);
    }

}
