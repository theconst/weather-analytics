package ua.kpi.server.model.conversion;

import ua.kpi.server.model.WindDirection;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

import static ua.kpi.server.model.WindDirection.*;

@Converter
public class WindDirectionAttributeConverter implements AttributeConverter<WindDirection, String> {

    private static final Map<String, WindDirection> LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION = new HashMap<>();

    private static final Map<WindDirection, String> WIND_DIRECTION_TO_LOCALIZED_STRING = new HashMap<>();

    // TODO: externalize configuration
    static {
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("Северный", NORTH);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("Южный", SOUTH);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("Восточный", EAST);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("Западный", WEST);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("С-В", NORTH_EAST);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("С-З", NORTH_WEST);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("Ю-В", SOUTH_EAST);
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.put("Ю-З", SOUTH_WEST);

        // Invert map
        LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.forEach((k, v) -> WIND_DIRECTION_TO_LOCALIZED_STRING.put(v, k));
    }

    @Override
    public String convertToDatabaseColumn(WindDirection attribute) {
        return WIND_DIRECTION_TO_LOCALIZED_STRING.get(attribute);
    }

    @Override
    public WindDirection convertToEntityAttribute(String dbData) {
        return LOCALIZED_WIND_DIRECTION_TO_WIND_DIRECTION.get(dbData);
    }
}
