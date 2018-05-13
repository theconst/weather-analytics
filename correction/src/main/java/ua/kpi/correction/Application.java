package ua.kpi.correction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.kpi.correction.batch.StreamPartitioner;
import ua.kpi.correction.interpolation.StreamInterpolator;
import ua.kpi.correction.model.RawWeatherData;
import ua.kpi.correction.repository.RawWeatherDataRepository;

import javax.transaction.Transactional;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    public Application(
            RawWeatherDataRepository rawWeatherDataRepository,
            StreamInterpolator<RawWeatherData> weatherDataStreamInterpolator,
            @Value("${app.batch.size}") Integer batchSize) {
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

    @Override
    @Transactional
    public void run(String... strings) {
        //TODO: move to separate cron job
        StreamPartitioner.partition(
                weatherDataStreamInterpolator.interpolate(rawWeatherDataRepository.findAllOrderedByNumber()),
                batchSize
        ).forEach(rawWeatherDataRepository::saveAll);
    }
}