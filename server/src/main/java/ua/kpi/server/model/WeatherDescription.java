package ua.kpi.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum WeatherDescription {

    CLEAR("CL", "Clear"),
    BRUME("BR", "Brume"),
    FOG("FG", "Fog"),
    RAIN("RA", "Rain"),
    SHOWER_RAIN("SHRA", "Shower rain"),
    SNOW_AND_RAIN("SNRA", "Snow and rain"),
    SNOW("SN", "Snow"),
    SHOWER_SNOW("SHSN", "Shower snow"),
    STORM("TS", "Thunderstorm"),
    DRIZZLE("DZ", "Drizzle"),
    FREEZE("FZ", "Ice-crusted ground"),
    HAIL("HL", "Hail");

    private final String code;
    private final String description;

    private static final Map<String, WeatherDescription> DESCRIPTION_BY_CODE = new HashMap<>();

    static {
        for (WeatherDescription wd : WeatherDescription.values()) {
            DESCRIPTION_BY_CODE.put(wd.getCode(), wd);
        }
    }

    public static WeatherDescription fromCode(String code) {
        return DESCRIPTION_BY_CODE.getOrDefault(code, CLEAR);
    }
}

