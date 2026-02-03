package ru.viktorgezz.vacation_pay_calculator.service.intrf;

import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRsDto;

/**
 * Интерфейс сервиса расчета отпускных выплат.
 * Реализуется классом {@link ru.viktorgezz.vacation_pay_calculator.service.impl.VacationPayServiceImpl}.
 */
public interface VacationPayService {

    /**
     * Рассчитывает сумму отпускных выплат.
     *
     * @param dto данные запроса для расчета
     * @return результат расчета отпускных выплат
     */
    VacationPayRsDto calculate(VacationPayRqDto dto);
}
