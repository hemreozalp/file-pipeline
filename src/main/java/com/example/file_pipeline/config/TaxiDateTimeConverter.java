package com.example.file_pipeline.config;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TaxiDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);

    @Override
    public LocalDateTime convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(source, formatter);
    }
}
