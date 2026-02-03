package ru.viktorgezz.vacation_pay_calculator.service.impl;

import org.springframework.stereotype.Service;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.VacationCalculationStrategyResolver;
import ru.viktorgezz.vacation_pay_calculator.calculation.strategy.intrf.VacationCalculationStrategy;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRsDto;
import ru.viktorgezz.vacation_pay_calculator.service.intrf.VacationPayService;
import ru.viktorgezz.vacation_pay_calculator.validation.VacationPayRequestValidator;

/**
 * Реализация сервиса расчета отпускных выплат.
 * Реализует интерфейс {@link VacationPayService}.
 */
@Service
public class VacationPayServiceImpl implements VacationPayService {

    private final VacationCalculationStrategyResolver calculationStrategyResolver;
    private final VacationPayRequestValidator vacationPayRequestValidator;

    public VacationPayServiceImpl(
            VacationCalculationStrategyResolver calculationStrategyResolver,
            VacationPayRequestValidator vacationPayRequestValidator
    ) {
        this.calculationStrategyResolver = calculationStrategyResolver;
        this.vacationPayRequestValidator = vacationPayRequestValidator;
    }

    @Override
    public VacationPayRsDto calculate(VacationPayRqDto dto) {
        vacationPayRequestValidator.validate(dto);
        final VacationCalculationStrategy strategy = calculationStrategyResolver.resolve(dto);
        return new VacationPayRsDto(strategy.calculate(dto));
    }
}
