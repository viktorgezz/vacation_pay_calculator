package ru.viktorgezz.vacation_pay_calculator.exception;

/**
 * Бизнес-исключение с кодом ошибки {@link ErrorCode}.
 * Наследуется от {@link RuntimeException}.
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String[] args;

    public BusinessException(ErrorCode errorCode, String... args) {
        super(getFormatterMsg(errorCode, args));
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * Форматирует сообщение об ошибке с использованием аргументов.
     *
     * @param msg  код ошибки
     * @param args аргументы для форматирования
     * @return отформатированное сообщение
     */
    private static String getFormatterMsg(ErrorCode msg, Object[] args) {
        if (args == null || args.length == 0) {
            return msg.getDefaultMessage();
        }

        return String.format(msg.getDefaultMessage(), args);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Возвращает аргументы для форматирования сообщения.
     *
     * @return массив аргументов
     */
    public String[] getArgs() {
        return args;
    }
}
