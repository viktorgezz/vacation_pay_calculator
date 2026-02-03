package ru.viktorgezz.vacation_pay_calculator.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VacationPayControllerTest {

    @LocalServerPort
    private int portServer;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = portServer;
    }

    @Test
    @DisplayName("Должен вернуть рассчитанную сумму отпускных когда указано фиксированное количество дней")
    void calculateVacationPay_ShouldReturnCalculatedAmount_WhenValidFixedDaysProvided() {
        BigDecimal salaryAverageValid = new BigDecimal("100000");
        Integer daysVacationValid = 14;

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("daysVacation", daysVacationValid);

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalVacationPay", notNullValue())
                .body("totalVacationPay", equalTo(47781.57f));
    }

    @Test
    @DisplayName("Должен вернуть рассчитанную сумму отпускных когда указан диапазон дат")
    void calculateVacationPay_ShouldReturnCalculatedAmount_WhenValidDateRangeProvided() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");
        LocalDate dateStartValid = LocalDate.of(2026, 1, 1);
        LocalDate dateEndValid = LocalDate.of(2026, 1, 10);

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("dateStart", dateStartValid.toString());
        bodyRequest.put("dateEnd", dateEndValid.toString());

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalVacationPay", notNullValue())
                .body("totalVacationPay", greaterThan(0f));
    }

    @Test
    @DisplayName("Должен вернуть рассчитанную сумму отпускных когда указан период в один день")
    void calculateVacationPay_ShouldReturnCalculatedAmount_WhenSingleDayPeriodProvided() {
        BigDecimal salaryAverageValid = new BigDecimal("30000");
        LocalDate dateSame = LocalDate.of(2026, 3, 15);

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("dateStart", dateSame.toString());
        bodyRequest.put("dateEnd", dateSame.toString());

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalVacationPay", notNullValue())
                .body("totalVacationPay", greaterThan(0f));
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации когда средняя зарплата не указана")
    void calculateVacationPay_ShouldReturnBadRequest_WhenAverageSalaryMissing() {
        Integer daysVacationValid = 14;

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("daysVacation", daysVacationValid);

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("validationErrors", notNullValue())
                .body("validationErrors.size()", greaterThan(0));
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации когда средняя зарплата отрицательная")
    void calculateVacationPay_ShouldReturnBadRequest_WhenAverageSalaryNegative() {
        BigDecimal salaryAverageNegative = new BigDecimal("-50000");
        Integer daysVacationValid = 14;

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageNegative);
        bodyRequest.put("daysVacation", daysVacationValid);

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("validationErrors", notNullValue());
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации когда средняя зарплата равна нулю")
    void calculateVacationPay_ShouldReturnBadRequest_WhenAverageSalaryZero() {
        BigDecimal salaryAverageZero = new BigDecimal("0");
        Integer daysVacationValid = 14;

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageZero);
        bodyRequest.put("daysVacation", daysVacationValid);

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("validationErrors", notNullValue());
    }

    @Test
    @DisplayName("Должен вернуть ошибку валидации когда количество дней отпуска отрицательное")
    void calculateVacationPay_ShouldReturnBadRequest_WhenVacationDaysNegative() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");
        Integer daysVacationNegative = -5;

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("daysVacation", daysVacationNegative);

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("validationErrors", notNullValue());
    }

    @Test
    @DisplayName("Должен вернуть ошибку бизнес-логики когда не указаны ни дни отпуска ни даты")
    void calculateVacationPay_ShouldReturnBadRequest_WhenParametersMissing() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("VACATION_PARAMETERS_MISSING"));
    }

    @Test
    @DisplayName("Должен вернуть ошибку бизнес-логики когда указаны и дни отпуска и даты одновременно")
    void calculateVacationPay_ShouldReturnBadRequest_WhenAllParametersSpecified() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");
        Integer daysVacationValid = 14;
        LocalDate dateStartValid = LocalDate.of(2026, 1, 1);
        LocalDate dateEndValid = LocalDate.of(2026, 1, 14);

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("daysVacation", daysVacationValid);
        bodyRequest.put("dateStart", dateStartValid.toString());
        bodyRequest.put("dateEnd", dateEndValid.toString());

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("VACATION_PARAMETERS_ALL_SPECIFIED"));
    }

    @Test
    @DisplayName("Должен вернуть ошибку бизнес-логики когда указана только дата начала отпуска")
    void calculateVacationPay_ShouldReturnBadRequest_WhenOnlyStartDateProvided() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");
        LocalDate dateStartValid = LocalDate.of(2026, 1, 1);

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("dateStart", dateStartValid.toString());

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("VACATION_DATES_INCOMPLETE"));
    }

    @Test
    @DisplayName("Должен вернуть ошибку бизнес-логики когда указана только дата окончания отпуска")
    void calculateVacationPay_ShouldReturnBadRequest_WhenOnlyEndDateProvided() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");
        LocalDate dateEndValid = LocalDate.of(2026, 1, 14);

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("dateEnd", dateEndValid.toString());

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("VACATION_DATES_INCOMPLETE"));
    }

    @Test
    @DisplayName("Должен вернуть ошибку бизнес-логики когда дата начала позже даты окончания")
    void calculateVacationPay_ShouldReturnBadRequest_WhenStartDateAfterEndDate() {
        BigDecimal salaryAverageValid = new BigDecimal("50000");
        LocalDate dateStartInvalid = LocalDate.of(2026, 1, 14);
        LocalDate dateEndInvalid = LocalDate.of(2026, 1, 1);

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("averageSalary", salaryAverageValid);
        bodyRequest.put("dateStart", dateStartInvalid.toString());
        bodyRequest.put("dateEnd", dateEndInvalid.toString());

        given()
                .contentType(ContentType.JSON)
                .body(bodyRequest)
                .when()
                .get("/calculacte")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("code", is("INVALID_VACATION_PERIOD"));
    }
}
