package ua.kpi.correction.model;

import lombok.Data;
import ua.kpi.correction.model.conversion.WeatherDescriptionsAttributeConverter;
import ua.kpi.correction.model.conversion.WindDirectionAttributeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@Entity
@Table(name = "weather")
public class RawWeatherData {

    private static final int YEAR = 2017;

    @Id
    @Column(name = "no")
    private Integer number;

    @Column(name = "day")
    private Integer day;

    @Column(name = "month")
    private Integer month;

    @Column(name = "UTC")
    private LocalTime utcTime;

    @Column(name = "T")
    private Integer temperature; //

    @Column(name = "U")
    private String relativeHumidity;

    @Column(name = "PPP")
    private Integer pressure; //

    @Column(name = "dd")
    @Convert(converter = WindDirectionAttributeConverter.class)
    private WindDirection windDirection; //

    @Column(name = "FF")
    private Integer averageWindSpeed; //

    @Column(name = "ww")
    @Convert(converter = WeatherDescriptionsAttributeConverter.class)
    private Set<WeatherDescription> weatherDescription; //

    @Column(name = "N")
    private Integer numberOfClouds; //

    @Column(name = "vv")
    private String rangeOfVisibility; //

    @Column(name = "hhh")
    private Integer cloudBottom; //

    public LocalDateTime getFullDateTime() {
        return LocalDateTime.of(LocalDate.of(YEAR, month, day), utcTime);
    }
}