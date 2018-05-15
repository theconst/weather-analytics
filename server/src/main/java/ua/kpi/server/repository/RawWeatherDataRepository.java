package ua.kpi.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ua.kpi.server.model.RawWeatherData;
import ua.kpi.server.model.TemperatureDataPointProjection;

import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;


public interface RawWeatherDataRepository extends EagerStreamingPagingAndSortingRepository<RawWeatherData, Integer> {

    int countByTimestampBetween(LocalDateTime from, LocalDateTime to);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query("SELECT w FROM RawWeatherData w " +
            "WHERE w.timestamp BETWEEN :from AND :to " +
            "ORDER BY w.timestamp")
    Stream<TemperatureDataPointProjection> findTemperatureDataBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}