package ru.viktorgezz.vacation_pay_calculator.calculation.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.intrf.VacationCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.holiday.HolidayService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.viktorgezz.vacation_pay_calculator.calculation.util.VacationPayFormula.calculateVacationPay;

/**
 * Стратегия расчета отпускных на основе календарных дат с учетом праздничных дней.
 * Реализует интерфейс {@link VacationCalculationStrategy}.
 */
@Component
public class CalendarBasedCalculationStrategy implements VacationCalculationStrategy {

    private static final Logger log = LoggerFactory.getLogger(CalendarBasedCalculationStrategy.class);

    private final HolidayService holidayService;

    public CalendarBasedCalculationStrategy(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @Override
    public boolean isSupports(VacationPayRqDto dto) {
        return isDatesProvidedOnly(dto);
    }

    @Override
    public BigDecimal calculate(VacationPayRqDto dto) {
        List<LocalDate> datesVacation =
                dto.getDateStart()
                        .datesUntil(dto.getDateEnd().plusDays(1))
                        .collect(Collectors.toList());

        final int countDays = datesVacation.size();
        final int countHolidays = holidayService.countHolidaysIn(datesVacation);
        final BigDecimal vacationPay = calculateVacationPay(
                dto.getAverageSalary(),
                countDays - countHolidays
        );

        log.debug("Average salary: {}, total days: {}, holidays: {}, payable days: {}. Calculated vacation pay: {}",
                dto.getAverageSalary(), countDays, countHolidays, countDays - countHolidays, vacationPay);

        return vacationPay;
    }

    /**
     * Проверяет, что в запросе указаны только даты начала и окончания отпуска.
     *
     * @param dto данные запроса
     * @return true, если указаны только даты
     */
    private boolean isDatesProvidedOnly(VacationPayRqDto dto) {
        return dto.getDaysVacation() == null
                && dto.getDateStart() != null
                && dto.getDateEnd() != null;
    }
}
