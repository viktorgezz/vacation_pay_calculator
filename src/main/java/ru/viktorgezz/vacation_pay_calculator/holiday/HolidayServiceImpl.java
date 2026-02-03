package ru.viktorgezz.vacation_pay_calculator.holiday;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

/**
 * Реализация сервиса работы с праздничными днями.
 * Реализует интерфейс {@link HolidayService}.
 */
@Component
public class HolidayServiceImpl implements HolidayService {

    private final JsonHolidayCalendar holidayCalendarProvider;

    public HolidayServiceImpl(JsonHolidayCalendar holidayCalendarProvider) {
        this.holidayCalendarProvider = holidayCalendarProvider;
    }

    @Override
    public int countHolidaysIn(final Collection<LocalDate> dates) {
        if (hasNoDates(dates)) {
            return 0;
        }

        final Set<LocalDate> holidays = holidayCalendarProvider.getHolidays();
        return Math.toIntExact(dates.stream()
                .filter(holidays::contains)
                .count());
    }

    /**
     * Проверяет, что коллекция дат пуста или равна null.
     *
     * @param dates коллекция дат
     * @return true, если коллекция пуста или равна null
     */
    private boolean hasNoDates(Collection<LocalDate> dates) {
        return dates == null || dates.isEmpty();
    }
}
