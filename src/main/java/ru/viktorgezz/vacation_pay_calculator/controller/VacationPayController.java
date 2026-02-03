package ru.viktorgezz.vacation_pay_calculator.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRqDto;
import ru.viktorgezz.vacation_pay_calculator.dto.VacationPayRsDto;
import ru.viktorgezz.vacation_pay_calculator.service.intrf.VacationPayService;

/**
 * REST контроллер для расчета отпускных выплат.
 */
@RestController
public class VacationPayController {

    private final VacationPayService vacationPayService;

    public VacationPayController(
            VacationPayService vacationPayService
    ) {
        this.vacationPayService = vacationPayService;
    }

    /**
     * Рассчитывает сумму отпускных выплат на основе переданных данных.
     *
     * @param vacationPayRqDto данные запроса для расчета отпускных
     * @return результат расчета отпускных выплат
     */
    @GetMapping("/calculacte")
    public VacationPayRsDto calculateVacationPay(
            @RequestBody @Valid VacationPayRqDto vacationPayRqDto
    ) {
        return vacationPayService.calculate(vacationPayRqDto);
    }
}
