package ru.viktorgezz.vacation_pay_calculator.calculation.strategy;

import org.springframework.stereotype.Component;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.intrf.VacationCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Резолвер стратегий расчета отпускных выплат.
 * Выбирает подходящую стратегию на основе данных запроса.
 */
@Component
public class VacationCalculationStrategyResolver {

    private final List<VacationCalculationStrategy> strategies;

    public VacationCalculationStrategyResolver(
            List<VacationCalculationStrategy> strategies
    ) {
        this.strategies = strategies;
    }

    /**
     * Выбирает подходящую стратегию расчета на основе данных запроса.
     *
     * @param dto данные запроса
     * @return подходящая стратегия расчета
     * @throws IllegalStateException если не найдена подходящая стратегия или найдено несколько
     */
    public VacationCalculationStrategy resolve(VacationPayRqDto dto) {
        return strategies
                .stream()
                .filter(strategy -> strategy.isSupports(dto))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (list.isEmpty()) {
                                throw new IllegalStateException(
                                        "A suitable vacation payment strategy has not been found."
                                );
                            }

                            if (list.size() > 1) {
                                throw new IllegalStateException(
                                        "More than one suitable vacation payment strategy was found."
                                );
                            }

                            return list.get(0);
                        }));
    }

}
