package ru.viktorgezz.vacation_pay_calculator.calculation.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.intrf.VacationCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;

import java.math.BigDecimal;

import static ru.viktorgezz.vacation_pay_calculator.calculation.util.VacationPayFormula.calculateVacationPay;

/**
 * Стратегия расчета отпускных на основе фиксированного количества дней.
 * Реализует интерфейс {@link VacationCalculationStrategy}.
 */
@Component
public class FixedDaysCalculationStrategy implements VacationCalculationStrategy {

    private static final Logger log = LoggerFactory.getLogger(FixedDaysCalculationStrategy.class);

    @Override
    public boolean isSupports(VacationPayRqDto dto) {
        return isVacationDaysProvidedOnly(dto);
    }

    @Override
    public BigDecimal calculate(VacationPayRqDto dto) {
        final BigDecimal vacationPay = calculateVacationPay(dto.getAverageSalary(), dto.getDaysVacation());

        log.debug("Average salary: {}, vacation days: {}. Calculated vacation pay: {}",
                dto.getAverageSalary(), dto.getDaysVacation(), vacationPay);

        return vacationPay;
    }

    /**
     * Проверяет, что в запросе указано только количество дней отпуска.
     *
     * @param dto данные запроса
     * @return true, если указано только количество дней
     */
    private boolean isVacationDaysProvidedOnly(VacationPayRqDto dto) {
        return dto.getDaysVacation() != null
                && dto.getDateStart() == null
                && dto.getDateEnd() == null;
    }
}
