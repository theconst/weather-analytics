package ua.kpi.correction.model.conversion;

import com.google.common.base.MoreObjects;
import ua.kpi.correction.model.WeatherDescription;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;

@Converter
public class WeatherDescriptionsAttributeConverter implements AttributeConverter<Set<WeatherDescription>, String> {

    private static final String SEPARATOR = "\\+";

    @Override
    public String convertToDatabaseColumn(Set<WeatherDescription> attribute) {
        return firstNonNull(attribute, Collections.<WeatherDescription>emptySet()).stream()
                .map(WeatherDescription::getCode)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public Set<WeatherDescription> convertToEntityAttribute(String dbData) {
        return Arrays.stream(firstNonNull(dbData, "").split(SEPARATOR))
                .map(WeatherDescription::fromCode)
                .collect(toSet());
    }
}
