package ru.viktorgezz.vacation_pay_calculator.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.exception.BusinessException;
import ru.viktorgezz.vacation_pay_calculator.exception.ErrorCode;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VacationPayRequestValidatorTest {

    @InjectMocks
    private VacationPayRequestValidator vacationPayRequestValidator;

    @Test
    @DisplayName("Должен выбросить исключение когда не указаны ни дни отпуска ни даты периода")
    void validate_ShouldThrowBusinessExceptionWithParametersMissing_WhenAllParametersAbsent() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        VacationPayRqDto requestDto = new VacationPayRqDto(salaryAverageActual, null, null, null);

        BusinessException exceptionActual = assertThrows(BusinessException.class,
                () -> vacationPayRequestValidator.validate(requestDto));

        assertEquals(ErrorCode.VACATION_PARAMETERS_MISSING, exceptionActual.getErrorCode());
    }

    @Test
    @DisplayName("Должен выбросить исключение когда указаны и дни отпуска и обе даты периода")
    void validate_ShouldThrowBusinessExceptionWithAllSpecified_WhenDaysAndDatesProvided() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        Integer daysVacationActual = 14;
        LocalDate dateStartActual = LocalDate.of(2026, 1, 1);
        LocalDate dateEndActual = LocalDate.of(2026, 1, 14);
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual,
                daysVacationActual,
                dateStartActual,
                dateEndActual
        );

        BusinessException exceptionActual = assertThrows(BusinessException.class,
                () -> vacationPayRequestValidator.validate(requestDto));

        assertEquals(ErrorCode.VACATION_PARAMETERS_ALL_SPECIFIED, exceptionActual.getErrorCode());
    }

    @Test
    @DisplayName("Должен выбросить исключение когда указана только дата начала отпуска")
    void validate_ShouldThrowBusinessExceptionWithDatesIncomplete_WhenOnlyStartDateProvided() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        LocalDate dateStartActual = LocalDate.of(2026, 1, 1);
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual, null, dateStartActual, null);

        BusinessException exceptionActual = assertThrows(BusinessException.class,
                () -> vacationPayRequestValidator.validate(requestDto));

        assertEquals(ErrorCode.VACATION_DATES_INCOMPLETE, exceptionActual.getErrorCode());
    }

    @Test
    @DisplayName("Должен выбросить исключение когда указана только дата окончания отпуска")
    void validate_ShouldThrowBusinessExceptionWithDatesIncomplete_WhenOnlyEndDateProvided() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        LocalDate dateEndActual = LocalDate.of(2026, 1, 14);
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual, null, null, dateEndActual);

        BusinessException exceptionActual = assertThrows(BusinessException.class,
                () -> vacationPayRequestValidator.validate(requestDto));

        assertEquals(ErrorCode.VACATION_DATES_INCOMPLETE, exceptionActual.getErrorCode());
    }

    @Test
    @DisplayName("Должен выбросить исключение когда дата начала позже даты окончания")
    void validate_ShouldThrowBusinessExceptionWithInvalidPeriod_WhenStartDateAfterEndDate() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        LocalDate dateStartActual = LocalDate.of(2026, 1, 14);
        LocalDate dateEndActual = LocalDate.of(2026, 1, 1);
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual, null, dateStartActual, dateEndActual);

        BusinessException exceptionActual = assertThrows(BusinessException.class,
                () -> vacationPayRequestValidator.validate(requestDto));

        assertEquals(ErrorCode.INVALID_VACATION_PERIOD, exceptionActual.getErrorCode());
    }

    @Test
    @DisplayName("Не должен выбросить исключение когда указаны только дни отпуска")
    void validate_ShouldNotThrow_WhenOnlyVacationDaysProvided() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        Integer daysVacationActual = 14;
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual, daysVacationActual, null, null);

        assertDoesNotThrow(() -> vacationPayRequestValidator.validate(requestDto));
    }

    @Test
    @DisplayName("Не должен выбросить исключение когда указаны даты периода и дата начала не позже даты окончания")
    void validate_ShouldNotThrow_WhenDatesProvidedAndStartNotAfterEnd() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        LocalDate dateStartActual = LocalDate.of(2026, 1, 1);
        LocalDate dateEndActual = LocalDate.of(2026, 1, 14);
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual, null, dateStartActual, dateEndActual);

        assertDoesNotThrow(() -> vacationPayRequestValidator.validate(requestDto));
    }

    @Test
    @DisplayName("Не должен выбросить исключение когда дата начала равна дате окончания")
    void validate_ShouldNotThrow_WhenStartDateEqualsEndDate() {
        BigDecimal salaryAverageActual = new BigDecimal("50000");
        LocalDate dateSameActual = LocalDate.of(2026, 3, 15);
        VacationPayRqDto requestDto = new VacationPayRqDto(
                salaryAverageActual, null, dateSameActual, dateSameActual);

        assertDoesNotThrow(() -> vacationPayRequestValidator.validate(requestDto));
    }
}
