package ua.kpi.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;

public interface WindSpeedProjection {

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class WindSpeedProjectionImpl implements WindSpeedProjection {

        private LocalDateTime timestamp;

        private Integer averageWindSpeed;
    }

    @JsonProperty("time")
    LocalDateTime getTimestamp();

    @JsonProperty("windSpeed")
    Integer getAverageWindSpeed();

    default Map.Entry<LocalDateTime, Integer> toWindSpeedDataPoint() {
        return new AbstractMap.SimpleEntry<>(getTimestamp(), getAverageWindSpeed());
    }

    static WindSpeedProjection of(LocalDateTime timestamp, Integer windSpeed) {
        return new WindSpeedProjection.WindSpeedProjectionImpl(timestamp, windSpeed);
    }

}
