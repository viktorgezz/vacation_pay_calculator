package ru.viktorgezz.vacation_pay_calculator.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO запроса для расчета отпускных выплат.
 */
public class VacationPayRqDto {

    @NotNull(message = "Average salary is required")
    @Positive(message = "Average salary must be greater than 0")
    private BigDecimal averageSalary;

    @Positive(message = "Vacation days must be greater than 0")
    private Integer daysVacation;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateEnd;


    public VacationPayRqDto() {
    }

    public VacationPayRqDto(BigDecimal averageSalary, Integer daysVacation, LocalDate dateStart, LocalDate dateEnd) {
        this.averageSalary = averageSalary;
        this.daysVacation = daysVacation;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public VacationPayRqDto(BigDecimal averageSalary, LocalDate dateStart, LocalDate dateEnd) {
        this.averageSalary = averageSalary;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public VacationPayRqDto(BigDecimal averageSalary, Integer daysVacation) {
        this.averageSalary = averageSalary;
        this.daysVacation = daysVacation;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(BigDecimal averageSalary) {
        this.averageSalary = averageSalary;
    }


    public Integer getDaysVacation() {
        return daysVacation;
    }

    public void setDaysVacation(Integer daysVacation) {
        this.daysVacation = daysVacation;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }
}
