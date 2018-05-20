package ua.kpi.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface TimeRange {

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    LocalDateTime getFrom();

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    LocalDateTime getTo();

}
