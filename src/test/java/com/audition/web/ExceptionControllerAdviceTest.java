package com.audition.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.web.advice.ExceptionControllerAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;

class ExceptionControllerAdviceTest {

    private ExceptionControllerAdvice exceptionControllerAdvice;
    private AuditionLogger logger;

    @BeforeEach
    void setUp() {
        logger = Mockito.mock(AuditionLogger.class);
        exceptionControllerAdvice = new ExceptionControllerAdvice(logger);
    }

    @Test
    void testHandleHttpClientException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        ProblemDetail problemDetail = exceptionControllerAdvice.handleHttpClientException(exception);
        assertNotNull(problemDetail);
        assertEquals(400, problemDetail.getStatus());
        assertEquals("400 Bad Request", problemDetail.getDetail());
        assertEquals("API Error Occurred", problemDetail.getTitle());
    }

    @Test
    void testHandleMainException() {
        Exception exception = new Exception("Internal Server Error");
        ProblemDetail problemDetail = exceptionControllerAdvice.handleMainException(exception);
        assertNotNull(problemDetail);
        assertEquals(500, problemDetail.getStatus());
        assertEquals("Internal Server Error", problemDetail.getDetail());
        assertEquals("API Error Occurred", problemDetail.getTitle());
    }

    @Test
    void testHandleSystemException() {
        SystemException exception = new SystemException("System Error", "Title", 500);
        ProblemDetail problemDetail = exceptionControllerAdvice.handleSystemException(exception);
        assertNotNull(problemDetail);
        assertEquals(500, problemDetail.getStatus());
        assertEquals("System Error", problemDetail.getDetail());
        assertEquals("Title", problemDetail.getTitle());
    }
}
