package ru.viktorgezz.vacation_pay_calculator.calculation.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.impl.FixedDaysCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FixedDaysCalculationStrategyTest {

    @InjectMocks
    private FixedDaysCalculationStrategy fixedDaysCalculationStrategy;

    @Test
    @DisplayName("Должен вернуть true когда указано только количество дней отпуска")
    void isSupports_ShouldReturnTrue_WhenOnlyVacationDaysProvided() {
        VacationPayRqDto requestDto = new VacationPayRqDto(
                new BigDecimal("50000"), 14
        );

        boolean resultActual = fixedDaysCalculationStrategy.isSupports(requestDto);

        assertTrue(resultActual);
    }

    @Test
    @DisplayName("Должен вернуть false когда указаны даты отпуска")
    void isSupports_ShouldReturnFalse_WhenDatesProvided() {
        VacationPayRqDto requestDto = new VacationPayRqDto(
                new BigDecimal("50000"),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 10)
        );

        boolean resultActual = fixedDaysCalculationStrategy.isSupports(requestDto);

        assertFalse(resultActual);
    }

    @Test
    @DisplayName("Должен вернуть false когда количество дней не указано")
    void isSupports_ShouldReturnFalse_WhenVacationDaysNotProvided() {
        VacationPayRqDto requestDto = new VacationPayRqDto();
        requestDto.setAverageSalary(new BigDecimal("50000"));

        boolean resultActual = fixedDaysCalculationStrategy.isSupports(requestDto);

        assertFalse(resultActual);
    }

    @Test
    @DisplayName("Должен рассчитать отпускные для указанного количества дней")
    void calculate_ShouldCalculateVacationPay_WhenVacationDaysProvided() {
        final BigDecimal salaryAverageExpected = new BigDecimal("100000");
        final int daysVacationExpected = 14;

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                daysVacationExpected
        );

        BigDecimal resultActual = fixedDaysCalculationStrategy.calculate(requestDto);

        BigDecimal payVacationExpected = salaryAverageExpected
                .divide(new BigDecimal("29.3"), 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysVacationExpected))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(payVacationExpected, resultActual);
    }

    @Test
    @DisplayName("Должен рассчитать отпускные для одного дня")
    void calculate_ShouldCalculateVacationPayForOneDay_WhenOneDayProvided() {
        BigDecimal salaryAverageExpected = new BigDecimal("50000");
        int daysVacationExpected = 1;

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                daysVacationExpected
        );

        BigDecimal resultActual = fixedDaysCalculationStrategy.calculate(requestDto);

        BigDecimal payVacationExpected = salaryAverageExpected
                .divide(new BigDecimal("29.3"), 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysVacationExpected))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(payVacationExpected, resultActual);
    }

    @Test
    @DisplayName("Должен рассчитать отпускные для большого количества дней")
    void calculate_ShouldCalculateVacationPayForManyDays_WhenLargeVacationDaysProvided() {
        BigDecimal salaryAverageExpected = new BigDecimal("150000");
        int daysVacationExpected = 28;

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                daysVacationExpected
        );

        BigDecimal resultActual = fixedDaysCalculationStrategy.calculate(requestDto);

        BigDecimal payVacationExpected = salaryAverageExpected
                .divide(new BigDecimal("29.3"), 10, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysVacationExpected))
                .setScale(2, java.math.RoundingMode.HALF_UP);

        assertEquals(payVacationExpected, resultActual);
    }
}
