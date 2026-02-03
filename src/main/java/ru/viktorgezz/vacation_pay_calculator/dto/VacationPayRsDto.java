package ru.viktorgezz.vacation_pay_calculator.dto;

import java.math.BigDecimal;

/**
 * DTO ответа с результатом расчета отпускных выплат.
 */
public class VacationPayRsDto {

    private final BigDecimal totalVacationPay;

    public VacationPayRsDto(BigDecimal totalVacationPay) {
        this.totalVacationPay = totalVacationPay;
    }

    public BigDecimal getTotalVacationPay() {
        return totalVacationPay;
    }
}
