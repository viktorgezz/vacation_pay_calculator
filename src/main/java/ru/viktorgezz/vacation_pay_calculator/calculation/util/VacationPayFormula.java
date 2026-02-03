package ru.viktorgezz.vacation_pay_calculator.calculation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Утилитный класс для расчета отпускных выплат по формуле.
 */
public class VacationPayFormula {

    private static final Logger log = LoggerFactory.getLogger(VacationPayFormula.class);
    private static final BigDecimal AVERAGE_DAYS_IN_MONTH = new BigDecimal("29.3");

    private VacationPayFormula() {
    }

    /**
     * Рассчитывает сумму отпускных.
     * Формула: (Средняя ЗП / 29.3) * Количество дней
     *
     * @param averageSalary Средняя зарплата за 12 месяцев
     * @param vacationDays  Количество оплачиваемых дней отпуска
     * @return Сумма отпускных, округленная до 2 знаков
     */
    public static BigDecimal calculateVacationPay(
            final BigDecimal averageSalary,
            final int vacationDays
    ) {
        BigDecimal dailyEarnings = averageSalary.divide(AVERAGE_DAYS_IN_MONTH, 10, RoundingMode.HALF_UP);
        BigDecimal totalPay = dailyEarnings.multiply(BigDecimal.valueOf(vacationDays));
        final BigDecimal vacationPay = totalPay.setScale(2, RoundingMode.HALF_UP);
        log.debug("Formula: ({} / {}) * {}. Result: {}",
                averageSalary, AVERAGE_DAYS_IN_MONTH, vacationDays, vacationPay);
        return vacationPay;
    }
}
