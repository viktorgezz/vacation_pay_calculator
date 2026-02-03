package ru.viktorgezz.vacation_pay_calculator.exception;

import org.springframework.http.HttpStatus;

/**
 * Перечень кодов и шаблонов сообщений ошибок бизнес-логики.
 */
public enum ErrorCode {

    INTERNAL_EXCEPTION("INTERNAL_EXCEPTION", "Internal error", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_VACATION_PERIOD("INVALID_VACATION_PERIOD",
            "Vacation start date must not be after end date", HttpStatus.BAD_REQUEST),

    VACATION_PARAMETERS_ALL_SPECIFIED("VACATION_PARAMETERS_ALL_SPECIFIED",
            "Vacation days and dates must not be specified all together", HttpStatus.BAD_REQUEST),

    VACATION_DATES_INCOMPLETE("VACATION_DATES_INCOMPLETE",
            "Both start date and end date must be specified for vacation period", HttpStatus.BAD_REQUEST),

    VACATION_PARAMETERS_MISSING("VACATION_PARAMETERS_MISSING",
            "Either vacation days or vacation period dates must be specified", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCode(String code, String defaultMessage, HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }

    /**
     * Возвращает код ошибки.
     *
     * @return код ошибки
     */
    public String getCode() {
        return code;
    }

    /**
     * Возвращает сообщение по умолчанию.
     *
     * @return сообщение по умолчанию
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Возвращает HTTP статус.
     *
     * @return HTTP статус
     */
    public HttpStatus getStatus() {
        return status;
    }
}
