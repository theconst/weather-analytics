package ua.kpi.correction.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import ua.kpi.correction.model.RawWeatherData;

import javax.persistence.QueryHint;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;


public interface RawWeatherDataRepository extends CrudRepository<RawWeatherData, Integer> {

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    @Query("SELECT w FROM RawWeatherData w ORDER BY w.number")
    Stream<RawWeatherData> findAllOrderedByNumber();

}
