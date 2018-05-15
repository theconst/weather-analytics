package ua.kpi.server.model;

import lombok.*;
import ua.kpi.server.model.conversion.WindDirectionAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.MODULE)
@Builder
@Entity
@Table(name = "weather")
public class RawWeatherData implements TemperatureDataPointProjection {

    private static final String SEPARATOR = "\\+";

    @Id
    @Column(name = "no")
    private Integer number;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "UTC")
    private LocalTime utcTime;

    @Column(name = "T")
    private Integer temperature;

    @Column(name = "U")
    private String relativeHumidity;

    @Column(name = "PPP")
    private Integer pressure;

    @Column(name = "dd")
    @Convert(converter = WindDirectionAttributeConverter.class)
    private WindDirection windDirection;

    @Column(name = "FF")
    private Integer averageWindSpeed;

    @Column(name = "ww")
    private String plainWeatherDescription;

    @Column(name = "N")
    private Integer numberOfClouds;

    @Column(name = "vv")
    private String rangeOfVisibility;

    @Column(name = "hhh")
    private Integer cloudBottom;

    @Transient
    public Collection<WeatherDescription> getWeatherDescription() {
        return stream(plainWeatherDescription.split(SEPARATOR))
                .map(WeatherDescription::fromCode)
                .collect(toList());
    }

    @Transient
    public void setWeatherDescription(Collection<WeatherDescription> weatherDescription) {
        setPlainWeatherDescription(weatherDescription.stream()
                .filter(Objects::nonNull)
                .map(WeatherDescription::getCode)
                .collect(joining(SEPARATOR))
        );
    }

}