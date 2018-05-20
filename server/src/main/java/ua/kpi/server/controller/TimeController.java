package ua.kpi.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.server.model.TimeRange;
import ua.kpi.server.repository.RawWeatherDataRepository;

import javax.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("timeData")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TimeController {

    private final RawWeatherDataRepository weatherDataRepository;

    @GetMapping("/")
    public TimeRange getWindDataBetween() {
        return weatherDataRepository.getTimeRange().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Database is empty"));
    }
}
