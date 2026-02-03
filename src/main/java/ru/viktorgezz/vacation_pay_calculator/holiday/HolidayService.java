package ru.viktorgezz.vacation_pay_calculator.holiday;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Интерфейс сервиса работы с праздничными днями.
 * Реализуется классом {@link HolidayServiceImpl}.
 */
public interface HolidayService {

    /**
     * Подсчитывает количество праздничных дней в указанной коллекции дат.
     *
     * @param dates коллекция дат
     * @return количество праздничных дней
     */
    int countHolidaysIn(Collection<LocalDate> dates);
    
}
