package ru.viktorgezz.vacation_pay_calculator.calculation.strategy.intrf;

import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;

import java.math.BigDecimal;

/**
 * Интерфейс стратегии расчета отпускных выплат.
 * Реализуется классами {@link ru.viktorgezz.vacation_pay_calculator.calculation.strategy.impl.CalendarBasedCalculationStrategy}
 * и {@link ru.viktorgezz.vacation_pay_calculator.calculation.strategy.impl.FixedDaysCalculationStrategy}.
 */
public interface VacationCalculationStrategy {

    /**
     * Проверяет, поддерживает ли стратегия расчет для указанного запроса.
     *
     * @param dto данные запроса
     * @return true, если стратегия поддерживает расчет
     */
    boolean isSupports(VacationPayRqDto dto);

    /**
     * Рассчитывает сумму отпускных выплат.
     *
     * @param dto данные запроса
     * @return сумма отпускных выплат
     */
    BigDecimal calculate(VacationPayRqDto dto);
}
