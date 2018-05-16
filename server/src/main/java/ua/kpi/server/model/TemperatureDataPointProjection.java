package ua.kpi.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;

public interface TemperatureDataPointProjection {

    @JsonProperty("time")
    LocalDateTime getTimestamp();

    @JsonProperty("temperature")
    Integer getTemperature();

    default Map.Entry<LocalDateTime, Integer> toTemperatureDataPoint() {
        return new AbstractMap.SimpleEntry<>(getTimestamp(), getTemperature());
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class TemperatureDataPointProjectionImpl implements TemperatureDataPointProjection {

        private LocalDateTime timestamp;

        private Integer temperature;
    }

    static TemperatureDataPointProjection of(LocalDateTime timestamp, Integer temperature) {
        return new TemperatureDataPointProjectionImpl(timestamp, temperature);
    }
}
