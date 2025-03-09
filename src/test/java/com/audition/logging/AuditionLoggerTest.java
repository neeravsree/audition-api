package com.audition.logging;

import com.audition.common.logging.AuditionLogger;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class AuditionLoggerTest {

    private AuditionLogger auditionLogger;
    private Logger logger;

    @BeforeEach
    void setUp() {
        auditionLogger = new AuditionLogger();
        logger = mock(Logger.class);
    }

    @Test
    void testInfoLogger_withMessage() {
        String message = "Info message";
        when(logger.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(logger, message);
    }

    @Test
    void testInfoLogger_withMessageAndObject() {
        String message = "Info message with object";
        Object object = new Object();
        when(logger.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(logger, message, object);
        verify(logger, times(1)).info(message, object);
    }

    @Test
    void testDebugLogger_withMessage() {
        String message = "Debug message";
        when(logger.isDebugEnabled()).thenReturn(true);
        auditionLogger.debug(logger, message);
        verify(logger, times(1)).debug(message);
    }

    @Test
    void testWarnLogger_withMessage() {
        String message = "Warn message";
        when(logger.isWarnEnabled()).thenReturn(true);
        auditionLogger.warn(logger, message);
        verify(logger, times(1)).warn(message);
    }

    @Test
    void testErrorLogger_withMessageAndException() {
        String message = "Error message";
        String eMessage = "Error details";
        when(logger.isErrorEnabled()).thenReturn(true);
        auditionLogger.error(logger, message, eMessage);
        verify(logger, times(1)).error(message);
    }

    @Test
    void testLogErrorWithException() {
        String message = "Error with exception";
        Exception exception = new Exception("Something went wrong");
        when(logger.isErrorEnabled()).thenReturn(true);
        auditionLogger.logErrorWithException(logger, message, exception);
        verify(logger, times(1)).error(message, exception);
    }

    @Test
    void testLogStandardProblemDetail() {
        ProblemDetail problemDetail = mock(ProblemDetail.class);
        when(problemDetail.getTitle()).thenReturn("Error Title");
        when(problemDetail.getStatus()).thenReturn(400);
        when(problemDetail.getDetail()).thenReturn("Error Detail");
        when(problemDetail.getInstance()).thenReturn(URI.create("Instance"));
        Exception exception = new Exception("Exception occurred");
        when(logger.isErrorEnabled()).thenReturn(true);
        auditionLogger.logStandardProblemDetail(logger, problemDetail, exception);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger, times(1)).error(captor.capture(), eq(exception));
        String capturedMessage = captor.getValue();
        assertTrue(capturedMessage.contains("ProblemDetail:"));
        assertTrue(capturedMessage.contains("Title: Error Title"));
        assertTrue(capturedMessage.contains("Status: 400"));
        assertTrue(capturedMessage.contains("Detail: Error Detail"));
        assertTrue(capturedMessage.contains("Instance: Instance"));
    }

    @Test
    void testLogHttpStatusCodeError() {
        String message = "HTTP error message";
        Integer errorCode = 500;
        when(logger.isErrorEnabled()).thenReturn(true);
        auditionLogger.logHttpStatusCodeError(logger, message, errorCode);
        verify(logger, times(1)).error("Error Code: 500 - Message: HTTP error message\n");
    }

    @Test
    void testInfoLogger_withInfoDisabled() {
        String message = "Info message";
        when(logger.isInfoEnabled()).thenReturn(false);
        auditionLogger.info(logger, message);
        verify(logger, never()).info(message);
    }

    @Test
    void testDebugLogger_withDebugDisabled() {
        String message = "Debug message";
        when(logger.isDebugEnabled()).thenReturn(false);
        auditionLogger.debug(logger, message);
        verify(logger, never()).debug(message);
    }

    @Test
    void testWarnLogger_withWarnDisabled() {
        String message = "Warn message";
        when(logger.isWarnEnabled()).thenReturn(false);
        auditionLogger.warn(logger, message);
        verify(logger, never()).warn(message);
    }

    @Test
    void testErrorLogger_withErrorDisabled() {
        String message = "Error message";
        when(logger.isErrorEnabled()).thenReturn(false);
        auditionLogger.error(logger, message, "Error details");
        verify(logger, never()).error(message);
    }
}
