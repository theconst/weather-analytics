package ua.kpi.correction.correction;

import org.springframework.stereotype.Component;
import ua.kpi.correction.interpolation.StreamInterpolator;
import ua.kpi.correction.model.RawWeatherData;

import java.util.stream.Stream;

import static ua.kpi.correction.interpolation.ObjectStreamInterpolator.interpolator;

/**
 * Interpolates weather data as if each argument was as single argument function
 */
@Component
public class RawWeatherDataInterpolator implements StreamInterpolator<RawWeatherData> {

    private final StreamInterpolator<RawWeatherData> interpolator;

    public RawWeatherDataInterpolator() {
        // humidity is null for some reason in the whole database
        interpolator = StreamInterpolator.<RawWeatherData>identity()
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getTemperature,
                        RawWeatherData::setTemperature))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getPressure,
                        RawWeatherData::setPressure))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getWindDirection,
                        RawWeatherData::setWindDirection))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getAverageWindSpeed,
                        RawWeatherData::setAverageWindSpeed))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getCloudBottom,
                        RawWeatherData::setCloudBottom))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getNumberOfClouds,
                        RawWeatherData::setNumberOfClouds))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getRangeOfVisibility,
                        RawWeatherData::setRangeOfVisibility))
                .compose(interpolator(
                        RawWeatherData::getNumber,
                        RawWeatherData::getWeatherDescription,
                        RawWeatherData::setWeatherDescription));
    }

    @Override
    public Stream<RawWeatherData> interpolate(Stream<RawWeatherData> data) {
        return interpolator.interpolate(data);
    }
}
