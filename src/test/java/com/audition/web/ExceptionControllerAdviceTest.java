package com.audition.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.constants.AuditionConstants;
import com.audition.web.advice.ExceptionControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

class ExceptionControllerAdviceTest {

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;
    @Mock
    private AuditionLogger logger;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionControllerAdvice)
            .build();
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

    @Test
    void testHandleHttpClientErrorException_withInvalidStatusCode() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(exception.getMessage()).thenReturn("Bad Request");
        ProblemDetail problemDetail = exceptionControllerAdvice.handleHttpClientException(exception);
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Bad Request", problemDetail.getDetail());
        assertEquals(AuditionConstants.DEFAULT_TITLE, problemDetail.getTitle());
        verify(logger).logErrorWithException(any(), anyString(), eq(exception));
    }
       @Test
    void testHandleOtherException() {
        Exception exception = new Exception("Other error");
        ProblemDetail problemDetail = exceptionControllerAdvice.handleMainException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Other error", problemDetail.getDetail());
        verify(logger).logErrorWithException(any(), anyString(), eq(exception));
    }

    @Test
    public void testHandleConstraintViolationException() {
        ConstraintViolationException exception = new ConstraintViolationException(AuditionConstants.ERROR_PATTERN, null);
        ResponseEntity<ProblemDetail> responseEntity = exceptionControllerAdvice.handleConstraintViolationExceptions(exception);
        ProblemDetail problemDetail = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(AuditionConstants.VALIDATION_ERROR, problemDetail.getTitle());
        assertEquals(AuditionConstants.ERROR_PATTERN, problemDetail.getDetail());
    }

}
