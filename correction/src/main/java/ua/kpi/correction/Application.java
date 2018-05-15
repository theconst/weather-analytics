package ua.kpi.correction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import ua.kpi.correction.interpolation.StreamInterpolator;
import ua.kpi.correction.model.RawWeatherData;
import ua.kpi.correction.repository.RawWeatherDataRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    public Application(
            RawWeatherDataRepository rawWeatherDataRepository,
            StreamInterpolator<RawWeatherData> weatherDataStreamInterpolator,
            @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") Integer batchSize) {
        this.rawWeatherDataRepository = rawWeatherDataRepository;
        this.weatherDataStreamInterpolator = weatherDataStreamInterpolator;
        this.batchSize = batchSize;
    }

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    private final RawWeatherDataRepository rawWeatherDataRepository;

    private final StreamInterpolator<RawWeatherData> weatherDataStreamInterpolator;

    private final Integer batchSize;

    /**
     * Updates timestamp and interpolates data in the table
     */
    @Override
    @Transactional
    public void run(String... ingored) {
        weatherDataStreamInterpolator.interpolate(
                rawWeatherDataRepository
                        .fetchAllPagesStreamStartingFrom(PageRequest.of(0, batchSize, Sort.by("number")))
                        .flatMap(Streamable::stream))
                // add additional field
                .forEach(interpolatedData -> interpolatedData.setTimestamp(
                        LocalDateTime.of(LocalDate.of(
                                interpolatedData.getYear(),
                                interpolatedData.getMonth(),
                                interpolatedData.getDay()
                        ), interpolatedData.getUtcTime())
                ));
    }
}