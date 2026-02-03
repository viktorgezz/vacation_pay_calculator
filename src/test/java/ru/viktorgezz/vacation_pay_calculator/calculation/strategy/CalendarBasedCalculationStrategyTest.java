package ru.viktorgezz.vacation_pay_calculator.calculation.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.impl.CalendarBasedCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.holiday.HolidayService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalendarBasedCalculationStrategyTest {

    @Mock
    private HolidayService holidayServiceMock;

    @InjectMocks
    private CalendarBasedCalculationStrategy calendarBasedCalculationStrategy;

    @Test
    @DisplayName("Должен вернуть true когда указаны только даты начала и окончания отпуска")
    void isSupports_ShouldReturnTrue_WhenOnlyDatesProvided() {
        VacationPayRqDto requestDto = new VacationPayRqDto();
        requestDto.setAverageSalary(new BigDecimal("50000"));
        requestDto.setDateStart(LocalDate.of(2026, 1, 1));
        requestDto.setDateEnd(LocalDate.of(2026, 1, 10));
        requestDto.setDaysVacation(null);

        boolean resultActual = calendarBasedCalculationStrategy.isSupports(requestDto);

        assertTrue(resultActual);
    }

    @Test
    @DisplayName("Должен вернуть false когда указано количество дней отпуска")
    void isSupports_ShouldReturnFalse_WhenVacationDaysProvided() {
        VacationPayRqDto requestDto = new VacationPayRqDto();
        requestDto.setAverageSalary(new BigDecimal("50000"));
        requestDto.setDaysVacation(10);
        requestDto.setDateStart(null);
        requestDto.setDateEnd(null);

        boolean resultActual = calendarBasedCalculationStrategy.isSupports(requestDto);

        assertFalse(resultActual);
    }

    @Test
    @DisplayName("Должен вернуть false когда даты не указаны")
    void isSupports_ShouldReturnFalse_WhenDatesNotProvided() {
        VacationPayRqDto requestDto = new VacationPayRqDto();
        requestDto.setAverageSalary(new BigDecimal("50000"));
        requestDto.setDaysVacation(null);
        requestDto.setDateStart(null);
        requestDto.setDateEnd(null);

        boolean resultActual = calendarBasedCalculationStrategy.isSupports(requestDto);

        assertFalse(resultActual);
    }

    @Test
    @DisplayName("Должен рассчитать отпускные с учетом праздничных дней")
    void calculate_ShouldCalculateVacationPayWithHolidays_WhenDatesProvided() {
        BigDecimal salaryAverageExpected = new BigDecimal("50000");
        LocalDate dateStartActual = LocalDate.of(2026, 1, 1);
        LocalDate dateEndActual = LocalDate.of(2026, 1, 10);
        int daysTotalExpected = 10;
        int countHolidaysExpected = 2;
        int daysPayableExpected = daysTotalExpected - countHolidaysExpected;

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                dateStartActual,
                dateEndActual
        );

        when(holidayServiceMock.countHolidaysIn(anyList())).thenReturn(countHolidaysExpected);

        BigDecimal resultActual = calendarBasedCalculationStrategy.calculate(requestDto);

        BigDecimal payVacationExpected = salaryAverageExpected
                .divide(new BigDecimal("29.3"), 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysPayableExpected))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(payVacationExpected, resultActual);
    }

    @Test
    @DisplayName("Должен рассчитать отпускные без праздничных дней")
    void calculate_ShouldCalculateVacationPayWithoutHolidays_WhenNoHolidaysInPeriod() {
        BigDecimal salaryAverageExpected = new BigDecimal("100000");
        LocalDate dateStartActual = LocalDate.of(2026, 2, 1);
        LocalDate dateEndActual = LocalDate.of(2026, 2, 5);
        int daysTotalExpected = 5;
        int countHolidaysExpected = 0;
        int daysPayableExpected = daysTotalExpected - countHolidaysExpected;

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                dateStartActual,
                dateEndActual
        );

        when(holidayServiceMock.countHolidaysIn(anyList())).thenReturn(countHolidaysExpected);

        BigDecimal resultActual = calendarBasedCalculationStrategy.calculate(requestDto);

        BigDecimal payVacationExpected = salaryAverageExpected
                .divide(new BigDecimal("29.3"), 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysPayableExpected))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(payVacationExpected, resultActual);
    }

    @Test
    @DisplayName("Должен рассчитать отпускные для одного дня")
    void calculate_ShouldCalculateVacationPayForOneDay_WhenStartAndEndDatesAreSame() {
        BigDecimal salaryAverageExpected = new BigDecimal("30000");
        LocalDate dateActual = LocalDate.of(2026, 3, 15);
        int daysTotalExpected = 1;
        int countHolidaysExpected = 0;
        int daysPayableExpected = daysTotalExpected - countHolidaysExpected;

        VacationPayRqDto requestDto = new VacationPayRqDto();
        requestDto.setAverageSalary(salaryAverageExpected);
        requestDto.setDateStart(dateActual);
        requestDto.setDateEnd(dateActual);

        when(holidayServiceMock.countHolidaysIn(anyList())).thenReturn(countHolidaysExpected);

        BigDecimal resultActual = calendarBasedCalculationStrategy.calculate(requestDto);

        BigDecimal payVacationExpected = salaryAverageExpected
                .divide(new BigDecimal("29.3"), 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysPayableExpected))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(payVacationExpected, resultActual);
    }
}
