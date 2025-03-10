package com.audition.constants;

public final class AuditionConstants {

    public static final String POSTS_API_URL = "https://jsonplaceholder.typicode.com/posts/";
    public static final String COMMENTS_API_URL = "https://jsonplaceholder.typicode.com/comments";
    public static final String POSTID = "postId";
    public static final String COMMENTS = "comments";
    public static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String POST_NOT_FOUND = "Post Not Found";
    public static final String POST_ID_NOT_FOUND = "The post with ID was not found";
    public static final String POST_ID_EMPTY = "Post ID must not be empty";
    public static final String JAEGER_URL = "http://localhost:14250";
    public static final String INJECT_RESPONSE_HEADERS = "inject-response-headers";
    public static final String TRACEID = "X-B3-TraceId";
    public static final String SPANID = "X-B3-SpanId";
    public static final String REQUEST = "Request";
    public static final String HEADERS = "Headers";
    public static final String BODY = "Body";
    public static final String RESPONSE_STATUS = "Response Status: {}";
    public static final String RESPONSE_HEADERS = "Response Headers: {}";
    public static final String RESPONSE_BODY = "Response Body: {}";
    public static final String VALIDATION_ERROR = "Validation Error";
    public static final String DEFAULT_TITLE = "API Error Occurred";
    public static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    public static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";
    public static final String ERROR_PATTERN = "Only numeric values are allowed";
    public static final String REGEX_PATTERN = "^[0-9]+$";
    public static final String ID = "id";
}
