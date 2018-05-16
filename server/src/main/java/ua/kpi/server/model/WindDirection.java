package ua.kpi.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WindDirection {
    NORTH("N"),
    SOUTH("S"),
    EAST("E"),
    WEST("W"),
    SOUTH_EAST("SE"),
    SOUTH_WEST("SW"),
    NORTH_EAST("NE"),
    NORTH_WEST("NW");

    private final String abbreviation;
}
