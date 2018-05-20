package ua.kpi.server.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ua.kpi.server.model.*;

import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;


public interface RawWeatherDataRepository extends EagerStreamingPagingAndSortingRepository<RawWeatherData, Integer> {

    int countByTimestampBetween(LocalDateTime from, LocalDateTime to);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query("SELECT w FROM RawWeatherData w " +
            "WHERE w.timestamp BETWEEN :from AND :to " +
            "ORDER BY w.timestamp")
    Stream<TemperatureDataPointProjection> findTemperatureDataBetweenOrderByTimestamp(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query("SELECT w FROM RawWeatherData w " +
            "WHERE w.timestamp BETWEEN :from AND :to " +
            "ORDER BY w.timestamp")
    Stream<WindSpeedProjection> findWindSpeedDataBetweenOrderByTimestamp(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query("SELECT w FROM RawWeatherData w " +
            "WHERE w.timestamp BETWEEN :from AND :to " +
            "ORDER BY w.timestamp")
    Stream<WindSpeedAndDirectionProjection> findWindSpeedAndDirectionBetweenOrderByTimestamp(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT min(w.timestamp) as from, max(w.timestamp) as to FROM RawWeatherData w")
    List<TimeRange> getTimeRange();

}
