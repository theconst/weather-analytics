package ua.kpi.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ua.kpi.server.common.ApiConstants.DATE_TIME_FORMAT;

public interface TimeRange {

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    LocalDateTime getFrom();

    @JsonFormat(pattern = DATE_TIME_FORMAT  )
    LocalDateTime getTo();

}
