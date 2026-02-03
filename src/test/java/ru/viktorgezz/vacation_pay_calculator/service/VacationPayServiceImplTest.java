package ru.viktorgezz.vacation_pay_calculator.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.VacationCalculationStrategyResolver;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.intrf.VacationCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRsDto;
import ru.viktorgezz.vacation_pay_calculator.exception.BusinessException;
import ru.viktorgezz.vacation_pay_calculator.exception.ErrorCode;
import ru.viktorgezz.vacation_pay_calculator.service.impl.VacationPayServiceImpl;
import ru.viktorgezz.vacation_pay_calculator.validation.VacationPayRequestValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacationPayServiceImplTest {

    @Mock
    private VacationCalculationStrategyResolver strategyResolverMock;

    @Mock
    private VacationPayRequestValidator requestValidatorMock;

    @Mock
    private VacationCalculationStrategy calculationStrategyMock;

    @InjectMocks
    private VacationPayServiceImpl vacationPayService;

    @Test
    @DisplayName("Должен рассчитать отпускные используя подходящую стратегию")
    void calculate_ShouldCalculateVacationPayUsingStrategy_WhenValidRequestProvided() {
        BigDecimal salaryAverageExpected = new BigDecimal("50000");
        int daysVacationExpected = 14;
        BigDecimal amountCalculatedExpected = new BigDecimal("23890.78");

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                daysVacationExpected
        );

        doNothing().when(requestValidatorMock).validate(any(VacationPayRqDto.class));
        when(strategyResolverMock.resolve(any(VacationPayRqDto.class))).thenReturn(calculationStrategyMock);
        when(calculationStrategyMock.calculate(any(VacationPayRqDto.class))).thenReturn(amountCalculatedExpected);

        VacationPayRsDto resultActual = vacationPayService.calculate(requestDto);

        assertNotNull(resultActual);
        assertEquals(amountCalculatedExpected, resultActual.getTotalVacationPay());
        verify(requestValidatorMock, times(1)).validate(requestDto);
        verify(strategyResolverMock, times(1)).resolve(requestDto);
        verify(calculationStrategyMock, times(1)).calculate(requestDto);
    }

    @Test
    @DisplayName("Должен вызвать валидатор перед расчетом")
    void calculate_ShouldCallValidatorBeforeCalculation_WhenRequestProvided() {
        BigDecimal salaryAverageExpected = new BigDecimal("100000");
        LocalDate dateStartActual = LocalDate.of(2026, 1, 1);
        LocalDate dateEndActual = LocalDate.of(2026, 1, 10);
        BigDecimal amountCalculatedExpected = new BigDecimal("27303.75");

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                dateStartActual,
                dateEndActual
        );

        doNothing().when(requestValidatorMock).validate(any(VacationPayRqDto.class));
        when(strategyResolverMock.resolve(any(VacationPayRqDto.class))).thenReturn(calculationStrategyMock);
        when(calculationStrategyMock.calculate(any(VacationPayRqDto.class))).thenReturn(amountCalculatedExpected);

        vacationPayService.calculate(requestDto);

        verify(requestValidatorMock, times(1)).validate(requestDto);
    }

    @Test
    @DisplayName("Должен использовать стратегию из резолвера для расчета")
    void calculate_ShouldUseStrategyFromResolver_WhenStrategyResolved() {
        BigDecimal salaryAverageExpected = new BigDecimal("75000");
        int daysVacationExpected = 21;
        BigDecimal amountCalculatedExpected = new BigDecimal("53720.48");

        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageExpected,
                daysVacationExpected
        );

        doNothing().when(requestValidatorMock).validate(any(VacationPayRqDto.class));
        when(strategyResolverMock.resolve(any(VacationPayRqDto.class))).thenReturn(calculationStrategyMock);
        when(calculationStrategyMock.calculate(any(VacationPayRqDto.class))).thenReturn(amountCalculatedExpected);

        VacationPayRsDto resultActual = vacationPayService.calculate(requestDto);

        assertEquals(amountCalculatedExpected, resultActual.getTotalVacationPay());
        verify(strategyResolverMock, times(1)).resolve(requestDto);
        verify(calculationStrategyMock, times(1)).calculate(requestDto);
    }

    @Test
    @DisplayName("Должен выбросить исключение когда валидация не прошла")
    void calculate_ShouldThrowException_WhenValidationFails() {
        VacationPayRqDto requestDto = new VacationPayRqDto();
        requestDto.setAverageSalary(new BigDecimal("50000"));

        doThrow(new BusinessException(
                ErrorCode.VACATION_PARAMETERS_MISSING)
        )
                .when(requestValidatorMock).validate(any(VacationPayRqDto.class));

        assertThrows(BusinessException.class,
                () -> vacationPayService.calculate(requestDto));

        verify(requestValidatorMock, times(1)).validate(requestDto);
        verify(strategyResolverMock, never()).resolve(any());
        verify(calculationStrategyMock, never()).calculate(any());
    }
}
