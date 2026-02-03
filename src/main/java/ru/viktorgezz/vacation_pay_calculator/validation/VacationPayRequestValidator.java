package ru.viktorgezz.vacation_pay_calculator.validation;

import org.springframework.stereotype.Component;

import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.exception.BusinessException;
import ru.viktorgezz.vacation_pay_calculator.exception.ErrorCode;

/**
 * Валидатор запроса на расчет отпускных выплат.
 */
@Component
public class VacationPayRequestValidator {

    /**
     * Валидирует данные запроса на расчет отпускных.
     *
     * @param dto данные запроса
     * @throws BusinessException если данные не прошли валидацию
     */
    public void validate(VacationPayRqDto dto) {
        if (areVacationParametersMissing(dto)) {
            throw new BusinessException(ErrorCode.VACATION_PARAMETERS_MISSING);
        }

        if (areAllVacationParametersProvided(dto)) {
            throw new BusinessException(ErrorCode.VACATION_PARAMETERS_ALL_SPECIFIED);
        }

        if (areVacationDatesIncomplete(dto)) {
            throw new BusinessException(ErrorCode.VACATION_DATES_INCOMPLETE);
        }

        if (isStartDateAfterEndDate(dto)) {
            throw new BusinessException(ErrorCode.INVALID_VACATION_PERIOD);
        }
    }

    /**
     * Проверяет, что все параметры отпуска отсутствуют.
     *
     * @param dto данные запроса
     * @return true, если все параметры отсутствуют
     */
    private boolean areVacationParametersMissing(VacationPayRqDto dto) {
        return dto.getDaysVacation() == null
                && dto.getDateStart() == null
                && dto.getDateEnd() == null;
    }

    /**
     * Проверяет, что указаны все параметры отпуска одновременно.
     *
     * @param dto данные запроса
     * @return true, если указаны все параметры
     */
    private boolean areAllVacationParametersProvided(VacationPayRqDto dto) {
        return dto.getDaysVacation() != null
                && dto.getDateStart() != null
                && dto.getDateEnd() != null;
    }

    /**
     * Проверяет, что указана только одна из дат (начала или окончания).
     *
     * @param dto данные запроса
     * @return true, если указана только одна дата
     */
    private boolean areVacationDatesIncomplete(VacationPayRqDto dto) {
        return (dto.getDateStart() == null) ^ (dto.getDateEnd() == null);
    }

    /**
     * Проверяет, что дата начала отпуска позже даты окончания.
     *
     * @param dto данные запроса
     * @return true, если дата начала позже даты окончания
     */
    private boolean isStartDateAfterEndDate(VacationPayRqDto dto) {
        return dto.getDateStart() != null
                && dto.getDateEnd() != null
                && dto.getDateStart().isAfter(dto.getDateEnd());
    }

}
