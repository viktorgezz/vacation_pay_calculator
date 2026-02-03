package ru.viktorgezz.vacation_pay_calculator.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.viktorgezz.vacation_pay_calculator.exception.BusinessException;
import ru.viktorgezz.vacation_pay_calculator.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Глобальный обработчик исключений REST-контроллеров приложения.
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    /**
     * Обрабатывает бизнес-исключения.
     *
     * @param e бизнес-исключение
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorRsDto> handleBusinessException(
            final BusinessException e
    ) {
        final ErrorRsDto body = new ErrorRsDto(
                e.getMessage(),
                e.getErrorCode().getCode()
        );

        log.debug(e.getMessage(), e);

        return ResponseEntity.status(
                e.getErrorCode().getStatus() != null ?
                        e.getErrorCode().getStatus() : HttpStatus.BAD_REQUEST
        ).body(body);
    }

    /**
     * Обрабатывает ошибки валидации параметров запроса.
     *
     * @param e исключение валидации
     * @return ответ с информацией об ошибках валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRsDto> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException e
    ) {
        final List<ValidationError> errors = new ArrayList<>();
        e.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    final String fieldName = ((FieldError) error).getField();
                    final String errorCode = error.getDefaultMessage();
                    errors.add(new ValidationError(
                                    fieldName,
                                    errorCode
                            )
                    );
                });

        final ErrorRsDto errorRsDto = new ErrorRsDto(
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorRsDto);
    }

    /**
     * Обрабатывает все остальные необработанные исключения.
     *
     * @param e исключение
     * @return ответ с информацией о внутренней ошибке
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRsDto> handleException(
            final Exception e
    ) {
        log.error(e.getMessage(), e);
        final ErrorRsDto body = new ErrorRsDto(
                ErrorCode.INTERNAL_EXCEPTION.getDefaultMessage(),
                ErrorCode.INTERNAL_EXCEPTION.getCode()
        );
        return ResponseEntity.status(ErrorCode.INTERNAL_EXCEPTION.getStatus())
                .body(body);
    }
}
