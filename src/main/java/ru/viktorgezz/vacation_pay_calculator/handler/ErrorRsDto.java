package ru.viktorgezz.vacation_pay_calculator.handler;

import java.util.List;

/**
 * DTO ответа об ошибке для REST API.
 */
public class ErrorRsDto {

    private String message;
    private String code;
    private List<ValidationError> validationErrors;

    public ErrorRsDto(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public ErrorRsDto(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ErrorRsDto() {
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", validationErrors=" + validationErrors +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}
