package ru.viktorgezz.vacation_pay_calculator.holiday;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Компонент для загрузки календаря праздничных дней из JSON файла.
 */
@Component
public class JsonHolidayCalendar {

    private final ObjectMapper objectMapper;
    private final Resource holidaysResource;

    private Set<LocalDate> holidays = Collections.emptySet();

    public JsonHolidayCalendar(
            ObjectMapper objectMapper,
            @Value("classpath:holidays/holidays_2026.json") Resource holidaysResource
    ) {
        this.objectMapper = objectMapper;
        this.holidaysResource = holidaysResource;
    }

    /**
     * Загружает список праздничных дней из JSON файла.
     *
     * @throws IOException если произошла ошибка при чтении файла
     */
    @PostConstruct
    public void loadHolidays() throws IOException {
        try (InputStream is = holidaysResource.getInputStream()) {
            List<String> dateStrings = objectMapper.readValue(is, new TypeReference<>() {
            });

            this.holidays = dateStrings
                    .stream()
                    .map(LocalDate::parse)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    public Set<LocalDate> getHolidays() {
        return holidays;
    }
}
