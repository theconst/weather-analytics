package ua.kpi.correction.model.conversion;

import ua.kpi.correction.model.WeatherDescription;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@Converter
public class WeatherDescriptionsAttributeConverter implements AttributeConverter<Set<WeatherDescription>, String> {

    private static final String SEPARATOR = "\\+";

    @Override
    public String convertToDatabaseColumn(Set<WeatherDescription> attribute) {
        if (isNull(attribute)) {
            return null;
        }
        if (attribute.isEmpty()) {
            return null;
        }

        return attribute.stream()
                .map(WeatherDescription::getCode)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public Set<WeatherDescription> convertToEntityAttribute(String dbData) {
        if (isNull(dbData)) {
            return null;
        }
        if (dbData.isEmpty()) {
            return null;
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(WeatherDescription::fromCode)
                .collect(toSet());
    }
}
