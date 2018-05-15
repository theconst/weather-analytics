package ua.kpi.correction.repository;

import ua.kpi.correction.model.RawWeatherData;

public interface RawWeatherDataRepository extends EagerStreamingPagingAndSortingRepository<RawWeatherData, Integer> {

}
