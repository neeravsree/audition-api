package com.audition.exception;

import com.audition.common.exception.SystemException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemExceptionTest {

    @Test
    void testSystemException_MessageConstructor() {
        String message = "Error occurred";
        SystemException exception = new SystemException(message);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testSystemException_MessageAndErrorCodeConstructor() {
        String message = "Error occurred";
        Integer errorCode = 400;
        SystemException exception = new SystemException(message, errorCode);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testSystemException_MessageAndThrowableConstructor() {
        String message = "Error occurred";
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testSystemException_DetailTitleErrorCodeConstructor() {
        String detail = "Error detail";
        String title = "Custom Error";
        Integer errorCode = 404;
        SystemException exception = new SystemException(detail, title, errorCode);
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
    }

    @Test
    void testSystemException_DetailTitleThrowableConstructor() {
        String detail = "Error detail";
        String title = "Custom Error";
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(detail, title, cause);
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(500, exception.getStatusCode()); // Default status code is 500 in this constructor
        assertEquals(detail, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testSystemException_DetailErrorCodeThrowableConstructor() {
        String detail = "Error detail";
        Integer errorCode = 502;
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(detail, errorCode, cause);
        assertEquals(detail, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testSystemException_DetailTitleErrorCodeThrowableConstructor() {
        String detail = "Error detail";
        String title = "Custom Error";
        Integer errorCode = 503;
        Throwable cause = new Throwable("Cause");
        SystemException exception = new SystemException(detail, title, errorCode, cause);
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(errorCode, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }
}
