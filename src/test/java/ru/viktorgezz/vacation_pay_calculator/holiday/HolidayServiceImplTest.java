package ru.viktorgezz.vacation_pay_calculator.holiday;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayServiceImplTest {

    @Mock
    private JsonHolidayCalendar holidayCalendarMock;

    @InjectMocks
    private HolidayServiceImpl holidayService;

    @Test
    @DisplayName("Должен вернуть количество праздничных дней в указанном периоде")
    void countHolidaysIn_ShouldReturnHolidaysCount_WhenDatesContainHolidays() {
        LocalDate dateHolidayFirst = LocalDate.of(2026, 1, 1);
        LocalDate dateHolidaySecond = LocalDate.of(2026, 1, 7);
        LocalDate dateRegularFirst = LocalDate.of(2026, 1, 2);
        LocalDate dateRegularSecond = LocalDate.of(2026, 1, 3);

        Set<LocalDate> setHolidaysExpected = new HashSet<>(Arrays.asList(dateHolidayFirst, dateHolidaySecond));
        List<LocalDate> collectionDatesActual = Arrays.asList(
                dateHolidayFirst,
                dateRegularFirst,
                dateRegularSecond,
                dateHolidaySecond
        );
        int countExpected = 2;

        when(holidayCalendarMock.getHolidays()).thenReturn(setHolidaysExpected);

        int resultActual = holidayService.countHolidaysIn(collectionDatesActual);

        assertEquals(countExpected, resultActual);
    }

    @Test
    @DisplayName("Должен вернуть ноль когда в периоде нет праздничных дней")
    void countHolidaysIn_ShouldReturnZero_WhenNoHolidaysInPeriod() {
        LocalDate dateRegularFirst = LocalDate.of(2026, 2, 1);
        LocalDate dateRegularSecond = LocalDate.of(2026, 2, 2);
        LocalDate dateRegularThird = LocalDate.of(2026, 2, 3);

        Set<LocalDate> setHolidaysExpected = new HashSet<>(Arrays.asList(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 7)
        ));
        List<LocalDate> collectionDatesActual = Arrays.asList(
                dateRegularFirst,
                dateRegularSecond,
                dateRegularThird
        );
        int countExpected = 0;

        when(holidayCalendarMock.getHolidays()).thenReturn(setHolidaysExpected);

        int resultActual = holidayService.countHolidaysIn(collectionDatesActual);

        assertEquals(countExpected, resultActual);
    }

    @Test
    @DisplayName("Должен вернуть ноль когда коллекция дат пуста")
    void countHolidaysIn_ShouldReturnZero_WhenDatesCollectionIsEmpty() {
        List<LocalDate> collectionDatesActual = Collections.emptyList();
        int countExpected = 0;

        int resultActual = holidayService.countHolidaysIn(collectionDatesActual);

        assertEquals(countExpected, resultActual);
    }

    @Test
    @DisplayName("Должен вернуть ноль когда коллекция дат равна null")
    void countHolidaysIn_ShouldReturnZero_WhenDatesCollectionIsNull() {
        int countExpected = 0;

        int resultActual = holidayService.countHolidaysIn(null);

        assertEquals(countExpected, resultActual);
    }

    @Test
    @DisplayName("Должен вернуть количество всех дней когда все дни являются праздничными")
    void countHolidaysIn_ShouldReturnAllDaysCount_WhenAllDaysAreHolidays() {
        LocalDate dateHolidayFirst = LocalDate.of(2026, 1, 1);
        LocalDate dateHolidaySecond = LocalDate.of(2026, 1, 7);
        LocalDate dateHolidayThird = LocalDate.of(2026, 1, 8);

        Set<LocalDate> setHolidaysExpected = new HashSet<>(Arrays.asList(
                dateHolidayFirst,
                dateHolidaySecond,
                dateHolidayThird
        ));
        List<LocalDate> collectionDatesActual = Arrays.asList(
                dateHolidayFirst,
                dateHolidaySecond,
                dateHolidayThird
        );
        int countExpected = 3;

        when(holidayCalendarMock.getHolidays()).thenReturn(setHolidaysExpected);

        int resultActual = holidayService.countHolidaysIn(collectionDatesActual);

        assertEquals(countExpected, resultActual);
    }

    @Test
    @DisplayName("Должен вернуть ноль когда календарь праздников пуст")
    void countHolidaysIn_ShouldReturnZero_WhenHolidayCalendarIsEmpty() {
        LocalDate dateRegularFirst = LocalDate.of(2026, 2, 1);
        LocalDate dateRegularSecond = LocalDate.of(2026, 2, 2);

        Set<LocalDate> setHolidaysExpected = Collections.emptySet();
        List<LocalDate> collectionDatesActual = Arrays.asList(
                dateRegularFirst,
                dateRegularSecond
        );
        int countExpected = 0;

        when(holidayCalendarMock.getHolidays()).thenReturn(setHolidaysExpected);

        int resultActual = holidayService.countHolidaysIn(collectionDatesActual);

        assertEquals(countExpected, resultActual);
    }
}
